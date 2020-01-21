package es.prodevelop.pui9.spring.configuration;

import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import javax.servlet.Filter;

import org.reflections.Reflections;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

/**
 * This is the main configuration for all the PUI Spring Applications. This
 * class is the responsible to make all working. It is the responsible to read
 * all the configurations from PUI and the Application, and execute them
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
public class PuiWebApplicationSpringConfiguration extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		Reflections ref = new Reflections("es.prodevelop");
		Set<Class<? extends AbstractAppSpringConfiguration>> set = ref
				.getSubTypesOf(AbstractAppSpringConfiguration.class);
		if (set.isEmpty()) {
			throw new NoClassDefFoundError("No application Config available");
		}

		Class<? extends AbstractAppSpringConfiguration> appConfigClass = set.iterator().next();
		if (Modifier.isAbstract(appConfigClass.getModifiers()) || Modifier.isInterface(appConfigClass.getModifiers())) {
			throw new NoClassDefFoundError("No assignable application Config available");
		}

		return new Class<?>[] { appConfigClass, PuiRootSpringConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/*" };
	}

	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] { new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true, true) };
	}

}
