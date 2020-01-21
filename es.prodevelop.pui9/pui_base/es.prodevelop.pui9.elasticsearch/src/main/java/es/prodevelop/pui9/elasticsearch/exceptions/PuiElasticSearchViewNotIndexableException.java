package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when a View should never be indexed by this
 * application (set in the PUI_ELASTICSEARCH_VIEWS table in the database)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchViewNotIndexableException extends PuiElasticSearchSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 309;

	public PuiElasticSearchViewNotIndexableException(String view) {
		super(CODE, view);
	}

}
