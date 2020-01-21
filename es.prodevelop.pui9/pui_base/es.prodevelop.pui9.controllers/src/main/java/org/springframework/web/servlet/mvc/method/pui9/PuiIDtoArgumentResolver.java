package org.springframework.web.servlet.mvc.method.pui9;

import java.lang.reflect.Constructor;
import java.util.Optional;

import javax.servlet.ServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;

import es.prodevelop.pui9.model.dto.DtoFactory;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Add support for defining a {@link IDto} object as parameter in a Web Service
 * handler
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiIDtoArgumentResolver extends ModelAttributeMethodProcessor {

	public PuiIDtoArgumentResolver() {
		super(false);
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return IDto.class.isAssignableFrom(parameter.getParameterType()) && parameter.getParameterType().isInterface();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Object createAttribute(String attributeName, MethodParameter parameter,
			WebDataBinderFactory binderFactory, NativeWebRequest webRequest) throws Exception {
		MethodParameter nestedParameter = parameter.nestedIfOptional();
		Class<?> clazz = nestedParameter.getNestedParameterType();
		clazz = DtoFactory.getClassFromInterface((Class<? extends IDto>) clazz);

		Constructor<?> ctor = BeanUtils.findPrimaryConstructor(clazz);
		if (ctor == null) {
			Constructor<?>[] ctors = clazz.getConstructors();
			if (ctors.length == 1) {
				ctor = ctors[0];
			} else {
				try {
					ctor = clazz.getDeclaredConstructor();
				} catch (NoSuchMethodException ex) {
					throw new IllegalStateException("No primary or default constructor found for " + clazz, ex);
				}
			}
		}

		Object attribute = constructAttribute(ctor, attributeName, nestedParameter, binderFactory, webRequest);
		if (parameter != nestedParameter) {
			attribute = Optional.of(attribute);
		}
		return attribute;
	}

	@Override
	protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest request) {
		ServletRequest servletRequest = request.getNativeRequest(ServletRequest.class);
		Assert.state(servletRequest != null, "No ServletRequest");
		ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
		servletBinder.bind(servletRequest);
	}

}
