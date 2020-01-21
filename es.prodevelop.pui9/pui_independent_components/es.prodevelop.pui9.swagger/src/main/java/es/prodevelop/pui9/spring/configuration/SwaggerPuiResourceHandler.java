package es.prodevelop.pui9.spring.configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

public class SwaggerPuiResourceHandler implements IPuiResourceHandler {

	@Override
	public void addResourceHandler(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

}
