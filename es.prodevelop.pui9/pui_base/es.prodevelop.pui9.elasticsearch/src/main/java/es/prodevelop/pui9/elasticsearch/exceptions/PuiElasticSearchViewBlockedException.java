package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when a view is blocked temporally from using in
 * ElasticSearch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchViewBlockedException extends PuiElasticSearchSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 308;

	public PuiElasticSearchViewBlockedException(String view) {
		super(CODE, view);
	}

}
