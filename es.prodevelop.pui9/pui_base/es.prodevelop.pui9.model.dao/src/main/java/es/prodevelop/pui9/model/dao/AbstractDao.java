package es.prodevelop.pui9.model.dao;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.db.helpers.IDatabaseHelper;
import es.prodevelop.pui9.db.utils.SqlUtils;
import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * This abstract class provides the implementation of the all the DAO for JDBC
 * approach. It uses the JdbcTemplate to manage the statements and connections
 * against the Database
 * <p>
 * If you want to use a DAO, you must to create an Autowired property using the
 * interface of this DAO.
 * 
 * @param <T> The whole {@link IDto} class that represents this DAO Class
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDao<T extends IDto> implements IDao<T>, RowMapper<T> {

	protected static final Log logger = LogFactory.getLog(AbstractDao.class);
	protected static final String NO_JOINS = "";
	protected static final String SELECT = "SELECT ";
	protected static final String ALL = "* ";
	protected static final String DISTINCT = "DISTINCT ";
	protected static final String FROM = "FROM ";
	protected static final String WHERE = " WHERE ";
	protected static final String ORDER_BY = " ORDER BY ";

	@Autowired
	protected DaoRegistry daoRegistry;

	@Autowired
	protected IDatabaseHelper dbHelper;

	@Autowired(required = false)
	private IListAdapter<T> gridAdapter;

	protected JdbcTemplate jdbcTemplate;

	protected Class<T> dtoClass;
	private List<String> columnNames = null;

	@Autowired
	private void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * Registers the DAO into the DaoRegistry
	 */
	@PostConstruct
	private void postConstruct() {
		daoRegistry.registerDao(getClass());

		dtoClass = daoRegistry.getDtoFromDao(getClass(), false);
		columnNames = DtoRegistry.getAllColumnNames(dtoClass);
	}

	/**
	 * Get the name of the entity, to be used in the SQL operations
	 * 
	 * @return The entity name
	 */
	protected String getEntityName() {
		return daoRegistry.getEntityName(this);
	}

	@Override
	public Long count() throws PuiDaoCountException {
		return count(null);
	}

	@Override
	public Long count(FilterBuilder filterBuilder) throws PuiDaoCountException {
		return count(null, false, filterBuilder);
	}

	@Override
	public Long count(String column, boolean distinct, FilterBuilder filterBuilder) throws PuiDaoCountException {
		if (StringUtils.isEmpty(column)) {
			column = ALL;
		}
		if (distinct) {
			column = DISTINCT + column;
		}

		StringBuilder query = new StringBuilder();
		query.append(SELECT + "count(" + column + ") ");
		query.append(FROM + getEntityName() + " " + SqlUtils.TABLE_PREFIX);

		if (daoRegistry.getTableLangName(this) != null) {
			query.append(addTranslationJoins());
		}

		if (filterBuilder != null) {
			String where = dbHelper.processFilters(getDtoClass(), filterBuilder.asFilterGroup(), true);
			if (!StringUtils.isEmpty(where)) {
				query.append(WHERE + where);
			}
		}

		return performCount(query.toString());
	}

	/**
	 * Really performs the Count against the database
	 * 
	 * @param sql The sql to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected Long performCount(String sql) throws PuiDaoCountException {
		try {
			return jdbcTemplate.queryForObject(sql, Long.class);
		} catch (DataAccessException e) {
			throw new PuiDaoCountException(e);
		}
	}

	@Override
	public T findOne(FilterBuilder filterBuilder) throws PuiDaoFindException {
		return findOne(filterBuilder, null);
	}

	@Override
	public T findOne(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException {
		List<T> list = doFindWhere(filterBuilder, null, language);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<T> findAll() throws PuiDaoFindException {
		return doFindWhere(null, null, PuiLanguageUtils.getSessionLanguage());
	}

	@Override
	public List<T> findAll(OrderBuilder orderBuilder) throws PuiDaoFindException {
		return doFindWhere(null, orderBuilder, PuiLanguageUtils.getSessionLanguage());
	}

	@Override
	public List<T> findAll(PuiLanguage language) throws PuiDaoFindException {
		return doFindWhere(null, null, language);
	}

	@Override
	public List<T> findAll(OrderBuilder orderBuilder, PuiLanguage language) throws PuiDaoFindException {
		return doFindWhere(null, orderBuilder, language);
	}

	@Override
	public List<T> findAllPagination(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language,
			Integer page, Integer size) throws PuiDaoFindException {
		try {
			String query = buildSelectSql(filterBuilder, orderBuilder, language);
			return getListPaginated(page, size, query);
		} catch (PuiDaoListException e) {
			throw new PuiDaoFindException(e);
		}
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder) throws PuiDaoFindException {
		return doFindWhere(filterBuilder, null, PuiLanguageUtils.getSessionLanguage());
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiDaoFindException {
		return doFindWhere(filterBuilder, orderBuilder, PuiLanguageUtils.getSessionLanguage());
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException {
		return doFindWhere(filterBuilder, null, language);
	}

	@Override
	public List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiDaoFindException {
		return doFindWhere(filterBuilder, orderBuilder, language);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Number> N getNextValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException {
		N value = getMaxValue(columnName, filterBuilder);

		if (value instanceof BigDecimal) {
			value = (N) ((BigDecimal) value).add(BigDecimal.ONE);
		} else if (value instanceof Integer) {
			value = (N) Integer.valueOf(((Integer) value) + 1);
		} else if (value instanceof Long) {
			value = (N) Long.valueOf(((Long) value) + 1L);
		} else if (value instanceof Double) {
			value = (N) Double.valueOf(((Double) value) + 1);
		} else if (value instanceof Float) {
			value = (N) Float.valueOf(((Float) value) + 1);
		} else if (value instanceof BigInteger) {
			value = (N) ((BigInteger) value).add(BigInteger.ONE);
		}

		return value;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <N extends Number> N getMaxValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException {
		String auxFieldName = DtoRegistry.getFieldNameFromColumnName(getDtoClass(), columnName);
		if (auxFieldName == null) {
			auxFieldName = columnName;
		}

		if (!DtoRegistry.getNumericFields(getDtoClass()).contains(auxFieldName)
				&& !DtoRegistry.getFloatingFields(getDtoClass()).contains(auxFieldName)) {
			throw new PuiDaoNoNumericColumnException(columnName);
		}

		String auxColumnName = DtoRegistry.getColumnNameFromFieldName(getDtoClass(), columnName);
		if (auxColumnName != null) {
			columnName = auxColumnName;
		}

		StringBuilder query = new StringBuilder();
		query.append(SELECT + "max(" + columnName + ") ");
		query.append(FROM + getEntityName() + " " + SqlUtils.TABLE_PREFIX);
		if (filterBuilder != null) {
			String where = dbHelper.processFilters(getDtoClass(), filterBuilder.asFilterGroup(), true);
			if (!StringUtils.isEmpty(where)) {
				query.append(WHERE + where);
			}
		}

		String fieldName = DtoRegistry.getFieldNameFromColumnName(getDtoClass(), columnName);
		Field field = DtoRegistry.getJavaFieldFromFieldName(getDtoClass(), fieldName);
		N value = null;
		try {
			value = performMaxValue(query.toString(), field.getType());
		} catch (Exception e) {
			value = null;
		}

		if (value == null) {
			// if no values, instantiate a new one
			try {
				value = (N) field.getType().getConstructor(String.class).newInstance("0");
			} catch (Exception e) {
				return null;
			}
		}

		return value;
	}

	/**
	 * Really performs the Max Value against the database
	 * 
	 * @param sql       The sql to be executed
	 * @param fieldType The type of the field
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	@SuppressWarnings("unchecked")
	protected <N extends Number> N performMaxValue(String sql, Class<?> fieldType) {
		return (N) jdbcTemplate.queryForObject(sql, fieldType);
	}

	@Override
	public List<T> executeCustomQuery(String sql) throws PuiDaoFindException {
		try {
			return jdbcTemplate.query(sql, this);
		} catch (DataAccessException e) {
			throw new PuiDaoFindException(e);
		}
	}

	@Override
	public List<T> executeCustomQueryWithParameters(String sql, List<Object> parameters) throws PuiDaoFindException {
		try {
			return jdbcTemplate.query(sql, this, parameters.toArray());
		} catch (DataAccessException e) {
			throw new PuiDaoFindException(e);
		}
	}

	@Override
	public SearchResponse<T> findForDataGrid(SearchRequest req) throws PuiDaoListException {
		// the filter includes the language filter
		FilterBuilder filterBuilder = req.buildSearchFilter(getDtoClass());
		OrderBuilder orderBuilder = req.createOrderForSearch();

		if (!StringUtils.isEmpty(req.getQueryLang())) {
			FilterBuilder langBuilder = FilterBuilder.newOrFilter().addEquals(IDto.LANG_COLUMN_NAME, req.getQueryLang())
					.addIsNull(IDto.LANG_COLUMN_NAME);

			FilterBuilder filterBuilderAux = filterBuilder;

			filterBuilder = FilterBuilder.newAndFilter();
			filterBuilder.addGroup(langBuilder);
			filterBuilder.addGroup(filterBuilderAux);
		}

		String filters = dbHelper.processFilters(getDtoClass(), filterBuilder.asFilterGroup(), true);
		String quickSearch = null;
		if (!CollectionUtils.isEmpty(req.getQueryFields())) {
			quickSearch = dbHelper.processSearchText(getDtoClass(), req.getQueryFields(), req.getQueryText());
		} else if (!CollectionUtils.isEmpty(req.getQueryFieldText())) {
			quickSearch = dbHelper.processSearchText(getDtoClass(), req.getQueryFieldText());
		}

		String where;
		if (StringUtils.isEmpty(filters) && StringUtils.isEmpty(quickSearch)) {
			where = null;
		} else if (!StringUtils.isEmpty(filters) && StringUtils.isEmpty(quickSearch)) {
			where = filters;
		} else if (StringUtils.isEmpty(filters) && !StringUtils.isEmpty(quickSearch)) {
			where = quickSearch;
		} else {
			where = "(" + filters + ") AND (" + quickSearch + ")";
		}

		StringBuilder query = new StringBuilder();
		query.append(SELECT);
		if (CollectionUtils.isEmpty(req.getColumns())) {
			query.append(ALL);
		} else {
			if (req.isDistinctValues() && req.getColumns().size() == 1) {
				query.append(DISTINCT);
				query.append(req.getColumns().get(0));
			} else {
				query.append(String.join(", ", req.getColumns()));
			}
		}
		query.append(FROM + getEntityName() + " " + SqlUtils.TABLE_PREFIX);

		if (daoRegistry.getTableLangName(this) != null) {
			query.append(addTranslationJoins());
		}

		if (!StringUtils.isEmpty(where)) {
			query.append(WHERE + where);
		}

		if (orderBuilder != null && !StringUtils.isEmpty(orderBuilder.toString())) {
			for (Order order : orderBuilder.getOrders()) {
				String column = DtoRegistry.getColumnNameFromFieldName(dtoClass, order.getColumn());
				if (column != null) {
					if (daoRegistry.getTableLangName(this) == null) {
						order.setColumn(column);
					} else {
						if (DtoRegistry.getColumnNames(dtoClass).contains(column)) {
							order.setColumn(SqlUtils.TABLE_PREFIX + "." + column);
						} else {
							order.setColumn(SqlUtils.TABLE_LANG_PREFIX + "." + column);
						}
					}
				}
			}

			for (Iterator<Order> it = orderBuilder.getOrders().iterator(); it.hasNext();) {
				Order order = it.next();
				if (!DtoRegistry.getColumnNames(dtoClass).contains(order.getColumn())) {
					it.remove();
				}
			}

			if (!CollectionUtils.isEmpty(orderBuilder.getOrders())) {
				query.append(ORDER_BY + orderBuilder.toString());
			}
		}

		Integer from = req.getPage() - 1;
		Integer size = req.getRows();
		Long total;
		try {
			StringBuilder queryCount = new StringBuilder();
			queryCount.append(SELECT + "count(" + ALL + ") ");
			queryCount.append(FROM + getEntityName() + " " + SqlUtils.TABLE_PREFIX);
			if (daoRegistry.getTableLangName(this) != null) {
				queryCount.append(addTranslationJoins());
			}
			if (!StringUtils.isEmpty(where)) {
				queryCount.append(WHERE + where);
			}
			total = performCount(queryCount.toString());
		} catch (PuiDaoCountException e) {
			throw new PuiDaoListException(e);
		}

		List<T> list = getListPaginated(from, size, query.toString());

		SearchResponse<T> res = new SearchResponse<>();
		res.setCurrentPage(from + 1);
		res.setCurrentRecords(list.size());
		res.setTotalRecords(total);
		res.setTotalPages(total / size);
		if (total % size > 0) {
			res.setTotalPages(res.getTotalPages() + 1);
		}
		res.setData(list);

		return res;
	}

	@Override
	public IListAdapter<T> getGridAdapter() {
		return gridAdapter;
	}

	/**
	 * Builds the customized SQL using the given filter, order and language and
	 * executes it
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @param language      The language to be applied
	 * @return The list of filtered and ordered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected List<T> doFindWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiDaoFindException {
		String sql = buildSelectSql(filterBuilder, orderBuilder, language);
		return executeCustomQuery(sql);
	}

	/**
	 * Builds the SQL using the given filter, order and language
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @param language      The language to be applied
	 * @return The SQL
	 */
	private String buildSelectSql(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language) {
		StringBuilder query = new StringBuilder();
		query.append(SELECT + ALL);
		query.append(FROM + getEntityName() + " " + SqlUtils.TABLE_PREFIX);

		if (daoRegistry.getTableLangName(this) != null) {
			query.append(addTranslationJoins());
		}

		Field field = DtoRegistry.getJavaFieldFromAllFields(getDtoClass(), IDto.LANG_FIELD_NAME);
		if (field != null && language != null) {
			FilterBuilder langFb = FilterBuilder.newOrFilter().addEquals(IDto.LANG_COLUMN_NAME, language.getIsocode())
					.addIsNull(IDto.LANG_COLUMN_NAME);

			FilterBuilder auxFb = filterBuilder;

			filterBuilder = FilterBuilder.newAndFilter().addGroup(auxFb).addGroup(langFb);
		}

		if (filterBuilder != null) {
			String where = dbHelper.processFilters(getDtoClass(), filterBuilder.asFilterGroup(), true);
			if (!StringUtils.isEmpty(where)) {
				query.append(WHERE + where);
			}
		}

		if (orderBuilder != null && !StringUtils.isEmpty(orderBuilder.toString())) {
			for (Order order : orderBuilder.getOrders()) {
				String column = DtoRegistry.getColumnNameFromFieldName(dtoClass, order.getColumn());
				if (column != null) {
					order.setColumn(column);
				}
			}

			for (Iterator<Order> it = orderBuilder.getOrders().iterator(); it.hasNext();) {
				Order order = it.next();
				if (!DtoRegistry.getColumnNames(dtoClass).contains(order.getColumn())) {
					it.remove();
				}
			}

			if (!CollectionUtils.isEmpty(orderBuilder.getOrders())) {
				query.append(ORDER_BY + orderBuilder.toString());
			}
		}

		query = SqlUtils.adjustSqlQuery(dtoClass, query);

		return query.toString();
	}

	/**
	 * Override this method to add the Left Joins to the translation tables (only
	 * useful in Tables, not in Views). Should never return null.
	 * 
	 * @return The Left Join statement
	 */
	protected String addTranslationJoins() {
		return NO_JOINS;
	}

	/**
	 * Executes an equality search over the given fieldName with the given value
	 * 
	 * @param fieldName The field name
	 * @param value     The value of the field
	 * @return The list of registries that acomplish the equality
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected List<T> findByColumn(String fieldName, Object value) throws PuiDaoFindException {
		return findByColumn(fieldName, value, PuiLanguageUtils.getSessionLanguage());
	}

	/**
	 * Executes an equality search over the given fieldName with the given value and
	 * for the given language
	 * 
	 * @param fieldName The field name
	 * @param value     The value of the field
	 * @param language  The language used in the search
	 * @return The list of registries that acomplish the equality
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected List<T> findByColumn(String fieldName, Object value, PuiLanguage language) throws PuiDaoFindException {
		String columnName = DtoRegistry.getColumnNameFromFieldName(dtoClass, fieldName);

		FilterBuilder filterBuilder = FilterBuilder.newAndFilter().addEquals(columnName, value);

		return findWhere(filterBuilder, language);
	}

	/**
	 * Executes a paginated query, starting from the given page with the given size
	 * 
	 * @param page  The page from which return the results (start with 0)
	 * @param size  The number of items to be returned
	 * @param query The query to be executed
	 * @return The paginated list
	 * @throws PuiDaoListException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private List<T> getListPaginated(int page, int size, String query) throws PuiDaoListException {
		String sql = dbHelper.getSqlForPagination(page, size, query);
		return performListPaginated(sql);
	}

	/**
	 * Really performs the List paginated against the database
	 * 
	 * @param sql The sql to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected List<T> performListPaginated(String sql) throws PuiDaoListException {
		try {
			return jdbcTemplate.query(sql, this);
		} catch (DataAccessException e) {
			throw new PuiDaoListException(e);
		}
	}

	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T dto;
		try {
			dto = dtoClass.newInstance();
		} catch (Exception e) {
			return null;
		}

		columnNames.forEach(columnName -> {
			try {
				String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, columnName);
				Field field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, columnName);
				if (field == null) {
					field = DtoRegistry.getJavaFieldFromLangFieldName(dtoClass, fieldName);
				}

				Object value = null;
				if (DtoRegistry.getDateTimeFields(dtoClass).contains(fieldName)) {
					value = rs.getTimestamp(columnName);
				} else {
					value = rs.getObject(columnName);
				}
				value = extractRealValue(field, value);

				FieldUtils.writeField(field, dto, value, true);
			} catch (Exception e) {
				// do nothing
			}
		});

		customizeDto(dto);

		return dto;
	}

	/**
	 * Extract the value for special cases: when the type of the field is different
	 * of the type that the DB returns to us
	 * 
	 * @param field The field of the DTO
	 * @param value The value extracted from database
	 * @return The value transformed
	 */
	private Object extractRealValue(Field field, Object value) {
		try {
			if (value instanceof Array) {
				List<Object> list = new ArrayList<>();
				Object[] array = (Object[]) ((Array) value).getArray();
				list.addAll(Arrays.asList(array));
				value = list;
			} else if (value instanceof Timestamp) {
				Timestamp timestamp = (Timestamp) value;
				Instant instant = timestamp.toInstant();
				value = instant;
			} else if (value instanceof Double) {
				if (field.getType().equals(BigDecimal.class)) {
					value = new BigDecimal(((Double) value).toString());
				}
			} else if (value instanceof BigDecimal) {
				if (field.getType().equals(Long.class)) {
					value = Long.valueOf(((BigDecimal) value).longValue());
				} else if (field.getType().equals(Integer.class)) {
					value = Integer.valueOf(((BigDecimal) value).intValue());
				} else {
					value = new BigDecimal(((BigDecimal) value).stripTrailingZeros().toPlainString());
				}
			} else if (value instanceof Integer) {
				if (field.getType().equals(Short.class)) {
					value = new Short(value.toString());
				}
			} else if (value instanceof Clob) {
				Clob clob = (Clob) value;
				StringWriter stringWriter = new StringWriter();
				IOUtils.copy(clob.getCharacterStream(), stringWriter);
				value = stringWriter.toString();
			}
		} catch (Exception e) {
			// do nothing
		}

		return value;
	}

	/**
	 * Override this method to execute some modification over the given DTO
	 * 
	 * @param dto The read DTO
	 */
	protected void customizeDto(T dto) {
	}

	@Override
	public Class<T> getDtoClass() {
		return dtoClass;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends IDao<T>> getDaoClass() {
		return (Class<IDao<T>>) (Object) getClass();
	}

	@Override
	public String toString() {
		return daoRegistry.getModelIdFromDao(getDaoClass());
	}

}