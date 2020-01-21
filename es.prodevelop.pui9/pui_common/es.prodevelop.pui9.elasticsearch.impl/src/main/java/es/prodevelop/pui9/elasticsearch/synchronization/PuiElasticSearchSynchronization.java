package es.prodevelop.pui9.elasticsearch.synchronization;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.elasticsearch.components.ViewsAnalysis;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCountException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchExistsIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchInsertDocumentException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchSynchronization;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchDocumentService;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchIndexService;
import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * Implementation of the {@link IPuiElasticSearchSynchronization} interface.
 * Allows to synchronize documents on demand (for instance, from a Web Service)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PuiElasticSearchSynchronization implements IPuiElasticSearchSynchronization {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IPuiElasticSearchEnablement elasticSearchEnablement;

	@Autowired
	private IPuiElasticSearchIndexService indexService;

	@Autowired
	private IPuiElasticSearchDocumentService documentService;

	@Autowired
	private PuiElasticSearchLiveSynchronization liveSynch;

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private ViewsAnalysis viewsAnalysis;

	private Map<Class<? extends IViewDto>, AtomicInteger> synchronizingViewCache = new HashMap<>();

	@PostConstruct
	private void postConstruct() {
		Duration initDelay = Duration.between(LocalDateTime.now(),
				LocalDate.now().plusDays(1).atTime(LocalTime.of(3, 0)));
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ElasticSearch_NightSynchronizer", true,
				initDelay.toMinutes(), TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES, () -> synchronize(null, false));
	}

	@Override
	public void synchronize(String viewToSynch, Boolean force) {
		logger.info("Starting Elastic Search synchronization");

		// check if elastic search is available or not. Maybe it is not connected to any
		// node
		if (!elasticSearchEnablement.isElasticSearchAvailable()) {
			logger.info("Elastic Search is not available");
			return;
		}

		try {
			Class<? extends IViewDto> paramViewDtoClass = null;
			if (!StringUtils.isEmpty(viewToSynch)) {
				viewToSynch = viewToSynch.trim().toLowerCase();
				paramViewDtoClass = daoRegistry.getDtoFromEntityName(viewToSynch, false, false);
				if (paramViewDtoClass == null) {
					return;
				}
			}

			List<String> indexes;
			try {
				indexes = indexService.getAllIndexes();
			} catch (PuiException e) {
				indexes = Collections.emptyList();
			}

			// remove indexes that should not exist
			for (String index : indexes) {
				String tableIndexName = index;
				if (!StringUtils.isEmpty(indexService.getIndexPrefix())) {
					tableIndexName = tableIndexName.replaceAll(indexService.getIndexPrefix() + "_", "");
				}

				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					PuiLanguage next = it.next();
					if (tableIndexName.endsWith("_" + next.getIsocode())) {
						tableIndexName = tableIndexName.substring(0, tableIndexName.lastIndexOf("_"));
						break;
					}
				}

				try {
					Class<? extends IViewDto> dtoClass = daoRegistry.getDtoFromEntityName(tableIndexName, false, false);
					if (dtoClass == null) {
						continue;
					}
					if (!elasticSearchEnablement.isViewIndexable(dtoClass)) {
						indexService.deleteIndex(dtoClass);
					}
				} catch (PuiElasticSearchDeleteIndexException | IllegalArgumentException e) {
					try {
						indexService.deleteIndex(index);
					} catch (PuiElasticSearchDeleteIndexException e2) {
					}
				}
			}

			Map<String, List<String>> viewsMap = viewsAnalysis.getIndexableViewsMap();
			for (String view : viewsMap.keySet()) {
				Class<? extends IViewDao> viewDaoClass = daoRegistry.getDaoFromEntityName(view, false);
				if (viewDaoClass == null) {
					continue;
				}
				IViewDao viewDao = PuiApplicationContext.getInstance().getBean(viewDaoClass);
				if (viewDao == null) {
					continue;
				}

				Class<? extends IViewDto> dtoClass = daoRegistry.getDtoFromDao(viewDaoClass, false);
				if (!elasticSearchEnablement.isViewIndexable(dtoClass)) {
					continue;
				}

				// if a view name is passed as parameter, only continue if the current view is
				// the same
				if (paramViewDtoClass != null && !paramViewDtoClass.isAssignableFrom(dtoClass)) {
					continue;
				}

				boolean hasLanguage = DtoRegistry.getJavaFieldFromColumnName(dtoClass, IDto.LANG_COLUMN_NAME) != null;

				addViewToSynchCache(dtoClass);

				// check index exists
				boolean exists;
				try {
					exists = indexService.existsIndex(dtoClass);
					if (exists && force) {
						exists = false;
						try {
							indexService.deleteIndex(dtoClass);
						} catch (PuiElasticSearchDeleteIndexException e) {
							// should never happens
							removeViewFromSynchCache(dtoClass);
							continue;
						}
					}
				} catch (PuiElasticSearchExistsIndexException e) {
					exists = false;
					try {
						indexService.deleteIndex(dtoClass);
					} catch (PuiElasticSearchDeleteIndexException e2) {
						// should never happens
						removeViewFromSynchCache(dtoClass);
						continue;
					}
				}

				if (exists) {
					// if exists, check if the mapping is the same from the existing index and the
					// DTO class
					exists = indexService.checkIndexMapping(dtoClass);
					if (!exists) {
						try {
							indexService.deleteIndex(dtoClass);
						} catch (PuiElasticSearchDeleteIndexException e) {
							// should never happens
							removeViewFromSynchCache(dtoClass);
							continue;
						}
					}
				}

				if (exists) {
					// if exists, check the index content size to be correct (all the indexes
					// (languages) of the same view should has the same size)
					try {
						long indexCount = indexService.countIndex(dtoClass);

						// if success, obtain the view content size
						FilterBuilder filterBuilder = null;
						if (hasLanguage) {
							// if dto has language, add the filter for it using the default language
							filterBuilder = FilterBuilder.newOrFilter()
									.addEquals(IDto.LANG_COLUMN_NAME,
											PuiLanguageUtils.getDefaultLanguage().getIsocode())
									.addIsNull(IDto.LANG_COLUMN_NAME);
						}

						long viewCount;
						try {
							viewCount = viewDao.count(filterBuilder);
						} catch (PuiDaoCountException e) {
							logger.info("The View '" + dtoClass.getSimpleName() + "' doesn't exist");
							removeViewFromSynchCache(dtoClass);
							continue;
						}

						// if size from view and elastic search doesn't coincide, delete the index
						if (indexCount != viewCount) {
							try {
								logger.info("The View and the Index size is not the same for '"
										+ dtoClass.getSimpleName() + "'. Delete the indexes and create them again");
								indexService.deleteIndex(dtoClass);
								exists = false;
							} catch (PuiElasticSearchDeleteIndexException e1) {
								// should never happens
								removeViewFromSynchCache(dtoClass);
								continue;
							}
						}
					} catch (PuiElasticSearchCountException e) {
						// if an error in the count exists, remove the index. This is because the
						// distint indexes for each language has distinct size
						try {
							logger.info("The Indexes for '" + dtoClass.getSimpleName()
									+ "' has distinct size. Delete the indexes and create them again");
							indexService.deleteIndex(dtoClass);
							exists = false;
						} catch (PuiElasticSearchDeleteIndexException e1) {
							// should never happens
							removeViewFromSynchCache(dtoClass);
							continue;
						}
					}
				}

				if (!exists) {
					// if the index doesn't exists, create it...
					try {
						logger.info("Creating indexes for '" + viewDao + "'");
						indexService.createIndex(dtoClass);
					} catch (PuiElasticSearchCreateIndexException e) {
						// should never happens
						logger.error("Error while indexing the View '" + dtoClass.getSimpleName() + "'");
					}

					Iterator<PuiLanguage> it;
					if (hasLanguage) {
						it = PuiLanguageUtils.getLanguagesIterator();
						logger.info("Filling indexes for '" + dtoClass.getSimpleName() + "' (all languages)");
					} else {
						it = Collections.singletonList(PuiLanguageUtils.getDefaultLanguage()).iterator();
						logger.info("Filling indexes for '" + dtoClass.getSimpleName() + "'");
					}

					OrderBuilder orderBuilder = OrderBuilder.newOrder();
					for (String identityField : viewsMap.get(view)) {
						orderBuilder.addOrder(Order.newOrderAsc(identityField));
					}

					// and fill it
					while (it.hasNext()) {
						addViewToSynchCache(dtoClass);
						PuiLanguage language = it.next();

//						new Thread(new Runnable() {
//							@Override
//							public void run() {
						int i = 0;
						List<IViewDto> list;
						for (;;) {
							try {
								list = (List<IViewDto>) viewDao.findAllPagination(null, orderBuilder, language, i++,
										SearchRequest.NUM_MAX_ROWS);
							} catch (PuiDaoFindException e) {
								list = Collections.emptyList();
							}
							if (list.isEmpty()) {
								break;
							}

							logger.info("Indexing " + list.size() + " registries");
							try {
								documentService.bulkInsertDocument(list, language);
							} catch (PuiElasticSearchInsertDocumentException | PuiElasticSearchNoNodesException e) {
								logger.error("Error while indexing the View '" + dtoClass.getSimpleName() + "': "
										+ e.getMessage());
							}
						}

						removeViewFromSynchCache(dtoClass);
//							}
//						}).start();
					}

					logger.info("The view '" + dtoClass.getSimpleName() + "' is now synchronized");
				} else {
					// the View and the Index are synchronized
					logger.info("The view '" + dtoClass.getSimpleName() + "' was synchronized");
					removeViewFromSynchCache(dtoClass);
				}
			}

			liveSynch.wakeUpThreads();
		} catch (PuiElasticSearchNoNodesException e) {
			logger.error("Error while indexing the ElasticSearch: " + e.getMessage());
		} finally {
			liveSynch.wakeUpThreads();
		}

		logger.info("Finishing Elastic Search synchronization");
	}

	private void addViewToSynchCache(Class<? extends IViewDto> dtoClass) {
		if (!synchronizingViewCache.containsKey(dtoClass)) {
			elasticSearchEnablement.addSynchronizingView(dtoClass);
			synchronizingViewCache.put(dtoClass, new AtomicInteger(0));
		} else {
			synchronizingViewCache.get(dtoClass).incrementAndGet();
		}
	}

	private void removeViewFromSynchCache(Class<? extends IViewDto> dtoClass) {
		AtomicInteger aInt = synchronizingViewCache.get(dtoClass);
		if (aInt == null) {
			liveSynch.wakeUpThreads();
			return;
		}

		int val = aInt.decrementAndGet();
		if (val <= 0) {
			synchronizingViewCache.remove(dtoClass);
			elasticSearchEnablement.removeSynchronizingView(dtoClass);
			logger.info("The view '" + dtoClass.getSimpleName() + "' is now synchronized");
		}
	}

}
