package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when checking if an index exists or not in the server
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchExistsIndexException extends AbstractPuiElasticSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 304;

	public PuiElasticSearchExistsIndexException(String indexes) {
		super(CODE, indexes);
	}

}
