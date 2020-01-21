package es.prodevelop.pui9.mvc.configuration;

import java.util.Map;

public interface IPuiRequestMappingHandlerMapping {

	Map<String, PuiControllersInfo> getUrlsAndFunctionalitiesByController();

	boolean isSessionRequired(Object handler);

	String getHandlerFunctionality(Object handler);

}
