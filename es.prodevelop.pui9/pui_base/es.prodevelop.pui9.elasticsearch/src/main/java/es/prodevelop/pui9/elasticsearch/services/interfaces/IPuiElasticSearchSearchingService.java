package es.prodevelop.pui9.elasticsearch.services.interfaces;

import java.util.List;

import es.prodevelop.pui9.elasticsearch.dto.ESSearchResultItem;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchSearchException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * API to manage the Searches against ElasticSearch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiElasticSearchSearchingService {

	/**
	 * Get the number of indexed documents that fits the given filter (language
	 * independent)
	 * 
	 * @param dtoClass      The View DTO Class that represents the View to be
	 *                      searched
	 * @param filterBuilder The filter to apply in the search
	 * @return The Number of elements that fits the given filter
	 * @throws PuiElasticSearchSearchException  If an error occurs while searching
	 *                                          the documents that fits the given
	 *                                          filter
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	Long count(Class<? extends IViewDto> dtoClass, FilterBuilder filterBuilder)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException;

	/**
	 * Get the maximum value of the given column that fits the given filter
	 * 
	 * @param dtoClass      The View DTO Class that represents the View to be
	 *                      searched
	 * @param column        The column to search the maximum value
	 * @param filterBuilder The filter to apply in the search
	 * @return The maximum value for the given column number (0 is no documents were
	 *         found)
	 * @throws PuiElasticSearchSearchException  If an error occurs while searching
	 *                                          the documents that fits the given
	 *                                          filter
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	<N extends Number> N getMaxValue(Class<? extends IViewDto> dtoClass, String column, FilterBuilder filterBuilder)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException;

	/**
	 * Get a document that fits the given filter, within the given index (language)
	 * 
	 * @param dtoClass      The View DTO Class that represents the View to be
	 *                      searched
	 * @param filterBuilder The filter to apply in the search
	 * @param orderBuilder  The order of the search
	 * @param language      The language of the documents (the index is calculated
	 *                      from it)
	 * @return The document from the given index (language) that fits the given
	 *         filter
	 * @throws PuiElasticSearchSearchException  If an error occurs while searching
	 *                                          the documents that fits the given
	 *                                          filter
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	ESSearchResultItem findOne(Class<? extends IViewDto> dtoClass, FilterBuilder filterBuilder,
			OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException;

	/**
	 * Search for the documents that fits the given filter, in the given order,
	 * within the given index (language)
	 * 
	 * @param dtoClass      The View DTO Class that represents the View to be
	 *                      searched
	 * @param filterBuilder The filter to apply in the search
	 * @param orderBuilder  The order of the search
	 * @param language      The language of the documents (the index is calculated
	 *                      from it)
	 * @return The list of document from the given index (language) that fits the
	 *         given filter, ordered by the given order
	 * @throws PuiElasticSearchSearchException  If an error occurs while searching
	 *                                          the documents that fits the given
	 *                                          filter
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	List<ESSearchResultItem> findMultiple(Class<? extends IViewDto> dtoClass, FilterBuilder filterBuilder,
			OrderBuilder orderBuilder, PuiLanguage language)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException;

	/**
	 * Executes a search to be used in the Grid
	 * 
	 * @param req The information of the search
	 * @return The list of documents in {@link SearchResponse} format
	 * @throws PuiElasticSearchSearchException  If an error occurs while searching
	 *                                          the documents that fits the given
	 *                                          filter
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	<V extends IViewDto> SearchResponse<V> findForDataGrid(SearchRequest req)
			throws PuiElasticSearchSearchException, PuiElasticSearchNoNodesException;

}
