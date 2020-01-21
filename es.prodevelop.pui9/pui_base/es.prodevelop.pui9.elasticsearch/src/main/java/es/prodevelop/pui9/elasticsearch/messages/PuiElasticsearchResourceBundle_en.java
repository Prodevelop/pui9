package es.prodevelop.pui9.elasticsearch.messages;

/**
 * English Translation for PUI ElasticSearch component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticsearchResourceBundle_en extends PuiElasticsearchResourceBundle {

	@Override
	protected String getCountMessage_301() {
		return "Error while counting the number of registries in the index \"{0}\": it''s not well indexed due to has distinct number of indexed documents for each language";
	}

	@Override
	protected String getCreateIndexMessage_302() {
		return "Error while creating the index \"{0}\"";
	}

	@Override
	protected String getDeleteIndexMessage_303() {
		return "Error while deleting the index \"{0}\"";
	}

	@Override
	protected String getExistsIndexMessage_304() {
		return "Error while checking the index \"{0}\" existence: translated views should has an index for each language";
	}

	@Override
	protected String getInsertDocumentMessage_305() {
		return "Error while inserting a document into the index \"{0}\"";
	}

	@Override
	protected String getNoNodesMessage_306() {
		return "ElasticSearch is not connected to any node. Please, check the connection between the application and the ElasticSearch server";
	}

	@Override
	protected String getSearchMessage_307() {
		return "Error while searching: {0}";
	}

	@Override
	protected String getViewBlockedMessage_308() {
		return "The view \"{0}\" is currently blocked for ElasticSearch";
	}

	@Override
	protected String getViewNotIndexableMessage_309() {
		return "The view \"{0}\" is configured as not indexable for ElasticSearch";
	}

}
