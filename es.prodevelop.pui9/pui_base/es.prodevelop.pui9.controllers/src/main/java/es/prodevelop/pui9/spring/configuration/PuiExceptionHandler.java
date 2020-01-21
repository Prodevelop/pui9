package es.prodevelop.pui9.spring.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiExceptionDto;

/**
 * This class acts as Exception Handler for all the PUI applications. Each
 * exception occurred in the code, and called from a Controller (a Web Service),
 * will be cached by this handler and return the desired object to the client.
 * <p>
 * This is special for all the {@link PuiException} thrown
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@ControllerAdvice
public class PuiExceptionHandler {

	private Log logger = LogFactory.getLog(this.getClass());

	@ExceptionHandler(Exception.class)
	private PuiExceptionDto handlePuiExceptionDto(Exception ex, WebRequest request) {
		PuiExceptionDto errorDto;
		boolean shouldLog = true;
		Exception realException = ex;
		if (!(realException instanceof PuiException) && (realException.getCause() instanceof PuiException)) {
			realException = (Exception) ex.getCause();
		}

		if (realException instanceof PuiException) {
			errorDto = ((PuiException) realException).asDto();
			shouldLog = ((PuiException) realException).shouldLog();
		} else {
			errorDto = new PuiException(realException).asDto();
		}

		HttpStatus status = HttpStatus.resolve(errorDto.getStatusCode());
		errorDto.setStatusCode(status.value());

		if (shouldLog) {
			logger.error(realException.toString(), realException);
		}

		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, realException, RequestAttributes.SCOPE_REQUEST);
		}

		return errorDto;
	}

}
