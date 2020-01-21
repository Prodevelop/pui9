package es.prodevelop.pui9.controller;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.codegen.pui9.utils.CodegenUtils;
import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.classpath.PuiDynamicClassLoader;
import es.prodevelop.pui9.model.generator.ClassGeneratorConfiguration;
import es.prodevelop.pui9.model.generator.DynamicClassGenerator;
import es.prodevelop.pui9.service.registry.ServiceRegistry;
import es.prodevelop.pui9.utils.IPuiObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This controller is used to generate new models from a table/view, and also to
 * obtain the generated model definition, using the
 * {@link DynamicClassGenerator} component. This controller is very related with
 * the PUI brick 'generatedmodels'
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@RequestMapping("/modelgenerator")
@Api(tags = "PUI Dynamic Model Generator")
public class DynamicModelGeneratorController extends AbstractPuiController {

	@Autowired
	private DynamicClassGenerator classGenerator;

	@Autowired
	private ServiceRegistry serviceRegistry;

	@ApiOperation(value = "All dynamic generated models", notes = "Get all the dynamic generated models")
	@PuiFunctionality(id = AbstractCommonController.ID_FUNCTIONALITY_GET, value = DynamicClassGenerator.READ_FUNCTIONALITY)
	@GetMapping(value = "/getModelsDefinition", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GeneratedModelDefinition> getModelsDefinition() {
		return getAllModelDefinitions(false);
	}

	@ApiOperation(value = "All dynamic generated models with controllers", notes = "Get all the dynamic generated models that has controllers")
	@PuiFunctionality(id = AbstractCommonController.ID_FUNCTIONALITY_GET, value = DynamicClassGenerator.READ_FUNCTIONALITY)
	@GetMapping(value = "/getModelsDefinitionWithControllers", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<GeneratedModelDefinition> getModelsDefinitionWithControllers() {
		return getAllModelDefinitions(true);
	}

	@ApiOperation(value = "A dynamic generated model", notes = "Get the dynamic generated model associated with the given table or view name")
	@PuiFunctionality(id = AbstractCommonController.ID_FUNCTIONALITY_GET, value = DynamicClassGenerator.READ_FUNCTIONALITY)
	@GetMapping(value = "/getModelDefinition", produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratedModelDefinition getModelDefinition(HttpServletRequest request,
			@ApiParam(value = "The table name") @RequestParam(required = false) String tableName,
			@ApiParam(value = "The view name") @RequestParam(required = false) String viewName)
			throws IllegalArgumentException {
		if (StringUtils.isEmpty(tableName) && StringUtils.isEmpty(viewName)) {
			throw new IllegalArgumentException("table or view should not be null");
		}

		String modelName = getModelName(tableName, viewName);
		return getModelDefinition(modelName);
	}

	@ApiOperation(value = "Generate code for given configuration", notes = "Generate the code for given configuration (table, view or both, including the controllers and client code)")
	@PuiFunctionality(id = "generate", value = DynamicClassGenerator.GEN_FUNCTIONALITY)
	@PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratedModelDefinition generate(
			@ApiParam(value = "The configuration", required = true) @RequestBody ClassGeneratorConfiguration config)
			throws Exception {
		classGenerator.executeCodeGeneration(config);

		String modelName = getModelName(config.getTableName(), config.getViewName());
		return getModelDefinition(modelName);
	}

	/**
	 * Get the list of Generated Models
	 */
	private List<GeneratedModelDefinition> getAllModelDefinitions(boolean withControllers) {
		List<GeneratedModelDefinition> definitions = new ArrayList<>();

		List<String> modelNames = getAllGeneratedModelsNames();
		for (String modelName : modelNames) {
			if (serviceRegistry.getServiceFromModel(modelName) == null) {
				continue;
			}
			GeneratedModelDefinition gmd = getModelDefinition(modelName);
			definitions.add(gmd);
		}

		return definitions;
	}

	/**
	 * Obtain the names of Generated Models
	 */
	private List<String> getAllGeneratedModelsNames() {
		List<String> models = new ArrayList<>();

		for (URL url : PuiDynamicClassLoader.getInstance().getURLs()) {
			try {
				File file = new File(url.toURI());
				String fileName = FilenameUtils.getBaseName(file.getName());
				fileName = fileName.replace(DynamicClassGenerator.packageName + ".", "");
				models.add(fileName);
			} catch (URISyntaxException e) {
				// do nothing
			}
		}

		Collections.sort(models);

		return models;
	}

	private GeneratedModelDefinition getModelDefinition(String modelName) {
		GeneratedModelDefinition gmd = new GeneratedModelDefinition(modelName);
		gmd.js = getJs(modelName);
		gmd.jsModel = getJsModel(modelName);
		gmd.html = getHtml(modelName);
		gmd.jsLanguage = getJsLanguage(modelName);

		return gmd;
	}

	/**
	 * Get the JS file content for the given generated model name
	 */
	private String getJs(String modelName) {
		return getResourceContent(modelName, null, ".js");
	}

	/**
	 * Get the JS Model file content for the given generated model name
	 */
	private String getJsModel(String modelName) {
		return getResourceContent(modelName, "Model", ".js");
	}

	/**
	 * Get the HTML file content for the given generated model name
	 */
	private String getHtml(String modelName) {
		return getResourceContent(modelName, "Form", ".html");
	}

	/**
	 * Get the JS Language file content for the given generated model name
	 */
	private String getJsLanguage(String modelName) {
		return getResourceContent(modelName, ".lang", ".js");
	}

	private String getModelName(String tableName, String viewName) {
		return CodegenUtils.convertEntityNameToJavaName(!StringUtils.isEmpty(tableName) ? tableName : viewName)
				.toLowerCase();
	}

	/**
	 * Get a resource content
	 */
	private String getResourceContent(String modelName, String prefix, String fileExtension) {
		if (prefix == null) {
			prefix = "";
		}
		try {
			String resourceName = DynamicClassGenerator.clientFolderName + "/" + modelName + "/" + modelName + prefix
					+ fileExtension;
			InputStream is = PuiDynamicClassLoader.getInstance().getResourceAsStream(resourceName);
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (Exception e) {
			return "";
		}
	}

	@SuppressWarnings("unused")
	private class GeneratedModelDefinition implements IPuiObject {

		private static final long serialVersionUID = 1L;

		String modelName;
		String js;
		String jsModel;
		String jsLanguage;
		String html;

		public GeneratedModelDefinition(String modelName) {
			this.modelName = modelName;
		}

	}

}
