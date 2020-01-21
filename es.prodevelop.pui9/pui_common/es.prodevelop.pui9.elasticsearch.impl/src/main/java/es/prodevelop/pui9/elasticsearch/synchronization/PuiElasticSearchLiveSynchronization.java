package es.prodevelop.pui9.elasticsearch.synchronization;

import java.lang.Thread.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.elasticsearch.components.ViewsAnalysis;
import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.elasticsearch.eventlistener.listener.AbstractElasticSearchListener;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchDocumentService;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.websocket.PuiWebSocket;

/**
 * Live synchronization process for ElasticSearch. It is a backgroud process
 * that is always being executed, and is fired on each
 * {@link AbstractElasticSearchListener} instance.<br>
 * <br>
 * It has a queue for each {@link ITableDto} class for not blocking amongst
 * them. Each operation of a {@link ITableDto} type is treated in a synchonized
 * way. That means that if multiple operations of a same DTO type are received,
 * they are processed one by one each (if two operations over the same element
 * are received, it will wait <b>1 second</b> between these executions)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchLiveSynchronization {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private IPuiElasticSearchEnablement elasticEnablement;

	@Autowired
	private IPuiElasticSearchDocumentService documentService;

	@Autowired
	private ViewsAnalysis viewsAnalysis;

	@Autowired
	private PuiWebSocket websocketUtils;

	@Autowired
	private PuiApplicationContext context;

	private AtomicLong noTransactionSequence;
	private Map<Class<? extends ITableDto>, Boolean> mapDtoIndexable;
	private Map<Class<? extends ITableDto>, List<ViewIndexableInfo>> mapDtoQueues;
	private Map<Class<? extends IViewDto>, ViewIndexableInfo> mapViewQueue;
	private boolean debug;

	@PostConstruct
	private void postConstruct() {
		noTransactionSequence = new AtomicLong(-1);
		mapDtoIndexable = new HashMap<>();
		mapDtoQueues = new HashMap<>();
		mapViewQueue = new HashMap<>();
		debug = true;

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ElasticSearch_QueuesAwake", false, 1, 1,
				TimeUnit.MINUTES, this::wakeUpThreads);
	}

	@PreDestroy
	private void preDestroy() {
		wakeUpThreads();
	}

	public void wakeUpThreads() {

	}

	public void queueOperation(ITableDto dtoPk, DocumentOperationType operation, Long transactionId, String jwt) {
		Class<? extends ITableDto> tableDtoClass = dtoPk.getClass();
		if (!isDtoIndexable(tableDtoClass)) {
			return;
		}

		analyzeDtoViews(tableDtoClass);

		FilterGroup pkFilter = FilterGroup.createFilterForDtoPk(dtoPk);
		mapDtoQueues.get(tableDtoClass).forEach(queue -> {
			queue.addOperation(dtoPk, operation, pkFilter, transactionId, jwt);
		});
	}

	private boolean isDtoIndexable(Class<? extends ITableDto> tableDtoClass) {
		if (!DtoRegistry.isRegistered(tableDtoClass)) {
			return false;
		}

		if (!mapDtoIndexable.containsKey(tableDtoClass)) {
			String table = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(tableDtoClass));

			boolean shouldBeQueued = false;
			for (String view : viewsAnalysis.getViews(table)) {
				Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
				if (viewDtoClass == null) {
					continue;
				}

				if (elasticEnablement.isViewIndexable(viewDtoClass)) {
					shouldBeQueued = true;
					break;
				}
			}
			mapDtoIndexable.put(tableDtoClass, shouldBeQueued);
		}

		if (!mapDtoIndexable.get(tableDtoClass)) {
			return false;
		}

		return true;
	}

	private synchronized void analyzeDtoViews(Class<? extends ITableDto> tableDtoClass) {
		if (mapDtoQueues.containsKey(tableDtoClass)) {
			return;
		}

		mapDtoQueues.put(tableDtoClass, new ArrayList<>());

		String table = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(tableDtoClass));
		Set<String> views = viewsAnalysis.getViews(table);

		for (String view : views) {
			Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
			if (viewDtoClass == null) {
				continue;
			}
			if (!elasticEnablement.isViewIndexable(viewDtoClass)) {
				continue;
			}

			if (context.getBean(daoRegistry.getDaoFromEntityName(view, false)) == null) {
				continue;
			}

			ViewIndexableInfo viewQueue = mapViewQueue.get(viewDtoClass);
			if (viewQueue == null) {
				viewQueue = new ViewIndexableInfo(viewDtoClass, view);
				mapViewQueue.put(viewDtoClass, viewQueue);
			}
			mapDtoQueues.get(tableDtoClass).add(viewQueue);
		}
	}

	private class ViewIndexableInfo {
		private Class<? extends IViewDto> viewDtoClass;
		private String view;
		private AtomicLong sequence;
		private Map<Long, AtomicInteger> mapTransactionOperations;
		private Map<Long, String> mapTransactionJwt;
		private Cache<Integer, Integer> hashCache;
		private PriorityBlockingQueue<ElasticSearchOperation> queue;
		private Thread thread;

		public ViewIndexableInfo(Class<? extends IViewDto> viewDtoClass, String view) {
			this.viewDtoClass = viewDtoClass;
			this.view = view;
			this.sequence = new AtomicLong(0);
			this.mapTransactionOperations = new HashMap<>();
			this.mapTransactionJwt = new HashMap<>();
			this.hashCache = CacheBuilder.newBuilder().expireAfterAccess(1000, TimeUnit.MILLISECONDS).build();
			this.queue = new PriorityBlockingQueue<>();
			this.thread = new Thread(new ViewQueue(), "PuiThread_ElasticSearch_View_" + viewDtoClass.getSimpleName());
			thread.start();
		}

		public synchronized void addOperation(ITableDto dtoPk, DocumentOperationType operation, FilterGroup pkFilter,
				Long transactionId, String jwt) {
			if (transactionId == null) {
				transactionId = noTransactionSequence.getAndDecrement();
			}

			ElasticSearchOperation eso = new ElasticSearchOperation(dtoPk, viewDtoClass, operation, pkFilter,
					transactionId);
			queue.add(eso);
			if (debug) {
				logger.debug("*** Queue: " + eso);
			}

			if (!mapTransactionOperations.containsKey(transactionId)) {
				mapTransactionOperations.put(transactionId, new AtomicInteger(1));
			} else {
				mapTransactionOperations.get(transactionId).getAndIncrement();
			}

			if (!mapTransactionJwt.containsKey(transactionId)) {
				mapTransactionJwt.put(transactionId, jwt);
			}

			if (thread.getState().equals(State.WAITING)) {
				synchronized (thread) {
					thread.notify();
				}
			}
		}

		private class ViewQueue implements Runnable {
			@Override
			public void run() {
				ITableDto lastDto = null;
				while (true) {
					try {
						if (!elasticEnablement.isElasticSearchAvailable()) {
							break;
						}

						if (queue.isEmpty()) {
							synchronized (thread) {
								thread.wait();
							}
							Thread.sleep(50);
							continue;
						}

						ElasticSearchOperation eso = queue.take();
						if (lastDto != null && eso.dtoPk.equals(lastDto)) {
							Thread.sleep(1500);
						}
						lastDto = eso.dtoPk;

						int hashCode = eso.dtoPk.hashCode();
						while (true) {
							if (hashCache.asMap().containsKey(hashCode)) {
								Thread.sleep(100);
								continue;
							}
							break;
						}
						hashCache.put(hashCode, hashCode);

						new Thread(new Runnable() {
							@Override
							public void run() {
								processViewDto(eso);
								if (mapTransactionOperations.get(eso.transactionId).decrementAndGet() == 0) {
									mapTransactionOperations.remove(eso.transactionId);
									String jwt = mapTransactionJwt.remove(eso.transactionId);
									sendWebsocketMessage(viewDtoClass, jwt);
								}
							}
						}, "PuiThread_ElasticSearch_ViewProcess_" + eso).start();
					} catch (InterruptedException e) {
						logger.debug("Error while operating with Thread", e);
					}
				}
			}

			private void processViewDto(ElasticSearchOperation eso) {
				if (eso == null) {
					return;
				}
				if (debug) {
					logger.debug("*** Process: " + eso);
				}

				switch (eso.operation) {
				case insert:
					documentService.insertDocument(eso.dtoPk, view, eso.pkFilter);
					break;
				case update:
					documentService.updateDocument(eso.dtoPk, view, eso.pkFilter);
					break;
				case delete:
					documentService.deleteDocument(eso.dtoPk, view, eso.pkFilter);
					break;
				}
			}
		}

		private class ElasticSearchOperation implements Comparable<ElasticSearchOperation> {
			private Long transactionId;
			private Long seqNum;
			private ITableDto dtoPk;
			private DocumentOperationType operation;
			private FilterGroup pkFilter;
			private String toString;

			public ElasticSearchOperation(ITableDto dtoPk, Class<? extends IViewDto> viewDtoClass,
					DocumentOperationType operation, FilterGroup pkFilter, Long transactionId) {
				this.transactionId = transactionId;
				this.seqNum = sequence.getAndIncrement();
				this.dtoPk = dtoPk;
				this.operation = operation;
				this.pkFilter = pkFilter;

				StringBuilder sb = new StringBuilder();
				sb.append(viewDtoClass.getSimpleName());
				sb.append("_");
				sb.append(dtoPk.getClass().getSimpleName());
				sb.append("_");
				sb.append(operation);
				sb.append("_");
				sb.append(transactionId);
				sb.append("_");
				sb.append(seqNum);

				toString = sb.toString();
			}

			@Override
			public int compareTo(ElasticSearchOperation o) {
				if (transactionId < o.transactionId) {
					return -1;
				} else if (transactionId > o.transactionId) {
					return 1;
				} else {
					return seqNum < o.seqNum ? -1 : 1;
				}
			}

			@Override
			public String toString() {
				return toString;
			}
		}

	}

	private void sendWebsocketMessage(Class<? extends IViewDto> viewDtoClass, String jwt) {
		Class<? extends ITableDto> tableDtoClass = daoRegistry.getTableDtoFromViewDto(viewDtoClass);
		if (tableDtoClass == null) {
			return;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// do nothing
		}

		String model = daoRegistry.getModelIdFromDto(tableDtoClass);
		Map<String, Object> result = new HashMap<>();
		result.put("model", model);
		result.put("isBroadcast", StringUtils.isEmpty(jwt));

		if (!StringUtils.isEmpty(jwt)) {
			websocketUtils.sendMessage(jwt, "elasticsearch", result);
		} else {
			websocketUtils.broadcastMessage("elasticsearch", result);
		}
	}

}
