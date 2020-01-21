package es.prodevelop.pui9.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.eventlistener.EventLauncher;
import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.exceptions.AbstractPuiDaoException;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoDuplicatedException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoNoNumericColumnException;
import es.prodevelop.pui9.exceptions.PuiDaoNotExistsException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiServiceCopyRegistryException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceExistsException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.service.registry.ServiceRegistry;
import es.prodevelop.pui9.utils.PuiDateUtil;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * This class is the abstract implementation of all the {@link IService}
 * services of PUI. All the services that uses the stack of PUI should use this
 * class in its Services.
 * <p>
 * This class offers a lot of methods that could be used to manage registries,
 * avoiding using directly the {@link IDao} methods.
 * <p>
 * If you want to use a service, you must to create an Autowired property using
 * the interface of this Service. It is highly recommended to use and reference
 * the Services instead of the DAOs
 * 
 * @param <TPK>  The {@link ITableDto} PK for the Table (if the service has one
 *               associated)
 * @param <T>    The whole {@link ITableDto} for the Table (if the service has
 *               one associated)
 * @param <V>    The {@link IViewDto} for the View (if the service has one
 *               associated)
 * @param <DAO>  The {@link ITableDao} for the Table
 * @param <VDAO> The {@link IViewDao} for the View
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractService<TPK extends ITableDto, T extends TPK, V extends IViewDto, DAO extends ITableDao<TPK, T>, VDAO extends IViewDao<V>>
		implements IService<TPK, T, V, DAO, VDAO> {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private DAO tableDao;

	@Autowired
	private VDAO viewDao;

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private PuiApplicationContext context;

	private List<MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?>> multiValuedAttributes = new ArrayList<>();

	@Autowired
	private EventLauncher eventLauncher;

	/**
	 * Registers the Service into ServiceRegistry
	 */
	@PostConstruct
	private void postConstruct() {
		serviceRegistry.registerService(this);
		addMultiValuedAttributes();
	}

	/**
	 * Get the language of the session in the current user request. If there is no
	 * session, the {@link PuiLanguageUtils#getDefaultLanguage()} will be returned
	 * 
	 * @return The Language to be used in the request
	 */
	protected PuiLanguage getSessionLanguage() {
		return PuiLanguageUtils.getSessionLanguage();
	}

	@Override
	public DAO getTableDao() {
		return tableDao;
	}

	@Override
	public VDAO getViewDao() {
		return viewDao;
	}

	@Override
	public Class<T> getTableDtoClass() {
		return tableDao.getDtoClass();
	}

	@Override
	public Class<TPK> getTableDtoPkClass() {
		return tableDao.getDtoPkClass();
	}

	@Override
	public Class<V> getViewDtoClass() {
		return viewDao.getDtoClass();
	}

	/**
	 * Get the DaoRegistry bean
	 * 
	 * @return The DaoRegistry
	 */
	protected DaoRegistry getDaoRegistry() {
		return daoRegistry;
	}

	/**
	 * Get the ServiceRegistry bean
	 * 
	 * @return The ServiceRegistry
	 */
	protected ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	/**
	 * Get the PUI application context
	 * 
	 * @return The PUI application context
	 */
	protected PuiApplicationContext getContext() {
		return context;
	}

	/**
	 * Get the PUI Event Launcher, that allows to fire PUI Events over the
	 * application. See {@link PuiEvent} class
	 * 
	 * @return
	 */
	protected EventLauncher getEventLauncher() {
		return eventLauncher;
	}

	/**
	 * Get the session of the user that is executing the current operation
	 * 
	 * @return The current user session or null if no one exists
	 */
	protected PuiUserSession getSession() {
		return PuiUserSession.getCurrentSession();
	}

	/**
	 * Override this method to add multivalued attributes configurations. Use
	 * {@link #addMultiValuedAttribute(MultiValuedAttribute)} method in order to add
	 * every multivalued attribute. Remember that the join should be done with the
	 * Java Fields (not with the Columns)
	 */
	protected void addMultiValuedAttributes() {
		// nothing to do
	}

	/**
	 * In order to call this method properly, override
	 * {@link #addMultiValuedAttributes()} method and call it so many times as
	 * multivalued attributes you have.
	 * 
	 * @param multivaluedAttribute The multivalued attribute to be added
	 */
	protected final void addMultiValuedAttribute(MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> multivaluedAttribute) {
		multiValuedAttributes.add(multivaluedAttribute);
	}

	@Override
	public T getNew() throws PuiServiceNewException {
		return getNew(getSessionLanguage());
	}

	@Override
	public T getNew(PuiLanguage language) throws PuiServiceNewException {
		try {
			T dto = getTableDtoClass().newInstance();
			PuiLanguageUtils.setLanguage(dto, language);
			afterNew(dto);
			afterGetMultivaluedAttributes(dto);

			return dto;
		} catch (InstantiationException | IllegalAccessException | PuiServiceException e) {
			throw new PuiServiceNewException(e);
		}
	}

	@Override
	public boolean exists(TPK dtoPk) throws PuiServiceExistsException {
		return exists(dtoPk, getSessionLanguage());
	}

	@Override
	public boolean exists(TPK dtoPk, PuiLanguage language) throws PuiServiceExistsException {
		try {
			PuiLanguageUtils.setLanguage(dtoPk, language);
			return tableDao.exists(dtoPk);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceExistsException(e);
		}
	}

	@Override
	public T getByPk(TPK dtoPk) throws PuiServiceGetException {
		return getByPk(dtoPk, getSessionLanguage());
	}

	@Override
	public T getByPk(TPK dtoPk, PuiLanguage language) throws PuiServiceGetException {
		try {
			T dto = tableDao.findOne(dtoPk, language);
			if (dto != null) {
				afterGet(dto);
				afterGetMultivaluedAttributes(dto);
			} else {
				throw new PuiDaoNotExistsException();
			}

			return dto;
		} catch (AbstractPuiDaoException | PuiServiceException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public V getViewByPk(final TPK dtoPk) throws PuiServiceGetException {
		return getViewByPk(dtoPk, getSessionLanguage());
	}

	@Override
	public V getViewByPk(final TPK dtoPk, PuiLanguage language) throws PuiServiceGetException {
		try {
			List<String> columnNames = DtoRegistry.getAllColumnNames(dtoPk.getClass());
			FilterBuilder filterBuilder = FilterBuilder.newAndFilter();

			for (String columnName : columnNames) {
				Field field = DtoRegistry.getJavaFieldFromColumnName(dtoPk.getClass(), columnName);
				try {
					Object value = FieldUtils.readField(field, dtoPk, true);
					filterBuilder.addEquals(columnName, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new PuiServiceGetException(e);
				}
			}

			List<V> list = getViewDao().findWhere(filterBuilder, language);
			if (!list.isEmpty()) {
				return list.get(0);
			} else {
				throw new PuiServiceGetException();
			}
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public List<T> getAll() throws PuiServiceGetException {
		return getAll(null, getSessionLanguage());
	}

	@Override
	public List<T> getAll(PuiLanguage language) throws PuiServiceGetException {
		return getAll(null, language);
	}

	@Override
	public List<T> getAll(OrderBuilder orderBuilder) throws PuiServiceGetException {
		return getAll(orderBuilder, getSessionLanguage());
	}

	@Override
	public List<T> getAll(OrderBuilder orderBuilder, PuiLanguage language) throws PuiServiceGetException {
		if (language == null) {
			language = getSessionLanguage();
		}

		try {
			List<T> list = getTableDao().findAll(orderBuilder, language);

			for (T dto : list) {
				afterGet(dto);
				afterGetMultivaluedAttributes(dto);
			}
			return list;
		} catch (PuiDaoFindException | PuiServiceException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public List<T> getAllWhere(FilterBuilder filterBuilder) throws PuiServiceGetException {
		return getAllWhere(filterBuilder, null);
	}

	@Override
	public List<T> getAllWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder) throws PuiServiceGetException {
		try {
			List<T> list = getTableDao().findWhere(filterBuilder, orderBuilder);

			for (T dto : list) {
				afterGet(dto);
				afterGetMultivaluedAttributes(dto);
			}
			return list;
		} catch (PuiDaoFindException | PuiServiceException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public List<V> getAllView() throws PuiServiceGetException {
		return getAllView(getSessionLanguage());
	}

	@Override
	public List<V> getAllView(PuiLanguage language) throws PuiServiceGetException {
		try {
			return viewDao.findAll(language);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public List<V> getAllViewWhere(FilterBuilder filterBuilder) throws PuiServiceGetException {
		return getAllViewWhere(filterBuilder, null);
	}

	@Override
	public List<V> getAllViewWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder)
			throws PuiServiceGetException {
		try {
			return getViewDao().findWhere(filterBuilder, orderBuilder);
		} catch (PuiDaoFindException e) {
			throw new PuiServiceGetException(e);
		}
	}

	@Override
	public T insert(T dto) throws PuiServiceInsertException {
		try {
			logger.debug("Insert: " + getTableDtoClass().getSimpleName());

			setAutoincrementableValues(Collections.singletonList(dto));

			// before check existence, remove all the sequence column values
			List<String> sequenceColumns = DtoRegistry.getSequenceColumns(getTableDtoPkClass());
			for (String seqCol : sequenceColumns) {
				Field seqField = DtoRegistry.getJavaFieldFromColumnName(getTableDtoPkClass(), seqCol);
				try {
					FieldUtils.writeField(seqField, dto, null, true);
				} catch (IllegalAccessException e) {
					// do nothing
				}
			}

			if (exists(dto.createPk())) {
				throw new PuiServiceInsertException(new PuiDaoDuplicatedException());
			}

			if (PuiLanguageUtils.hasLanguageSupport(dto) && PuiLanguageUtils.getLanguage(dto) == null) {
				PuiLanguageUtils.setLanguage(dto, getSessionLanguage());
			}

			beforeInsert(dto);
			dto = tableDao.save(dto);
			afterInsert(dto);
			afterInsertMultivaluedAttributes(dto);

			return dto;
		} catch (PuiDaoSaveException | PuiDaoNoNumericColumnException e) {
			throw new PuiServiceInsertException(e);
		} catch (PuiServiceInsertException e) {
			throw e;
		} catch (PuiServiceException e) {
			throw new PuiServiceInsertException(e);
		}
	}

	@Override
	public List<T> bulkInsert(List<T> dtoList) throws PuiServiceInsertException {
		if (CollectionUtils.isEmpty(dtoList)) {
			return Collections.emptyList();
		}

		try {
			logger.debug("Bulk Insert: " + getTableDtoClass().getSimpleName());

			setAutoincrementableValues(dtoList);

			dtoList = tableDao.bulkInsert(dtoList);

			return dtoList;
		} catch (PuiDaoSaveException | PuiDaoNoNumericColumnException e) {
			throw new PuiServiceInsertException(e);
		}
	}

	@Override
	public T update(T dto) throws PuiServiceUpdateException {
		try {
			TPK dtoPk = dto.<TPK>createPk();
			logger.debug("Update: " + getTableDtoClass().getSimpleName());

			PuiLanguage language = PuiLanguageUtils.getLanguage(dto);

			T oldDto = null;
			if (language == null) {
				oldDto = getByPk(dtoPk);
			} else {
				oldDto = getByPk(dtoPk, language);
			}

			if (oldDto == null) {
				throw new PuiServiceGetException();
			}

			if (PuiLanguageUtils.hasLanguageSupport(dto) && PuiLanguageUtils.getLanguage(dto) == null) {
				PuiLanguageUtils.setLanguage(dto, getSessionLanguage());
			}

			beforeUpdate(oldDto, dto);
			beforeUpdateMultivaluedAttributes(dto);
			dto = tableDao.save(dto);
			afterUpdate(oldDto, dto);

			return dto;
		} catch (PuiDaoSaveException | PuiServiceException e) {
			throw new PuiServiceUpdateException(e);
		}
	}

	@Override
	public List<T> bulkUpdate(List<T> dtoList) throws PuiServiceUpdateException {
		if (CollectionUtils.isEmpty(dtoList)) {
			return Collections.emptyList();
		}

		try {
			logger.debug("Bulk Update: " + getTableDtoClass().getSimpleName());

			dtoList = tableDao.bulkUpdate(dtoList);

			return dtoList;
		} catch (PuiDaoSaveException e) {
			throw new PuiServiceUpdateException(e);
		}
	}

	@Override
	public T patch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiServiceUpdateException {
		logger.debug("Patch: " + getTableDtoClass().getSimpleName());

		try {
			T oldDto = getByPk(dtoPk);
			beforePatch(dtoPk, fieldValuesMap);

			T dto = getNew();
			PuiObjectUtils.copyProperties(dto, oldDto);
			Map<String, Object> fieldValuesMapCopy = new HashMap<>(fieldValuesMap);
			try {
				List<String> toRemove = new ArrayList<>();
				for (Iterator<Entry<String, Object>> it = fieldValuesMapCopy.entrySet().iterator(); it.hasNext();) {
					Entry<String, Object> next = it.next();
					Field field = DtoRegistry.getJavaFieldFromFieldName(getTableDtoClass(), next.getKey());
					if (field == null) {
						field = DtoRegistry.getJavaFieldFromColumnName(getTableDtoClass(), next.getKey());
						if (field != null) {
							toRemove.add(next.getKey());
						}
					}
				}

				for (String rem : toRemove) {
					Object val = fieldValuesMapCopy.remove(rem);
					String fieldName = DtoRegistry.getFieldNameFromColumnName(getTableDtoClass(), rem);
					fieldValuesMapCopy.put(fieldName, val);
				}

				for (Iterator<Entry<String, Object>> it = fieldValuesMap.entrySet().iterator(); it.hasNext();) {
					Entry<String, Object> next = it.next();
					if (DtoRegistry.getDateTimeFields(getTableDtoClass()).contains(next.getKey())
							&& next.getValue() instanceof String) {
						Instant instant = PuiDateUtil.stringToInstant((String) next.getValue());
						next.setValue(instant);
					}
				}

				BeanUtils.populate(dto, fieldValuesMap);
			} catch (IllegalAccessException | InvocationTargetException e1) {
				logger.error("Error while populating the dto with a map values");
			}

			beforeUpdate(oldDto, dto);
			tableDao.patch(dtoPk, fieldValuesMap);
			afterUpdate(oldDto, dto);

			return dto;
		} catch (PuiDaoSaveException | PuiServiceException e) {
			throw new PuiServiceUpdateException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public TPK delete(TPK dtoPk) throws PuiServiceDeleteException {
		if (dtoPk == null) {
			return null;
		}

		try {
			logger.debug("Delete: " + getTableDtoClass().getSimpleName());

			T dto = null;
			if (getTableDtoPkClass().equals(dtoPk.getClass())) {
				dto = getByPk(dtoPk);
			} else {
				dto = (T) dtoPk;
				dtoPk = dto.createPk();
			}

			if (dto != null) {
				beforeDelete(dto);
				beforeDeleteMultivaluedAttributes(dto);

				dtoPk = tableDao.delete(dtoPk);

				afterDelete(dto);
			}

			return dtoPk;
		} catch (PuiDaoDeleteException e) {
			throw new PuiServiceDeleteException(e);
		} catch (PuiServiceException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public List<TPK> bulkDelete(List<? extends TPK> dtoPkList) throws PuiServiceDeleteException {
		if (CollectionUtils.isEmpty(dtoPkList)) {
			return Collections.emptyList();
		}

		try {
			logger.debug("Bulk Delete: " + getTableDtoClass().getSimpleName());

			List<TPK> realDtoList = new ArrayList<>();
			for (TPK dto : dtoPkList) {
				if (tableDao.getDtoPkClass().equals(dto.getClass())) {
					realDtoList.add(dto);
				} else {
					realDtoList.add(dto.createPk());
				}
			}

			realDtoList = tableDao.bulkDelete(realDtoList);

			return realDtoList;
		} catch (PuiDaoDeleteException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public void deleteAll() throws PuiServiceDeleteException {
		try {
			tableDao.deleteAll();
		} catch (PuiDaoDeleteException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public void deleteAll(PuiLanguage language) throws PuiServiceDeleteException {
		try {
			tableDao.deleteAll(language);
		} catch (PuiDaoDeleteException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public T copy(TPK pk) throws PuiServiceCopyRegistryException {
		try {
			T dto = getByPk(pk);
			DtoRegistry.getPkFields(getTableDtoClass()).forEach(pkFieldName -> {
				try {
					DtoRegistry.getJavaFieldFromFieldName(getTableDtoClass(), pkFieldName).set(dto, null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// do nothing
				}
			});
			afterCopy(pk, dto);
			return dto;
		} catch (PuiServiceException e) {
			throw new PuiServiceCopyRegistryException(e);
		}
	}

	/**
	 * It is called in the insert operation.
	 * <p>
	 * Set all the autoincrementable fields in the DTO (only if it is not set
	 * before).
	 * <p>
	 * For each autoincrementable field,
	 * {@link #getAutoincrementableColumnFilter(ITableDto, String)} is called in
	 * order to provide a filter to be used in the calculus of the next value of the
	 * column
	 * 
	 * @param dto The registry to be modified
	 * @throws PuiDaoNoNumericColumnException If the column to be modified is not of
	 *                                        numeric type
	 */
	protected void setAutoincrementableValues(List<T> dtoList) throws PuiDaoNoNumericColumnException {
		List<String> fieldNames = DtoRegistry.getAutoincrementableFieldNames(getTableDtoPkClass());
		Map<String, Field> map = DtoRegistry.getMapFieldsFromFieldName(getTableDtoPkClass());

		for (String fieldName : fieldNames) {
			try {
				Field field = map.get(fieldName);
				if (field.get(dtoList.get(0)) != null) {
					continue;
				}

				String columnName = DtoRegistry.getColumnNameFromFieldName(getTableDtoPkClass(), fieldName);
				FilterBuilder filterBuilder = getAutoincrementableColumnFilter(dtoList.get(0), columnName);

				Number nextId = getTableDao().getNextValue(columnName, filterBuilder);

				for (T dto : dtoList) {
					FieldUtils.writeField(field, dto, nextId, true);

					if (nextId instanceof Long) {
						nextId = nextId.longValue() + 1;
					} else if (nextId instanceof Integer) {
						nextId = nextId.intValue() + 1;
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
			}
		}
	}

	/**
	 * Creates a filter to be applied in the calculus of the next value of the
	 * column
	 * 
	 * @param dto        The registry to be modified
	 * @param columnName The autoincrementable column name
	 * @return The filter to be used in the calculus of the next value
	 */
	protected FilterBuilder getAutoincrementableColumnFilter(T dto, String columnName) {
		return null;
	}

	/**
	 * Do something before inserting the registry
	 * 
	 * @param dto The registry about to be inserted
	 */
	protected void beforeInsert(T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before updating the registry
	 * 
	 * @param oldDto The old registry before being updated
	 * @param dto    The new registry to be updated
	 */
	protected void beforeUpdate(T oldDto, T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before patching the registry. Use it mostly to modify the fields
	 * to be updated
	 * 
	 * @param dtoPk          The PK of the registry to be updated
	 * @param fieldValuesMap The map with the fields to be patched and its new
	 *                       values
	 */
	protected void beforePatch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something before deleting the registry
	 * 
	 * @param dto The registry to be deleted
	 */
	protected void beforeDelete(T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after getting an existing registry
	 * 
	 * @param dto The existing registry
	 */
	protected void afterGet(T dto) throws PuiServiceException {
		// Nothing to do
	}

	/**
	 * Do something after creating a new registry
	 * 
	 * @param dto The new registry
	 */
	protected void afterNew(T dto) throws PuiServiceException {
		// Nothing to do
	}

	/**
	 * Do something after inserting the registry
	 * 
	 * @param dto The inserted registry
	 */
	protected void afterInsert(T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after updating the registry
	 * 
	 * @param oldDto The old registry before being updated
	 * @param dto    The new updated registry
	 */
	protected void afterUpdate(T oldDto, T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after deleting the registry
	 * 
	 * @param dto The deleted registry
	 */
	protected void afterDelete(T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * Do something after copying the registry
	 * 
	 * @param pk  The PK of the copied registry
	 * @param dto The copied registry
	 */
	protected void afterCopy(TPK pk, T dto) throws PuiServiceException {
		// nothing to do
	}

	/**
	 * This method is called after getting an existing registry or creating a new
	 * one in order to populate the multivalued attributes
	 * 
	 * @param dto The registry to populated its multivalued attributes
	 */
	private void afterGetMultivaluedAttributes(T dto) {
		for (MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> mva : multiValuedAttributes) {
			mva.populate(dto);
		}
	}

	/**
	 * This method is called after inserting a registry in order to evaluate the
	 * multivalued attributes
	 * 
	 * @param dto The registry to evaluate its multivalued attributes
	 * @throws PuiServiceInsertException If an error is throws while inserting the
	 *                                   multivalued attributes
	 */
	private void afterInsertMultivaluedAttributes(T dto) throws PuiServiceInsertException {
		for (MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> mva : multiValuedAttributes) {
			mva.insert(dto);
		}
	}

	/**
	 * This method is called before updating a registry in order to evaluate the
	 * multivalued attributes
	 * 
	 * @param dto The registry to evaluate its multivalued attributes
	 * @throws PuiServiceInsertException If an error is throws while updating the
	 *                                   multivalued attributes
	 */
	private void beforeUpdateMultivaluedAttributes(T dto) throws PuiServiceUpdateException {
		for (MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> mva : multiValuedAttributes) {
			mva.update(dto);
		}
	}

	/**
	 * This method is called before deleting a registry in order to evaluate the
	 * multivalued attributes
	 * 
	 * @param dto The registry to evaluate its multivalued attributes
	 * @throws PuiServiceInsertException If an error is throws while deleting the
	 *                                   multivalued attributes
	 */
	private void beforeDeleteMultivaluedAttributes(T dto) throws PuiServiceDeleteException {
		for (MultiValuedAttribute<T, ?, ?, ?, ?, ?, ?> mva : multiValuedAttributes) {
			mva.delete(dto, true);
		}
	}

}
