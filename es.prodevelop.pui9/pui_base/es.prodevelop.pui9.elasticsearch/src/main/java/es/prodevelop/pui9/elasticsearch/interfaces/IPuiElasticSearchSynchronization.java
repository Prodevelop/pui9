package es.prodevelop.pui9.elasticsearch.interfaces;

/**
 * ElasticSearch synchronization process. Used to execute a synchronization on
 * demand
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiElasticSearchSynchronization {

	/**
	 * Do the Elastic Search synchronization from the Database. You can indicate to
	 * force the reindex or not
	 * 
	 * @param view  The name of the view to force reindexing. By default, null
	 * @param force If true, the views will always be reindexed; if false, only will
	 *              be reindexed if the number of registers differs from the DDBB
	 *              View and the Elastic Search Index
	 */
	void synchronize(String view, Boolean force);

}
