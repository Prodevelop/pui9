package es.prodevelop.pui9.elasticsearch.exceptions;

import es.prodevelop.pui9.elasticsearch.messages.PuiElasticSearchMessages;
import es.prodevelop.pui9.exceptions.PuiException;

/**
 * Abstract exception for ElasticSearch. Use more concrete classes of this one
 * for specific errors
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiElasticSearchException extends PuiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Create an Exception providing only the unique code
	 * 
	 * @param code
	 *            The unique exception code
	 */
	public AbstractPuiElasticSearchException(Integer code) {
		this(code, new Object[0]);
	}

	/**
	 * Create an Exception providing the unique code and the parameters of the
	 * message to be replaced
	 * 
	 * @param code
	 *            The unique exception code
	 * @param parameters
	 *            The parameters of the message to be replaced
	 */
	public AbstractPuiElasticSearchException(Integer code, Object... parameters) {
		super(null, code, PuiElasticSearchMessages.getSingleton().getString(code), parameters);
	}

}
