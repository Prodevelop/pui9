package es.prodevelop.pui9.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.eventlistener.EventLauncher;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * This abstract controller is the top of the controllers of PUI. All the
 * controllers should inherit from this one in order to have access to some
 * useful methods, but it's not mandatory to inherit it
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiController {

	protected static final String ID_FUNCTIONALITY_INSERT = "insert";
	protected static final String ID_FUNCTIONALITY_UPDATE = "update";
	protected static final String ID_FUNCTIONALITY_DELETE = "delete";
	protected static final String ID_FUNCTIONALITY_GET = "get";
	protected static final String ID_FUNCTIONALITY_LIST = "list";
	protected static final String METHOD_FUNCTIONALITY_INSERT = "getInsertFunctionality";
	protected static final String METHOD_FUNCTIONALITY_UPDATE = "getUpdateFunctionality";
	protected static final String METHOD_FUNCTIONALITY_DELETE = "getDeleteFunctionality";
	protected static final String METHOD_FUNCTIONALITY_GET = "getGetFunctionality";
	protected static final String METHOD_FUNCTIONALITY_LIST = "getListFunctionality";

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private PuiApplicationContext context;

	@Autowired
	private EventLauncher eventLauncher;

	/**
	 * Get the PUI Event Launcher, that allows to fire PUI Events over the
	 * application. See {@link PuiEvent} class
	 * 
	 * @return
	 */
	protected EventLauncher getEventLauncher() {
		return eventLauncher;
	}

	protected PuiApplicationContext getPuiContext() {
		return context;
	}

	protected Gson getGson() {
		return GsonSingleton.getSingleton().getGson();
	}

	protected PuiUserSession getSession() {
		return PuiUserSession.getCurrentSession();
	}

	/**
	 * Returns the language to be used in the queries. Possible to get null
	 */
	protected PuiLanguage getLanguage() {
		try {
			return getSession().getLanguage();
		} catch (Exception e) {
			return PuiLanguageUtils.getDefaultLanguage();
		}
	}

	/**
	 * Get the permission name to Insert a registry
	 */
	protected String getInsertFunctionality() {
		return getWriteFunctionality();
	}

	/**
	 * Get the permission name to Update a registry
	 */
	protected String getUpdateFunctionality() {
		return getWriteFunctionality();
	}

	/**
	 * Get the permission name to Delete a registry
	 */
	protected String getDeleteFunctionality() {
		return getWriteFunctionality();
	}

	/**
	 * Get the permission name to Get a registry
	 */
	protected String getGetFunctionality() {
		return getReadFunctionality();
	}

	/**
	 * Get the permission name to List a model
	 */
	protected String getListFunctionality() {
		return getReadFunctionality();
	}

	/**
	 * Get the permission name to Read any registry
	 */
	protected String getReadFunctionality() {
		return null;
	}

	/**
	 * Get the permission name to Write any registry
	 */
	protected String getWriteFunctionality() {
		return null;
	}

}
