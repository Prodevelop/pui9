package es.prodevelop.pui9.elasticsearch.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.controller.AbstractPuiController;
import es.prodevelop.pui9.elasticsearch.components.ViewsAnalysis;
import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchSynchronization;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchIndexService;
import es.prodevelop.pui9.eventlistener.event.DeleteDaoEvent;
import es.prodevelop.pui9.eventlistener.event.InsertDaoEvent;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.eventlistener.event.UpdateDaoEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.utils.IPuiObject;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This controller provide some useful methods to manage Elastic Search for the
 * application. It allows to activate or deactivate it, get the information of
 * the indexes, reindex an index, delete an index, index a new document...
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@RequestMapping("/elasticsearch")
@Api(tags = "PUI Elasticsearch")
public class PuiElasticSearchController extends AbstractPuiController {

	private static final String ID_FUNCTIONALITY_ADMIN = "admin";
	private static final String FUNCTIONALITY_ADMIN = "ADMIN_ELASTICSEARCH";

	@Autowired(required = false)
	private IPuiElasticSearchEnablement elasticSearchEnablement;

	@Autowired(required = false)
	private IPuiElasticSearchIndexService elasticSearchIndex;

	@Autowired(required = false)
	private IPuiElasticSearchSynchronization elasticSearchSynchronization;

	@Autowired(required = false)
	private ViewsAnalysis viewsAnalysis;

	@Autowired
	private DaoRegistry daoRegistry;

	private Map<String, List<PkFieldInfo>> tablePkCache = new HashMap<>();

	@Override
	protected String getReadFunctionality() {
		return "READ_ELASTICSEARCH";
	}

	@Override
	protected String getWriteFunctionality() {
		return "WRITE_ELASTICSEARCH";
	}

	/**
	 * Allow to reindex the index represented by the given view name
	 * 
	 * @param view  The name of the view you want to reindex the associated index
	 *              (or indexes, in case of translated view)
	 * @param force If set to true, the index will be always reindexed; if set to
	 *              false, it will be reindexed only if it's not valid (distinct
	 *              number of registries in the database view and in the index)
	 * @return A message saying that Elastic Search is being updated
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@ApiOperation(value = "Reindex ElasticSearch", notes = "Reindex ElasticSearch depending on the given parameters")
	@GetMapping(value = "/reindex", produces = MediaType.APPLICATION_JSON_VALUE)
	public String reindex(
			@ApiParam(value = "The view name of the index to be reindexed") @RequestParam(required = false) String view,
			@ApiParam(value = "Force reindex, even if it's valid") @RequestParam(required = false) boolean force) {
		if (elasticSearchEnablement == null) {
			return "Elastic search is not available";
		}

		new Thread(() -> elasticSearchSynchronization.synchronize(view, force),
				"PuiThread_ElasticSearchSynchronizerController").start();

		StringBuilder sb = new StringBuilder();
		sb.append("Elastic Search synchronization process is running in background. ");
		sb.append("It may take several minutes until it finishes, ");
		sb.append("depending on the number of views to index ");
		sb.append("and the number of registries of each view, ");
		sb.append("so be patient... \\^_^/");

		return sb.toString();
	}

	/**
	 * Activate or deactivate Elastic Search for this application
	 * 
	 * @param active true to activate; false to deactivate
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@ApiOperation(value = "Activate or deactivate ElasticSearch", notes = "Activate or deactivate ElasticSearch status")
	@GetMapping(value = "/setActive")
	public void setActive(@ApiParam(value = "Active or deactive", required = true) @RequestParam Boolean active) {
		if (elasticSearchEnablement == null) {
			return;
		}

		elasticSearchEnablement.setElasticSearchActive(active);
	}

	/**
	 * Set an index represented by the given view as blocked of unblocked. Valid if
	 * you want to disable Elastic Search only for certain views
	 * 
	 * @param view    The name of the view you want to block or unblock the
	 *                associated index (or indexes, in case of translated view)
	 * @param blocked true to block; false to unblock
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@ApiOperation(value = "Set index as blocked or not", notes = "Set an index as blocked or not")
	@GetMapping(value = "/setBlocked")
	public void setBlocked(
			@ApiParam(value = "The view that represents the index", required = true) @RequestParam String view,
			@ApiParam(value = "Blocked or not", required = true) @RequestParam boolean blocked) {
		if (elasticSearchEnablement == null) {
			return;
		}

		view = view.trim().toLowerCase();
		if (!daoRegistry.existsDaoForEntity(view)) {
			return;
		}

		Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
		if (viewDtoClass == null) {
			return;
		}

		if (blocked) {
			elasticSearchEnablement.addBlockedView(viewDtoClass);
		} else {
			elasticSearchEnablement.removeBlockedView(viewDtoClass);
		}
	}

	/**
	 * Delete the index represented by the given view
	 * 
	 * @param view The name of the view you want to delete the associated index (or
	 *             indexes, in case of translated view)
	 * @throws PuiElasticSearchNoNodesException     If Elastic Search is not
	 *                                              connected to any Node
	 * @throws PuiElasticSearchDeleteIndexException If any error occurs while
	 *                                              deleting the Index
	 */
	@PuiFunctionality(id = ID_FUNCTIONALITY_ADMIN, value = FUNCTIONALITY_ADMIN)
	@ApiOperation(value = "Delete the index of the given view", notes = "Delete the index of the given view")
	@DeleteMapping(value = "/deleteIndex")
	public void deleteIndex(
			@ApiParam(value = "The view that represents the index", required = true) @RequestParam String view)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchDeleteIndexException {
		if (elasticSearchEnablement == null) {
			return;
		}

		view = view.trim().toLowerCase();
		if (!daoRegistry.existsDaoForEntity(view)) {
			return;
		}

		Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
		if (viewDtoClass == null) {
			return;
		}

		elasticSearchIndex.deleteIndex(viewDtoClass);
	}

