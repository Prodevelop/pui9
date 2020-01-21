package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when it is not connected to any Node
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchNoNodesException extends AbstractPuiElasticSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 306;

	public PuiElasticSearchNoNodesException() {
		super(CODE);
	}

}
