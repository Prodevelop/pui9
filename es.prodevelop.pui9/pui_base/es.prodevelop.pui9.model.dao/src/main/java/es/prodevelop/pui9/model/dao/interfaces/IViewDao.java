package es.prodevelop.pui9.model.dao.interfaces;

import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.search.SearchRequest;

/**
 * 
 * This interface represents a DAO for a View. The only method provided by this
 * interface valid to be used outside PUI, is
 * {@link #findForDataGrid(SearchRequest)} method, that returns a list of
 * registries for the clients. Other useful methods are defined in the
 * {@link IDao} interface
 * 
 * @param <T> The whole {@link IDto} class for this DAO
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IViewDao<T extends IViewDto> extends IDao<T> {

}
