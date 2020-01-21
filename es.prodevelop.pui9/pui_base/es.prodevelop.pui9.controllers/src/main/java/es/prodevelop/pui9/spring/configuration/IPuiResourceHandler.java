package es.prodevelop.pui9.spring.configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * Subclass this interface if you want to provide some resources in the
 * application. Just by creating a subclass and implementing the
 * {@link #addResourceHandler(ResourceHandlerRegistry)} method, the resources
 * will be added
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPuiResourceHandler {

	void addResourceHandler(ResourceHandlerRegistry registry);

}