	/**
	 * Indicate Elastic Search that a document should be inserted, updated or
	 * deleted from the index.
	 * <p>
	 * It is commonly used in applications that not only the own application manages
	 * the used database. This case, the other applications should call this method
	 * in order to maintain synchronized the Elastic Search indexes
	 * 
	 * @param type  The operation type: insert, update or delete
	 * @param table The name of the affected table
	 * @param pk    The PK of the registry to be processed
	 * @return Always returns true if all worked fine
	 * @throws PuiException If Elastic Search is not enabled for the application; if
	 *                      the table is not mapped in the code or if the PK is not
	 *                      well formed for the given table
	 */
	@SuppressWarnings("rawtypes")
	@PuiFunctionality(id = ID_FUNCTIONALITY_INSERT, value = METHOD_FUNCTIONALITY_INSERT)
	@ApiOperation(value = "Insert, Update or Delete the given document from the given view", notes = "Insert, Update or Delete the given document from the given view")
	@PostMapping(value = "/document", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void document(
			@ApiParam(value = "Type of operation", required = true) @RequestParam DocumentOperationType type,
			@ApiParam(value = "Name of the table to operate with", required = true) @RequestParam String table,
			@ApiParam(value = "PK of the element", required = true) @RequestBody String pk) throws PuiException {
		if (elasticSearchEnablement == null) {
			throw new PuiException("Elastic Search is not enabled for this application");
		}

		table = table.trim().toLowerCase();
		Class<? extends ITableDao> tableDaoClass;
		if (ITableDao.class.isAssignableFrom(daoRegistry.getDaoFromEntityName(table, false))) {
			// table really is a table
			tableDaoClass = daoRegistry.getDaoFromEntityName(table, false);
		} else {
			// table maybe is a view? find the associated table
			Class<? extends IViewDao> viewDaoClass = daoRegistry.getDaoFromEntityName(table, false);
			tableDaoClass = daoRegistry.getTableDaoFromViewDao(viewDaoClass);
		}

		if (tableDaoClass == null) {
			throw new PuiException("The given table is not mapped in the code");
		}

		Class<? extends ITableDto> tableDtoClass = daoRegistry.getDtoFromDao(tableDaoClass, true);
		ITableDto dtoPk;
		try {
			dtoPk = getGson().fromJson(pk, tableDtoClass);
		} catch (Exception e) {
			throw new PuiException("Could not parse the given Dto Pk to the given table");
		}

		PuiEvent event = null;
		switch (type) {
		case insert:
			event = new InsertDaoEvent(dtoPk);
			break;
		case update:
			event = new UpdateDaoEvent(dtoPk, null);
			break;
		case delete:
			event = new DeleteDaoEvent(dtoPk);
			break;
		}

		logger.debug("Reveived an '" + type + "' operation for Elastic Search against table '" + table + "' with pk '"
				+ pk + "'");

		getEventLauncher().fireSync(event);
	}

	/**
	 * Count the number of registries in the database
	 * 
	 * @param view    The view to be checked
	 * @param hasLang If want to take into account the languages or not
	 * @return The number of registries in the database
	 * @throws PuiException If an exception occurs while counting
	 */
	@SuppressWarnings("rawtypes")
	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@ApiOperation(value = "Number of elements indexed for the view", notes = "Get the number of indexed elements for the given view")
	@GetMapping(value = "/count", produces = MediaType.APPLICATION_JSON_VALUE)
	public Long count(@ApiParam(value = "The view", required = true) @RequestParam String view,
			@ApiParam(value = "Has language support", required = true) @RequestParam boolean hasLang)
			throws PuiException {
		Class<? extends IViewDao> viewDaoClass = daoRegistry.getDaoFromEntityName(view, false);
		if (viewDaoClass == null) {
			return -1L;
		}

		IViewDao viewDao;
		try {
			viewDao = PuiApplicationContext.getInstance().getBean(viewDaoClass);
		} catch (Exception e) {
			return -1L;
		}

		Long count = viewDao.count();
		if (hasLang) {
			count /= PuiLanguageUtils.getLanguageCount();
		}

		return count;
	}

	/**
	 * Get a complete information about the current status of all the indexes of
	 * Elastic Search
	 * 
	 * @return The Elastic Search information
	 * @throws PuiException If any error occur while collecting the information
	 */
	@SuppressWarnings("rawtypes")
	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@ApiOperation(value = "Get ElasticSearch information", notes = "Get ElasticSearch information of the server")
	@GetMapping(value = "/getInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ElasticInfo getInfo() throws PuiException {
		ElasticInfo info = new ElasticInfo();

		if (elasticSearchEnablement == null) {
			return info;
		}

		info.available = elasticSearchEnablement.isElasticSearchAvailable();
		info.active = elasticSearchEnablement.isElasticSearchActive();
		info.isSynchronizingAnyView = elasticSearchEnablement.isSynchronizingAnyView();

		if (!info.active) {
			return info;
		}

		info.models = viewsAnalysis.getElasticSearchModels();

		for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
			info.languages.add(it.next());
		}

		if (info.languages.size() == 1) {
			info.languages.clear();
		}

		Collections.sort(info.languages);

		List<String> indexes;
		try {
			indexes = elasticSearchIndex.getAllIndexes();
		} catch (PuiException e) {
			indexes = Collections.emptyList();
		}

		Map<String, Long> counts = elasticSearchIndex.countIndex(indexes);

		Map<String, Map<PuiLanguage, Long>> countsLanguage = new HashMap<>();
		for (Entry<String, Long> entry : counts.entrySet()) {
			String index = entry.getKey();
			Long count = entry.getValue();
			String tableIndexName = index;

			if (!StringUtils.isEmpty(elasticSearchIndex.getIndexPrefix())) {
				tableIndexName = tableIndexName.replaceAll(elasticSearchIndex.getIndexPrefix() + "_", "");
			}

			PuiLanguage language = null;
			for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
				PuiLanguage next = it.next();
				if (tableIndexName.endsWith("_" + next.getIsocode())) {
					language = next;
					tableIndexName = tableIndexName.substring(0, tableIndexName.lastIndexOf('_'));
					break;
				}
			}

			if (!daoRegistry.existsDaoForEntity(tableIndexName)) {
				continue;
			}

			if (!countsLanguage.containsKey(tableIndexName)) {
				countsLanguage.put(tableIndexName, new HashMap<>());
			}

			countsLanguage.get(tableIndexName).put(language, count);
		}

