package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;

/**
 * Event for the List action
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ListEvent<V extends IViewDto> extends PuiEvent<SearchRequest> {

	private static final long serialVersionUID = 1L;

	private SearchResponse<V> response;

	public ListEvent(SearchRequest request, SearchResponse<V> response) {
		super(request, "list");
		this.response = response;
	}

	public SearchResponse<V> getResponse() {
		return response;
	}

}
