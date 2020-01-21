package es.prodevelop.pui9.interceptors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * This is a base {@link HandlerInterceptor} implementation for PUI. Basically
 * it allows to manage the responses to convert them into a JSON response
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiInterceptor implements HandlerInterceptor {

	protected final Log logger = LogFactory.getLog(this.getClass());

}
