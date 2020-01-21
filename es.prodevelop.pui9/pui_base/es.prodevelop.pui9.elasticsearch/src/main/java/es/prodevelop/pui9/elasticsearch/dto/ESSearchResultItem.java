package es.prodevelop.pui9.elasticsearch.dto;

import java.io.Serializable;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

/**
 * This class represents an item in the ElasticSearch search operation. It
 * contains the ElasticSearch Identifier and the associated {@link IViewDto}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ESSearchResultItem implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private IViewDto dto;

	/**
	 * Default constructor for search Items
	 * 
	 * @param id
	 *            The ElasticSearch identifier
	 * @param dto
	 *            The element as {@link IViewDto} representation
	 */
	public ESSearchResultItem(String id, IViewDto dto) {
		this.id = id;
		this.dto = dto;
	}

	/**
	 * Get the ElasticSearch identifier for this item
	 * 
	 * @return The ElasticSearch identifier for this item
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the Item as {@link IViewDto} representation
	 * 
	 * @return The Item as {@link IViewDto} representation
	 */
	public IViewDto getDto() {
		return dto;
	}

}