package org.springframework.web.servlet.mvc.method.pui9;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

public class PuiExceptionHandlerExceptionResolver extends ExceptionHandlerExceptionResolver {

	private List<HttpMessageConverter<?>> converters;

	public PuiExceptionHandlerExceptionResolver(List<HttpMessageConverter<?>> converters) {
		this.converters = converters;
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();

		// add the Pui Objects Method Processor the first in the list
		List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>();
		handlers.add(new PuiObjectsMethodProcessor(converters));
		handlers.addAll(getReturnValueHandlers().getHandlers());

		setReturnValueHandlers(handlers);
	}

	

}
