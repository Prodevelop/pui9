package es.prodevelop.pui9.model.dao;

import org.springframework.beans.factory.annotation.Autowired;

import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchSearchException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchSearchingService;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;

/**
 * This abstract class provides the implementation of the all the View DAO for
 * JDBC approach. It implements {@link IViewDao} interface for bringing the
 * necessary methods to manage the views
 * 
 * @param <T> The whole {@link IDto} class that represents this DAO Class
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractViewDao<T extends IViewDto> extends AbstractDao<T> implements IViewDao<T> {

	@Autowired(required = false)
	private IPuiElasticSearchEnablement elasticSearchEnablement;

	@Autowired(required = false)
	private IPuiElasticSearchSearchingService elasticSearchSearchingService;

	@Override
	public SearchResponse<T> findForDataGrid(SearchRequest req) throws PuiDaoListException {
		if (elasticSearchEnablement != null && elasticSearchEnablement.isElasticSearchAvailable()
				&& elasticSearchEnablement.isElasticSearchActive()) {
			try {
				return elasticSearchSearchingService.findForDataGrid(req);
			} catch (PuiElasticSearchSearchException | PuiElasticSearchNoNodesException e) {
				return super.findForDataGrid(req);
			}
		} else {
			return super.findForDataGrid(req);
		}
	}

}