package es.prodevelop.pui9.elasticsearch.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import io.swagger.annotations.ApiModel;

/**
 * This class is a representation of the search result against ElasticSearch. It
 * stores the time that the search took, the total number of registries and the
 * result items
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@ApiModel(value = "ESTypeResultDto description")
public class ESSearchResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long total;
	private Double time;
	private List<ESSearchResultItem> items;

	/**
	 * Default constructor for Search
	 * 
	 * @param total
	 *            The amount of registries that the search retrieved
	 * @param time
	 *            The time that the search took
	 */
	public ESSearchResult(Long total, Double time) {
		this.time = time;
		this.total = total;
		this.items = new ArrayList<>();
	}

	/**
	 * Get the total amount of records of the search
	 * 
	 * @return The total amount of records of the search
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * Get the time that the search took in milliseconds
	 * 
	 * @return The time that the search took in milliseconds
	 */
	public Double getTime() {
		return time;
	}

	/**
	 * Add an item to the search result
	 * 
	 * @param item
	 *            a Search item
	 */
	public void addItem(ESSearchResultItem item) {
		items.add(item);
	}

	/**
	 * Get all the items of the search
	 * 
	 * @return The items of the search
	 */
	public List<ESSearchResultItem> getItems() {
		return items;
	}

	/**
	 * Get the search result as list of {@link IViewDto}
	 * 
	 * @return The search result as list of {@link IViewDto}
	 */
	@SuppressWarnings("unchecked")
	public <V extends IViewDto> List<V> getDtoList() {
		List<V> list = new ArrayList<>();

		for (ESSearchResultItem item : items) {
			list.add((V) item.getDto());
		}

		return list;
	}

}
