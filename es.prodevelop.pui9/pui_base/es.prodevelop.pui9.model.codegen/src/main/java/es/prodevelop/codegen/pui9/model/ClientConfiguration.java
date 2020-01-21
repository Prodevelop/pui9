package es.prodevelop.codegen.pui9.model;

public class ClientConfiguration {

	private transient PuiConfiguration configuration;

	private boolean generate;
	private String clientProject;

	public PuiConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PuiConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isGenerate() {
		return generate;
	}

	public void setGenerate(boolean generate) {
		this.generate = generate;
	}

	public String getClientProject() {
		return clientProject;
	}

	public void setClientProject(String clientProject) {
		this.clientProject = clientProject;
	}

	@Override
	public String toString() {
		return "Generate: " + generate + "\nClient Project: " + clientProject;
	}

}
