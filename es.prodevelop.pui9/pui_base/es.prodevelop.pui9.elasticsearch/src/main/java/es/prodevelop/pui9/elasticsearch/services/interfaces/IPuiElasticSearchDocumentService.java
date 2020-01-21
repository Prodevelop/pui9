package es.prodevelop.pui9.elasticsearch.services.interfaces;

import java.util.List;

import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchInsertDocumentException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * API to manage documents into ElasticSearch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiElasticSearchDocumentService {

	/**
	 * Inserts the given DTO list into ElasticSearch. Ensure that the list of
	 * registries are not indexed
	 * 
	 * @param dtoList  The List of View DTO to be inserted
	 * @param language The language of the DTO to choose the correct index
	 * @throws PuiElasticSearchNoNodesException        If Elastic Search is not
	 *                                                 connected to any Node
	 * @throws PuiElasticSearchInsertDocumentException If any error occurs while
	 *                                                 inserting the document
	 */
	void bulkInsertDocument(List<IViewDto> dtoList, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchInsertDocumentException;

	/**
	 * Inserts the given DTO into ElasticSearch
	 * 
	 * @param dto      The Table DTO to be indexed
	 * @param view     The view to be indexed, where the DTO participates
	 * @param pkFilter The PK filter of the given DTO
	 */
	void insertDocument(ITableDto dto, String view, FilterGroup pkFilter);

	/**
	 * Updates the given DTO into ElasticSearch
	 * 
	 * @param dto      The Table DTO to be indexed
	 * @param view     The view to be indexed, where the DTO participates
	 * @param pkFilter The PK filter of the given DTO
	 */
	void updateDocument(ITableDto dto, String view, FilterGroup pkFilter);

	/**
	 * Deletes the given DTO from ElasticSearch
	 * 
	 * @param dto      The Table DTO to be indexed
	 * @param view     The view to be indexed, where the DTO participates
	 * @param pkFilter The PK filter of the given DTO
	 */
	void deleteDocument(ITableDto dto, String view, FilterGroup pkFilter);

}
