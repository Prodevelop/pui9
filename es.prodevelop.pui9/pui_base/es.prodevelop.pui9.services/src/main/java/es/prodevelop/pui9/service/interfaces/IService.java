package es.prodevelop.pui9.service.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceCopyRegistryException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceExistsException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * Interface Service for all the Services of PUI. It offers multiple useful
 * methods that avoid to use directly the DAO. All the developers should use
 * this layer as possible before using directly the DAO layer
 * <p>
 * If you want to use a service, you must to create an Autowired property using
 * the interface of this Service. It is highly recommended to use and reference
 * the Services instead of the DAOs
 * 
 * @param <TPK>  The {@link ITableDto} PK for the Table (if the service has one
 *               associated)
 * @param <T>    The whole {@link ITableDto} for the Table (if the service has
 *               one associated)
 * @param <V>    The {@link IViewDto} for the View (if the service has one
 *               associated)
 * @param <DAO>  The {@link ITableDao} for the Table
 * @param <VDAO> The {@link IViewDao} for the View
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IService<TPK extends ITableDto, T extends TPK, V extends IViewDto, DAO extends ITableDao<TPK, T>, VDAO extends IViewDao<V>> {

	/**
	 * Get the DAO that represents the Table
	 * 
	 * @return The DAO of the Table
	 */
	DAO getTableDao();

	/**
	 * Get the DAO that represents the View
	 * 
	 * @return The DAO of the View
	 */
	VDAO getViewDao();

	/**
	 * Get the DTO class that represents the whole Table
	 * 
	 * @return The DTO class of the whole Table
	 */
	Class<T> getTableDtoClass();

	/**
	 * Get the DTO PK class that represents the PK of the Table
	 * 
	 * @return The DTO PK class of the PK of the Table
	 */
	Class<TPK> getTableDtoPkClass();

	/**
	 * Get the DTO class that represents the View
	 * 
	 * @return The DTO class of the View
	 */
	Class<V> getViewDtoClass();

	/**
	 * Returns a new empty registry. Typically used to create new registries into
	 * the Database
	 * 
	 * @return A new object representing the Table
	 * @throws PuiServiceNewException If an exception is thrown while creating a new
	 *                                object
	 */
	T getNew() throws PuiServiceNewException;

	/**
	 * Returns a new empty regitry for the given language. Typically used to create
	 * new registries into the Database
	 * 
	 * @param language The desired language of the object
	 * @return A new object representing the Table
	 * @throws PuiServiceNewException If an exception is thrown while creating a new
	 *                                object
	 */
	T getNew(PuiLanguage language) throws PuiServiceNewException;

	/**
	 * Checks if exists a registry represented by the given PK in the Database
	 * 
	 * @param dtoPk The PK of the registry
	 * @return true if exists; false if not
	 * @throws PuiServiceExistsException If an exception is thrown while checking
	 *                                   the existence of the registry
	 */
	boolean exists(TPK dtoPk) throws PuiServiceExistsException;

	/**
	 * Checks if exists a registry represented by the given PK in the Database for
	 * the given language
	 * 
	 * @param dtoPk    The PK of the registry
	 * @param language The desired language
	 * @return true if exists; false if not
	 * @throws PuiServiceExistsException If an exception is thrown while checking
	 *                                   the existence of the registry
	 */
	boolean exists(TPK dtoPk, PuiLanguage language) throws PuiServiceExistsException;

	/**
	 * Get the registry represented by the given PK.
	 * 
	 * @param dtoPk The PK of the registry
	 * @return The registry from the Table
	 * @throws PuiServiceGetException If the registry doesn't exist
	 */
	T getByPk(TPK dtoPk) throws PuiServiceGetException;

	/**
	 * Get the registry represented by the given PK for the given language
	 * 
	 * @param dtoPk    The PK of the registry
	 * @param language The desired language
	 * @return The registry from the Table
	 * @throws PuiServiceGetException If the registry doesn't exist
	 */
	T getByPk(TPK dtoPk, PuiLanguage language) throws PuiServiceGetException;

	/**
	 * Get the registry from the View that belongs to the given PK in the associated
	 * Table. Take into account that the name of the columns in the PK must coincide
	 * in the View
	 * 
	 * @param dtoPk The PK of the registry in the Table
	 * @return The registry from the View
	 * @throws PuiServiceGetException If the registry doesn't exist
	 */
	V getViewByPk(TPK dtoPk) throws PuiServiceGetException;

	/**
	 * Get the registry from the View that belongs to the given PK in the associated
	 * Table, for the given language. Take into account that the name of the columns
	 * in the PK must coincide in the View
	 * 
	 * @param dtoPk    The PK of the registry in the Table
	 * @param language The desired language
	 * @return The registry from the View
	 * @throws PuiServiceGetException If the registry doesn't exist
	 */
	V getViewByPk(TPK dtoPk, PuiLanguage language) throws PuiServiceGetException;

	/**
	 * Get all the registries from the Table (use this method when the amount of
	 * registries in the table is not so large)
	 * 
	 * @return All the registries in the Table; empty list when no registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<T> getAll() throws PuiServiceGetException;

	/**
	 * Get all the registries from the Table ordered by given order (use this method
	 * when the amount of registries is not so large)
	 * 
	 * @param orderBuilder The desired order
	 * @return All the ordered registries in the Table; empty list when no
	 *         registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<T> getAll(OrderBuilder orderBuilder) throws PuiServiceGetException;

	/**
	 * Get all the registries from the Table for the given language (use this method
	 * when the amount of registries is not so large)
	 * 
	 * @param language The desired language
	 * @return All the registries in the Table for the given language; empty list
	 *         when no registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<T> getAll(PuiLanguage language) throws PuiServiceGetException;

	/**
	 * Get all the registries from the Table for the given language ordered by given
	 * order (use this method when the amount of registries is not so large)
	 * 
	 * @param orderBuilder The desired order
	 * @param language     The desired language
	 * @return All the ordered registries in the Table for the given language; empty
	 *         list when no registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<T> getAll(OrderBuilder orderBuilder, PuiLanguage language) throws PuiServiceGetException;

	/**
	 * Returns all the registries from the Table that accomplish the given filter
	 * 
	 * @param filterBuilder The filter
	 * @return The list of registries that accomplish the filter; empty list when no
	 *         registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<T> getAllWhere(FilterBuilder filterBuilder) throws PuiServiceGetException;

	/**
	 * Returns all the registries from the Table that accomplish the given filter,
	 * ordered by the given order
	 * 
	 * @param filterBuilder The filter
	 * @param orderBuilder  The desired order
	 * @return The ordered list of registries that accomplish the filter; empty list
	 *         when no registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<T> getAllWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiServiceGetException;

	/**
	 * Get all the registries from the View (use this method when the amount of
	 * registries is not so large)
	 * 
	 * @return All the registries in the View; empty list when no registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<V> getAllView() throws PuiServiceGetException;

	/**
	 * Get all the registries in the View for the given language(use this method
	 * when the amount of registries is not so large)
	 * 
	 * @param language The desired language
	 * 
	 * @return All the registries in the View for the given languagew; empty list
	 *         when no registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<V> getAllView(PuiLanguage language) throws PuiServiceGetException;

	/**
	 * Returns all the registries from the View that accomplish the given filter
	 * 
	 * @param filterBuilder The filter
	 * @return The list of registries that accomplish the filter; empty list when no
	 *         registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<V> getAllViewWhere(FilterBuilder filterBuilder) throws PuiServiceGetException;

	/**
	 * Returns all the registries from the View that accomplish the given filter,
	 * ordered by the given order
	 * 
	 * @param filterBuilder The filter
	 * @param orderBuilder  The desired order
	 * @return The ordered list of registries that accomplish the filter; empty list
	 *         when no registries
	 * @throws PuiServiceGetException If an exception is thrown while getting the
	 *                                registries
	 */
	List<V> getAllViewWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiServiceGetException;

	/**
	 * Inserts the given registry into the Database
	 * 
	 * @param dto The registry to be inserted
	 * @return The same inserted registry
	 * @throws PuiServiceInsertException If an exception is thrown while inserting
	 *                                   the registry
	 */
	@Transactional(rollbackFor = PuiException.class)
	T insert(T dto) throws PuiServiceInsertException;

	/**
	 * Performs a bulk insert for all the given objects. Use this method only if you
	 * are sure that the objects don't exist in the database. Methods beforeInsert
	 * and afterInsert are not called
	 * 
	 * @param dtoList The list of registries to be inserted
	 * @return The same inserted registries
	 * @throws PuiServiceInsertException If an exception is thrown while inserting
	 *                                   the registries
	 */
	@Transactional(rollbackFor = PuiException.class)
	List<T> bulkInsert(List<T> dtoList) throws PuiServiceInsertException;

	/**
	 * Updates the given registry into the Database
	 * 
	 * @param dto The registry to be inserted
	 * @return The same updated registry
	 * @throws PuiServiceUpdateException If an exception is thrown while updating
	 *                                   the registry
	 */
	@Transactional(rollbackFor = PuiException.class)
	T update(T dto) throws PuiServiceUpdateException;

	/**
	 * Performs a bulk update for all the given objects. Use this method only if you
	 * are sure that all the objects exist in the database. Methods beforeUpdate and
	 * afterUpdate are not called
	 * 
	 * @param dtoList The registries to be inserted
	 * @return The same updated registries
	 * @throws PuiServiceUpdateException If an exception is thrown while updating
	 *                                   the registries
	 */
	@Transactional(rollbackFor = PuiException.class)
	List<T> bulkUpdate(List<T> dtoList) throws PuiServiceUpdateException;

	/**
	 * Updates only part of the the given registry into the Database. Preferably,
	 * use FIELDS instead of COLUMNS as keys in the map
	 * 
	 * @param dtoPk          The PK of the registry to be updated
	 * @param fieldValuesMap A map of the field names with the associated new values
	 * @return The same updated registry
	 * @throws PuiServiceUpdateException If an exception is thrown while updating
	 *                                   the registry
	 */
	@Transactional(rollbackFor = PuiException.class)
	T patch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiServiceUpdateException;

	/**
	 * Deletes the given registry from the Database
	 * 
	 * @param dtoPk The PK of the registry to be deleted
	 * @return The same PK of the deleted registry
	 * @throws PuiServiceDeleteException If an exception is thrown while deleting
	 *                                   the registry
	 */
	@Transactional(rollbackFor = PuiException.class)
	TPK delete(TPK dtoPk) throws PuiServiceDeleteException;

	/**
	 * Performs a bulk delete of all the given objects. Use this method only if you
	 * are sure that all the objects exist in the database. Methods beforeDelete and
	 * afterDelete are not called
	 * 
	 * @param dtoPkList The PK list of the registry to be deleted
	 * @return The same PK list of the deleted registries
	 * @throws PuiServiceDeleteException If an exception is thrown while deleting
	 *                                   the registries
	 */
	@Transactional(rollbackFor = PuiException.class)
	List<TPK> bulkDelete(List<? extends TPK> dtoPkList) throws PuiServiceDeleteException;

	/**
	 * Deletes all the registries from the Database
	 * 
	 * @throws PuiServiceDeleteException If an exception is thrown while deleting
	 *                                   the registries
	 */
	@Transactional(rollbackFor = PuiException.class)
	void deleteAll() throws PuiServiceDeleteException;

	/**
	 * Deletes all the registries with the given language from the Ddatabase
	 * 
	 * @param language The language of the registries to be deleted
	 * @throws PuiServiceDeleteException If an exception is thrown while deleting
	 *                                   the registries
	 */
	@Transactional(rollbackFor = PuiException.class)
	void deleteAll(PuiLanguage language) throws PuiServiceDeleteException;

	/**
	 * Makes an exact copy of the registry represented by the given PK. This method
	 * calls the getByPk(TPK) method, and remove the values of the PK
	 * 
	 * @param pk The PK of the registry to be copied
	 * @return The copy of the registry
	 * @throws PuiServiceCopyRegistryException If an exception is throws while
	 *                                         copying the registry
	 */
	@Transactional(rollbackFor = PuiException.class)
	T copy(TPK pk) throws PuiServiceCopyRegistryException;
}