		for (Entry<String, Map<PuiLanguage, Long>> entry : countsLanguage.entrySet()) {
			String viewName = entry.getKey();
			Class<? extends IViewDto> viewDtoClass = daoRegistry.getDtoFromEntityName(viewName, false, false);
			if (viewDtoClass == null) {
				continue;
			}

			IndexInfo iInfo = new IndexInfo();
			iInfo.name = viewName;
			if (elasticSearchEnablement.isSynchronizingView(viewDtoClass)) {
				iInfo.status = IndexStatus.synchronizing;
			} else if (elasticSearchEnablement.isViewBlocked(viewDtoClass)) {
				iInfo.status = IndexStatus.blocked;
			}
			if (entry.getValue().size() == 1) {
				iInfo.count = entry.getValue().values().iterator().next();
				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					PuiLanguage lang = it.next();
					LangInfo li = new LangInfo();
					li.language = lang.getIsocode();
					li.count = -1L;
					iInfo.counts.add(li);
				}
			} else {
				Long prevCount = -1L;
				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					PuiLanguage lang = it.next();
					Long count = entry.getValue().get(lang) != null ? entry.getValue().get(lang) : 0L;
					if (prevCount.equals(-1L)) {
						prevCount = count;
					}
					LangInfo li = new LangInfo();
					li.language = lang.getIsocode();
					li.count = count;
					if (!prevCount.equals(-1L) && !prevCount.equals(count)
							&& !iInfo.status.equals(IndexStatus.blocked)) {
						iInfo.status = IndexStatus.error;
					}
					iInfo.counts.add(li);
				}
			}

			Collections.sort(iInfo.counts);

			Set<String> relatedTables = viewsAnalysis.getRelatedTables(viewName);
			for (String table : relatedTables) {
				RelatedTableInfo rtInfo = new RelatedTableInfo();
				rtInfo.name = table;

				if (!tablePkCache.containsKey(table)) {
					if (!daoRegistry.existsDaoForEntity(table)) {
						continue;
					}
					Class<? extends ITableDao> tableDaoClass = daoRegistry.getDaoFromEntityName(table, false);
					if (tableDaoClass == null) {
						tablePkCache.put(table, null);
						continue;
					}
					Class<? extends IDto> tableDtoClass = daoRegistry.getDtoFromDao(tableDaoClass, true);
					List<String> fieldNames = DtoRegistry.getPkFields(tableDtoClass);
					String tableModelId = daoRegistry.getModelIdFromDao(tableDaoClass);
					List<PkFieldInfo> pkInfoList = new ArrayList<>();

					for (String fieldName : fieldNames) {
						String fieldLabel = tableModelId + "." + fieldName;
						FieldTypeEnum type = null;
						if (DtoRegistry.getStringFields(tableDtoClass).contains(fieldName)) {
							type = FieldTypeEnum.string;
						} else if (DtoRegistry.getNumericFields(tableDtoClass).contains(fieldName)) {
							type = FieldTypeEnum.numeric;
						} else if (DtoRegistry.getDateTimeFields(tableDtoClass).contains(fieldName)) {
							type = FieldTypeEnum.date;
						}
						PkFieldInfo pkField = new PkFieldInfo();
						pkField.name = fieldName;
						pkField.label = fieldLabel;
						pkField.type = type;
						pkInfoList.add(pkField);
					}

					tablePkCache.put(table, pkInfoList);
				}

				rtInfo.pk = tablePkCache.get(table);
				if (rtInfo.pk == null) {
					rtInfo.pk = new ArrayList<>();
				}

				iInfo.relatedTables.add(rtInfo);
			}

			Collections.sort(iInfo.relatedTables);

			info.indexes.add(iInfo);
		}

		Collections.sort(info.indexes);

		return info;
	}

	@SuppressWarnings("unused")
	private class ElasticInfo implements IPuiObject {

		private static final long serialVersionUID = 1L;

		boolean available = false;
		boolean active = false;
		boolean isSynchronizingAnyView = false;
		List<String> models = new ArrayList<>();
		List<PuiLanguage> languages = new ArrayList<>();
		List<IndexInfo> indexes = new ArrayList<>();
	}

	@SuppressWarnings("unused")
	private class IndexInfo implements Comparable<IndexInfo> {
		String name = "";
		Long count = -1L;
		List<LangInfo> counts = new ArrayList<>();
		Long bbdd = -1L;
		IndexStatus status = IndexStatus.wait;
		List<RelatedTableInfo> relatedTables = new ArrayList<>();

		@Override
		public int compareTo(IndexInfo o) {
			return name.compareTo(o.name);
		}
	}

	@SuppressWarnings("unused")
	private class LangInfo implements Comparable<LangInfo> {
		String language;
		Long count;

		@Override
		public int compareTo(LangInfo o) {
			return language.compareTo(o.language);
		}
	}

	private class RelatedTableInfo implements Comparable<RelatedTableInfo> {
		String name = "";
		List<PkFieldInfo> pk = new ArrayList<>();

		@Override
		public int compareTo(RelatedTableInfo o) {
			return name.compareTo(o.name);
		}
	}

	@SuppressWarnings("unused")
	private class PkFieldInfo {
		String name = "";
		String label = "";
		FieldTypeEnum type = FieldTypeEnum.string;
		String value = null;
	}

	private static enum FieldTypeEnum {
		string, numeric, date;
	}

	private enum IndexStatus {
		wait, valid, synchronizing, blocked, error
	}

}
