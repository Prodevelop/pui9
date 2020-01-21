package es.prodevelop.pui9.common.service.interfaces;

import java.util.List;
import java.util.Map;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModelPk;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.model.configuration.PuiModelConfiguration;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiModelService extends IService<IPuiModelPk, IPuiModel, INullView, IPuiModelDao, INullViewDao> {

	/**
	 * Guess the model from the {@link SearchRequest} object, using the ViewDtoClass
	 * and the TableDtoClass. If a model is guessed, it is assigned to the
	 * {@link SearchRequest#setModel(String)} property
	 * 
	 * @param req The request object
	 * @return The whole PUI Model
	 * @throws PuiServiceGetException if any error is thrown
	 */
	IPuiModel guessModel(SearchRequest req) throws PuiServiceGetException;

	/**
	 * Perform a search using the request information (pagination, filtering, order,
	 * etc...)
	 * 
	 * @param <TYPE> The type of the returning objects
	 * @param req    The request
	 * @return An object with the search information
	 * @throws PuiServiceGetException if any error is thrown
	 */
	<TYPE> SearchResponse<TYPE> search(SearchRequest req) throws PuiServiceGetException;

	/**
	 * Reload the models cache
	 * 
	 * @param force If the models should be always reloaded
	 */
	void reloadModels(boolean force);

	/**
	 * Get the PUI Models Configuration (user based configuration)
	 * 
	 * @return A Map with the name of the model and its configuration
	 */
	Map<String, PuiModelConfiguration> getPuiModelConfigurations();

	/**
	 * Get the Original PUI Models Configuration, without user based information
	 * 
	 * @return A Map with the name of the modle and its configuration
	 */
	Map<String, PuiModelConfiguration> getOriginalPuiModelConfigurations();

	/**
	 * Get the list of all the models in the application
	 * 
	 * @return
	 */
	List<String> getAllModels();

}