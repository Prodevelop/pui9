package es.prodevelop.pui9.elasticsearch.services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest.OpType;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiElasticsearchViewsDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViews;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.elasticsearch.components.ViewsAnalysis;
import es.prodevelop.pui9.elasticsearch.components.ViewsAnalysis.JoinTableDef;
import es.prodevelop.pui9.elasticsearch.dto.ESSearchResultItem;
import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchExistsIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchInsertDocumentException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchSearchException;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchDocumentService;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchIndexService;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchSearchingService;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * Implementation for the API to manage Documents for ElasticSaerch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchDocumentService extends AbstractPuiElasticSearchService
		implements IPuiElasticSearchDocumentService {

	private static final String NO_LANG = "ZZ";
	private static final String PK_SEPARATOR = "#";

	@Autowired
	private IPuiElasticSearchIndexService indexService;

	@Autowired
	private IPuiElasticSearchSearchingService searchingService;

	@Autowired
	private ViewsAnalysis viewsAnalysis;

	@Autowired
	private PuiApplicationContext context;

	@Autowired
	private IPuiElasticsearchViewsDao elasticsearchViewsDao;

	@Qualifier("appname")
	@Autowired(required = false)
	private String appname = "DEFAULT";

	private Map<String, List<String>> idsCache = new HashMap<>();

	@PostConstruct
	private void postConstruct() {
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ElasticSearch_RefreshIndexes", true, 0, 10,
				TimeUnit.MINUTES, () -> {
					try {
						idsCache.clear();
						List<IPuiElasticsearchViews> list = elasticsearchViewsDao.findByAppname(appname);
						idsCache = list.stream()
								.collect(Collectors.toMap(IPuiElasticsearchViews::getViewname,
										esv -> Stream.of(esv.getIdentityfields().split(",", -1)).map(String::trim)
												.map(String::toLowerCase).collect(Collectors.toList())));
					} catch (PuiDaoFindException e) {
						// do nothing
					}
				});
	}

	@Override
	public void insertDocument(ITableDto dtoPk, String view, FilterGroup pkFilter) {
		innerInsertOrUpdate(dtoPk, view, pkFilter, DocumentOperationType.insert);
	}

	@Override
	public void updateDocument(ITableDto dtoPk, String view, FilterGroup pkFilter) {
		innerInsertOrUpdate(dtoPk, view, pkFilter, DocumentOperationType.update);
	}

	@Override
	public void deleteDocument(ITableDto dtoPk, String view, FilterGroup pkFilter) {
		innerDeleteDocument(dtoPk, view, pkFilter);
	}

	@Override
	public void bulkInsertDocument(List<IViewDto> dtoList, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchInsertDocumentException {
		if (CollectionUtils.isEmpty(dtoList)) {
			return;
		}

		Class<? extends IViewDto> viewDtoClass = dtoList.get(0).getClass();
		if (INullView.class.isAssignableFrom(viewDtoClass)) {
			return;
		}

		try {
			if (!indexService.existsIndex(viewDtoClass, language)) {
				indexService.createIndex(viewDtoClass, language);
			}
		} catch (PuiElasticSearchExistsIndexException | PuiElasticSearchCreateIndexException e) {
			return;
		}

		String index = getIndexForLanguage(viewDtoClass, language);

		BulkRequest request = new BulkRequest();
		for (IViewDto dto : dtoList) {
			String id = getIdForDto(dto);
			String json = GsonSingleton.getSingleton().getGson().toJson(dto);

			IndexRequest indReq = new IndexRequest();
			indReq.opType(OpType.CREATE);
			indReq.index(index);
			indReq.id(id);
			indReq.source(json, XContentType.JSON);
			request.add(indReq);
		}

		if (request.numberOfActions() == 0) {
			return;
		}

		BulkResponse response;
		try {
			response = getClient().bulk(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			logger.debug("Could not synchronize set of documents into '" + index + "'");
			throw new PuiElasticSearchInsertDocumentException(index);
		}

		if (response.status().equals(RestStatus.CREATED) || response.status().equals(RestStatus.OK)) {
			logger.debug("An amount of " + dtoList.size() + " documents were synchronized into '" + index + "'");
		} else {
			logger.debug("Could not synchronize set of documents into '" + index + "': " + response.status());
			throw new PuiElasticSearchInsertDocumentException(index);
		}
	}

	private void innerInsertOrUpdate(ITableDto dtoPk, String view, FilterGroup pkFilter, DocumentOperationType opType) {
		Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
		if (viewDtoClass == null) {
			return;
		}

		String table = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dtoPk.getClass()));
		List<LinkedList<JoinTableDef>> tableOrderList = viewsAnalysis.getTableOrder(table, view);

		try {
			List<String> indexes = getIndexesForDto(viewDtoClass);
			IViewDao<IViewDto> viewDao = context.getBean(daoRegistry.getDaoFromEntityName(view, false));
			if (viewDao == null) {
				return;
			}

			for (LinkedList<JoinTableDef> tableOrder : tableOrderList) {
				String sql = getPartialSql(tableOrder, dtoPk);

				Map<String, String> nameAliasMap = tableOrder.descendingIterator().next().getNameAliasMap();
				Class<IDao<IDto>> entityDaoClass = daoRegistry
						.getDaoFromEntityName(tableOrder.descendingIterator().next().getTableName(), false);
				if (entityDaoClass == null) {
					continue;
				}

				IDao<IDto> entityDao = context.getBean(entityDaoClass);
				List<IDto> result;
				try {
					result = entityDao.executeCustomQuery(sql);
				} catch (PuiDaoFindException e) {
					result = Collections.emptyList();
				}

				if (CollectionUtils.isEmpty(result)) {
					continue;
				}

				Class<? extends IDto> dtoPkClass = result.get(0).getClass();
				FilterBuilder filterBuilder = FilterBuilder.newOrFilter();
				for (IDto res : result) {
					FilterBuilder filterBuilderReg = FilterBuilder.newAndFilter();
					for (String pkFieldName : DtoRegistry.getPkFields(dtoPkClass)) {
						String aliasField = pkFieldName;
						if (nameAliasMap.containsKey(pkFieldName)) {
							aliasField = nameAliasMap.get(pkFieldName);
						}
						if (DtoRegistry.getColumnNames(viewDtoClass).contains(aliasField)) {
							try {
								Object value = DtoRegistry.getJavaFieldFromFieldName(dtoPkClass, pkFieldName).get(res);
								filterBuilderReg.addEquals(aliasField, value);
							} catch (IllegalArgumentException | IllegalAccessException e) {
								continue;
							} catch (NullPointerException e) {
								break;
							}
						}
					}
					filterBuilder.addGroup(filterBuilderReg);
				}

				// add pk filter
				filterBuilder = FilterBuilder.newAndFilter().addGroup(filterBuilder)
						.addGroup(FilterBuilder.newFilter(pkFilter));

				// obtain the registry from the view
				List<IViewDto> viewDtoList;
				try {
					viewDtoList = viewDao.findWhere(filterBuilder);
				} catch (PuiDaoFindException e) {
					// should never occurs
					continue;
				}

				Map<String, String> mapLangIndex = new HashMap<>();
				for (String index : indexes) {
					PuiLanguage language = getLanguageFromIndex(index);
					if (language != null) {
						mapLangIndex.put(language.getIsocode(), index);
					} else {
						mapLangIndex.put(NO_LANG, index);
					}

					// create the index if not exists
					try {
						if (!indexService.existsIndex(viewDtoClass, language)) {
							indexService.createIndex(viewDtoClass, language);
						}
					} catch (PuiElasticSearchExistsIndexException | PuiElasticSearchCreateIndexException e) {
						continue;
					}
				}

				BulkRequest request = new BulkRequest();

				for (IViewDto viewDto : viewDtoList) {
					PuiLanguage dtoLang = PuiLanguageUtils.getLanguage(viewDto);
					String lang = NO_LANG;
					if (dtoLang != null) {
						lang = dtoLang.getIsocode();
					}
					String index = mapLangIndex.get(lang);

					String id = getIdForDto(viewDto);
					String json = GsonSingleton.getSingleton().getGson().toJson(viewDto);

					if (opType.equals(DocumentOperationType.insert)) {
						IndexRequest req = new IndexRequest();
						req.opType(OpType.INDEX);
						req.index(index);
						req.id(id);
						req.source(json, XContentType.JSON);
						request.add(req);
					} else if (opType.equals(DocumentOperationType.update)) {
						GetRequest getReq = new GetRequest();
						getReq.index(index);
						getReq.id(id);
						GetResponse getResp = getClient().get(getReq, RequestOptions.DEFAULT);
						if (getResp.isExists()) {
							UpdateRequest req = new UpdateRequest();
							req.index(index);
							req.id(id);
							req.doc(json, XContentType.JSON);
							request.add(req);
						} else {
							IndexRequest req = new IndexRequest();
							req.opType(OpType.INDEX);
							req.index(index);
							req.id(id);
							req.source(json, XContentType.JSON);
							request.add(req);
						}
					}
				}

				if (request.numberOfActions() > 0) {
					getClient().bulkAsync(request, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
						@Override
						public void onResponse(BulkResponse response) {
							// do nothing
						}

						@Override
						public void onFailure(Exception e) {
							// do nothing
						}
					});
				}
			}
		} catch (PuiElasticSearchNoNodesException e) {
			// do nothing
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void innerDeleteDocument(ITableDto dtoPk, String view, FilterGroup pkFilter) {
		Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
		if (viewDtoClass == null) {
			return;
		}

		String table = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dtoPk.getClass()));
		List<LinkedList<JoinTableDef>> tableOrderList = viewsAnalysis.getTableOrder(table, view);

		try {
			List<String> indexes = getIndexesForDto(viewDtoClass);

			FilterBuilder filterBuilder = FilterBuilder.newAndFilter();
			for (LinkedList<JoinTableDef> tableOrder : tableOrderList) {
				Map<String, String> nameAliasMap = tableOrder.descendingIterator().next().getNameAliasMap();

				for (String pkFieldName : DtoRegistry.getPkFields(dtoPk.getClass())) {
					String aliasField = pkFieldName;
					if (nameAliasMap.containsKey(pkFieldName)) {
						aliasField = nameAliasMap.get(pkFieldName);
					}
					if (DtoRegistry.getColumnNames(viewDtoClass).contains(aliasField)) {
						try {
							Object value = DtoRegistry.getJavaFieldFromFieldName(dtoPk.getClass(), pkFieldName)
									.get(dtoPk);
							filterBuilder.addEquals(aliasField, value);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// do nothing
						} catch (NullPointerException e) {
							break;
						}
					}
				}
			}

			// add pk filter
			filterBuilder = FilterBuilder.newAndFilter().addGroup(filterBuilder)
					.addGroup(FilterBuilder.newFilter(pkFilter));

			for (String index : indexes) {
				BulkRequest request = new BulkRequest();
				PuiLanguage language = getLanguageFromIndex(index);

				try {
					if (!indexService.existsIndex(viewDtoClass, language)) {
						continue;
					}
				} catch (PuiElasticSearchExistsIndexException e) {
					continue;
				}

				// obtain the registry from Elastic Search
				List<ESSearchResultItem> list;
				try {
					list = searchingService.findMultiple(viewDtoClass, filterBuilder, null, language);
				} catch (PuiElasticSearchSearchException e1) {
					// should never occurs
					continue;
				}

				for (ESSearchResultItem item : list) {
					DeleteRequest req = new DeleteRequest();
					req.index(index);
					req.id(item.getId());
					request.add(req);
				}

				if (request.numberOfActions() > 0) {
					getClient().bulkAsync(request, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
						@Override
						public void onResponse(BulkResponse response) {
							// do nothing
						}

						@Override
						public void onFailure(Exception e) {
							// do nothing
						}
					});
				}
			}
		} catch (PuiElasticSearchNoNodesException e) {
			return;
		} catch (Exception e) {
			logger.error(e);
			return;
		}
	}

	private String getIdForDto(IViewDto dto) {
		String entityName = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dto.getClass()));
		List<String> ids = idsCache.get(entityName);
		StringBuilder sb = new StringBuilder();

		for (Iterator<String> it = ids.iterator(); it.hasNext();) {
			String next = it.next();
			Field field = DtoRegistry.getJavaFieldFromColumnName(dto.getClass(), next);
			if (field == null) {
				field = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), next);
			}
			try {
				sb.append(field.get(dto).toString());
			} catch (Exception e) {
				// do nothing
			}
			if (it.hasNext()) {
				sb.append(PK_SEPARATOR);
			}
		}

		return sb.toString();
	}

	private String getPartialSql(LinkedList<JoinTableDef> tableOrder, ITableDto dto) {
		StringBuilder sb = new StringBuilder();

		for (int i = tableOrder.size() - 1; i >= 0; i--) {
			JoinTableDef fromTableDef = tableOrder.get(i);
			if (sb.length() > 0 && fromTableDef.getJoinCode().toLowerCase().trim().startsWith("from")) {
				sb.append("\nUNION\n");
			}

			Class<? extends IDto> dtoClass = daoRegistry.getDtoFromEntityName(fromTableDef.getTableName(), false,
					false);
			if (dtoClass == null || !DtoRegistry.isRegistered(dtoClass)) {
				return null;
			}
			List<String> pkFields = DtoRegistry.getPkFields(dtoClass);

			StringBuilder sbProjection = new StringBuilder();
			for (Iterator<String> pkIt = pkFields.iterator(); pkIt.hasNext();) {
				String pkColumn = DtoRegistry.getColumnNameFromFieldName(dtoClass, pkIt.next());
				sbProjection.append(
						(fromTableDef.getTableAlias() != null ? fromTableDef.getTableAlias() + "." : "") + pkColumn);
				if (pkIt.hasNext()) {
					sbProjection.append(", ");
				}
			}
			sb.append("SELECT " + sbProjection.toString() + "\n");
			sb.append(fromTableDef.getJoinCode() + "\n");

			JoinTableDef lastJoinDef = fromTableDef;
			for (i--; i >= 0; i--) {
				JoinTableDef joinTableDef = tableOrder.get(i);
				if (joinTableDef.getJoinCode().toLowerCase().trim().startsWith("from")) {
					i++;
					break;
				}

				lastJoinDef = joinTableDef;
				sb.append(joinTableDef.getJoinCode() + "\n");
			}

			dtoClass = dto.getClass();
			pkFields = DtoRegistry.getPkFields(dtoClass);
			sb.append("WHERE ");
			try {
				for (Iterator<String> pkIt = pkFields.iterator(); pkIt.hasNext();) {
					String pkField = pkIt.next();
					String pkColumn = DtoRegistry.getColumnNameFromFieldName(dtoClass, pkField);
					Object value = DtoRegistry.getJavaFieldFromColumnName(dtoClass, pkColumn).get(dto);
					if (DtoRegistry.getStringFields(dtoClass).contains(pkField)) {
						value = "'" + value + "'";
					}
					sb.append((lastJoinDef.getTableAlias() != null ? lastJoinDef.getTableAlias()
							: lastJoinDef.getTableName()) + "." + pkColumn + " = " + value.toString());
					if (pkIt.hasNext()) {
						sb.append(" AND ");
					} else {
						sb.append("\n");
					}
				}
			} catch (Exception e) {
				return null;
			}
			sb.append("GROUP BY " + sbProjection.toString());
		}

		return sb.toString();
	}

}
