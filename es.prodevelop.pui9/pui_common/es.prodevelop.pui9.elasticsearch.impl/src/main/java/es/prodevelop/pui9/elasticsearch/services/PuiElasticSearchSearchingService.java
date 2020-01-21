package es.prodevelop.pui9.elasticsearch.services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.elasticsearch.dto.ESSearchResult;
import es.prodevelop.pui9.elasticsearch.dto.ESSearchResultItem;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchSearchException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchViewBlockedException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchViewNotIndexableException;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchSearchingService;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.filter.FilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.order.OrderDirection;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiDateUtil;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * Implementation for the API to manage the Searchs for ElasticSaerch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchSearchingService extends AbstractPuiElasticSearchService
		implements IPuiElasticSearchSearchingService {

	private static final String KEYWORD_LITERAL = "keyword";

	private final DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	@Override
	public Long count(Class<? extends IViewDto> dtoClass, FilterBuilder filterBuilder)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException {
		List<ESSearchResultItem> list = findMultiple(dtoClass, filterBuilder, null, null);
		return (long) list.size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Number> N getMaxValue(Class<? extends IViewDto> dtoClass, String column,
			FilterBuilder filterBuilder) throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException {
		OrderBuilder orderBuilder = OrderBuilder.newOrder(Order.newOrder(column, OrderDirection.desc));
		ESSearchResultItem item = findOne(dtoClass, filterBuilder, orderBuilder, null);

		N value = null;
		Field field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, column);
		if (item != null) {
			IViewDto dto = item.getDto();
			try {
				value = (N) FieldUtils.readField(field, dto, true);
			} catch (Exception e) {
				value = null;
			}
		}

		if (value == null) {
			// if no values, instantiate a new one
			try {
				value = (N) field.getType().getConstructor(String.class).newInstance("0");
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				return null;
			}
		}

		return value;
	}

	@Override
	public ESSearchResultItem findOne(Class<? extends IViewDto> dtoClass, FilterBuilder filterBuilder,
			OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException {
		List<ESSearchResultItem> list = findMultiple(dtoClass, filterBuilder, orderBuilder, language);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes" })
	public List<ESSearchResultItem> findMultiple(Class<? extends IViewDto> dtoClass, FilterBuilder filterBuilder,
			OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			throw new PuiElasticSearchSearchException("NullView");
		}

		String id = daoRegistry.getModelIdFromDto(dtoClass);
		Class<? extends IDao> daoClass = daoRegistry.getDaoFromModelId(id);
		String view = daoRegistry.getEntityName(daoClass);

		SearchRequest req = new SearchRequest();
		req.setModel(null);
		req.setPage(1);
		req.setRows(SearchRequest.NUM_MAX_ROWS);
		req.setQueryLang(language != null ? language.getIsocode() : null);
		req.setQueryText(null);
		req.setQueryFields(null);
		req.setQueryFlexible(false);
		req.setFilter(filterBuilder != null ? filterBuilder.asFilterGroup() : null);
		req.setOrder(orderBuilder != null ? orderBuilder.getOrders() : null);
		req.setDbTableName(view);

		ESSearchResult result = doSearch(req);
		return result.getItems();
	}

	@Override
	public <V extends IViewDto> SearchResponse<V> findForDataGrid(SearchRequest req)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException {
		ESSearchResult result = doSearch(req);

		SearchResponse<V> res = new SearchResponse<>();
		res.setCurrentPage(req.getPage() - 1);
		res.setCurrentRecords(result.getDtoList().size());
		res.setTotalRecords(result.getTotal());
		res.setTotalPages(result.getTotal() / req.getRows());
		if (result.getTotal() % req.getRows() > 0) {
			res.setTotalPages(res.getTotalPages() + 1);
		}
		res.setData(result.getDtoList());

		return res;
	}

	/**
	 * Executes the search over the ElasticSearch server
	 * 
	 * @param req The search request
	 * @return The result of the search
	 * @throws PuiElasticSearchSearchException  If an error occurs while searching
	 *                                          the documents that fits the given
	 *                                          filter
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	private ESSearchResult doSearch(SearchRequest req)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException {
		Class<? extends IViewDto> dtoClass = daoRegistry.getDtoFromEntityName(req.getDbTableName(), false, false);
		if (dtoClass == null) {
			throw new PuiElasticSearchSearchException("Doesn't exist the DTO for the entity " + req.getDbTableName());
		}
		if (puiElasticSearchEnablement.isViewBlocked(dtoClass)) {
			throw new PuiElasticSearchViewBlockedException(dtoClass.getSimpleName());
		}

		if (!puiElasticSearchEnablement.isViewIndexable(dtoClass)) {
			throw new PuiElasticSearchViewNotIndexableException(dtoClass.getSimpleName());
		}

		FilterBuilder filterBuilder = req.buildSearchFilter(dtoClass);
		OrderBuilder orderBuilder = req.createOrderForSearch();

		for (Order order : orderBuilder.getOrders()) {
			String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, order.getColumn());
			if (fieldName != null) {
				order.setColumn(fieldName);
			}
		}

		PuiLanguage language = !StringUtils.isEmpty(req.getQueryLang()) ? new PuiLanguage(req.getQueryLang()) : null;
		String index = getIndexForLanguage(dtoClass, language);

		// The Search Builder
		org.elasticsearch.action.search.SearchRequest request = new org.elasticsearch.action.search.SearchRequest();
		request.source(new SearchSourceBuilder());
		request.indices(index);

		// the query
		QueryBuilder qb = parseQueryText(dtoClass, req.getQueryText(), req.getQueryFields(), req.getQueryFieldText(),
				req.isQueryFlexible());

		// the filter
		QueryBuilder filtersQb = processFilters(dtoClass, filterBuilder.asFilterGroup());
		if (filtersQb != null) {
			if (qb instanceof BoolQueryBuilder) {
				((BoolQueryBuilder) qb).must(filtersQb);
			} else {
				BoolQueryBuilder bqb = QueryBuilders.boolQuery();
				bqb.must(qb);
				bqb.must(filtersQb);
				qb = bqb;
			}
		}
		request.source().query(qb);

		// the order
		List<FieldSortBuilder> orders = parseQueryOrder(dtoClass, orderBuilder);
		for (FieldSortBuilder fsb : orders) {
			request.source().sort(fsb);
		}

		// the pagination
		Integer page = req.getPage() - 1;
		Integer size = req.getRows();

		Integer from = page * size;
		request.source().from(from);
		request.source().size(size);

		// Parse the response
		try {
			org.elasticsearch.action.search.SearchResponse response = getClient().search(request,
					RequestOptions.DEFAULT);

			ESSearchResult result = new ESSearchResult(response.getHits().getTotalHits().value,
					response.getTook().secondsFrac());
			for (SearchHit sh : response.getHits()) {
				String json = sh.getSourceAsString();
				IViewDto dto = GsonSingleton.getSingleton().getGson().fromJson(json, dtoClass);
				result.addItem(new ESSearchResultItem(sh.getId(), dto));
			}

			return result;
		} catch (ElasticsearchException | IOException e) {
			throw new PuiElasticSearchSearchException(e.getMessage());
		}
	}

	/**
	 * Create a QueryBuilder using the query text and the query fields
	 * 
	 * @param dtoClass        The DTO class type for the search
	 * @param text            The searching text
	 * @param fieldsList      The list of fields where apply the text filter
	 * @param fieldTextMap    A map where the key are the fields and the value is
	 *                        the text to find for each field
	 * @param isQueryFlexible Indicate if the query is flexible, taking into account
	 *                        the spelling errors
	 * @return A QueryBuilder that represents the query of the user
	 */
	private QueryBuilder parseQueryText(Class<? extends IViewDto> dtoClass, String text, List<String> fieldsList,
			Map<String, String> fieldTextMap, boolean isQueryFlexible) {
		BoolQueryBuilder qb = QueryBuilders.boolQuery();

		if (!StringUtils.isEmpty(text) && !fieldsList.isEmpty()) {
			BoolQueryBuilder bqb = QueryBuilders.boolQuery().minimumShouldMatch(1);
			String textValue = parseValueToString(text, true, isQueryFlexible);
			String termValue = ".*" + parseValueToTermString(text) + ".*";
			Number numberValue = parseValueToNumber(text);
			Instant dateTimeValue = parseValueToInstant(text);
			String dateStart = getDateStart(dateTimeValue);
			String dateEnd = getDateEnd(dateTimeValue);

			fieldsList.stream().filter(field -> !fieldTextMap.containsKey(field)).forEach(field -> {
				if (isString(dtoClass, field) && (textValue != null || termValue != null) && dateTimeValue == null) {
					if (isTextTerm(dtoClass, field)) {
						bqb.should(QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, termValue));
					} else {
						bqb.should(QueryBuilders.regexpQuery(field, termValue));
					}
				} else if (isNumber(dtoClass, field) && numberValue != null) {
					bqb.should(QueryBuilders.termQuery(field, numberValue));
				} else if (isDate(dtoClass, field) && dateTimeValue != null) {
					bqb.should(QueryBuilders.rangeQuery(field).gte(dateStart).lte(dateEnd));
				}
			});

			qb.must(bqb);
		}

		if (!fieldTextMap.isEmpty()) {
			BoolQueryBuilder bqb = QueryBuilders.boolQuery();
			fieldTextMap.entrySet().stream()
					.filter(entry -> !StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue()))
					.forEach(entry -> {
						String field = entry.getKey();
						String val = entry.getValue();

						String textValue = parseValueToString(val, true, isQueryFlexible);
						String termValue = ".*" + parseValueToTermString(val) + ".*";
						Number numberValue = parseValueToNumber(val);
						Instant dateTimeValue = parseValueToInstant(val);
						String dateStart = getDateStart(dateTimeValue);
						String dateEnd = getDateEnd(dateTimeValue);

						if (isString(dtoClass, field) && (textValue != null || termValue != null)
								&& dateTimeValue == null) {
							if (isTextTerm(dtoClass, field)) {
								bqb.must(QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, termValue));
							} else {
								bqb.must(QueryBuilders.queryStringQuery(textValue).field(field)
										.defaultOperator(Operator.OR));
							}
						} else if (isNumber(dtoClass, field) && numberValue != null) {
							bqb.must(QueryBuilders.termQuery(field, numberValue));
						} else if (isDate(dtoClass, field) && dateTimeValue != null) {
							bqb.must(QueryBuilders.rangeQuery(field).gte(dateStart).lte(dateEnd));
						}
					});

			qb.must(bqb);
		}

		return qb;
	}

	/**
	 * Create a FieldSortBuilder with the given order
	 * 
	 * @param dtoClass     The DTO class type for the search
	 * @param orderBuilder The Order of the request
	 * @return The FieldSortBuilder for ElasticSearch
	 */
	private List<FieldSortBuilder> parseQueryOrder(Class<? extends IViewDto> dtoClass, OrderBuilder orderBuilder) {
		List<FieldSortBuilder> orders = new ArrayList<>();

		if (orderBuilder != null) {
			for (Order order : orderBuilder.getOrders()) {
				String column = order.getColumn();
				if (isTextTerm(dtoClass, column)) {
					column += "." + KEYWORD_LITERAL;
				}
				orders.add(SortBuilders.fieldSort(column).order(SortOrder.fromString(order.getDirection().name())));
			}
		}

		return orders;
	}

	/**
	 * Create a QueryBuilder for the filters of the request
	 * 
	 * @param dtoClass The DTO class type for the search
	 * @param filters  The filters of the request
	 * @return A BoolQueryBuilder of ElasticSearch that represents the user filter
	 */
	private BoolQueryBuilder processFilters(Class<? extends IViewDto> dtoClass, FilterGroup filters) {
		if (filters == null) {
			return null;
		}

		List<QueryBuilder> filterQbList = new ArrayList<>();

		for (FilterRule rule : filters.getRules()) {
			QueryBuilder qb = processRule(dtoClass, rule);
			if (qb != null) {
				filterQbList.add(qb);
			}
		}

		for (FilterGroup filter : filters.getGroups()) {
			QueryBuilder qb = processFilters(dtoClass, filter);
			if (qb != null) {
				filterQbList.add(qb);
			}
		}

		BoolQueryBuilder bqb = QueryBuilders.boolQuery();
		if (filterQbList.isEmpty()) {
			bqb = null;
		} else if (filterQbList.size() == 1) {
			bqb.must(filterQbList.get(0));
		} else {
			bqb = QueryBuilders.boolQuery();
			for (QueryBuilder filterQb : filterQbList) {
				switch (filters.getGroupOp()) {
				case and:
					bqb.must(filterQb);
					break;
				case or:
					bqb.should(filterQb);
					bqb.minimumShouldMatch(1);
					break;
				}
			}
		}

		return bqb;
	}

	/**
	 * Create a QueryBuilder for the given Rule of a Filter
	 * 
	 * @param dtoClass The DTO class type for the search
	 * @param rule     The Rule of the Filter
	 * @return A QueryBuilder of ElasticSearch that represents the Rule
	 */
	@SuppressWarnings("unchecked")
	private QueryBuilder processRule(Class<? extends IViewDto> dtoClass, FilterRule rule) {
		if (rule == null) {
			return null;
		}

		String field = rule.getField();
		if (field.equals(IDto.LANG_COLUMN_NAME)) {
			return null;
		}

		// ensure to select the view field name
		if (!DtoRegistry.getAllFields(dtoClass).contains(field)) {
			// if the fields list doesn't contain the given field, check if it was a column
			// and get the corresponding field name
			field = DtoRegistry.getFieldNameFromColumnName(dtoClass, field);
			if (!DtoRegistry.getAllFields(dtoClass).contains(field)) {
				// if it still doesn't exist, return null
				return null;
			}
		}

		String textValue = parseValueToString(rule.getData(), false, true);
		String termValue = parseValueToTermString(rule.getData());
		Instant instantValue = parseValueToInstant(rule.getData());
		String dateStart = getDateStart(instantValue);
		String dateEnd = getDateEnd(instantValue);
		Number numberValue = parseValueToNumber(rule.getData());

		if (StringUtils.isEmpty(textValue) && instantValue == null && numberValue == null) {
			switch (rule.getOp()) {
			case eq:
				rule.setOp(FilterRuleOperation.nu);
				break;
			case ne:
				rule.setOp(FilterRuleOperation.nn);
				break;
			case nu:
			case nn:
				break;
			case eqt:
			case net:
			case ltt:
			case let:
			case gtt:
			case get:
				numberValue = Integer.valueOf(0);
				break;
			default:
				return null;
			}
		}

		QueryBuilder qb = null;
		List<Object> values;

		switch (rule.getOp()) {
		case eq:
			if (isDate(dtoClass, field) && instantValue != null) {
				qb = QueryBuilders.rangeQuery(field).gte(dateStart).lte(dateEnd);
			} else if (isString(dtoClass, field)) {
				if (isTextTerm(dtoClass, field)) {
					qb = QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, termValue);
				} else {
					// never search by this kind of fields because it's a huge field
					qb = QueryBuilders.simpleQueryStringQuery("fake_text_never_searched");
				}
			} else if (isNumber(dtoClass, field)) {
				if (numberValue != null) {
					qb = QueryBuilders.termQuery(field, numberValue);
				}
			}
			break;
		case eqt:
			if (isDate(dtoClass, field)) {
				long numDays = Long.parseLong(numberValue.toString());
				dateStart = getDateStart(Instant.now().plus(numDays, ChronoUnit.DAYS));
				dateEnd = getDateEnd(Instant.now().plus(numDays, ChronoUnit.DAYS));

				qb = QueryBuilders.rangeQuery(field).gte(dateStart).lte(dateEnd);
			}
			break;
		case ne:
			if (isDate(dtoClass, field) && instantValue != null) {
				qb = QueryBuilders.rangeQuery(field).gte(dateStart).lte(dateEnd);
			} else if (isString(dtoClass, field)) {
				if (isTextTerm(dtoClass, field)) {
					qb = QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, termValue);
				} else {
					// never search by this kind of fields because it's a huge field
					qb = QueryBuilders.boolQuery()
							.mustNot(QueryBuilders.simpleQueryStringQuery("fake_text_never_searched"));
				}
			} else if (isNumber(dtoClass, field)) {
				if (numberValue != null) {
					qb = QueryBuilders.termQuery(field, numberValue);
				}
			}
			qb = QueryBuilders.boolQuery().mustNot(qb);
			break;
		case net:
			if (isDate(dtoClass, field)) {
				long numDays = Long.parseLong(numberValue.toString());
				dateStart = getDateStart(Instant.now().plus(numDays, ChronoUnit.DAYS));
				dateEnd = getDateEnd(Instant.now().plus(numDays, ChronoUnit.DAYS));

				qb = QueryBuilders.rangeQuery(field).gte(dateStart).lte(dateEnd);
			}
			qb = QueryBuilders.boolQuery().mustNot(qb);
			break;
		case bw:
			if (isTextTerm(dtoClass, field)) {
				qb = QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, termValue + ".*");
			} else {
				qb = QueryBuilders.regexpQuery(field, termValue + ".*");
			}
			break;
		case bn:
			if (isTextTerm(dtoClass, field)) {
				qb = QueryBuilders.boolQuery()
						.mustNot(QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, termValue + ".*"));
			} else {
				qb = QueryBuilders.boolQuery().mustNot(QueryBuilders.regexpQuery(field, termValue + ".*"));
			}
			break;
		case ew:
			if (isTextTerm(dtoClass, field)) {
				qb = QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, ".*" + termValue);
			} else {
				qb = QueryBuilders.regexpQuery(field, ".*" + termValue);
			}
			break;
		case en:
			if (isTextTerm(dtoClass, field)) {
				qb = QueryBuilders.boolQuery()
						.mustNot(QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, ".*" + termValue));
			} else {
				qb = QueryBuilders.boolQuery().mustNot(QueryBuilders.regexpQuery(field, ".*" + termValue));
			}
			break;
		case cn:
			if (isTextTerm(dtoClass, field)) {
				qb = QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, ".*" + termValue + ".*");
			} else {
				qb = QueryBuilders.regexpQuery(field, ".*" + termValue + ".*");
			}
			break;
		case nc:
			if (isTextTerm(dtoClass, field)) {
				qb = QueryBuilders.boolQuery()
						.mustNot(QueryBuilders.regexpQuery(field + "." + KEYWORD_LITERAL, ".*" + termValue + ".*"));
			} else {
				qb = QueryBuilders.boolQuery().mustNot(QueryBuilders.regexpQuery(field, ".*" + termValue + ".*"));
			}
			break;
		case lt:
			if (isNumber(dtoClass, field)) {
				if (numberValue != null) {
					qb = QueryBuilders.rangeQuery(field).lt(numberValue);
				}
			} else if (isDate(dtoClass, field) && instantValue != null) {
				qb = QueryBuilders.rangeQuery(field).lt(dateEnd);
			}
			break;
		case ltt:
			if (isDate(dtoClass, field)) {
				long numDays = Long.parseLong(numberValue.toString());
				dateEnd = getDateEnd(Instant.now().plus(numDays, ChronoUnit.DAYS));

				qb = QueryBuilders.rangeQuery(field).lt(dateEnd);
			}
			break;
		case le:
			if (isNumber(dtoClass, field)) {
				if (numberValue != null) {
					qb = QueryBuilders.rangeQuery(field).lte(numberValue);
				}
			} else if (isDate(dtoClass, field) && instantValue != null) {
				qb = QueryBuilders.rangeQuery(field).lte(dateEnd);
			}
			break;
		case let:
			if (isDate(dtoClass, field)) {
				long numDays = Long.parseLong(numberValue.toString());
				dateEnd = getDateEnd(Instant.now().plus(numDays, ChronoUnit.DAYS));

				qb = QueryBuilders.rangeQuery(field).lte(dateEnd);
			}
			break;
		case gt:
			if (isNumber(dtoClass, field)) {
				if (numberValue != null) {
					qb = QueryBuilders.rangeQuery(field).gt(numberValue);
				}
			} else if (isDate(dtoClass, field) && instantValue != null) {
				qb = QueryBuilders.rangeQuery(field).gt(dateStart);
			}
			break;
		case gtt:
			if (isDate(dtoClass, field)) {
				long numDays = Long.parseLong(numberValue.toString());
				dateStart = getDateStart(Instant.now().plus(numDays, ChronoUnit.DAYS));

				qb = QueryBuilders.rangeQuery(field).gt(dateStart);
			}
			break;
		case ge:
			if (isNumber(dtoClass, field)) {
				if (numberValue != null) {
					qb = QueryBuilders.rangeQuery(field).gte(numberValue);
				}
			} else if (isDate(dtoClass, field) && instantValue != null) {
				qb = QueryBuilders.rangeQuery(field).gte(dateStart);
			}
			break;
		case get:
			if (isDate(dtoClass, field)) {
				long numDays = Long.parseLong(numberValue.toString());
				dateStart = getDateStart(Instant.now().plus(numDays, ChronoUnit.DAYS));

				qb = QueryBuilders.rangeQuery(field).gte(dateStart);
			}
			break;
		case bt:
			if (rule.getData() instanceof List) {
				values = (List<Object>) rule.getData();
				Object from = values.get(0);
				Object to = values.get(1);
				if (isNumber(dtoClass, field)) {
					Number lower = parseValueToNumber(from);
					Number upper = parseValueToNumber(to);
					qb = QueryBuilders.rangeQuery(field).gte(lower).lte(upper);
				} else if (isDate(dtoClass, field)) {
					Instant lower = parseValueToInstant(from);
					Instant upper = parseValueToInstant(to);
					if (lower != null && upper != null) {
						qb = QueryBuilders.rangeQuery(field).gte(getDateStart(lower)).lte(getDateEnd(upper));
					}
				}
			}
			break;
		case nbt:
			if (rule.getData() instanceof List) {
				values = (List<Object>) rule.getData();
				Object from = values.get(0);
				Object to = values.get(1);
				if (isNumber(dtoClass, field)) {
					Number lower = parseValueToNumber(from);
					Number upper = parseValueToNumber(to);
					qb = QueryBuilders.rangeQuery(field).gte(lower).lte(upper);
				} else if (isDate(dtoClass, field)) {
					Instant lower = parseValueToInstant(from);
					Instant upper = parseValueToInstant(to);
					if (lower != null && upper != null) {
						qb = QueryBuilders.rangeQuery(field).gte(getDateStart(lower)).lte(getDateEnd(upper));
					}
				}
				qb = QueryBuilders.boolQuery().mustNot(qb);
			}
			break;
		case in:
			if (isTextTerm(dtoClass, field)) {
				field += "." + KEYWORD_LITERAL;
			}
			if (rule.getData() instanceof List) {
				qb = QueryBuilders.termsQuery(field, (List<Object>) rule.getData());
			} else if (rule.getData() instanceof String) {
				values = new ArrayList<>();
				for (String val : rule.getData().toString().split(",")) {
					values.add(val.trim());
				}
				qb = QueryBuilders.termsQuery(field, values);
			}
			break;
		case ni:
			if (isTextTerm(dtoClass, field)) {
				field += "." + KEYWORD_LITERAL;
			}
			if (rule.getData() instanceof List) {
				qb = QueryBuilders.termsQuery(field, (List<Object>) rule.getData());
			} else if (rule.getData() instanceof String) {
				values = new ArrayList<>();
				for (String val : rule.getData().toString().split(",")) {
					values.add(val.trim());
				}
				qb = QueryBuilders.termsQuery(field, values);
			}
			qb = QueryBuilders.boolQuery().mustNot(qb);
			break;
		case nu:
			qb = QueryBuilders.existsQuery(field);
			qb = QueryBuilders.boolQuery().mustNot(qb);
			break;
		case nn:
			qb = QueryBuilders.existsQuery(field);
			break;
		case geo_bounding_box:
			throw new NotImplementedException("Geometry not supported in Elastic Search by now");
		case geo_intersects:
			throw new NotImplementedException("Geometry not supported in Elastic Search by now");
		}

		return qb;
	}

	/**
	 * Returns the query as String to be used with text fields
	 * 
	 * @param value           The searching value
	 * @param withWildcards   Use wildcards at the beginning and the end of the
	 *                        value
	 * @param isQueryFlexible If the value should be flexible against spelling
	 *                        errors or not
	 * @return The modified value
	 */
	private String parseValueToString(Object value, boolean withWildcards, boolean isQueryFlexible) {
		if (value == null) {
			return null;
		}

		String text;
		if (value instanceof String) {
			text = (String) value;
			if (StringUtils.isEmpty(text)) {
				return null;
			}

			text = text.trim().replaceAll("[ ]+", " ");

			if (withWildcards) {
				if (text.indexOf(" ") == 0) {
					if (!isQueryFlexible) {
						text = "\"*" + text + "*\" " + text;
					} else {
						text = "*" + text + "* " + text;
					}
				} else {
					if (!isQueryFlexible) {
						text = "\"*" + text + "*\"";
					}
				}
			}
		} else {
			text = value.toString();
			if (withWildcards) {
				text = "*" + text + "*";
			}
		}

		return text;
	}

	/**
	 * Convert the value into a term
	 * 
	 * @param value The original value
	 * @return The modified value as term string
	 */
	private String parseValueToTermString(Object value) {
		if (value == null) {
			return null;
		}

		String text;
		if (value instanceof String) {
			text = (String) value;
			if (StringUtils.isEmpty(text)) {
				return null;
			}

			text = text.toLowerCase().trim().replaceAll("[ ]+", " ");

			StringBuilder sb = new StringBuilder();
			for (char c : text.toCharArray()) {
				if (Character.isSpaceChar(c)) {
					sb.append(".*");
				} else {
					sb.append("[");
					sb.append(convertChar(c));
					sb.append("]");
				}
			}

			text = sb.toString();
		} else {
			text = value.toString();
			text = ".*" + text + ".*";
		}

		return text;
	}

	/**
	 * Returns the query as Number to be used with numeric fields
	 * 
	 * @param value The value to be searched
	 * @return The value as Number
	 */
	private Number parseValueToNumber(Object value) {
		if (value instanceof Number) {
			return (Number) value;
		} else if (value instanceof String) {
			String val = (String) value;
			if (val.startsWith("\"")) {
				val = val.substring(1);
			}
			if (val.endsWith("\"")) {
				val = val.substring(0, val.length() - 1);
			}

			val = val.replace(",", ".");

			try {
				// is Integer?
				return Integer.parseInt(val);
			} catch (Exception e1) {
				try {
					// is Long?
					return Long.parseLong(val);
				} catch (Exception e2) {
					try {
						// is Double?
						return Double.parseDouble(val);
					} catch (Exception e3) {
						try {
							// is BigDecimal?
							return new BigDecimal(val);
						} catch (Exception e4) {
							return null;
						}
					}
				}
			}
		} else {
			return null;
		}
	}

	/**
	 * Returns the query value as Date to be used with date fields
	 * 
	 * @param value The value to be searched
	 * @return The value as Date
	 */
	private Instant parseValueToInstant(Object value) {
		if (value instanceof Instant) {
			return (Instant) value;
		} else if (value instanceof String) {
			String val = (String) value;
			if (val.startsWith("\"")) {
				val = val.substring(1);
			}
			if (val.endsWith("\"")) {
				val = val.substring(0, val.length() - 1);
			}

			return PuiDateUtil.stringToInstant(val);
		} else {
			return null;
		}
	}

	/**
	 * Return a String that represents the init of a date
	 */
	private String getDateStart(Instant instant) {
		if (instant == null) {
			return null;
		}

		instant = LocalDateTime.ofInstant(instant, ZoneOffset.UTC).withHour(0).withMinute(0).toInstant(ZoneOffset.UTC);
		return PuiDateUtil.temporalAccessorToString(instant, formatterDay);
	}

	/**
	 * Return a String that represents the end of a date
	 */
	private String getDateEnd(Instant instant) {
		if (instant == null) {
			return null;
		}

		instant = LocalDateTime.ofInstant(instant, ZoneOffset.UTC).withHour(23).withMinute(59)
				.toInstant(ZoneOffset.UTC);
		return PuiDateUtil.temporalAccessorToString(instant, formatterDay);
	}

	/**
	 * Check if the given field is a text field
	 */
	private boolean isString(Class<? extends IViewDto> dtoClass, String field) {
		return DtoRegistry.getStringFields(dtoClass).contains(field);
	}

	/**
	 * Check if the given field is a numeric field
	 */
	private boolean isNumber(Class<? extends IViewDto> dtoClass, String field) {
		return DtoRegistry.getNumericFields(dtoClass).contains(field)
				|| DtoRegistry.getFloatingFields(dtoClass).contains(field);
	}

	/**
	 * Check if the given field is a date field
	 */
	private boolean isDate(Class<? extends IViewDto> dtoClass, String field) {
		return DtoRegistry.getDateTimeFields(dtoClass).contains(field);
	}

	/**
	 * Convert the given character into the multiple existing ways it has
	 * (uppercase, lowercase, with accents...)
	 */
	private String convertChar(char c) {
		StringBuilder sb = new StringBuilder();

		switch (c) {
		case 'a':
			sb.append('A');
			sb.append('Á');
			sb.append('À');
			sb.append('Ä');
			sb.append('Â');
			sb.append('Ã');
			sb.append('a');
			sb.append('á');
			sb.append('à');
			sb.append('ä');
			sb.append('â');
			sb.append('ã');
			break;
		case 'e':
			sb.append('E');
			sb.append('É');
			sb.append('È');
			sb.append('Ë');
			sb.append('Ê');
			sb.append('e');
			sb.append('é');
			sb.append('è');
			sb.append('ë');
			sb.append('ê');
			break;
		case 'i':
			sb.append('I');
			sb.append('Í');
			sb.append('Ì');
			sb.append('Ï');
			sb.append('Î');
			sb.append('i');
			sb.append('í');
			sb.append('ì');
			sb.append('ï');
			sb.append('î');
			break;
		case 'o':
			sb.append('O');
			sb.append('Ó');
			sb.append('Ò');
			sb.append('Ö');
			sb.append('Ô');
			sb.append('Õ');
			sb.append('o');
			sb.append('ó');
			sb.append('ò');
			sb.append('ö');
			sb.append('ô');
			sb.append('õ');
			break;
		case 'u':
			sb.append('U');
			sb.append('Ú');
			sb.append('Ù');
			sb.append('Ü');
			sb.append('Û');
			sb.append('u');
			sb.append('ú');
			sb.append('ù');
			sb.append('ü');
			sb.append('û');
			break;
		default:
			if (Character.isDigit(c)) {
				sb.append(c);
			} else {
				sb.append(Character.toUpperCase(c));
				sb.append(c);
			}
			break;
		}

		return sb.toString();
	}

}
