package es.prodevelop.pui9.elasticsearch.exceptions;

/**
 * ElasticSearch exception when deleting a document
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiElasticSearchDeleteIndexException extends AbstractPuiElasticSearchException {

	private static final long serialVersionUID = 1L;

	public static final Integer CODE = 303;

	public PuiElasticSearchDeleteIndexException(String index) {
		super(CODE, index);
	}

}
