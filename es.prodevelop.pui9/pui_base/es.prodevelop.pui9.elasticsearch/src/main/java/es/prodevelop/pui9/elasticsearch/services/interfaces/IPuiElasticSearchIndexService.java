package es.prodevelop.pui9.elasticsearch.services.interfaces;

import java.util.List;
import java.util.Map;

import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCountException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchExistsIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * API to manage the Indexes of ElaticSearch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiElasticSearchIndexService {

	/**
	 * Get the prefix for the indexes
	 * 
	 * @return The index prefix
	 */
	String getIndexPrefix();

	/**
	 * Create the indexes related with the view that is represented by the given
	 * View DTO Class
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @throws PuiElasticSearchNoNodesException     If Elastic Search is not
	 *                                              connected to any Node
	 * @throws PuiElasticSearchCreateIndexException If any error occurs while
	 *                                              creating the Index
	 */
	void createIndex(Class<? extends IViewDto> dtoClass)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchCreateIndexException;

	/**
	 * Create the index related with the view that is represented by the given View
	 * DTO Class, for the given language
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @param language The language that represents the index to be created. May be
	 *                 null, in that case an index for each existing language will
	 *                 be created
	 * @throws PuiElasticSearchNoNodesException     If Elastic Search is not
	 *                                              connected to any Node
	 * @throws PuiElasticSearchCreateIndexException If any error occurs while
	 *                                              creating the Index
	 */
	void createIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchCreateIndexException;

	/**
	 * Check if the existing index of the given Dto has the same mapping than the
	 * Dto
	 * 
	 * @param dtoClass The View DTO Class that represents the view
	 * @return True if the index is valid; False if not
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	boolean checkIndexMapping(Class<? extends IViewDto> dtoClass) throws PuiElasticSearchNoNodesException;

	/**
	 * Delete the given index
	 * 
	 * @param index The Index to be deleted
	 * @throws PuiElasticSearchNoNodesException     If Elastic Search is not
	 *                                              connected to any Node
	 * @throws PuiElasticSearchDeleteIndexException If any error occurs while
	 *                                              deleting the Index
	 */
	void deleteIndex(String index) throws PuiElasticSearchNoNodesException, PuiElasticSearchDeleteIndexException;

	/**
	 * Delete the indexes related with the view that is represented by the given
	 * View DTO Class
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @throws PuiElasticSearchNoNodesException     If Elastic Search is not
	 *                                              connected to any Node
	 * @throws PuiElasticSearchDeleteIndexException If any error occurs while
	 *                                              deleting the Index
	 */

	void deleteIndex(Class<? extends IViewDto> dtoClass)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchDeleteIndexException;

	/**
	 * Delete the index related with the view that is represented by the given View
	 * DTO Class, for the given language
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @param language The language that represents the index to be deleted. May be
	 *                 null, in that case an index for each existing language will
	 *                 be deleted
	 * @throws PuiElasticSearchNoNodesException     If Elastic Search is not
	 *                                              connected to any Node
	 * @throws PuiElasticSearchDeleteIndexException If any error occurs while
	 *                                              creating the Index
	 */
	void deleteIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchDeleteIndexException;

	/**
	 * Check if the index that belongs to the given View DTO Class exists or not
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @return true if the index exists, false if not
	 * @throws PuiElasticSearchNoNodesException     If Elastic Search is not
	 *                                              connected to any Node
	 * @throws PuiElasticSearchExistsIndexException If there is a index lack of a
	 *                                              translated View
	 */
	boolean existsIndex(Class<? extends IViewDto> dtoClass)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchExistsIndexException;

	/**
	 * Check if the index that belongs to the given View DTO Class exists or not,
	 * for the given language
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @param language The language that represents the index to be checked. May be
	 *                 null, in that case an index for each existing language will
	 *                 be checked
	 * @return true if the index exists, false if not
	 * @throws PuiElasticSearchNoNodesException     If Elastic Search is not
	 *                                              connected to any Node
	 * @throws PuiElasticSearchExistsIndexException If the number of existing
	 *                                              indexes and the number of
	 *                                              indexes that should exists is
	 *                                              not equal
	 */
	boolean existsIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchExistsIndexException;

	/**
	 * Get the list of existing indexes names
	 * 
	 * @return The list of existing indexes names
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	List<String> getAllIndexes() throws PuiElasticSearchNoNodesException;

	/**
	 * Get the number of indexed documents for the given View DTO Class
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @return The number of indexed documents
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 * @throws PuiElasticSearchCountException   If multiple indexes (languages) are
	 *                                          checked and the value is distinct
	 *                                          for each one
	 */
	long countIndex(Class<? extends IViewDto> dtoClass)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchCountException;

	/**
	 * Get the number of indexed documents for the given View DTO Class
	 * 
	 * @param dtoClass The View DTO Class that represents the View
	 * @param language The language that represents the index to be checked. May be
	 *                 null, in that case an index for each existing language will
	 *                 be checked
	 * @return The number of indexed documents
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 * @throws PuiElasticSearchCountException   If multiple indexes (languages) are
	 *                                          checked and the value is distinct
	 *                                          for each one
	 */
	long countIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchCountException;

	/**
	 * Get the number of registries for the given indexex
	 * 
	 * @param indexes The list of the indexes to retrieve the count
	 * @return The number of registries for each index
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	Map<String, Long> countIndex(List<String> indexes) throws PuiElasticSearchNoNodesException;
}
