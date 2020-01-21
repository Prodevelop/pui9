package es.prodevelop.pui9.spring.configuration;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.pui9.PuiExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.pui9.PuiIDtoArgumentResolver;
import org.springframework.web.servlet.mvc.method.pui9.PuiRequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.pui9.PuiRequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import es.prodevelop.pui9.data.converters.DtoConverter;
import es.prodevelop.pui9.data.converters.InstantConverter;
import es.prodevelop.pui9.data.converters.LocalDateConverter;
import es.prodevelop.pui9.data.converters.MapConverter;
import es.prodevelop.pui9.data.converters.MultipartFileConverter;
import es.prodevelop.pui9.data.converters.PuiGsonHttpMessageConverter;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

/**
 * This is the Root Configuration for all the PUI applications. Contains all the
 * common shared configuration for all of them. This configuration is loaded
 * after the concrete configuration of the application, in the
 * {@link PuiWebApplicationSpringConfiguration}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
@ComponentScan(basePackages = { "es.prodevelop.pui9" })
public class PuiRootSpringConfiguration extends WebMvcConfigurationSupport {

	@Autowired
	private AbstractAppSpringConfiguration appConfig;

	/**
	 * Don't remove this method override, because if you do it, the PUI applications
	 * initialization will fail
	 */
	@Bean
	@Override
	public PuiRequestMappingHandlerMapping requestMappingHandlerMapping(
			@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
			@Qualifier("mvcConversionService") FormattingConversionService conversionService,
			@Qualifier("mvcResourceUrlProvider") ResourceUrlProvider resourceUrlProvider) {
		return (PuiRequestMappingHandlerMapping) super.requestMappingHandlerMapping(contentNegotiationManager,
				conversionService, resourceUrlProvider);
	}

	/**
	 * Don't remove this method override, because if you do it, the PUI applications
	 * initialization will fail
	 */
	@Bean
	@Override
	public PuiRequestMappingHandlerAdapter requestMappingHandlerAdapter(
			@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager,
			@Qualifier("mvcConversionService") FormattingConversionService conversionService,
			@Qualifier("mvcValidator") Validator validator) {
		return (PuiRequestMappingHandlerAdapter) super.requestMappingHandlerAdapter(contentNegotiationManager,
				conversionService, validator);
	}

	/**
	 * Creates a PUI extension of the {@link RequestMappingHandlerMapping} class
	 */
	@Override
	protected PuiRequestMappingHandlerMapping createRequestMappingHandlerMapping() {
		return new PuiRequestMappingHandlerMapping();
	}

	/**
	 * Creates a PUI extension of the {@link RequestMappingHandlerAdapter} class
	 */
	@Override
	protected PuiRequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
		return new PuiRequestMappingHandlerAdapter(getMessageConverters());
	}

	/**
	 * Creates a PUI extension of the {@link ExceptionHandlerExceptionResolver}
	 * class
	 */
	@Override
	protected PuiExceptionHandlerExceptionResolver createExceptionHandlerExceptionResolver() {
		return new PuiExceptionHandlerExceptionResolver(getMessageConverters());
	}

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new PuiIDtoArgumentResolver());
	}

	@Override
	protected void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(appConfig.actionInterceptor()).addPathPatterns("/**");
	}

	/**
	 * Substitute the {@link PuiGsonHttpMessageConverter} and the
	 * {@link FormHttpMessageConverter} from GSON default by the PUI ones
	 */
	@Override
	protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		// remove
		for (Iterator<HttpMessageConverter<?>> it = converters.iterator(); it.hasNext();) {
			HttpMessageConverter<?> next = it.next();
			if (next instanceof org.springframework.http.converter.json.GsonHttpMessageConverter
					|| next.getClass().getSimpleName().contains("Jackson")
					|| next instanceof AllEncompassingFormHttpMessageConverter) {
				it.remove();
			}
		}

		PuiGsonHttpMessageConverter gsonHttpMessageConverter = getApplicationContext()
				.getBean(PuiGsonHttpMessageConverter.class);
		converters.add(0, gsonHttpMessageConverter);
	}

	/**
	 * Add the PUI Generic Converters
	 */
	@Override
	protected void addFormatters(FormatterRegistry registry) {
		for (GenericConverter converter : getPuiGenericConverters()) {
			registry.addConverter(converter);
		}
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		Reflections ref = new Reflections("es.prodevelop");
		Set<Class<? extends IPuiResourceHandler>> set = ref.getSubTypesOf(IPuiResourceHandler.class);
		set.forEach(handlerClass -> {
			try {
				IPuiResourceHandler handler = handlerClass.newInstance();
				handler.addResourceHandler(registry);
			} catch (InstantiationException | IllegalAccessException e) {
				// do nothing
			}
		});
	}

	/**
	 * This method creates a bean of type CommonsMultipartResoler (with name
	 * 'multipartResolver'), that is used for uploading files to the server.
	 * Encoding is set to UTF-8
	 */
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setDefaultEncoding(StandardCharsets.UTF_8.name());
		return resolver;
	}

	/**
	 * This method creates a bean of type ConvertionServiceFactoryBean (with name
	 * 'conversionService'), necessary to look for all the "beanable" classes. Don't
	 * delete it
	 */
	@Bean
	public ConversionServiceFactoryBean conversionService() {
		Set<GenericConverter> converters = new HashSet<>();
		converters.addAll(getPuiGenericConverters());

		ConversionServiceFactoryBean conversionService = new ConversionServiceFactoryBean();
		conversionService.setConverters(converters);

		return conversionService;
	}

	/**
	 * Get all the PUI Generic Converters
	 * 
	 * @return list of all the own Generic Converters
	 */
	private List<GenericConverter> getPuiGenericConverters() {
		List<GenericConverter> converters = new ArrayList<>();

		DtoConverter dtoConverter = getApplicationContext().getBean(DtoConverter.class);
		InstantConverter instantConverter = getApplicationContext().getBean(InstantConverter.class);
		LocalDateConverter localDateConverter = getApplicationContext().getBean(LocalDateConverter.class);
		MapConverter mapConverter = getApplicationContext().getBean(MapConverter.class);
		MultipartFileConverter mfConverter = getApplicationContext().getBean(MultipartFileConverter.class);

		converters.add(dtoConverter);
		converters.add(instantConverter);
		converters.add(localDateConverter);
		converters.add(mapConverter);
		converters.add(mfConverter);

		return converters;
	}

}
