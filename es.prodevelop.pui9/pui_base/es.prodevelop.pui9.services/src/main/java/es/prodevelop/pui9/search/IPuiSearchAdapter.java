package es.prodevelop.pui9.search;

import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

/**
 * This is an interface to deal with the API search. The only one method takes
 * the request and makes the possible to return the expected response
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiSearchAdapter {

	/**
	 * The search method to retrieve the requested data
	 * 
	 * @param <V> The IViewDto returned
	 * @param req The parameters of the search
	 * @return The information of the search (current page, total pages, number of
	 *         registries, total of registries) including the list of registries
	 * @throws PuiServiceGetException If any exception occurs while getting the
	 *                                registry
	 */
	<V extends IViewDto> SearchResponse<V> search(SearchRequest req) throws PuiServiceGetException;

}
