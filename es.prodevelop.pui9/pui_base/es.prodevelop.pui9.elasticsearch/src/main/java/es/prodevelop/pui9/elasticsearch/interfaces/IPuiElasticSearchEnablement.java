package es.prodevelop.pui9.elasticsearch.interfaces;

import es.prodevelop.pui9.model.dto.interfaces.IViewDto;

/**
 * This interface provides some basic check configuration operations against
 * ElasticSearch, like availability, activation, synchronization, blocking and
 * indexable views...
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiElasticSearchEnablement {

	/**
	 * Check if Elastic Search is available or not (connected to any Node)
	 */
	boolean isElasticSearchAvailable();

	/**
	 * Set Elastic Search service activation status
	 */
	void setElasticSearchActive(boolean active);

	/**
	 * Check if Elastic Search service is active
	 */
	boolean isElasticSearchActive();

	/**
	 * Check if Elastic Search is synchronizing any View
	 */
	Boolean isSynchronizingAnyView();

	/**
	 * Check if Elastic Search is synchonizing the given view
	 */
	boolean isSynchronizingView(Class<? extends IViewDto> dtoClass);

	/**
	 * Add the given view as being synchronized
	 */
	void addSynchronizingView(Class<? extends IViewDto> dtoClass);

	/**
	 * Remove the given view from being synchronized
	 */
	void removeSynchronizingView(Class<? extends IViewDto> dtoClass);

	/**
	 * Add the given View to the blocked list
	 */
	void addBlockedView(Class<? extends IViewDto> dtoClass);

	/**
	 * Remove the given View from the blocked list
	 */
	void removeBlockedView(Class<? extends IViewDto> dtoClass);

	/**
	 * Check if the View is blocked or not
	 */
	boolean isViewBlocked(Class<? extends IViewDto> dtoClass);

	/**
	 * Include the given View for being indexed by ElasticSearch
	 */
	void addIndexableView(Class<? extends IViewDto> dtoClass);

	/**
	 * Remove the given View from the indexable Views list
	 */
	void removeIndexableView(Class<? extends IViewDto> dtoClass);

	/**
	 * Check if the View is indexable by ElasticSearch or not
	 */
	boolean isViewIndexable(Class<? extends IViewDto> dtoClass);

}
