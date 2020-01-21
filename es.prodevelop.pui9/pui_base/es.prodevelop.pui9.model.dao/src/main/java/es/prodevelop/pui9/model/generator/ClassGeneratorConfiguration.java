package es.prodevelop.pui9.model.generator;

import org.springframework.util.StringUtils;

/**
 * Configuration class for the Class Generator. You can set the table and/or the
 * view of the model to be generated. By default, the server is always
 * generated. The client needs the table and the view to be generated. You can
 * set the flexibility.<br>
 * <br>
 * If you set <b>"generateServer"</b> to <b>true</b>, the generator will do the
 * best to generate it. <br>
 * The same applies to the client, with <b>"generateClient"</b> attribute.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ClassGeneratorConfiguration {

	private String tableName;
	private String viewName;
	private boolean generateServer;
	private boolean generateClient;

	/**
	 * Configure the generator setting the table and the view. Also you can specify
	 * if you want to generate the Server and the Client. The generator will do the
	 * best to generate all of them.
	 */
	public ClassGeneratorConfiguration(String tableName, String viewName, boolean generateServer,
			boolean generateClient) {
		this.tableName = tableName;
		this.viewName = viewName;
		this.generateServer = generateServer;
		this.generateClient = generateClient;
	}

	/**
	 * The name of the table to be generated
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Set the name of the table
	 */
	void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Check if the table has been set
	 */
	public boolean isTableSet() {
		return !StringUtils.isEmpty(tableName);
	}

	/**
	 * The name of the view to be generated
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Set the name of the view
	 */
	void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * Check if the view has been set
	 */
	public boolean isViewSet() {
		return !StringUtils.isEmpty(viewName);
	}

	/**
	 * Get if the server should be generated or not
	 */
	public boolean isGenerateServer() {
		return generateServer && (!StringUtils.isEmpty(tableName) || !StringUtils.isEmpty(viewName));
	}

	/**
	 * Get if the client should be generated or not
	 */
	public boolean isGenerateClient() {
		return generateClient && !StringUtils.isEmpty(tableName) && !StringUtils.isEmpty(viewName);
	}

	/**
	 * Check if the configuration is valid or not
	 */
	public boolean isValidConfiguration() {
		if (StringUtils.isEmpty(tableName) && StringUtils.isEmpty(viewName)) {
			return false;
		}
		if (!isGenerateServer() && !isGenerateClient()) {
			return false;
		}

		return true;
	}

}
