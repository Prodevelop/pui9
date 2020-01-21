package es.prodevelop.pui9.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonSyntaxException;

import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.utils.IPuiObject;
import io.swagger.annotations.ApiModelProperty;

/**
 * This object is used with the Searching request operation to configure the
 * operation. The main attributes to set are:
 * <ul>
 * <li><b>modelName</b>: The name of the model we want to search</li>
 * <li><b>page</b>: The current page to start the search (starting with 1)</li>
 * <li><b>rows</b>: The number of rows to return for each page</li>
 * <li><b>queryLang</b>: The language to use</li>
 * <li><b>queryText</b>: The text to search against the visible fields
 * {@link #queryFields}</li>
 * <li><b>queryFields</b>: the list of fields where the {@link #queryText} will
 * be searched</li>
 * <li><b>queryFieldText</b>: if the search text is per column (a hashmap where
 * the Key is the column name and the Value is the text to search for each
 * column)</li>
 * <li><b>queryFlexible</b>: for Elastic Search, indicating that the searching
 * text should be treated with flexibility, allowing spelling errors</li>
 * <li><b>filters</b>: the Filter of the search</li>
 * <li><b>orderColumn</b>: the column used to order the results</li>
 * <li><b>orderDirection</b>: the direction of the order</li>
 * </ul>
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class SearchRequest implements IPuiObject {

	private static final long serialVersionUID = 1L;

	public static final Integer NUM_MAX_ROWS = 10000;
	public static final Integer DEFAULT_PAGE = 1;
	public static final Integer DEFAULT_ROWS = 20;
	public static final String SEARCH_PARAMETER = "?";

	@ApiModelProperty(required = false, example = "")
	private String model = "";
	@ApiModelProperty(required = false, example = "1")
	private Integer page = DEFAULT_PAGE;
	@ApiModelProperty(required = false, example = "20")
	private Integer rows = DEFAULT_ROWS;
	@ApiModelProperty(required = false, example = "es")
	private String queryLang = "";
	@ApiModelProperty(required = false, example = "es")
	private String queryText = "";
	@ApiModelProperty(required = false, example = "[]")
	private List<String> queryFields = new ArrayList<>();
	@ApiModelProperty(required = false, example = "[]")
	private Map<String, String> queryFieldText = new HashMap<>();
	@ApiModelProperty(required = false, example = "false")
	private boolean queryFlexible = false;
	@ApiModelProperty(required = false, example = "{}")
	private FilterGroup filter = null;
	@ApiModelProperty(required = false, example = "[]")
	private List<Order> order;
	@ApiModelProperty(required = false, example = "[]")
	private List<String> columns = new ArrayList<>();
	@ApiModelProperty(required = false, example = "false")
	private boolean distinctValues = false;

	@ApiModelProperty(hidden = true)
	private transient String dbTableName;
	@ApiModelProperty(hidden = true)
	private transient FilterGroup dbFilters;
	@ApiModelProperty(hidden = true)
	private transient IListAdapter<? extends IDto> gridAdapter;
	@ApiModelProperty(hidden = true)
	private transient Map<String, Object> properties = new HashMap<>();
	@ApiModelProperty(hidden = true)
	private transient Class<? extends IViewDto> viewDtoClass;
	@ApiModelProperty(hidden = true)
	private transient Class<? extends ITableDto> tableDtoClass;

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getPage() {
		if (page != null) {
			if (page > 0) {
				return page;
			} else {
				return DEFAULT_PAGE;
			}
		} else {
			return DEFAULT_PAGE;
		}
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		if (rows != null) {
			if (rows > 0) {
				return rows;
			} else {
				return DEFAULT_ROWS;
			}
		} else {
			return NUM_MAX_ROWS;
		}
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getQueryLang() {
		return queryLang;
	}

	public void setQueryLang(String queryLang) {
		this.queryLang = queryLang;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public List<String> getQueryFields() {
		return queryFields != null ? queryFields : Collections.emptyList();
	}

	public void setQueryFields(List<String> queryFields) {
		this.queryFields = queryFields;
	}

	public Map<String, String> getQueryFieldText() {
		return queryFieldText != null ? queryFieldText : Collections.emptyMap();
	}

	public void setQueryFieldText(Map<String, String> queryFieldText) {
		this.queryFieldText = queryFieldText;
	}

	public boolean isQueryFlexible() {
		return queryFlexible;
	}

	public void setQueryFlexible(boolean queryFlexible) {
		this.queryFlexible = queryFlexible;
	}

	public void setFilter(FilterGroup filter) {
		this.filter = filter;
	}

	public List<Order> getOrder() {
		if (order == null) {
			order = Collections.emptyList();
		}
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public List<String> getColumns() {
		if (columns == null) {
			columns = Collections.emptyList();
		}
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public String getDbTableName() {
		return dbTableName;
	}

	public void setDbTableName(String dbTableName) {
		this.dbTableName = dbTableName;
	}

	@ApiModelProperty(hidden = true)
	public void setDbFilters(FilterGroup dbFilters) {
		this.dbFilters = dbFilters;
	}

	@ApiModelProperty(hidden = true)
	public void setGridAdapter(IListAdapter<? extends IDto> gridAdapter) {
		this.gridAdapter = gridAdapter;
	}

	public boolean isDistinctValues() {
		return distinctValues;
	}

	public void setDistinctValues(boolean distinctValues) {
		this.distinctValues = distinctValues;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(String key) {
		return (T) properties.get(key);
	}

	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	public Class<? extends IViewDto> getViewDtoClass() {
		return viewDtoClass;
	}

	public void setViewDtoClass(Class<? extends IViewDto> viewDtoClass) {
		this.viewDtoClass = viewDtoClass;
	}

	public Class<? extends ITableDto> getTableDtoClass() {
		return tableDtoClass;
	}

	public void setTableDtoClass(Class<? extends ITableDto> tableDtoClass) {
		this.tableDtoClass = tableDtoClass;
	}

	/**
	 * Build the {@link FilterBuilder} object from the Filter of the search and the
	 * filters in the database of the model
	 * 
	 * @return The filter as FilterBuilder representation
	 */
	public FilterBuilder buildSearchFilter(Class<? extends IDto> dtoClass) {
		// filters from dabatase: substitute every filter parameter with the
		// correct value
		FilterGroup dbFilter = null;
		if (this.dbFilters != null) {
			String dbFiltersJson = "";
			if (this.dbFilters.equals(IListAdapter.EMPTY_FILTER)) {
				dbFiltersJson = SEARCH_PARAMETER;
			} else {
				dbFiltersJson = this.dbFilters.toJson();
			}

			String paramsFiltroFijo = this.gridAdapter != null ? this.gridAdapter.getFixedFilterParameters(this) : null;
			if (dbFiltersJson.contains("?") && !StringUtils.isEmpty(paramsFiltroFijo)) {
				for (String split : paramsFiltroFijo.split("\\" + IListAdapter.SEPARATOR)) {
					dbFiltersJson = dbFiltersJson.replaceFirst("\\" + SEARCH_PARAMETER, split);
				}
			}

			try {
				dbFilter = FilterGroup.fromJson(dbFiltersJson);
				purgueBadRules(dbFilter);
				setDbFilters(dbFilter);
			} catch (JsonSyntaxException e) {
				// do nothing
			}
		}

		// filters from request
		FilterGroup.modifyDateFilters(dtoClass, this.filter);

		// compose filters
		FilterGroup filters;
		if (this.filter != null && dbFilter == null) {
			filters = this.filter;
		} else if (dbFilter != null) {
			if (this.filter == null) {
				filters = dbFilter;
			} else {
				filters = new FilterGroup();
				filters.getGroups().add(this.filter);
				filters.getGroups().add(dbFilter);
			}
		} else {
			filters = null;
		}

		return filters != null ? FilterBuilder.newFilter(filters) : FilterBuilder.newAndFilter();
	}

	/**
	 * Iterate the given filter to remove the bad defined rules: those that contains
	 * the character {@link #SEARCH_PARAMETER} after processing the associated
	 * Adapter
	 */
	private void purgueBadRules(FilterGroup filter) {
		if (filter == null) {
			return;
		}

		filter.getRules().removeIf(rule -> rule.getData().toString().contains(SEARCH_PARAMETER));
		filter.getGroups().forEach(this::purgueBadRules);
	}

	/**
	 * Build the {@link OrderBuilder} object from the order and direction of the
	 * request
	 * 
	 * @return The order as {@link OrderBuilder} representation
	 */
	public OrderBuilder createOrderForSearch() {
		OrderBuilder orderBuilder = OrderBuilder.newOrder();

		if (!getOrder().isEmpty()) {
			for (Order ord : getOrder()) {
				orderBuilder.addOrder(ord);
			}
		}

		return orderBuilder;
	}

}
