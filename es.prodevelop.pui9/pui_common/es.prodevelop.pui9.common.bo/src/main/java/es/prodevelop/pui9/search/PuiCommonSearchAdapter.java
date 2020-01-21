package es.prodevelop.pui9.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

/**
 * This is the default implementation for the search adapter for PUI9. It uses
 * the PUI model service to use the search method to retrieve the data list
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiCommonSearchAdapter implements IPuiSearchAdapter {

	@Autowired
	private IPuiModelService modelService;

	@Override
	public <V extends IViewDto> SearchResponse<V> search(SearchRequest req) throws PuiServiceGetException {
		return modelService.search(req);
	}

}
