package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when an error in the Search operation occurs
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchSearchException extends AbstractPuiElasticSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 307;

	public PuiElasticSearchSearchException(String cause) {
		this(CODE, cause);
	}

	public PuiElasticSearchSearchException(Integer code, String cause) {
		super(code, cause);
	}

}
