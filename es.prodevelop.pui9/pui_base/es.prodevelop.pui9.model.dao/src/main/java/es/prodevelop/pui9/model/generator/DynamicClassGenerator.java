package es.prodevelop.pui9.model.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.codegen.pui9.db.CodegenModelsDatabaseUtils;
import es.prodevelop.codegen.pui9.model.ClientConfiguration;
import es.prodevelop.codegen.pui9.model.DatabaseConnection;
import es.prodevelop.codegen.pui9.model.DatabaseType;
import es.prodevelop.codegen.pui9.model.PuiConfiguration;
import es.prodevelop.codegen.pui9.model.ServerConfiguration;
import es.prodevelop.codegen.pui9.model.Table;
import es.prodevelop.codegen.pui9.model.View;
import es.prodevelop.pui9.classpath.PuiClasspathFinderRegistry;
import es.prodevelop.pui9.classpath.PuiDynamicClassLoader;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.db.DatabaseUtils;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.generator.layers.AbstractControllerLayerGenerator;
import es.prodevelop.pui9.model.generator.layers.AbstractDaoLayerGenerator;
import es.prodevelop.pui9.model.generator.layers.AbstractServiceLayerGenerator;
import es.prodevelop.pui9.model.generator.layers.ClientLayerGenerator;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * This class allows to generate the DAO and DTO classes of a View. It is very
 * useful when you need the DTO and DAO of a new View in Runtime. For instance,
 * to use it in a "Select" component, or in the Docgen PUI component. It
 * generates the classes and load them within the Classpath, and also registers
 * all of them within the DaoRegistry and DtoRegistry registers
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DynamicClassGenerator {

	public static final String generatedFolder = "pui_generated";
	public static final String packageName = "es.prodevelop.pui9.generated";
	public static final String clientFolderName = "client";
	public static final String filenameWildcard = "%filename%";
	public static final String resourceWildcard = "%resource%";

	public static final String READ_FUNCTIONALITY = "READ_PUI_GENERATED_ENTITY";
	public static final String WRITE_FUNCTIONALITY = "WRITE_PUI_GENERATED_ENTITY";
	public static final String GEN_FUNCTIONALITY = "GEN_PUI_GENERATED_ENTITY";

	public static final String PUI_VERSION_FOLDER = "1.x.y";
	public static final String PUI_VERSION = "1.3.0";

	private final String templateFolder = "/versions/" + PUI_VERSION_FOLDER + "/templates";

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private DatabaseUtils databaseUtils;

	@Autowired
	private AbstractDaoLayerGenerator daoLayerGenerator;

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired(required = false)
	private AbstractServiceLayerGenerator serviceLayerGenerator;

	@Autowired(required = false)
	private AbstractControllerLayerGenerator controllerLayerGenerator;

	@Autowired(required = false)
	private ClientLayerGenerator clientLayerGenerator;

	private Configuration freemarkerConfig;

	private Map<String, Boolean> generatedCache = new HashMap<>();

	private DynamicClassGenerator() {
	}

	/**
	 * Initialize freemarker configuration
	 */
	@PostConstruct
	private void postConstruct() {
		freemarkerConfig = new Configuration(Configuration.getVersion());
		freemarkerConfig.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(),
				templateFolder);
		freemarkerConfig.setObjectWrapper(new DefaultObjectWrapper(Configuration.getVersion()));
		freemarkerConfig.setWhitespaceStripping(false);
	}

	/**
	 * Execute the code generation with the given configuration. This method is
	 * executed in a synchronized way, in order to avoid colisions in the code
	 * generation
	 * 
	 * @param config The configuration
	 * @return true if all worked fine; false if not
	 * @throws Exception
	 */
	public synchronized void executeCodeGeneration(ClassGeneratorConfiguration config) throws Exception {
		if (generatedCache.containsKey(config.getTableName()) || generatedCache.containsKey(config.getViewName())) {
			return;
		}

		if (!config.isValidConfiguration()) {
			throw new Exception("Configuration is not valid");
		}
		if (config.isTableSet()) {
			generatedCache.put(config.getTableName(), true);
		}
		if (config.isViewSet()) {
			generatedCache.put(config.getViewName(), true);
		}

		long start = System.currentTimeMillis();

		boolean jarIncludedInClasspath = false;
		List<TemplateFileInfo> templateList = new ArrayList<>();
		try {
			logger.info(
					"*** Generating code for table (" + (config.getTableName() != null ? config.getTableName() : "-")
							+ ") / view (" + (config.getViewName() != null ? config.getViewName() : "-") + ")");

			PuiConfiguration model = createGeneratorModel(config);

			List<TemplateFileInfo> javaTemplateList = new ArrayList<>();
			List<TemplateFileInfo> daoTemplateList = new ArrayList<>();
			List<TemplateFileInfo> serviceTemplateList = new ArrayList<>();
			List<TemplateFileInfo> controllerTemplateList = new ArrayList<>();
			List<TemplateFileInfo> clientTemplateList = new ArrayList<>();

			if (model.getServer().isGenerate()) {
				if (model.getSelectedTable() != null
						&& !daoRegistry.existsDaoForEntity(model.getSelectedTable().getDbName())) {
					daoTemplateList.addAll(generateTableTemplates(model));
				}
				if (model.getSelectedView() != null
						&& !daoRegistry.existsDaoForEntity(model.getSelectedView().getDbName())) {
					daoTemplateList.addAll(generateViewTemplates(model));
				}
				javaTemplateList.addAll(daoTemplateList);

				if (model.getServer().isGenerateFull()) {
					serviceTemplateList.addAll(generateServiceTemplates(model));
					controllerTemplateList.addAll(generateControllerTemplates(model));
					javaTemplateList.addAll(serviceTemplateList);
					javaTemplateList.addAll(controllerTemplateList);
				}
			}
			templateList.addAll(javaTemplateList);

			if (model.getClient().isGenerate()) {
				clientTemplateList.addAll(generateClientTemplates(model));
				templateList.addAll(clientTemplateList);
			}

			if (CollectionUtils.isEmpty(templateList)) {
				throw new Exception("No templates were generated");
			}

			String modelName = model.getSelectedTable() != null ? model.getSelectedTable().getJavaName()
					: model.getSelectedView().getJavaName();
			modelName = modelName.toLowerCase();

			createTempFiles(templateList);

			boolean serverCreated = false;
			if (model.getServer().isGenerate()) {
				try {
					compileClasses(javaTemplateList);
					serverCreated = true;
				} catch (Exception e) {
					throw e;
				}
			}
			if (model.getClient().isGenerate() && clientLayerGenerator != null) {
				try {
					copyResources(modelName, clientTemplateList);
				} catch (Exception e) {
					throw e;
				}
			}

			File jarFile = buildJar(templateList.get(0).getTempBinFolder(), config, modelName);
			if (jarFile != null) {
				loadJar(jarFile);
			}

			if (serverCreated) {
				loadClasses(daoTemplateList, serviceTemplateList, controllerTemplateList);
			}

			jarIncludedInClasspath = true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			jarIncludedInClasspath = false;
			throw e;
		} finally {
			if (!jarIncludedInClasspath) {
				if (config.isTableSet()) {
					generatedCache.remove(config.getTableName());
				}
				if (config.isViewSet()) {
					generatedCache.remove(config.getViewName());
				}
			}
			if (templateList != null) {
				for (TemplateFileInfo tfi : templateList) {
					if (tfi.getTempSrcFile() != null) {
						Files.deleteIfExists(tfi.getTempSrcFile().toPath());
					}
					if (tfi.getTempBinFile() != null) {
						if (!tfi.getTempBinFile().delete()) {
							Files.deleteIfExists(tfi.getTempBinFile().toPath());
						}
					}
				}
			}

			long end = System.currentTimeMillis();
			long total = end - start;
			logger.info("*** Generation code for table ("
					+ (config.getTableName() != null ? config.getTableName() : "-") + ") / view ("
					+ (config.getViewName() != null ? config.getViewName() : "-") + ") took " + total + "ms");
		}
	}

	/**
	 * Generate the table templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the table
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateTableTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> tableTemplateList = Collections.emptyList();
		if (model.getSelectedTable() != null) {
			tableTemplateList = daoLayerGenerator.getTemplateList(false);

			generateTemplates(tableTemplateList, model, model.getSelectedTable().getJavaName());
		}
		return tableTemplateList;
	}

	/**
	 * Generate the view templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the view
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateViewTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> viewTemplateList = Collections.emptyList();
		if (model.getSelectedView() != null) {
			viewTemplateList = daoLayerGenerator.getTemplateList(true);

			generateTemplates(viewTemplateList, model, model.getSelectedView().getJavaName());
		}
		return viewTemplateList;
	}

	/**
	 * Generate the service templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the service
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateServiceTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> serviceTemplateList = Collections.emptyList();
		if (serviceLayerGenerator != null) {
			serviceTemplateList = serviceLayerGenerator.getTemplateList(false);

			generateTemplates(serviceTemplateList, model,
					model.getSelectedTable() != null ? model.getSelectedTable().getJavaName()
							: model.getSelectedView().getJavaName());
		}
		return serviceTemplateList;
	}

	/**
	 * Generate the controller templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the controller
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateControllerTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> controllerTemplateList = Collections.emptyList();
		if (controllerLayerGenerator != null) {
			controllerTemplateList = controllerLayerGenerator.getTemplateList(false);

			generateTemplates(controllerTemplateList, model,
					model.getSelectedTable() != null ? model.getSelectedTable().getJavaName()
							: model.getSelectedView().getJavaName());
		}
		return controllerTemplateList;
	}

	/**
	 * Generate the client templates
	 * 
	 * @param model The model generator
	 * @return The list of templates for the service
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private List<TemplateFileInfo> generateClientTemplates(PuiConfiguration model) throws Exception {
		List<TemplateFileInfo> clientTemplateList = Collections.emptyList();
		if (model.getSelectedTable() != null && model.getSelectedView() != null && clientLayerGenerator != null) {
			clientTemplateList = clientLayerGenerator.getTemplateList(false);

			generateTemplates(clientTemplateList, model,
					model.getSelectedTable() != null ? model.getSelectedTable().getJavaName()
							: model.getSelectedView().getJavaName());
		}
		return clientTemplateList;
	}

	/**
	 * Generate the contents of the given template file info
	 * 
	 * @param templateList The template list to be generated
	 * @param model        The data model for the freemarker template
	 * @param modelName    The name of the entity to be generated
	 * @throws Exception If any exception is thrown in the template generation
	 */
	private void generateTemplates(List<TemplateFileInfo> templateList, PuiConfiguration model, String modelName)
			throws Exception {
		for (TemplateFileInfo tfi : templateList) {
			StringWriter outputWriter = new StringWriter();
			freemarkerConfig.getTemplate(tfi.getTemplateName());
			freemarkerConfig.setSharedVariable("config", model);
			Template tpl = freemarkerConfig.getTemplate(tfi.getTemplateName());
			tpl.setOutputEncoding(StandardCharsets.UTF_8.name());
			tpl.process(model, outputWriter);
			outputWriter.flush();
			outputWriter.close();

			String contents = outputWriter.toString();
			if (tfi.getGeneratedFileName().contains(filenameWildcard)) {
				tfi.setFileName(tfi.getGeneratedFileName().replace(filenameWildcard, modelName));
			} else if (tfi.getGeneratedFileName().contains(resourceWildcard)) {
				tfi.setFileName(tfi.getGeneratedFileName().replace(resourceWildcard, modelName.toLowerCase()));
			}
			tfi.setContents(contents);
		}
	}

	/**
	 * Creates the Generator Model that will be used to generate the file contents
	 * by Freemarker
	 * 
	 * @param config The class generator configuration
	 * @return The code generator model
	 * @throws SQLException If any exception is throws while creating the generator
	 *                      model
	 */
	private PuiConfiguration createGeneratorModel(ClassGeneratorConfiguration config) throws SQLException {
		PuiConfiguration model = new PuiConfiguration();
		model.setPui9Version(PUI_VERSION);
		{
			CodegenModelsDatabaseUtils.singleton(databaseUtils.getMainDatasource().getConnection());
			model.setDatabase(new DatabaseConnection());
			model.getDatabase().setConfiguration(model);
			model.getDatabase().setType(DatabaseType.getByOfficialName(
					databaseUtils.getMainDatasource().getConnection().getMetaData().getDatabaseProductName()));
		}
		{
			model.setServer(new ServerConfiguration());
			model.getServer().setConfiguration(model);
			model.getServer().setDtoProject(packageName);
			model.getServer().setDaoProject(packageName);
			model.getServer().setBoProject(packageName);
			model.getServer().setWebProject(packageName);
			model.getServer().setSuperController("es.prodevelop.pui9.controller.AbstractCommonController");
			model.getServer().setGenerate(config.isGenerateServer());
			model.getServer().setGenerateFull(true);
		}
		{
			model.setClient(new ClientConfiguration());
			model.getClient().setConfiguration(model);
			model.getClient().setGenerate(config.isGenerateClient());
			// TODO: remove this in the future
			model.getClient().setGenerate(false);
			model.getClient().setClientProject(packageName);
		}

		if (!StringUtils.isEmpty(config.getTableName())) {
			Table table = new Table();
			table.setConfiguration(model);
			table.setDbName(config.getTableName());
			boolean exists = CodegenModelsDatabaseUtils.singleton().loadTable(table);
			if (exists) {
				model.setSelectedTable(table);
				model.getServer().setReadFunctionality(READ_FUNCTIONALITY);
				model.getServer().setWriteFunctionality(WRITE_FUNCTIONALITY);
			}
		}
		if (!StringUtils.isEmpty(config.getViewName())) {
			View view = new View();
			view.setConfiguration(model);
			view.setDbName(config.getViewName());
			boolean exists = CodegenModelsDatabaseUtils.singleton().loadView(view);
			if (exists) {
				model.setSelectedView(view);
			}
		}

		model.getServer().computeJavaAttributes();

		return model;
	}

	/**
	 * Create the files content. The target folder is the temp folder of the user
	 * who launched the application (tomcat or jboss)
	 * 
	 * @param templateList The template list from where the files will be created
	 * @throws Exception If any exception is thrown in while creating the files
	 *                   content
	 */
	private void createTempFiles(List<TemplateFileInfo> templateList) throws Exception {
		File tempFolder = new File(FileUtils.getTempDirectory(), generatedFolder + System.currentTimeMillis());

		File tempSrcFolder = new File(tempFolder, "src");
		tempSrcFolder.mkdirs();
		File tempBinFolder = new File(tempFolder, "bin");
		tempBinFolder.mkdirs();

		for (TemplateFileInfo tfi : templateList) {
			tfi.setTempBinFolder(tempBinFolder);
			tfi.setTempSrcFile(new File(tempSrcFolder, tfi.getFileName()));
			tfi.getTempSrcFile().createNewFile();

			FileWriter writer = new FileWriter(tfi.getTempSrcFile());
			writer.write(tfi.getContents());
			writer.close();
		}
	}

	/**
	 * Compile all the given Java classes. The target folder is the same where this
	 * class JAR is placed. The classpath of the application is used for compiling
	 * the classes. See {@link PuiClasspathFinderRegistry} class from PUI
	 * 
	 * @param templateList The list of templates
	 * @throws Exception If any exception was thrown while compiling the classes
	 */
	private void compileClasses(List<TemplateFileInfo> templateList) throws Exception {
		File binFolder = templateList.get(0).getTempBinFolder();

		List<File> files = new ArrayList<>();

		for (TemplateFileInfo tfi : templateList) {
			if (tfi.getIsJava()) {
				files.add(tfi.getTempSrcFile());
				String subfolders = tfi.getPackageName().replace(".", "/");
				String name = subfolders + File.separator + tfi.getFileName()
						.replace(JavaFileObject.Kind.SOURCE.extension, JavaFileObject.Kind.CLASS.extension);
				tfi.setTempBinFile(new File(binFolder, name));
			}
		}

		List<File> classpath = PuiClasspathFinderRegistry.getInstance().getClasspath();
		if (classpath.isEmpty()) {
			throw new Exception("No classpath available");
		}

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(binFolder));
		fileManager.setLocation(StandardLocation.CLASS_PATH, classpath);

		Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(files);
		boolean result = compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
		fileManager.close();
		if (!result) {
			throw new Exception("Generated code do not compile");
		}
	}

	/**
	 * Copy the generated files that are not Java type
	 * 
	 * @param entityName   The name of the entity
	 * @param templateList The template list
	 * @throws Exception If any exception was thrown while copying the resources
	 */
	private void copyResources(String entityName, List<TemplateFileInfo> templateList) throws Exception {
		File binFolder = templateList.get(0).getTempBinFolder();

		File clientFolder = new File(binFolder, clientFolderName);
		clientFolder = new File(clientFolder, entityName);
		clientFolder.mkdirs();

		for (TemplateFileInfo tfi : templateList) {
			if (!tfi.getIsJava()) {
				FileUtils.copyFileToDirectory(tfi.getTempSrcFile(), clientFolder);
				tfi.setTempBinFile(new File(clientFolder, tfi.getFileName()));
			}
		}
	}

	/**
	 * Builds the JAR file with the generated files
	 * 
	 * @param tempBinFolder The temp folder where the files were created
	 * @param config        The generator configuration
	 * @param entityName    The entity name
	 * @return The generated JAR file
	 * @throws Exception If any exception is thrown while building the JAR
	 */
	private File buildJar(File tempBinFolder, ClassGeneratorConfiguration config, String entityName) throws Exception {
		File jarFile = new File(tempBinFolder, packageName + "." + entityName + ".jar");

		ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(jarFile);

		if (config.isGenerateServer()) {
			File jarFolder = new File(tempBinFolder, packageName.split("\\.")[0]);
			addFileToZip(zaos, jarFolder.getPath(), "");
			FileUtils.deleteDirectory(jarFolder);
		}

		if (config.isGenerateClient()) {
			File jarFolder = new File(tempBinFolder, clientFolderName);
			addFileToZip(zaos, jarFolder.getPath(), "");
			FileUtils.deleteDirectory(jarFolder);
		}

		zaos.close();

		return jarFile;
	}

	/**
	 * Add the folder contents to the given zip archive output stream
	 * 
	 * @param zipOutputStream The zip file out stream
	 * @param path            The path file to be added
	 * @param base            The base path for the file to be added
	 * @throws Exception If any exception is thrown while adding a file to the zip
	 */
	private void addFileToZip(ZipArchiveOutputStream zipOutputStream, String path, String base) throws Exception {
		final File f = new File(path);
		final String entryName = base + f.getName();
		final ZipArchiveEntry zipEntry = new ZipArchiveEntry(f, entryName);

		zipOutputStream.putArchiveEntry(zipEntry);

		if (f.isFile()) {
			FileInputStream fInputStream = null;
			try {
				fInputStream = new FileInputStream(f);
				IOUtils.copy(fInputStream, zipOutputStream);
				zipOutputStream.closeArchiveEntry();
			} finally {
				IOUtils.closeQuietly(fInputStream);
			}
		} else {
			zipOutputStream.closeArchiveEntry();
			final File[] children = f.listFiles();

			if (children != null) {
				for (File child : children) {
					addFileToZip(zipOutputStream, child.getAbsolutePath(), entryName + "/");
				}
			}
		}
	}

	/**
	 * Load the generated Jar File into the classpath. See
	 * {@link PuiDynamicClassLoader} class
	 * 
	 * @param jarFile The JAR file to be added
	 * @throws Exception If any Exception is thrown while loading the JAR file
	 */
	private void loadJar(File jarFile) throws Exception {
		PuiDynamicClassLoader classLoader = PuiDynamicClassLoader.getInstance();
		URL jarUrl = jarFile.toURI().toURL();
		classLoader.addURL(jarUrl);
	}

	/**
	 * Register the Java Classes within the {@link DaoRegistry DaoRegistry} and the
	 * {@link DtoRegistry} registers but using Spring, in order to create the Beans
	 * properly
	 * 
	 * @param daoTemplateList        The list of DAO templates to load
	 * @param serviceTemplateList    The list of Service templates to load
	 * @param controllerTemplateList The list of Controller templates to load
	 * @throws Exception If any exception is thrown while loading the classes into
	 *                   Spring
	 */
	private void loadClasses(List<TemplateFileInfo> daoTemplateList, List<TemplateFileInfo> serviceTemplateList,
			List<TemplateFileInfo> controllerTemplateList) throws Exception {
		daoLayerGenerator.registerClasses(daoTemplateList);
		if (serviceLayerGenerator != null) {
			serviceLayerGenerator.registerClasses(serviceTemplateList);
		}
		if (controllerLayerGenerator != null) {
			controllerLayerGenerator.registerClasses(controllerTemplateList);
		}
	}

}
