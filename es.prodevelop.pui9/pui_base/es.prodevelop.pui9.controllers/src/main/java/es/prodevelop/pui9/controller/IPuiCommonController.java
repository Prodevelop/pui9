package es.prodevelop.pui9.controller;

import java.util.Map;

import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * This is the interface that all the PUI Controllers should implement in order
 * to be compliant to the way of working with PUI.
 * <p>
 * It brings the most common web services:
 * <ul>
 * <li>template: returns a new empty object</li>
 * <li>get: returns the registry requested by its PK</li>
 * <li>list: returns a list of registries that accomplish the given parameters
 * (filters, pagination...)</li>
 * <li>export: export to CSV or Excel a list of registries</li>
 * <li>insert: insert a new registry</li>
 * <li>update: update the whole given registry</li>
 * <li>patch: update only part of the given registry</li>
 * <li>delete: delete the registry by its PK</li>
 * </ul>
 * 
 * @param <TPK>  The {@link ITableDto} PK for the Table (if the service has one
 *               associated)
 * @param <T>    The whole {@link ITableDto} for the Table (if the service has
 *               one associated)
 * @param <V>    The {@link IViewDto} for the View (if the service has one
 *               associated)
 * @param <DAO>  The {@link ITableDao} for the Table
 * @param <VDAO> The {@link IViewDao} for the View
 * @param <S>    The {@link IService} for the Controller
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiCommonController<TPK extends ITableDto, T extends TPK, V extends IViewDto, DAO extends ITableDao<TPK, T>, VDAO extends IViewDao<V>, S extends IService<TPK, T, V, DAO, VDAO>> {

	/**
	 * Get the template of the registry. Mainly used for creating a new registry
	 * 
	 * @return A new empty registry
	 * @throws PuiServiceNewException If any exception occurs while creating the new
	 *                                registry
	 */
	T template() throws PuiServiceNewException;

	/**
	 * Get an existent registry from its PK
	 * 
	 * @param dtoPk The PK of the registry
	 * @return The whole requested registry
	 * @throws PuiServiceGetException If any exception occurs while getting the
	 *                                registry
	 */
	T get(TPK dtoPk) throws PuiServiceGetException;

	/**
	 * Insert a new registry
	 * 
	 * @param dto The whole registry
	 * @return The inserted registry
	 * @throws PuiServiceInsertException If any exception occurs while inserting the
	 *                                   registry
	 */
	T insert(T dto) throws PuiServiceInsertException;

	/**
	 * Update an existing registry
	 * 
	 * @param dto The whole registry
	 * @return The updated registry
	 * @throws PuiServiceGetException    If any exception occurs while getting the
	 *                                   registry
	 * @throws PuiServiceUpdateException If any exception occurs while updating the
	 *                                   registry
	 */
	T update(T dto) throws PuiServiceGetException, PuiServiceUpdateException;

	/**
	 * Update only part of a registry
	 * 
	 * @param dtoPk      The PK of the registry to be updated partially
	 * @param properties A map of the properties to be updated
	 * @return The updated registry
	 * @throws PuiServiceGetException    If any exception occurs while getting the
	 *                                   registry
	 * @throws PuiServiceUpdateException If any exception occurs while updating the
	 *                                   registry
	 */
	T patch(TPK dtoPk, Map<String, Object> properties) throws PuiServiceGetException, PuiServiceUpdateException;

	/**
	 * Delete an existing registry
	 * 
	 * @param dtoPk The PK of the registry to be deleted
	 * @return The deleted registry
	 * @throws PuiServiceGetException    If any exception occurs while getting the
	 *                                   registry
	 * @throws PuiServiceDeleteException If any exception occurs while deleting the
	 *                                   registry
	 */
	TPK delete(TPK dtoPk) throws PuiServiceGetException, PuiServiceDeleteException;

	/**
	 * List all registries that accomplish the given parameters (filters,
	 * pagination...)
	 * 
	 * @param req The parameters of the search
	 * @return The information of the search (current page, total pages, number of
	 *         registries, total of registries) including the list of registries
	 * @throws PuiServiceGetException If any exception occurs while getting the
	 *                                registry
	 */
	SearchResponse<V> list(SearchRequest req) throws PuiServiceGetException;

	/**
	 * Export the list of registries to CSV or Excel
	 * 
	 * @param req The parameters of the export
	 * @return The descriptor of the download
	 */
	FileDownload export(ExportRequest req);

	/**
	 * Check if the "get" operation is allowed by this Controller
	 * 
	 * @return true if allowed; false if not
	 */
	boolean allowGet();

	/**
	 * Check if the "template" operation is allowed by this Controller
	 * 
	 * @return true if allowed; false if not
	 */
	public boolean allowTemplate();

	/**
	 * Check if the "insert" operation is allowed by this Controller
	 * 
	 * @return true if allowed; false if not
	 */
	boolean allowInsert();

	/**
	 * Check if the "update" operation is allowed by this Controller
	 * 
	 * @return true if allowed; false if not
	 */
	boolean allowUpdate();

	/**
	 * Check if the "patch" operation is allowed by this Controller
	 * 
	 * @return true if allowed; false if not
	 */
	boolean allowPatch();

	/**
	 * Check if the "delete" operation is allowed by this Controller
	 * 
	 * @return true if allowed; false if not
	 */
	boolean allowDelete();

	/**
	 * Check if the "list" operation is allowed by this Controller
	 * 
	 * @return true if allowed; false if not
	 */
	boolean allowList();

	/**
	 * Check if the "export" operation is allowed by this Controller
	 * 
	 * @return true if allowed; false if not
	 */
	boolean allowExport();

}
