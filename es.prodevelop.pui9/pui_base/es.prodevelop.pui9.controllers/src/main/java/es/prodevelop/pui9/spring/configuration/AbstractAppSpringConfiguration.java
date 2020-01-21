package es.prodevelop.pui9.spring.configuration;

import java.util.Collections;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jndi.JndiTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import es.prodevelop.pui9.controller.AbstractPuiController;
import es.prodevelop.pui9.interceptors.PuiInterceptor;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

/**
 * This is an abstract Application Configuration for Spring that all the PUI
 * applications should implement. Basically need to provide implementation for
 * the kind of interceptor to be used. You can define in the concrete class more
 * dataSources if needed.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
public abstract class AbstractAppSpringConfiguration {

	private HandlerInterceptor interceptor;
	private DataSource dataSource;
	private String appname;
	private List<Class<? extends AbstractPuiController>> overridedPuiControllers;

	@Bean
	public HandlerInterceptor actionInterceptor() {
		if (interceptor == null) {
			interceptor = getHandlerInterceptor();
		}
		return interceptor;
	}

	@Bean
	public DataSource dataSource() throws NamingException {
		if (dataSource == null) {
			JndiTemplate jndi = new JndiTemplate();
			dataSource = (DataSource) jndi.lookup(getJndiName());
		}
		return dataSource;
	}

	@Bean
	public String appname() {
		if (appname == null) {
			appname = getAppnameForElasticsearch();
		}
		return appname;
	}

	/**
	 * Get a list with the overrided PUI controllers
	 * 
	 * @return The list of the overrided PUI controllers
	 */
	public List<Class<? extends AbstractPuiController>> getOverridedPuiControllers() {
		if (overridedPuiControllers == null) {
			overridedPuiControllers = fillOverridedPuiControllers();
		}
		return overridedPuiControllers;
	}

	// @Bean
	// public DataSource dataSource2() {
	// return new
	// EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
	// }

	/**
	 * Provide the implementation class for the {@link HandlerInterceptor} object to
	 * use
	 * 
	 * @return The interceptor to be used
	 */
	protected abstract PuiInterceptor getHandlerInterceptor();

	/**
	 * The name of the JNDI for the datasource
	 * 
	 * @return the JNDI name
	 */
	protected abstract String getJndiName();

	protected String getAppnameForElasticsearch() {
		return null;
	}

	/**
	 * A list with the overrided PUI controllers. An overrided controller, should be
	 * declared with {@link Primary} annotation
	 * 
	 * @return
	 */
	protected List<Class<? extends AbstractPuiController>> fillOverridedPuiControllers() {
		return Collections.emptyList();
	}

}
