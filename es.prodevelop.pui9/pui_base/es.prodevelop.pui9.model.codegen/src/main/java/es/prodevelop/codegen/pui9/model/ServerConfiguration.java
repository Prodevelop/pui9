package es.prodevelop.codegen.pui9.model;

import org.apache.commons.lang3.StringUtils;

public class ServerConfiguration {

	private transient PuiConfiguration configuration;
	private transient boolean generateFull;
	private transient String readFunctionality = "";
	private transient String writeFunctionality = "";
	private transient String dtoBasePath;
	private transient String dtoJavaPackage;
	private transient String daoBasePath;
	private transient String daoJavaPackage;
	private transient String boBasePath;
	private transient String boJavaPackage;
	private transient String webBasePath;
	private transient String webJavaPackage;

	private boolean generate;
	private String dtoProject;
	private String daoProject;
	private String boProject;
	private String webProject;
	private String superController = "AbstractController";

	public PuiConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PuiConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isGenerateFull() {
		return generateFull;
	}

	public void setGenerateFull(boolean generateFull) {
		this.generateFull = generateFull;
	}

	public String getReadFunctionality() {
		return readFunctionality;
	}

	public void setReadFunctionality(String readFunctionality) {
		this.readFunctionality = readFunctionality;
	}

	public String getWriteFunctionality() {
		return writeFunctionality;
	}

	public void setWriteFunctionality(String writeFunctionality) {
		this.writeFunctionality = writeFunctionality;
	}

	public String getDtoBasePath() {
		return dtoBasePath;
	}

	public String getDtoJavaPackage() {
		return dtoJavaPackage;
	}

	public String getDaoBasePath() {
		return daoBasePath;
	}

	public String getDaoJavaPackage() {
		return daoJavaPackage;
	}

	public String getBoBasePath() {
		return boBasePath;
	}

	public String getBoJavaPackage() {
		return boJavaPackage;
	}

	public String getWebBasePath() {
		return webBasePath;
	}

	public String getWebJavaPackage() {
		return webJavaPackage;
	}

	public boolean isGenerate() {
		return generate;
	}

	public void setGenerate(boolean generate) {
		this.generate = generate;
	}

	public String getDtoProject() {
		return dtoProject;
	}

	public void setDtoProject(String dtoProject) {
		this.dtoProject = dtoProject;
	}

	public String getDaoProject() {
		return daoProject;
	}

	public void setDaoProject(String daoProject) {
		this.daoProject = daoProject;
	}

	public String getBoProject() {
		return boProject;
	}

	public void setBoProject(String boProject) {
		this.boProject = boProject;
	}

	public String getWebProject() {
		return webProject;
	}

	public void setWebProject(String webProject) {
		this.webProject = webProject;
	}

	public String getSuperController() {
		return superController;
	}

	public void setSuperController(String superController) {
		this.superController = superController;
	}

	/**
	 * Before calling this method, take into accound these conditions:
	 * <p>
	 * <ul>
	 * <li>All the Java projects are set</li>
	 * </ul>
	 */
	public void computeJavaAttributes() {
		String srcPackage = "/src/main/java/";

		{
			dtoJavaPackage = dtoProject;
			if (dtoJavaPackage.endsWith(".dto")) {
				dtoJavaPackage = dtoJavaPackage.substring(0, dtoJavaPackage.lastIndexOf(".dto"));
			}
			dtoBasePath = srcPackage + convertProjectToPackage(dtoJavaPackage) + "/";
		}

		{
			daoJavaPackage = daoProject;
			if (daoJavaPackage.endsWith(".dao")) {
				daoJavaPackage = daoJavaPackage.substring(0, daoJavaPackage.lastIndexOf(".dao"));
			}
			daoBasePath = srcPackage + convertProjectToPackage(daoJavaPackage) + "/";
		}

		{
			boJavaPackage = boProject;
			if (boJavaPackage.endsWith(".bo")) {
				boJavaPackage = boJavaPackage.substring(0, boJavaPackage.lastIndexOf(".bo"));
			}
			boBasePath = srcPackage + convertProjectToPackage(boJavaPackage) + "/";
		}

		{
			webJavaPackage = webProject;
			if (webJavaPackage.endsWith(".web")) {
				webJavaPackage = webJavaPackage.substring(0, webJavaPackage.lastIndexOf(".web"));
			}
			webBasePath = srcPackage + convertProjectToPackage(webJavaPackage) + "/";
		}
	}

	private String convertProjectToPackage(String projectName) {
		return StringUtils.replace(projectName, ".", "/");
	}

	@Override
	public String toString() {
		return "Generate: " + generate + "\nGenerate Full: " + generateFull + "\nDto Project: " + dtoProject
				+ "\nDao Project: " + daoProject + "\nBo Project: " + boProject + "\nWebProject: " + webProject;
	}

}
