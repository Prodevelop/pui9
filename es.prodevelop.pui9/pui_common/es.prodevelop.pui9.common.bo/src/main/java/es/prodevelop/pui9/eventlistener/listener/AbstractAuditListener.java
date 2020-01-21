package es.prodevelop.pui9.eventlistener.listener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.common.model.dto.PuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.service.interfaces.IPuiAuditService;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.IPuiObject;

/**
 * Abstract implementation of Audit Activity Listener. Registers into PuiAudit
 * table all the actions that affects to the activated DAOs
 */
public abstract class AbstractAuditListener<E extends PuiEvent<?>> extends PuiListener<E> {

	private static final String PK_SEPARATOR = "#";

	@Autowired
	protected IPuiAuditService auditService;

	@Autowired
	protected DaoRegistry daoRegistry;

	@Override
	protected boolean passFilter(E eventObject) {
		return eventObject.getSource() != null && getUserSession() != null;
	}

	@Override
	protected void process(E event) throws PuiException {
		PuiUserSession userSession = getUserSession();

		IPuiAudit puiAudit = new PuiAudit();
		puiAudit.setUsr(userSession.getUsr());
		puiAudit.setDatetime(Instant.now());
		puiAudit.setIp(userSession.getIp());
		puiAudit.setType(event.getEventId());
		if (event.getSource() instanceof ITableDto) {
			puiAudit.setModel(daoRegistry.getModelIdFromDto(((ITableDto) event.getSource()).getClass()));
		}

		fillEventObject(event, puiAudit);
		try {
			auditService.insert(puiAudit);
		} catch (Exception e) {
			// some error when inserting the audit activity
		}
	}

	/**
	 * Generate a PuiAudit object to insert it into the PUI Audit table in the
	 * database. By default usr, datetime and type values was filled previously.
	 */
	protected abstract void fillEventObject(E event, IPuiAudit puiAudit);

	/**
	 * Get the PK of the given DTO in an specific format: pk1#pk2#...#pkN
	 */
	protected String getDtoPK(IDto dto) {
		if (dto == null) {
			return "";
		}

		List<String> fieldNames = DtoRegistry.getPkFields(dto.getClass());
		StringBuilder pkValue = new StringBuilder();
		for (Iterator<String> it = fieldNames.iterator(); it.hasNext();) {
			Field field = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), it.next());
			try {
				Object value = FieldUtils.readField(field, dto, true);
				String strValue = "";
				if (value != null) {
					strValue = value.toString();
				} else {
					strValue = "null";
				}
				pkValue.append(strValue);
				if (it.hasNext()) {
					pkValue.append(PK_SEPARATOR);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}
		}

		return pkValue.toString();
	}

	protected List<OneObject> processOneObject(IPuiObject puiObject) {
		BeanMap map = new BeanMap(puiObject);
		PropertyUtilsBean propUtils = new PropertyUtilsBean();
		List<OneObject> list = new ArrayList<>();

		for (Object propNameObject : map.keySet()) {
			try {
				String propertyName = (String) propNameObject;
				if (StringUtils.endsWithIgnoreCase(propertyName, "noasig") || propertyName.equalsIgnoreCase("class")) {
					continue;
				}
				Object property = propUtils.getProperty(puiObject, propertyName);
				list.add(new OneObject(propertyName, property));
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				// do nothing
			}
		}

		return list;
	}

	protected List<TwoObject> processTwoObject(IPuiObject puiObject1, IPuiObject puiObject2) {
		BeanMap map = new BeanMap(puiObject1);
		PropertyUtilsBean propUtils = new PropertyUtilsBean();
		List<TwoObject> list = new ArrayList<>();

		for (Object propNameObject : map.keySet()) {
			try {
				String propertyName = (String) propNameObject;
				if (StringUtils.endsWithIgnoreCase(propertyName, "noasig") || propertyName.equalsIgnoreCase("class")) {
					continue;
				}
				Object propertyOld = propUtils.getProperty(puiObject1, propertyName);
				Object propertyNew = propUtils.getProperty(puiObject2, propertyName);
				if (!Objects.equals(propertyOld, propertyNew)) {
					list.add(new TwoObject(propertyName, propertyOld, propertyNew));
				}
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				// do nothing
			}
		}

		return list;
	}

	protected class OneObject {
		private String attribute;
		private Object value;

		public OneObject(String attribute, Object value) {
			this.attribute = attribute;
			this.value = processValue(value);
		}

		public String getAttribute() {
			return attribute;
		}

		public Object getValue() {
			return value;
		}

		/**
		 * If the value is a list, if the value is a tableDto, convert it into its pk
		 */
		protected Object processValue(Object val) {
			Object processed;

			if (val instanceof List) {
				List<Object> list = new ArrayList<>();
				for (Object obj : (List<?>) val) {
					if (obj instanceof ITableDto) {
						ITableDto pk = ((ITableDto) obj).createPk();
						List<OneObject> sublist = processOneObject(pk);
						list.add(sublist);
					} else {
						list.add(obj);
					}
				}
				processed = list;
			} else {
				processed = val;
			}

			return processed;
		}
	}

	protected class TwoObject extends OneObject {
		private Object oldValue;

		public TwoObject(String attribute, Object oldValue, Object newValue) {
			super(attribute, newValue);
			this.oldValue = processValue(oldValue);
		}

		public Object getOldValue() {
			return oldValue;
		}
	}

}
