package es.prodevelop.pui9.spring.configuration;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.aop.framework.ProxyProcessorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.pui9.PuiTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.gson.internal.bind.TypeAdapters;

import es.prodevelop.pui9.classpath.PuiDynamicClassLoader;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.json.adapters.IDtoClassTypeAdapter;
import es.prodevelop.pui9.json.adapters.IDtoTypeAdapter;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

/**
 * Spring configuration for the DAO layer. JDBC approach should implement needed
 * methods
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
@EnableTransactionManagement
public class PuiDaoSpringConfiguration {

	@Autowired
	protected DataSource dataSource;

	@Autowired
	@Qualifier(AopConfigUtils.AUTO_PROXY_CREATOR_BEAN_NAME)
	private ProxyProcessorSupport proxySupport;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PuiDaoSpringConfiguration() {
		// do it here instead of in the postConstruct method due to ensure that these
		// precious adapters are always included
		GsonSingleton.getSingleton()
				.registerTypeAdapterFactory(TypeAdapters.newFactory((Class) Class.class, new IDtoClassTypeAdapter()));
		GsonSingleton.getSingleton().registerTypeAdapterFactory(IDtoTypeAdapter.FACTORY);
	}

	/**
	 * Registers a couple of GSON adapters for dealing with {@link IDto} classes
	 */
	@PostConstruct
	private void postConstruct() {
		proxySupport.setBeanClassLoader(PuiDynamicClassLoader.getInstance());
	}

	/**
	 * Create a bean for the "transactionManager" object
	 * 
	 * @return The transaction manager object
	 */
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new PuiTransactionManager(dataSource);
	}

}
