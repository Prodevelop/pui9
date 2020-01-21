package es.prodevelop.pui9.model.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelFilter;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelConfig;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserModelFilter;

/**
 * The full configuration of a model. It contains general information like: the
 * available URLs for a model (extracted from the associated controller); the
 * default configuration configured in PUI_MODEL; the list of functionalities
 * that are required for all of its web services; the list of columns of the
 * associated entity (and for each column the name, the translatable title, tye
 * type of the column, the visibility and if it belongs to the PK or not); the
 * list of general filters configured in PUI_MODEL_FILTER. Also contains custom
 * information per user like: the list of user filters associated to this model
 * configured in PUI_USER_MODEL_FILTER; the list of user configurations
 * associated to this model configured in PUI_USER_MODEL_CONFIG
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiModelConfiguration {

	private String name;
	private String entity;
	private ModelConfiguration defaultConfiguration = new ModelConfiguration();
	private Map<String, String> url = new HashMap<>();
	private Map<String, String> functionalities = new HashMap<>();
	private List<PuiModelColumn> columns = new ArrayList<>();
	private List<IPuiUserModelFilter> userFilters = new ArrayList<>();
	private List<IPuiModelFilter> modelFilters = new ArrayList<>();
	private Map<String, List<IPuiUserModelConfig>> configurations = new HashMap<>();

	public PuiModelConfiguration(String name) {
		this(name, null);
	}

	public PuiModelConfiguration(String name, String entity) {
		this.name = name;
		this.entity = entity;
	}

	public String getName() {
		return name;
	}

	public String getEntity() {
		return entity;
	}

	public ModelConfiguration getDefaultConfiguration() {
		return defaultConfiguration;
	}

	public void setDefaultConfiguration(ModelConfiguration defaultConfiguration) {
		this.defaultConfiguration = defaultConfiguration;
	}

	public Map<String, String> getUrl() {
		return url;
	}

	public Map<String, String> getFunctionalities() {
		return functionalities;
	}

	public List<PuiModelColumn> getColumns() {
		return columns;
	}

	public void addColumn(PuiModelColumn column) {
		columns.add(column);
	}

	public List<IPuiUserModelFilter> getUserFilters() {
		return userFilters;
	}

	public void addUserFilter(IPuiUserModelFilter userFilter) {
		userFilters.add(userFilter);
	}

	public List<IPuiModelFilter> getModelFilters() {
		return modelFilters;
	}

	public void addModelFilter(IPuiModelFilter modelFilter) {
		modelFilters.add(modelFilter);
	}

	public Map<String, List<IPuiUserModelConfig>> getConfigurations() {
		return configurations;
	}

	public void addConfiguration(String type, IPuiUserModelConfig configuration) {
		if (!configurations.containsKey(type)) {
			configurations.put(type, new ArrayList<>());
		}
		configurations.get(type).add(configuration);
	}

}
