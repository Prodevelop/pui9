package es.prodevelop.pui9.spring.configuration;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.method.pui9.PuiRequestMappingHandlerMapping;

import com.google.common.base.Predicates;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.json.adapters.SpringfoxJsonToGsonAdapter;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;
import es.prodevelop.pui9.utils.PuiPropertiesManager;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@PuiSpringConfiguration
public class PuiSwaggerSpringConfiguration {

	@Autowired
	private PuiRequestMappingHandlerMapping requestMapping;

	private Properties properties;

	@Bean
	public Docket api() throws IOException {
		loadPropertiesFile();
		GsonSingleton.getSingleton().registerTypeAdapter(Json.class, new SpringfoxJsonToGsonAdapter());

		Docket docket = new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/"))).build().apiInfo(getApiInfo())
				.securitySchemes(authorizationTypes()).securityContexts(securityContext())
				.globalOperationParameters(staticParameters());
		String baseUrl = properties.getProperty("swagger.baseUrl");
		if (!StringUtils.isEmpty(baseUrl)) {
			docket.pathProvider(
					new RelativePathProvider(PuiApplicationContext.getInstance().getBean(ServletContext.class)) {
						@Override
						public String getApplicationBasePath() {
							return baseUrl;
						}
					});
		}

		return docket;
	}

	/**
	 * Return a list of authorization types allowed in the application
	 * 
	 * @return The list of authorization types
	 */
	protected List<BasicAuth> authorizationTypes() {
		List<BasicAuth> auths = new ArrayList<>();
		auths.add(new BasicAuth("swagger"));

		return auths;
	}

	/**
	 * Return a list of security contexts to be applied to the api
	 * 
	 * @return The list of security contexts
	 */
	protected List<SecurityContext> securityContext() {
		List<SecurityContext> securityContexts = new ArrayList<>();

		List<String> predicateList = new ArrayList<>();
		requestMapping.getHandlerMethods().forEach((info, handlerMethod) -> {
			if (requestMapping.isSessionRequired(handlerMethod)) {
				predicateList.add(info.getPatternsCondition().getPatterns().iterator().next());
			}
		});

		List<AuthorizationScope> scopes = Collections
				.singletonList(new AuthorizationScope("global", "accessEverything"));
		List<SecurityReference> defaultAuth = Collections
				.singletonList(new SecurityReference("swagger", scopes.toArray(new AuthorizationScope[0])));
		securityContexts.add(SecurityContext.builder().securityReferences(defaultAuth)
				.forPaths(Predicates.in(predicateList)).build());

		return securityContexts;
	}

	/**
	 * Return a list of static parameters that will be included to all the requests
	 * 
	 * @return The list of parameters
	 */
	protected List<Parameter> staticParameters() {
		List<String> timezoneList = Collections.singletonList(ZoneId.systemDefault().getId());
		Parameter timezoneParameter = new ParameterBuilder().parameterType("header").name("Timezone").order(0)
				.hidden(true).required(true).modelRef(new ModelRef("string"))
				.allowableValues(new AllowableListValues(timezoneList, "string")).build();

		List<Parameter> parameters = new ArrayList<>();
		parameters.add(timezoneParameter);

		return parameters;
	}

	@Bean
	public UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder().defaultModelsExpandDepth(0).defaultModelExpandDepth(0)
				.displayRequestDuration(true).docExpansion(DocExpansion.NONE).build();
	}

	private void loadPropertiesFile() throws IOException {
		properties = PuiPropertiesManager.loadPropertiesFile("swagger.properties");
	}

	private ApiInfo getApiInfo() {
		String title = properties.getProperty("swagger.title");
		String description = properties.getProperty("swagger.description");
		String version = properties.getProperty("swagger.version");
		String termsOfServiceUrl = properties.getProperty("swagger.termsOfServiceUrl");
		String license = properties.getProperty("swagger.license");
		String licenseUrl = properties.getProperty("swagger.licenseUrl");

		String contactName = properties.getProperty("swagger.contact.name");
		String contactUrl = properties.getProperty("swagger.contact.url");
		String contactEmail = properties.getProperty("swagger.contact.email");

		Contact contact = new Contact(contactName, contactUrl, contactEmail);

		return new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, licenseUrl,
				Collections.emptyList());
	}

}