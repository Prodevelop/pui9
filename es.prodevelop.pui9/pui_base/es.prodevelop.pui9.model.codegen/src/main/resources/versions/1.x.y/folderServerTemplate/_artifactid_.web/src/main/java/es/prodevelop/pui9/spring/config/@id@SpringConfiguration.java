package es.prodevelop.pui9.spring.config;

import org.springframework.context.annotation.ComponentScan;

import es.prodevelop.pui9.interceptors.CommonInterceptor;
import es.prodevelop.pui9.interceptors.PuiInterceptor;
import es.prodevelop.pui9.spring.configuration.AbstractAppSpringConfiguration;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

@PuiSpringConfiguration
@ComponentScan(basePackages = { "_artifactid_" })
public class _id_SpringConfiguration extends AbstractAppSpringConfiguration {

	@Override
	protected String getJndiName() {
		return "java:comp/env/jdbc/_artifactid_";
	}

	@Override
	protected PuiInterceptor getHandlerInterceptor() {
		return new CommonInterceptor();
	}

	@Override
	protected String getAppnameForElasticsearch() {
		return "_id_";
	}

}
