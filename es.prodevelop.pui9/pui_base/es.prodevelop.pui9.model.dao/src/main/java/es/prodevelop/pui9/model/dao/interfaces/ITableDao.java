package es.prodevelop.pui9.model.dao.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiDaoUpdateException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * This interface represents a DAO for a Table. It provides useful methods to
 * manage the registries in the table: insert and update (save), delete, exists,
 * findOne (by PK)...
 * 
 * @param <TPK> The PK {@link IDto} class for this DAO
 * @param <T>   The whole {@link IDto} class for this DAO
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface ITableDao<TPK extends ITableDto, T extends TPK> extends IDao<T> {

	/**
	 * Returns whether an entity with the given id exists.
	 * 
	 * @param dtoPk The DTO PK (Should not be null)
	 * @return true if the registry exists; false if not
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	boolean exists(TPK dtoPk) throws PuiDaoFindException;

	/**
	 * Saves a given entity (both for insert or update)
	 * <p>
	 * Use the returned instance for further operations as the save operation might
	 * have changed the entity instance completely.
	 * 
	 * @param dto The DTO to be saved
	 * @return The saved DTO
	 * @throws PuiDaoSaveException If any SQL error while executing the statement is
	 *                             thrown
	 */
	@Transactional(rollbackFor = PuiException.class)
	T save(T dto) throws PuiDaoSaveException;

	/**
	 * Performs a bulk insert for all the given objects. Use this method only if you
	 * are sure that the objects don't exist in the database.
	 * 
	 * @param dtoList The list of objects to be inserted
	 * @return The same list of objects
	 */
	@Transactional(rollbackFor = PuiException.class)
	List<T> bulkInsert(List<T> dtoList) throws PuiDaoInsertException;

	/**
	 * Performs a bulk update for all the given objects. Use this method only if you
	 * are sure that all the objects exist in the database.
	 * 
	 * @param dtoList The list of objects to be updated
	 * @return The same list of objects
	 */
	@Transactional(rollbackFor = PuiException.class)
	List<T> bulkUpdate(List<T> dtoList) throws PuiDaoUpdateException;

	/**
	 * Performs a patch over the given DTO.
	 * 
	 * @param dtoPk          The PK of the DTO to be patched
	 * @param fieldValuesMap The map of fields and values to be set
	 * @return The PK of the DTO
	 */
	@Transactional(rollbackFor = PuiException.class)
	TPK patch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiDaoSaveException;

	/**
	 * Deletes the entity with the given id.
	 * 
	 * @param dtoPk The DTO PK of the registry to be deleted
	 * @return The DTO PK of the deleted registry
	 * @throws PuiDaoDeleteException If any SQL error while executing the statement
	 *                               is thrown
	 */
	@Transactional(rollbackFor = PuiException.class)
	TPK delete(TPK dtoPk) throws PuiDaoDeleteException;

	/**
	 * Performs a bulk delete of all the given objects. Use this method only if you
	 * are sure that all the objects exist in the database.
	 * 
	 * @param dtoList The list of objects to be deleted
	 * @return The same list of objects
	 */
	@Transactional(rollbackFor = PuiException.class)
	List<TPK> bulkDelete(List<TPK> dtoPkList) throws PuiDaoDeleteException;

	/**
	 * Deletes all the registries in the table. Use it in care, due to all the
	 * registries will be deleted
	 * 
	 * @throws PuiDaoDeleteException If any SQL error while executing the statement
	 *                               is thrown
	 */
	@Transactional(rollbackFor = PuiException.class)
	void deleteAll() throws PuiDaoDeleteException;

	/**
	 * Deletes all the registries with the given language from the table. Use it in
	 * care, due to all the registries of given language will be deleted
	 * 
	 * @param language The language of the DTO to be deleted
	 * @throws PuiDaoDeleteException If any SQL error while executing the statement
	 *                               is thrown
	 */
	@Transactional(rollbackFor = PuiException.class)
	void deleteAll(PuiLanguage language) throws PuiDaoDeleteException;

	/**
	 * Delete all the registry in the table that accomplish the given condition
	 * 
	 * @param filterBuilder The Filter to be applied in the delete operation
	 * @throws PuiDaoDeleteException If any SQL error while executing the statement
	 *                               is thrown
	 */
	@Transactional(rollbackFor = PuiException.class)
	void deleteWhere(FilterBuilder filterBuilder) throws PuiDaoDeleteException;

	/**
	 * Get the whole registry identified by the given DTO PK
	 * 
	 * @param dtoPk The PK of the registry to be retrieved
	 * @return The whole registry
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	T findOne(TPK dtoPk) throws PuiDaoFindException;

	/**
	 * Get the whole registry identified by the given DTO PK, for the given language
	 * 
	 * @param dtoPk The PK of the registry to be retrieved
	 * @param lang  The language of the registry
	 * @return The whole registry of the given language
	 * @throws PuiDaoFindException If any SQL error while executing the statement is
	 *                             thrown
	 */
	T findOne(TPK dtoPk, PuiLanguage lang) throws PuiDaoFindException;

	/**
	 * Returns the Class that represents the associated DTO PK Object
	 * 
	 * @return The DTO PK Class
	 */
	Class<TPK> getDtoPkClass();

}
