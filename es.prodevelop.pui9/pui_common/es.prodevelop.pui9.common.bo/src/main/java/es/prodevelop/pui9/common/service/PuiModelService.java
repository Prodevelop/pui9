package es.prodevelop.pui9.common.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.JsonSyntaxException;

import es.prodevelop.pui9.common.exceptions.PuiCommonModelException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.PuiModelPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfig;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelFilterService;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelConfigService;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserModelFilterService;
import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.configuration.ModelConfiguration;
import es.prodevelop.pui9.model.configuration.PuiModelColumn;
import es.prodevelop.pui9.model.configuration.PuiModelConfiguration;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.mvc.configuration.IPuiRequestMappingHandlerMapping;
import es.prodevelop.pui9.mvc.configuration.PuiControllersInfo;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

/**
 * @generated
 */
@Service
public class PuiModelService extends AbstractService<IPuiModelPk, IPuiModel, INullView, IPuiModelDao, INullViewDao>
		implements IPuiModelService {

	@Autowired
	private IPuiUserModelFilterService userModelFilterService;

	@Autowired
	private IPuiModelFilterService modelFilterService;

	@Autowired
	private IPuiUserModelConfigService userModelConfigService;

	@Autowired
	private IPuiRequestMappingHandlerMapping requestMapping;

	private PuiModelConfigurationUtil modelConfigUtil;
	private Map<String, PuiModelConfiguration> modelConfigCache;
	private Type modelConfigType = new TypeToken<Map<String, PuiModelConfiguration>>() {
		private static final long serialVersionUID = 1L;
	}.getType();

	@PostConstruct
	private void postConstruct() {
		modelConfigUtil = new PuiModelConfigurationUtil();
		modelConfigCache = new HashMap<>();

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadPuiModels", true, 1, 1, TimeUnit.HOURS,
				() -> reloadModels(true));
	}

	@Override
	public synchronized void reloadModels(boolean force) {
		if (force || CollectionUtils.isEmpty(modelConfigCache)) {
			modelConfigCache.clear();
			modelConfigCache.putAll(modelConfigUtil.getModelConfiguration());
		}
	}

	@Override
	public IPuiModel guessModel(SearchRequest req) throws PuiServiceGetException {
		IPuiModel puiModel = null;
		if (StringUtils.isEmpty(req.getModel())) {
			if (req.getViewDtoClass() != null) {
				puiModel = getModelFromEntity(
						getDaoRegistry().getEntityName(getDaoRegistry().getDaoFromDto(req.getViewDtoClass())));
			}

			if (puiModel == null && req.getTableDtoClass() != null) {
				puiModel = getModelFromEntity(
						getDaoRegistry().getEntityName(getDaoRegistry().getDaoFromDto(req.getTableDtoClass())));
			}
		} else {
			puiModel = getByPk(new PuiModelPk(req.getModel()));
		}

		if (puiModel == null) {
			throw new PuiServiceGetException(new PuiCommonModelException(req.getModel()));
		}

		req.setModel(puiModel.getModel());

		return puiModel;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <TYPE> SearchResponse<TYPE> search(SearchRequest req) throws PuiServiceGetException {
		IPuiModel puiModel = guessModel(req);

		if (StringUtils.isEmpty(req.getQueryLang())) {
			req.setQueryLang(getSession().getLanguage().getIsocode());
		}

		Class<? extends IDao> daoClass = getDaoRegistry().getDaoFromEntityName(puiModel.getEntity(), false);
		if (daoClass == null) {
			throw new PuiServiceGetException(new Exception("No dao exists for entity " + puiModel.getEntity()));
		}
		IDao<?> dao = (IDao<?>) getContext().getBean(daoClass);

		try {
			req.setDbFilters(FilterGroup.fromJson(puiModel.getFilter()));
		} catch (JsonSyntaxException e) {
			req.setDbFilters(IListAdapter.EMPTY_FILTER);
		}

		req.setDbTableName(puiModel.getEntity());
		req.setGridAdapter(dao.getGridAdapter());
		try {
			return (SearchResponse<TYPE>) dao.findForDataGrid(req);
		} catch (PuiDaoListException e) {
			throw new PuiServiceGetException(e);
		}
	}

	private IPuiModel getModelFromEntity(String entityName) {
		try {
			List<IPuiModel> puiModels = getTableDao().findByEntity(entityName);
			return CollectionUtils.isEmpty(puiModels) ? null : puiModels.get(0);
		} catch (PuiDaoFindException e) {
			return null;
		}
	}

	@Override
	public Map<String, PuiModelConfiguration> getPuiModelConfigurations() {
		if (CollectionUtils.isEmpty(modelConfigCache)) {
			reloadModels(false);
		}

		String json = GsonSingleton.getSingleton().getGson().toJson(modelConfigCache);
		Map<String, PuiModelConfiguration> config = GsonSingleton.getSingleton().getGson().fromJson(json,
				modelConfigType);
		modelConfigUtil.fillUserInformation(config);
		return config;
	}

	@Override
	public Map<String, PuiModelConfiguration> getOriginalPuiModelConfigurations() {
		if (CollectionUtils.isEmpty(modelConfigCache)) {
			reloadModels(false);
		}
		return modelConfigCache;
	}

	@Override
	public List<String> getAllModels() {
		List<String> allModels = new ArrayList<>();
		try {
			getAll().forEach(model -> allModels.add(model.getModel()));
		} catch (PuiServiceGetException e) {
			// do nothing
		}
		return allModels;
	}

	private class PuiModelConfigurationUtil {

		public Map<String, PuiModelConfiguration> getModelConfiguration() {
			Map<String, PuiModelConfiguration> map = new HashMap<>();

			List<IPuiModel> models;
			try {
				models = PuiModelService.this.getAll();
			} catch (PuiServiceGetException e) {
				models = Collections.emptyList();
			}

			for (IPuiModel model : models) {
				PuiModelConfiguration config = new PuiModelConfiguration(model.getModel(), model.getEntity());
				try {
					ModelConfiguration defaultConfig = GsonSingleton.getSingleton().getGson()
							.fromJson(model.getConfiguration(), ModelConfiguration.class);
					if (defaultConfig != null) {
						config.setDefaultConfiguration(defaultConfig);
					}
				} catch (Exception e) {
					// do nothing
				}
				addColumns(config, model.getEntity());
				if (!config.getColumns().isEmpty()) {
					map.put(model.getModel(), config);
				}
			}

			addModelFilters(map);

			Map<String, PuiControllersInfo> controllerInfo = requestMapping.getUrlsAndFunctionalitiesByController();
			controllerInfo.forEach((model, info) -> {
				if (!map.containsKey(model)) {
					PuiModelConfiguration config = new PuiModelConfiguration(model);
					map.put(model, config);
				}
				map.get(model).getFunctionalities().putAll(info.getFunctionalities());
				map.get(model).getUrl().putAll(info.getUrl());
			});

			return map;
		}

		public void fillUserInformation(Map<String, PuiModelConfiguration> config) {
			if (PuiUserSession.getCurrentSession() == null) {
				return;
			}

			addUserModelFilters(config);
			addUserConfigurations(config);
		}

		/**
		 * Add the columns of the given entity (typically is a view) to the model
		 * configuration
		 * 
		 * @param config The model configuration
		 * @param entity The entity to retrieve the columns (typically a view)
		 */
		@SuppressWarnings("rawtypes")
		private void addColumns(PuiModelConfiguration config, String entity) {
			Class<? extends IDao> entityDaoClass = PuiModelService.this.getDaoRegistry().getDaoFromEntityName(entity,
					false);
			if (entityDaoClass == null) {
				return;
			}

			String id = PuiModelService.this.getDaoRegistry().getModelIdFromDao(entityDaoClass);
			Class<? extends IDto> entityDtoClass = PuiModelService.this.getDaoRegistry().getDtoFromDao(entityDaoClass,
					false);
			List<String> fields = DtoRegistry.getAllFields(entityDtoClass);
			List<String> pkFields = new ArrayList<>();
			try {
				Class<? extends IDto> tableDtoClass = PuiModelService.this.getDaoRegistry()
						.getTableDtoFromModelId(config.getName(), true);
				if (tableDtoClass != null) {
					pkFields = DtoRegistry.getPkFields(tableDtoClass);
				}
			} catch (Exception e) {
				// do nothing
			}

			for (String fieldName : fields) {
				String title = id + "." + fieldName;
				boolean isPk = pkFields.contains(fieldName);
				ColumnVisibility visibility = DtoRegistry.getColumnVisibility(entityDtoClass, fieldName);
				ColumnType type;
				if (DtoRegistry.getStringFields(entityDtoClass).contains(fieldName)) {
					type = ColumnType.text;
				} else if (DtoRegistry.getNumericFields(entityDtoClass).contains(fieldName)) {
					type = ColumnType.numeric;
				} else if (DtoRegistry.getFloatingFields(entityDtoClass).contains(fieldName)) {
					type = ColumnType.decimal;
				} else if (DtoRegistry.getDateTimeFields(entityDtoClass).contains(fieldName)) {
					type = ColumnType.datetime;
				} else if (DtoRegistry.getBooleanFields(entityDtoClass).contains(fieldName)) {
					type = ColumnType.logic;
				} else {
					type = ColumnType.text;
				}

				PuiModelColumn pmc = new PuiModelColumn(fieldName, title, type, isPk, visibility);
				config.addColumn(pmc);
			}
		}

		private void addModelFilters(Map<String, PuiModelConfiguration> config) {
			FilterBuilder filterBuilder = FilterBuilder.newAndFilter().addInString(IPuiUserModelFilter.MODEL_COLUMN,
					new ArrayList<>(config.keySet()));
			List<IPuiModelFilter> modelFilters;
			try {
				modelFilters = modelFilterService.getAllWhere(filterBuilder);
			} catch (PuiServiceGetException e) {
				modelFilters = Collections.emptyList();
			}

			for (IPuiModelFilter pmf : modelFilters) {
				if (config.containsKey(pmf.getModel())) {
					config.get(pmf.getModel()).addModelFilter(pmf);
				}
			}
		}

		private void addUserModelFilters(Map<String, PuiModelConfiguration> config) {
			FilterBuilder filterBuilder = FilterBuilder.newAndFilter()
					.addInString(IPuiUserModelFilter.MODEL_COLUMN, new ArrayList<>(config.keySet()))
					.addEquals(IPuiUserModelFilter.USR_COLUMN, PuiUserSession.getCurrentSession().getUsr());
			List<IPuiUserModelFilter> userFilters;
			try {
				userFilters = userModelFilterService.getAllWhere(filterBuilder);
			} catch (PuiServiceGetException e) {
				userFilters = Collections.emptyList();
			}
			for (IPuiUserModelFilter umf : userFilters) {
				config.get(umf.getModel()).addUserFilter(umf);
			}
		}

		private void addUserConfigurations(Map<String, PuiModelConfiguration> config) {
			FilterBuilder filterBuilder = FilterBuilder.newAndFilter()
					.addInString(IPuiUserModelConfig.MODEL_COLUMN, new ArrayList<>(config.keySet()))
					.addEquals(IPuiUserModelFilter.USR_COLUMN, PuiUserSession.getCurrentSession().getUsr());
			List<IPuiUserModelConfig> configurations;
			try {
				configurations = userModelConfigService.getAllWhere(filterBuilder);
			} catch (PuiServiceGetException e) {
				configurations = Collections.emptyList();
			}
			for (IPuiUserModelConfig umc : configurations) {
				config.get(umc.getModel()).addConfiguration(umc.getType(), umc);
			}
		}

	}

}