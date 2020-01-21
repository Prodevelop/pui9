package org.springframework.web.servlet.mvc.method.pui9;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.annotations.PuiRemoveMapping;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.controller.IPuiCommonController;
import es.prodevelop.pui9.mvc.configuration.IPuiRequestMappingHandlerMapping;
import es.prodevelop.pui9.mvc.configuration.PuiControllersInfo;
import es.prodevelop.pui9.spring.configuration.AbstractAppSpringConfiguration;
import es.prodevelop.pui9.spring.configuration.PuiRootSpringConfiguration;

/**
 * The bean of this class is automatically created by the
 * {@link PuiRootSpringConfiguration} class configuration. This extension class
 * checks all the mappings created by the the {@link IPuiCommonController} in
 * order to see if they are available or not, taking into account the allowXXX
 * methods
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiRequestMappingHandlerMapping extends RequestMappingHandlerMapping
		implements IPuiRequestMappingHandlerMapping {

	@SuppressWarnings("rawtypes")
	private Map<Class<?>, IPuiCommonController> cache;
	private Map<String, PuiControllersInfo> mapControllerInfo;
	private Map<Object, Boolean> cacheHandlerSessionRequired = new HashMap<>();
	private Map<Object, String> cacheHandlerFunctionality = new HashMap<>();

	@Autowired
	private AbstractAppSpringConfiguration appConfig;

	@Override
	protected void initHandlerMethods() {
		cache = new HashMap<>();
		super.initHandlerMethods();
		cache.clear();
	}

	/**
	 * This method allows to register a new Mapping in runtime
	 */
	public void registerNewMapping(Object handler) {
		detectHandlerMethods(handler);
	}

	@Override
	@SuppressWarnings("rawtypes")
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
		if (info == null) {
			// method is not a request mapping
			return info;
		}

		if (appConfig.getOverridedPuiControllers().contains(handlerType)) {
			return null;
		}

		if (method.isAnnotationPresent(PuiRemoveMapping.class)) {
			return null;
		}

		if (!IPuiCommonController.class.isAssignableFrom(handlerType)) {
			// handler class is not a PuiController controller
			return info;
		}

		IPuiCommonController beanController = cache.get(handlerType);
		if (beanController == null) {
			try {
				beanController = (IPuiCommonController) handlerType.newInstance();
				cache.put(handlerType, beanController);
			} catch (InstantiationException | IllegalAccessException e) {
				return null;
			}
		}

		PuiCommonActionEnum action = null;
		try {
			action = PuiCommonActionEnum.valueOf(method.getName());
		} catch (IllegalArgumentException e) {
			return info;
		}

		boolean allowAction = false;

		switch (action) {
		case get:
			allowAction = beanController.allowGet();
			break;
		case template:
			allowAction = beanController.allowTemplate();
			break;
		case insert:
			allowAction = beanController.allowInsert();
			break;
		case update:
			allowAction = beanController.allowUpdate();
			break;
		case patch:
			allowAction = beanController.allowPatch();
			break;
		case delete:
			allowAction = beanController.allowDelete();
			break;
		case list:
			allowAction = beanController.allowList();
			break;
		case export:
			allowAction = beanController.allowExport();
			break;
		}

		return allowAction ? info : null;
	}

	@Override
	public Map<String, PuiControllersInfo> getUrlsAndFunctionalitiesByController() {
		if (mapControllerInfo == null) {
			mapControllerInfo = new HashMap<>();

			for (Entry<RequestMappingInfo, HandlerMethod> entry : getHandlerMethods().entrySet()) {
				RequestMappingInfo mappingInfo = entry.getKey();
				HandlerMethod handlerMethod = entry.getValue();

				String pattern = !mappingInfo.getPatternsCondition().getPatterns().isEmpty()
						? mappingInfo.getPatternsCondition().getPatterns().iterator().next()
						: "/";

				if (pattern.contains("swagger")) {
					continue;
				}

				String[] parts = pattern.split("/");
				if (parts.length == 0) {
					continue;
				}

				String controller = !StringUtils.isEmpty(parts[0]) ? parts[0] : parts[1];
				if (!mapControllerInfo.containsKey(controller)) {
					mapControllerInfo.put(controller, new PuiControllersInfo(controller));
				}

				{
					// urls
					String name = handlerMethod.getMethod().getName();
					mapControllerInfo.get(controller).addUrl(name, pattern);
				}

				{
					// functionalities
					PuiFunctionality funcAnn = handlerMethod.getMethodAnnotation(PuiFunctionality.class);
					if (funcAnn == null) {
						continue;
					}

					if (mapControllerInfo.get(controller).existFunctionality(funcAnn.id())) {
						continue;
					}

					String val = funcAnn.value();
					String functionality = null;

					// check if the value is a method looking for it in the controller class
					// hierarchy
					Class<?> controllerClass = handlerMethod.getBeanType();
					Method funcMethod = null;
					do {
						try {
							funcMethod = controllerClass.getDeclaredMethod(val);
						} catch (NoSuchMethodException | NullPointerException | SecurityException e) {
							// do nothing
						}
					} while (funcMethod == null && (controllerClass = controllerClass.getSuperclass()) != null);

					// if a method is found, execute it to obtain the value
					if (funcMethod != null) {
						try {
							funcMethod.setAccessible(true);
							Object controllerObj = handlerMethod.getBean();
							if (handlerMethod.getBean() instanceof String) {
								controllerObj = PuiApplicationContext.getInstance().getAppContext()
										.getBean((String) handlerMethod.getBean());
							}
							functionality = (String) funcMethod.invoke(controllerObj);
							if (functionality == null) {
								functionality = "";
							}
							funcMethod.setAccessible(false);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							continue;
						}
					}

					if (functionality == null) {
						functionality = val;
					}

					mapControllerInfo.get(controller).addFunctionality(funcAnn.id(), functionality);
				}
			}
		}

		return mapControllerInfo;
	}

	/**
	 * Check if a session is required to exist for cunsuming the given Web Service.
	 * Basically it checks if the {@link PuiNoSessionRequired} annotation exists for
	 * the consumed method or its declaring class (or any class in the hierarchy)
	 * 
	 * @param handler The handler that represents the Web Service to be consumed
	 * @return true if the Web Service requires a session; false if not
	 */
	@Override
	public boolean isSessionRequired(Object handler) {
		if (handler instanceof HandlerMethod && ((HandlerMethod) handler).getMethod().getDeclaringClass().getName()
				.contains("springfox.documentation")) {
			// is a swagger request, building Swagger page
			return false;
		}

		if (!cacheHandlerSessionRequired.containsKey(handler)) {
			if (!(handler instanceof HandlerMethod)) {
				cacheHandlerSessionRequired.put(handler, false);
			} else {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				Method method = handlerMethod.getMethod();

				// check if the method is annotated with @PuiNoSessionRequired
				Annotation annotation = method.getAnnotation(PuiNoSessionRequired.class);
				if (annotation != null) {
					cacheHandlerSessionRequired.put(handler, false);
				} else {
					// check if the class or any super class is annotated with @PuiNoSessionRequired
					Class<?> clazz = handlerMethod.getBeanType();
					while (clazz != null) {
						annotation = clazz.getAnnotation(PuiNoSessionRequired.class);
						if (annotation != null) {
							cacheHandlerSessionRequired.put(handler, false);
							break;
						}
						clazz = clazz.getSuperclass();
					}

					if (!cacheHandlerSessionRequired.containsKey(handler)) {
						cacheHandlerSessionRequired.put(handler, true);
					}
				}
			}
		}

		return cacheHandlerSessionRequired.get(handler);
	}

	@Override
	public String getHandlerFunctionality(Object handler) {
		if (!cacheHandlerFunctionality.containsKey(handler)) {
			// if handler is not a Handler Method, accept the request
			if (!(handler instanceof HandlerMethod)) {
				cacheHandlerFunctionality.put(handler, null);
			} else {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				Method method = handlerMethod.getMethod();

				PuiFunctionality puiFunctionality = method.getAnnotation(PuiFunctionality.class);
				// if PuiFunctionality annotation is not specified, accept the request
				if (puiFunctionality == null) {
					cacheHandlerFunctionality.put(handler, null);
				} else {
					// if PuiFunctionality annotation is set as not to be checked, accept the
					// request
					if (!puiFunctionality.check()) {
						cacheHandlerFunctionality.put(handler, null);
					} else {
						String functionality = puiFunctionality.value();

						// check if the value is a method looking for it in the controller class
						// hierarchy
						Class<?> clazz = method.getDeclaringClass();
						Method funcMethod = null;
						do {
							try {
								funcMethod = clazz.getDeclaredMethod(functionality);
							} catch (NoSuchMethodException | NullPointerException | SecurityException e) {
								// do nothing
							}
						} while (funcMethod == null && (clazz = clazz.getSuperclass()) != null);

						if (funcMethod != null) {
							// if a method is found, execute it to obtain the value
							try {
								funcMethod.setAccessible(true);
								functionality = (String) funcMethod.invoke(handlerMethod.getBean());
								funcMethod.setAccessible(false);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
								functionality = null;
							}
						}

						cacheHandlerFunctionality.put(handler, functionality);
					}
				}
			}
		}

		return cacheHandlerFunctionality.get(handler);
	}

	private enum PuiCommonActionEnum {
		get, template, insert, update, patch, delete, list, export;
	}
}
