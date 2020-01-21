package es.prodevelop.pui9.mvc.configuration;

import java.util.HashMap;
import java.util.Map;

public class PuiControllersInfo {

	private String name;
	private Map<String, String> url = new HashMap<>();
	private Map<String, String> functionalities = new HashMap<>();

	public PuiControllersInfo(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getUrl() {
		return url;
	}

	public void addUrl(String name, String url) {
		this.url.put(name, url);
	}

	public Map<String, String> getFunctionalities() {
		return functionalities;
	}

	public boolean existFunctionality(String id) {
		return functionalities.containsKey(id);
	}

	public void addFunctionality(String id, String functionality) {
		if (!functionalities.containsKey(id)) {
			functionalities.put(id, functionality);
		}
	}

}
