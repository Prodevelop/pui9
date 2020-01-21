package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when executing the count operation against an index
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchCountException extends AbstractPuiElasticSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 301;

	public PuiElasticSearchCountException(String index) {
		super(CODE, index);
	}

}
