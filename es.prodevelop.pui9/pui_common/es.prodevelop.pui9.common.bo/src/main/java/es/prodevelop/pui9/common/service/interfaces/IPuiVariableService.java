package es.prodevelop.pui9.common.service.interfaces;

import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiVariableDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariablePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiVariableDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiVariableService
		extends IService<IPuiVariablePk, IPuiVariable, IVPuiVariable, IPuiVariableDao, IVPuiVariableDao> {

	/**
	 * Get the value of a variable, casted to the given class type. If the cast
	 * fails, it returns null
	 * 
	 * @param <TYPE>   The type to convert the value of the variable
	 * @param clazz    The class of the type
	 * @param variable The variable name
	 * @return The casted value of the variable, or null
	 */
	<TYPE> TYPE getVariable(Class<TYPE> clazz, String variable);

	/**
	 * Get the value of a variable (in String type)
	 * 
	 * @param variable The variable name
	 * @return The value of the variable
	 */
	String getVariable(String variable);

	/**
	 * Modify the value of a variable
	 * 
	 * @param variable The variable name
	 * @param value    The variable value
	 */
	@Transactional(rollbackFor = PuiException.class)
	void modifyVariable(String variable, String value);

	/**
	 * Reload all the variables from the database to the cache
	 */
	void reloadVariables();

	/**
	 * Check the variable DEVELOPMENT_ENVIRONMENT and return its value depending on
	 * if the environment is for development or not
	 * 
	 * @return true or false
	 */
	Boolean isDevelopmentEnvironment();

	/**
	 * Get the variable APPLICATION_LEGAL_TEXT and return its value
	 * 
	 * @return true or false
	 */
	String getApplicationLegalText();

}