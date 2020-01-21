package es.prodevelop.pui9.common.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiVariableDao;
import es.prodevelop.pui9.common.model.dto.PuiVariablePk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariablePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiVariableDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiVariable;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.eventlistener.event.VariableUpdatedEvent;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

/**
 * @generated
 */
@Service
public class PuiVariableService
		extends AbstractService<IPuiVariablePk, IPuiVariable, IVPuiVariable, IPuiVariableDao, IVPuiVariableDao>
		implements IPuiVariableService {

	private Map<String, String> cache;

	@PostConstruct
	private void postConstruct() {
		cache = new HashMap<>();

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadVariables", true, 0, 1, TimeUnit.HOURS,
				this::reloadVariables);
	}

	@Override
	protected void afterInsert(IPuiVariable dto) throws PuiServiceException {
		loadVariable(dto.getVariable());
	}

	@Override
	protected void afterUpdate(IPuiVariable oldDto, IPuiVariable dto) throws PuiServiceException {
		loadVariable(dto.getVariable());
		getEventLauncher().fireAsync(new VariableUpdatedEvent(dto, oldDto.getValue()));
	}

	@Override
	protected void afterDelete(IPuiVariable dto) throws PuiServiceException {
		loadVariable(dto.getVariable());
	}

	@SuppressWarnings("unchecked")
	public <TYPE> TYPE getVariable(Class<TYPE> clazz, String variable) {
		Assert.notNull(clazz, "The given class should not be null");

		String value = getVariable(variable);
		Object castedValue = null;

		try {
			if (clazz.equals(Integer.class)) {
				castedValue = Integer.valueOf(value);
			} else if (clazz.equals(Long.class)) {
				castedValue = Long.valueOf(value);
			} else if (clazz.equals(Double.class)) {
				castedValue = Double.valueOf(value);
			} else if (clazz.equals(Boolean.class)) {
				castedValue = Boolean.valueOf(value);
			} else if (clazz.equals(BigDecimal.class)) {
				castedValue = new BigDecimal(value);
			} else if (clazz.equals(String.class)) {
				castedValue = value;
			}
		} catch (Exception e) {
			// do nothing
		}

		return (TYPE) castedValue;
	}

	@Override
	public String getVariable(String variable) {
		Assert.notNull(variable, "The variable name should not be null");

		if (!cache.containsKey(variable)) {
			loadVariable(variable);
		}

		return cache.get(variable);
	}

	@Override
	public void modifyVariable(String variable, String value) {
		try {
			IPuiVariablePk pk = new PuiVariablePk(variable);
			IPuiVariable dto = getByPk(pk);
			dto.setValue(value);
			update(dto);
		} catch (PuiServiceException e) {
			// do nothing
		}
	}

	@Override
	public void reloadVariables() {
		try {
			List<IPuiVariable> list = getAll();
			if (list.isEmpty()) {
				return;
			}

			Map<String, String> oldCache = new HashMap<>(cache);
			cache.clear();

			for (IPuiVariable var : list) {
				cache.put(var.getVariable(), var.getValue());
				String oldValue = oldCache.get(var.getVariable());
				if (oldValue != null) {
					getEventLauncher().fireAsync(new VariableUpdatedEvent(var, oldValue));
				}
			}
		} catch (PuiServiceGetException e) {
			// do nothing
		}
	}

	@Override
	public Boolean isDevelopmentEnvironment() {
		return getVariable(Boolean.class, PuiVariableValues.DEVELOPMENT_ENVIRONMENT.name());
	}

	@Override
	public String getApplicationLegalText() {
		return getVariable(String.class, PuiVariableValues.APPLICATION_LEGAL_TEXT.name());
	}

	/**
	 * Load the given variable into the cache. This method will never be called in a
	 * desired environment, due to the Timer refreshes all the variables when
	 * executed. But who knows, it's a mystery why this variable did not be loaded
	 * :D
	 */
	private String loadVariable(String codevar) {
		try {
			IPuiVariablePk pk = new PuiVariablePk(codevar);
			IPuiVariable variable = getTableDao().findOne(pk);
			if (variable != null) {
				cache.put(codevar, variable.getValue());
				return variable.getValue();
			} else {
				cache.remove(codevar);
			}
		} catch (PuiDaoFindException e) {
			// do nothing
		}

		return null;
	}

}