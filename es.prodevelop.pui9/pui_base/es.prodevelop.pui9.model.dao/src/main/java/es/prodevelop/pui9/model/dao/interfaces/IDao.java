package es.prodevelop.pui9.model.dao.interfaces;

import java.util.List;

import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.list.adapters.IListAdapter;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * This interface represents a DAO for JDBC approach. All the DAO classes that
 * belongs to JDBC, should inherit from this interface
 * <p>
 * If you want to use a DAO, you must to create an Autowired property using the
 * interface of this DAO.
 * 
 * @param <T> The whole DTO class that represents this DAO Interface
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IDao<T extends IDto> {

	/**
	 * Get the number of rows in the entity
	 * 
	 * @return The number of rows
	 * @throws PuiDaoCountException If any SQL error while executing the statement
	 *                              is thrown
	 */
	Long count() throws PuiDaoCountException;

	/**
	 * Get the number of rows in the table that accomplish the given filter
	 * 
	 * @param filterBuilder The filter applied to the operation
	 * @return The number of rows
	 * @throws PuiDaoCountException If any SQL error while executing the statement
	 *                              is thrown
	 */
	Long count(FilterBuilder filterBuilder) throws PuiDaoCountException;

	/**
	 * Get the number of rows in the table that accomplish the given filter. Also
	 * you can specify a column to make the count over it, and if you want to take
	 * into account only distinct values of this column
	 * 
	 * @param column        The column to make the count over it (<b>*</b> by
	 *                      default)
	 * @param distinct      If a column is specified, you can set if take into
	 *                      account only distinct values or not
	 * @param filterBuilder The filter applied to the operation)
	 * @return The number of rows
	 * @throws PuiDaoCountException If any SQL error while executing the statement
	 *                              is thrown
	 */
	Long count(String column, boolean distinct, FilterBuilder filterBuilder) throws PuiDaoCountException;

	/**
	 * Returns the next value for the given column that accomplished the given
	 * filter
	 * 
	 * @param columnName    The column to retrieve the next value
	 * @param filterBuilder The filter to be applied
	 * @return The next value for given column
	 * @throws PuiDaoNoNumericColumnException If the given column is not a numeric
	 *                                        column
	 */
	<N extends Number> N getNextValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException;

	/**
	 * Returns the max value for the given Column that accomplished the given
	 * condition
	 * 
	 * @param columnName    The column to retrieve the next value
	 * @param filterBuilder The filter to be applied
	 * @return The max value for given column
	 * @throws PuiDaoNoNumericColumnException If the given column is not a numeric
	 *                                        column
	 */
	<N extends Number> N getMaxValue(String columnName, FilterBuilder filterBuilder)
			throws PuiDaoNoNumericColumnException;

	/**
	 * Returns a single row matching the given filter
	 * 
	 * @param filterBuilder The filter to be applied
	 * @return The registry
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	T findOne(FilterBuilder filterBuilder) throws PuiDaoFindException;

	/**
	 * Returns a single row matching the given filter, using the given language
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param language      The language to be retrieved
	 * @return The registry for the specified language
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	T findOne(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException;

	/**
	 * Returns all the registries of this entity
	 * 
	 * @return All the registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAll() throws PuiDaoFindException;

	/**
	 * Returns all the registries of this entity for the given language
	 * 
	 * @param language The language to be applied
	 * @return All the registries for the specified language
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAll(PuiLanguage language) throws PuiDaoFindException;

	/**
	 * Returns all the registries of this entity ordered by the given order
	 * 
	 * @param orderBuilder The order to be applied
	 * @return All the registries order
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAll(OrderBuilder orderBuilder) throws PuiDaoFindException;

	/**
	 * Returns all the registries of this entity for the given language ordered by
	 * the given order
	 * 
	 * @param orderBuilder The order to be applied
	 * @param language     The language to be applied
	 * @return All the registries for the specified language ordered
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAll(OrderBuilder orderBuilder, PuiLanguage language) throws PuiDaoFindException;

	/**
	 * Returns a list of registries (as maximum of registries specified in the size
	 * parameter) that accomplished the given filter for the given language, ordered
	 * with the given order, and paginated from the given page
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @param language      The language to be applied
	 * @param from          The page to retrieve (starting from 0)
	 * @param size          The number of registries to retrieve
	 * @return The paginated list of registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findAllPagination(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language,
			Integer from, Integer size) throws PuiDaoFindException;

	/**
	 * Returns a list of registries that accomplish the given filter
	 * 
	 * @param filterBuilder The filter to be applied
	 * @return The list of filtered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findWhere(FilterBuilder filterBuilder) throws PuiDaoFindException;

	/**
	 * Returns a list of registries that accomplish the given filter, ordered by the
	 * given order
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @return The list of filtered and ordered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiDaoFindException;

	/**
	 * Returns a list of registries that accomplish the given filter, and only for
	 * the specified language
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param language      The language to be applied
	 * @return The list of filtered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findWhere(FilterBuilder filterBuilder, PuiLanguage language) throws PuiDaoFindException;

	/**
	 * Returns a list of registries that accomplish the given filter, ordered by the
	 * given order, and only for the specified language
	 * 
	 * @param filterBuilder The filter to be applied
	 * @param orderBuilder  The order to be applied
	 * @param language      The language to be applied
	 * @return The list of filtered and ordered registries
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> findWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiDaoFindException;

	/**
	 * Execute a custom SQL statement over the entity
	 * 
	 * @param sql The SQL statement
	 * @return The list of registries that accomplish the given statement
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> executeCustomQuery(String sql) throws PuiDaoFindException;

	/**
	 * Execute a custom SQL statement over the entity, with the capability of using
	 * parameters in the query
	 * 
	 * @param sql        The SQL statement
	 * @param parameters The parameters of the query
	 * @return The list of registries that accomplish the given statement
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	List<T> executeCustomQueryWithParameters(String sql, List<Object> parameters) throws PuiDaoFindException;

	/**
	 * Returns the Class that represents the associated DTO Object
	 * 
	 * @return The DTO Class
	 */
	Class<T> getDtoClass();

	/**
	 * Returns the Dao Class. Use this method to obtain the real Dao class, useful
	 * if the class is a proxy
	 * 
	 * @return The real DAO Class
	 */
	Class<? extends IDao<T>> getDaoClass();

	/**
	 * Returns a list of records of the Table/View for the given configuration
	 * 
	 * @param req The configuration of the search
	 * @return The response with the data that fits the request
	 * @throws PuiDaoListException If an error is thrown while executing the
	 *                             statement
	 */
	SearchResponse<T> findForDataGrid(SearchRequest req) throws PuiDaoListException;

	/**
	 * This method returns the Grid Adapter, used to configure the searches
	 * 
	 * @return The GridAdapter for the DTO represented by this DAO
	 */
	IListAdapter<T> getGridAdapter();

}
