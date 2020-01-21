package es.prodevelop.pui9.model.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.db.utils.SqlUtils;
import es.prodevelop.pui9.eventlistener.ThreadDaoEvents;
import es.prodevelop.pui9.eventlistener.event.DeleteDaoEvent;
import es.prodevelop.pui9.eventlistener.event.InsertDaoEvent;
import es.prodevelop.pui9.eventlistener.event.UpdateDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiDaoAttributeLengthException;
import es.prodevelop.pui9.exceptions.PuiDaoDataAccessException;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoDuplicatedException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnInsertException;
import es.prodevelop.pui9.exceptions.PuiDaoIntegrityOnUpdateException;
import es.prodevelop.pui9.exceptions.PuiDaoNotExistsException;
import es.prodevelop.pui9.exceptions.PuiDaoNullParametersException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiDaoUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;
import es.prodevelop.pui9.utils.PuiObjectUtils;

/**
 * This abstract class provides the implementation of the all the Table DAO for
 * JDBC approach. It implements {@link ITableDao} interface for bringing the
 * necessary methods to manage the tables
 * 
 * @param <TPK> The PK {@link IDto} class that represents this DAO Class
 * @param <T>   The whole {@link IDto} class that represents this DAO Class
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractTableDao<TPK extends ITableDto, T extends TPK> extends AbstractDao<T>
		implements ITableDao<TPK, T> {

	protected static final String INSERT_INTO = "INSERT INTO ";
	protected static final String UPDATE = "UPDATE ";
	protected static final String DELETE_FROM = "DELETE FROM ";
	protected static final String SET = " SET ";
	protected static final String AND = " AND ";

	@Autowired
	private ThreadDaoEvents threadDaoEvents;

	private ITableDao<ITableDto, ITableDto> tableTranslationDao = null;

	@Override
	public boolean exists(TPK dtoPk) throws PuiDaoFindException {
		return findOne(dtoPk) != null;
	}

	@Override
	public T findOne(TPK dtoPk) throws PuiDaoFindException {
		return findOne(dtoPk, PuiLanguageUtils.getSessionLanguage());
	}

	@Override
	public T findOne(TPK dtoPk, PuiLanguage lang) throws PuiDaoFindException {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		FilterBuilder filterBuilder = FilterBuilder.newAndFilter();
		for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
			String next = it.next();
			try {
				Field field = mapPk.get(next);
				Object val = FieldUtils.readField(field, dtoPk, true);
				filterBuilder.addEquals(next, val);
			} catch (Exception e) {
				// do nothing
			}
		}

		List<T> list = findWhere(filterBuilder, lang);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public T save(T dto) throws PuiDaoSaveException {
		if (dto == null) {
			return null;
		}

		PuiLanguage lang = PuiLanguageUtils.getLanguage(dto);
		T oldDto;
		try {
			oldDto = findOne(dto.<TPK>createPk(), lang);
		} catch (PuiDaoFindException e) {
			throw new PuiDaoSaveException(e);
		}

		try {
			checkValues(dto);
		} catch (PuiDaoNullParametersException | PuiDaoAttributeLengthException e) {
			throw new PuiDaoSaveException(e);
		}

		if (oldDto == null) {
			dto = insert(dto);
		} else {
			dto = update(dto, oldDto);
		}

		return dto;
	}

	/**
	 * Insert operation of the given DTO against the Database
	 * 
	 * @param dto The DTO to be inserted
	 * @return The inserted DTO
	 * @throws PuiDaoInsertException If any SQL error while executing the statement
	 *                               is thrown
	 */
	protected T insert(T dto) throws PuiDaoInsertException {
		try {
			doInsert(Collections.singletonList(dto));
			insertTranslations(Collections.singletonList(dto));
			afterInsert(Collections.singletonList(dto));

			return dto;
		} catch (DuplicateKeyException e) {
			throw new PuiDaoInsertException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnInsertException(e);
		} catch (Exception e) {
			throw new PuiDaoInsertException(new PuiDaoDataAccessException(e));
		}
	}

	@Override
	public List<T> bulkInsert(List<T> dtoList) throws PuiDaoInsertException {
		if (dtoList == null) {
			return Collections.emptyList();
		}

		try {
			doInsert(dtoList);
			insertTranslations(dtoList);
			afterInsert(dtoList);

			return dtoList;
		} catch (DuplicateKeyException e) {
			throw new PuiDaoInsertException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnInsertException(e);
		} catch (Exception e) {
			throw new PuiDaoInsertException(new PuiDaoDataAccessException(e));
		}
	}

	/**
	 * Perform the insert in the table of the specified DTO
	 * 
	 * @param dtoList The DTO list to be inserted
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void doInsert(List<T> dtoList) throws DataAccessException {
		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(getDtoClass()));
		List<String> sequenceColumns = DtoRegistry.getSequenceColumns(getDtoClass());
		columnNames.removeAll(sequenceColumns);

		StringBuilder query = new StringBuilder();
		query.append(INSERT_INTO);
		query.append(getEntityName());
		query.append(" (");
		for (Iterator<String> it = columnNames.iterator(); it.hasNext();) {
			String columnName = it.next();
			query.append(columnName);
			if (it.hasNext()) {
				query.append(", ");
			}
		}
		query.append(" ) VALUES (");

		for (Iterator<String> it = columnNames.iterator(); it.hasNext();) {
			it.next();
			query.append("?");
			if (it.hasNext()) {
				query.append(", ");
			}
		}
		query.append(")");

		performInsert(dtoList, query.toString());
	}

	/**
	 * Really performs the Insert into the database
	 * 
	 * @param dtoList The DTO list to be inserted
	 * @param sql     The query to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void performInsert(List<T> dtoList, String sql) throws DataAccessException {
		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(getDtoClass()));
		List<String> sequenceColumns = DtoRegistry.getSequenceColumns(getDtoClass());
		columnNames.removeAll(sequenceColumns);
		Map<String, Field> map = DtoRegistry.getMapFieldsFromColumnName(getDtoClass());

		if (dtoList.size() == 1) {
			// when the list size of DTO is only 1 element...

			// following code sets the parameters for the statement, using a KeyHolder to
			// retrieve any generated key by the database
			GeneratedKeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					int nextParameter = 1;
					for (String columnName : columnNames) {
						try {
							Field field = map.get(columnName);
							Object objValue = FieldUtils.readField(field, dtoList.get(0), true);
							if (objValue instanceof Instant) {
								objValue = Timestamp.from((Instant) objValue);
							}
							objValue = modifyInsertColumnValue(dtoList.get(0), columnName, objValue);

							statement.setObject(nextParameter++, objValue);
						} catch (Exception e) {
							// do nothing
						}
					}

					return statement;
				}
			}, holder);

			if (!sequenceColumns.isEmpty()) {
				Object val = null;
				try {
					val = holder.getKey();
				} catch (InvalidDataAccessApiUsageException e) {
					val = holder.getKeys().get(sequenceColumns.get(0));
				}
				if (val != null) {
					String seqColumn = sequenceColumns.get(0);
					Field field = map.get(seqColumn);
					if (!field.getType().equals(val.getClass())) {
						if (Long.class.equals(field.getType())) {
							val = new Long(val.toString());
						} else if (Integer.class.equals(field.getType())) {
							val = new Integer(val.toString());
						} else if (BigDecimal.class.equals(field.getType())) {
							val = new BigDecimal(val.toString());
						}
					}
					try {
						FieldUtils.writeField(field, dtoList.get(0), val, true);
					} catch (Exception e) {
						// should never occurs
					}
				}
			}
		} else {
			// when the list size of DTO is more than 1 element...
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					T dto = dtoList.get(i);
					int nextParameter = 1;
					for (String columnName : columnNames) {
						try {
							Field field = map.get(columnName);
							Object objValue = FieldUtils.readField(field, dto, true);
							if (objValue instanceof Instant) {
								objValue = Timestamp.from((Instant) objValue);
							}
							objValue = modifyInsertColumnValue(dtoList.get(0), columnName, objValue);

							ps.setObject(nextParameter++, objValue);
						} catch (Exception e) {
							// do nothing
						}
					}
				}

				@Override
				public int getBatchSize() {
					return dtoList.size();
				}
			});
		}
	}

	/**
	 * This method is useful to modify the value used in the insert sql. The default
	 * returned value is the character '?'
	 * 
	 * @param dto        The DTO to be inserted
	 * @param columnName The column to be modified
	 * @param value      The value to be inserted
	 * 
	 * @return The modified value. By default, the same parameter value
	 */
	protected Object modifyInsertColumnValue(T dto, String columnName, Object value) {
		return value;
	}

	/**
	 * Insert the translations of the given DTO by calling the
	 * {@link #save(ITableDto)} method of the translation DAO
	 * 
	 * @param dtoList The DTO list to be inserted
	 * @throws PuiDaoSaveException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private void insertTranslations(List<T> dtoList) throws PuiDaoSaveException {
		if (getTableTranslationDao() == null) {
			return;
		}

		Class<ITableDto> translationClass = getTableTranslationDao().getDtoClass();
		Field langField = DtoRegistry.getJavaFieldFromColumnName(translationClass, IDto.LANG_COLUMN_NAME);
		Field langStatusField = DtoRegistry.getJavaFieldFromColumnName(translationClass, IDto.LANG_STATUS_COLUMN_NAME);

		try {
			for (T dto : dtoList) {
				ITableDto translation = translationClass.newInstance();
				PuiObjectUtils.copyProperties(translation, dto);
				FieldUtils.writeField(langStatusField, translation, PuiConstants.TRUE_INT, true);
				getTableTranslationDao().save(translation);

				String baseLang = (String) langField.get(translation);
				for (PuiLanguage lang : getAllLanguages()) {
					if (lang.getIsocode().equals(baseLang)) {
						continue;
					}
					translation = translationClass.newInstance();
					PuiObjectUtils.copyProperties(translation, dto);
					FieldUtils.writeField(langField, translation, lang.getIsocode(), true);
					FieldUtils.writeField(langStatusField, translation, PuiConstants.FALSE_INT, true);
					getTableTranslationDao().save(translation);
				}
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// do nothing
		}
	}

	/**
	 * Perform some actions after inserting the DTO. By default, the
	 * {@link InsertDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param dtoList The inserted DTO list
	 */
	protected void afterInsert(List<T> dtoList) {
		for (T dto : dtoList) {
			InsertDaoEvent eventType = new InsertDaoEvent(dto);
			threadDaoEvents.addEventType(eventType);
		}
	}

	/**
	 * Executes the update operation of the given DTO against the Database
	 * 
	 * @param dto    The DTO to be updated
	 * @param oldDto The old DTO
	 * @return The updated DTO
	 * @throws PuiDaoUpdateException If any SQL error while executing the statement
	 *                               is thrown
	 */
	protected T update(T dto, T oldDto) throws PuiDaoUpdateException {
		try {
			doUpdate(Collections.singletonList(dto));
			updateTranslations(Collections.singletonList(dto));
			afterUpdate(oldDto, Collections.singletonList(dto));

			return dto;
		} catch (DuplicateKeyException e) {
			throw new PuiDaoUpdateException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnUpdateException(e);
		} catch (Exception e) {
			throw new PuiDaoUpdateException(new PuiDaoDataAccessException(e));
		}
	}

	@Override
	public List<T> bulkUpdate(List<T> dtoList) throws PuiDaoUpdateException {
		if (dtoList == null) {
			return Collections.emptyList();
		}

		try {
			doUpdate(dtoList);
			updateTranslations(dtoList);
			afterUpdate(null, dtoList);

			return dtoList;
		} catch (DuplicateKeyException e) {
			throw new PuiDaoUpdateException(new PuiDaoDuplicatedException());
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnUpdateException(e);
		} catch (Exception e) {
			throw new PuiDaoUpdateException(new PuiDaoDataAccessException(e));
		}
	}

	/**
	 * Perform the update in the table of the specified DTO
	 * 
	 * @param dtoPk The PK of the DTO to be updated
	 * @param dto   The DTO to be updated
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void doUpdate(List<T> dtoList) throws DataAccessException {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(getDtoClass()));
		List<String> sequenceColumns = DtoRegistry.getSequenceColumns(getDtoClass());
		columnNames.removeAll(sequenceColumns);

		StringBuilder query = new StringBuilder(UPDATE);
		query.append(getEntityName());
		query.append(SET);

		for (Iterator<String> it = columnNames.iterator(); it.hasNext();) {
			String next = it.next();
			query.append(next + " = ?");
			if (it.hasNext()) {
				query.append(", ");
			}
		}

		query.append(WHERE);

		for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
			String next = it.next();

			query.append(next + " = ?");
			if (it.hasNext()) {
				query.append(AND);
			}
		}

		performUpdate(dtoList, query.toString());
	}

	/**
	 * Really performs the Uptate into the database
	 * 
	 * @param dtoList The DTO list to be updated
	 * @param sql     The query to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void performUpdate(List<T> dtoList, String sql) throws DataAccessException {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(getDtoClass()));
		List<String> sequenceColumns = DtoRegistry.getSequenceColumns(getDtoClass());
		columnNames.removeAll(sequenceColumns);

		Map<String, Field> map = DtoRegistry.getMapFieldsFromColumnName(getDtoClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		if (dtoList.size() == 1) {
			// when the list size of DTO is only 1 element...
			List<Object> values = new ArrayList<>();
			for (String columnName : columnNames) {
				try {
					Field field = map.get(columnName);
					Object val = FieldUtils.readField(field, dtoList.get(0), true);
					if (val instanceof Instant) {
						val = Timestamp.from((Instant) val);
					}
					val = modifyUpdateColumnValue(dtoList.get(0), columnName, val);
					values.add(val);
				} catch (Exception e) {
					// do nothing
				}
			}

			TPK dtoPk = dtoList.get(0).createPk();
			for (String pkColumnName : pkColumnNames) {
				Field field = mapPk.get(pkColumnName);
				try {
					Object val = FieldUtils.readField(field, dtoPk, true);
					values.add(val);
				} catch (Exception e1) {
					try {
						Object val = FieldUtils.readField(field, dtoList.get(0), true);
						values.add(val);
					} catch (Exception e2) {
						// do nothing
					}
				}
			}

			jdbcTemplate.update(sql, values.toArray());
		} else {
			// when the list size of DTO is more than 1 element...
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					T dto = dtoList.get(i);
					int nextParameter = 1;
					for (String columnName : columnNames) {
						try {
							Field field = map.get(columnName);
							Object objValue = FieldUtils.readField(field, dto, true);
							if (objValue instanceof Instant) {
								objValue = Timestamp.from((Instant) objValue);
							}
							objValue = modifyUpdateColumnValue(dto, columnName, objValue);

							ps.setObject(nextParameter++, objValue);
						} catch (Exception e) {
							// do nothing
						}
					}

					TPK dtoPk = dto.createPk();
					for (String pkColumnName : pkColumnNames) {
						Field field = mapPk.get(pkColumnName);
						try {
							Object val = FieldUtils.readField(field, dtoPk, true);
							ps.setObject(nextParameter++, val);
						} catch (Exception e1) {
							try {
								Object val = FieldUtils.readField(field, dto, true);
								ps.setObject(nextParameter++, val);
							} catch (Exception e2) {
								// do nothing
							}
						}
					}
				}

				@Override
				public int getBatchSize() {
					return dtoList.size();
				}
			});
		}
	}

	/**
	 * This method is useful to modify the value used in the update sql. The default
	 * returned value is the character '?'
	 * 
	 * @param dto        The DTO to be updated
	 * @param columnName The column to be modified
	 * @param value      The value to be updated
	 * 
	 * @return The modified value. By default, the same parameter value
	 */
	protected Object modifyUpdateColumnValue(T dto, String columnName, Object value) {
		return value;
	}

	/**
	 * Update the translations of the given DTO by calling the
	 * {@link #save(ITableDto)} method of the translation DAO
	 * 
	 * @param dto The DTO to be updated
	 * @throws PuiDaoSaveException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private void updateTranslations(List<T> dtoList) throws PuiDaoSaveException {
		if (getTableTranslationDao() == null) {
			return;
		}

		Class<ITableDto> translationClass = getTableTranslationDao().getDtoClass();
		try {
			for (T dto : dtoList) {
				ITableDto translation = translationClass.newInstance();
				PuiObjectUtils.copyProperties(translation, dto);
				getTableTranslationDao().save(translation);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// do nothing
		}
	}

	/**
	 * Perform some actions after updating the DTO. By default, the
	 * {@link UpdateDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param oldDto  The old DTO
	 * @param dtoList The updated DTO
	 */
	protected void afterUpdate(T oldDto, List<T> dtoList) {
		for (T dto : dtoList) {
			threadDaoEvents.addEventType(new UpdateDaoEvent(dto, oldDto));
		}
	}

	public TPK patch(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiDaoSaveException {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		Map<String, Field> map = new HashMap<>();
		map.putAll(DtoRegistry.getMapFieldsFromColumnName(getDtoClass()));
		map.putAll(DtoRegistry.getMapFieldsFromFieldName(getDtoClass()));

		Map<String, Object> fieldValuesMapCopy = new HashMap<>(fieldValuesMap);
		fieldValuesMapCopy.keySet().removeIf(fieldName -> !map.containsKey(fieldName));

		if (!fieldValuesMapCopy.isEmpty()) {
			StringBuilder query = new StringBuilder(UPDATE);
			query.append(getEntityName());
			query.append(SET);

			List<Object> values = new ArrayList<>();
			for (Iterator<Entry<String, Object>> it = fieldValuesMapCopy.entrySet().iterator(); it.hasNext();) {
				Entry<String, Object> next = it.next();
				String column = next.getKey();
				if (!DtoRegistry.getColumnNames(getDtoClass()).contains(column)) {
					column = DtoRegistry.getColumnNameFromFieldName(getDtoClass(), column);
				}
				query.append(column + " = ?");
				if (it.hasNext()) {
					query.append(", ");
				}
				Object val = next.getValue();
				if (val instanceof Instant) {
					val = Timestamp.from((Instant) val);
				}
				values.add(val);
			}

			query.append(WHERE);

			for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
				String next = it.next();

				query.append(next + " = ?");
				if (it.hasNext()) {
					query.append(AND);
				}

				try {
					Field field = mapPk.get(next);
					Object val = FieldUtils.readField(field, dtoPk, true);
					values.add(val);
				} catch (Exception e1) {
					// do nothing
				}
			}

			jdbcTemplate.update(query.toString(), values.toArray());
		}

		patchTranslations(dtoPk, fieldValuesMap);

		return dtoPk;
	}

	/**
	 * Update the translations of the given DTO by calling the
	 * {@link #save(ITableDto)} method of the translation DAO
	 * 
	 * @param dto The DTO to be updated
	 * @throws PuiDaoSaveException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private void patchTranslations(TPK dtoPk, Map<String, Object> fieldValuesMap) throws PuiDaoSaveException {
		if (getTableTranslationDao() == null) {
			return;
		}

		Class<ITableDto> translationClass = getTableTranslationDao().getDtoClass();
		ITableDto translation;
		try {
			translation = translationClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return;
		}

		PuiObjectUtils.copyProperties(translation, dtoPk);
		PuiLanguage lang;
		if (fieldValuesMap.containsKey(IDto.LANG_COLUMN_NAME)) {
			lang = new PuiLanguage(fieldValuesMap.get(IDto.LANG_COLUMN_NAME).toString());
		} else {
			lang = PuiLanguageUtils.getSessionLanguage();
		}

		PuiLanguageUtils.setLanguage(translation, lang);
		getTableTranslationDao().patch(translation.createPk(), fieldValuesMap);
	}

	@Override
	public TPK delete(TPK dtoPk) throws PuiDaoDeleteException {
		try {
			boolean exists = exists(dtoPk);
			if (!exists) {
				throw new PuiDaoDeleteException(new PuiDaoNotExistsException());
			}
		} catch (PuiDaoFindException e) {
			throw new PuiDaoDeleteException(e);
		}

		try {
			deleteTranslations(Collections.singletonList(dtoPk));
			dtoPk = doDelete(Collections.singletonList(dtoPk)).get(0);
			afterDelete(Collections.singletonList(dtoPk));
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnDeleteException(e);
		} catch (Exception e) {
			throw new PuiDaoDeleteException(new PuiDaoDataAccessException(e));
		}

		return dtoPk;
	}

	@Override
	public List<TPK> bulkDelete(List<TPK> dtoPkList) throws PuiDaoDeleteException {
		if (dtoPkList == null) {
			return Collections.emptyList();
		}

		try {
			deleteTranslations(dtoPkList);
			dtoPkList = doDelete(dtoPkList);
			afterDelete(dtoPkList);
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnDeleteException(e);
		} catch (Exception e) {
			throw new PuiDaoDeleteException(new PuiDaoDataAccessException(e));
		}

		return dtoPkList;
	}

	/**
	 * Deletes the given DTO from the language table
	 * 
	 * @param dtoPkList The DTO PK list to be deleted
	 * @throws PuiDaoDeleteException If any SQL error while executing the statement
	 *                               is thrown
	 */
	private void deleteTranslations(List<TPK> dtoPkList) throws PuiDaoDeleteException {
		if (getTableTranslationDao() == null) {
			return;
		}

		Class<ITableDto> translationClass = getTableTranslationDao().getDtoPkClass();
		try {
			FilterBuilder filter = FilterBuilder.newOrFilter();
			for (TPK dtoPk : dtoPkList) {
				ITableDto translation = translationClass.newInstance();
				PuiObjectUtils.copyProperties(translation, dtoPk);
				FilterBuilder pkFilter = FilterBuilder.newAndFilter();

				DtoRegistry.getPkFields(getDtoPkClass()).forEach(pkField -> {
					try {
						Object value = FieldUtils.readField(
								DtoRegistry.getJavaFieldFromFieldName(getDtoPkClass(), pkField), dtoPk, true);
						pkFilter.addEquals(pkField, value);
					} catch (IllegalAccessException e) {
						// do nothing
					}
				});

				filter.addGroup(pkFilter);
			}

			getTableTranslationDao().deleteWhere(filter);
		} catch (InstantiationException | IllegalAccessException e) {
		}
	}

	/**
	 * Perform the delete in the table of the specified DTO
	 * 
	 * @param dtoPkList The PK of the DTO to be deleted
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	private List<TPK> doDelete(List<TPK> dtoPkList) throws DataAccessException {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		StringBuilder query = new StringBuilder(DELETE_FROM);
		query.append(getEntityName());
		query.append(WHERE);

		for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
			String columnName = it.next();
			query.append(columnName + " = ?");
			if (it.hasNext()) {
				query.append(AND);
			}
		}

		performDelete(dtoPkList, query.toString());

		return dtoPkList;
	}

	/**
	 * Really performs the Delete into the database
	 * 
	 * @param dtoPkList The DTO list to be deleted
	 * @param sql       The query to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void performDelete(List<TPK> dtoPkList, String sql) throws DataAccessException {
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		if (dtoPkList.size() == 1) {
			// when the list size of DTO is only 1 element...
			List<Object> values = new ArrayList<>();
			for (String pkColumnName : pkColumnNames) {
				try {
					Field field = mapPk.get(pkColumnName);
					Object value = FieldUtils.readField(field, dtoPkList.get(0), true);
					values.add(value);
				} catch (Exception e) {
					// do nothing
				}
			}

			jdbcTemplate.update(sql, values.toArray());
		} else {
			// when the list size of DTO is more than 1 element...
			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					TPK dto = dtoPkList.get(i);
					int nextParameter = 1;
					for (String pkColumnName : pkColumnNames) {
						try {
							Field field = mapPk.get(pkColumnName);
							Object value = FieldUtils.readField(field, dto, true);
							ps.setObject(nextParameter++, value);
						} catch (Exception e) {
							// do nothing
						}
					}
				}

				@Override
				public int getBatchSize() {
					return dtoPkList.size();
				}
			});
		}
	}

	/**
	 * Perform some actions after deleting the DTO. By default, the
	 * {@link DeleteDaoEvent} will be fired, and all the associated
	 * {@link PuiListener} will be executed
	 * 
	 * @param dtoPkList The deleted DTO list
	 */
	protected void afterDelete(List<TPK> dtoPkList) {
		for (TPK dtoPk : dtoPkList) {
			DeleteDaoEvent eventType = new DeleteDaoEvent(dtoPk);
			threadDaoEvents.addEventType(eventType);
		}
	}

	@Override
	public void deleteAll() throws PuiDaoDeleteException {
		deleteWhere((FilterBuilder) null);
	}

	@Override
	public void deleteAll(PuiLanguage language) throws PuiDaoDeleteException {
		deleteWhere(FilterBuilder.newAndFilter().addEquals(IDto.LANG_COLUMN_NAME, language.getIsocode()));
	}

	@Override
	public void deleteWhere(FilterBuilder filterBuilder) throws PuiDaoDeleteException {
		StringBuilder query = new StringBuilder(DELETE_FROM);
		query.append(getEntityName());
		if (filterBuilder != null) {
			String where = dbHelper.processFilters(getDtoClass(), filterBuilder.asFilterGroup(), false);
			if (!StringUtils.isEmpty(where)) {
				query.append(WHERE + where);
			}
		}

		performDeleteWhere(query.toString());
	}

	/**
	 * Really performs the Delete into the database
	 * 
	 * @param sql The query to be executed
	 * @throws DataAccessException If any SQL error while executing the statement is
	 *                             thrown
	 */
	protected void performDeleteWhere(String sql) throws PuiDaoDeleteException {
		try {
			jdbcTemplate.update(sql);
		} catch (DataIntegrityViolationException e) {
			throw new PuiDaoIntegrityOnDeleteException(e);
		} catch (Exception e) {
			throw new PuiDaoDeleteException(new PuiDaoDataAccessException(e));
		}
	}

	/**
	 * Check the values of the given DTO. By default, null values and maximum length
	 * values are checked
	 * 
	 * @param dto The DTO to be checked
	 * @throws PuiDaoNullParametersException  If an attribute is set to null and its
	 *                                        value is mandatory
	 * @throws PuiDaoAttributeLengthException If an attribute length is higher that
	 *                                        the indicated in the
	 *                                        {@link PuiField#maxlength()} attribute
	 */
	protected void checkValues(ITableDto dto) throws PuiDaoNullParametersException, PuiDaoAttributeLengthException {
		Map<String, Field> map = new HashMap<>();
		map.putAll(DtoRegistry.getMapFieldsFromFieldName(dto.getClass()));
		map.putAll(DtoRegistry.getLangMapFieldsFromFieldName(dto.getClass()));

		// Not null values
		List<String> notNullFieldNames = DtoRegistry.getNotNullFields(dto.getClass());
		for (String fieldName : notNullFieldNames) {
			if (DtoRegistry.getSequenceFields(dto.getClass()).contains(fieldName)) {
				continue;
			}

			try {
				Field field = map.get(fieldName);
				Object value = FieldUtils.readField(field, dto, true);

				if (value == null) {
					throw new PuiDaoNullParametersException(fieldName);
				}

				if (DtoRegistry.getStringFields(dto.getClass()).contains(fieldName) && StringUtils.isEmpty(value)) {
					throw new PuiDaoNullParametersException(fieldName);
				}
			} catch (PuiDaoNullParametersException e) {
				throw e;
			} catch (Exception e) {
				// do nothing
			}
		}

		// Max length
		Map<String, Integer> maxLengthFieldNames = DtoRegistry.getFieldNamesMaxLength(dto.getClass());
		for (Entry<String, Integer> entry : maxLengthFieldNames.entrySet()) {
			try {
				String fieldName = entry.getKey();
				Integer maxLength = entry.getValue();
				if (maxLength.equals(-1)) {
					continue;
				}
				Field field = map.get(fieldName);
				if (!field.getType().equals(String.class)) {
					continue;
				}
				String value = (String) FieldUtils.readField(field, dto, true);
				if (value != null && value.length() > maxLength) {
					throw new PuiDaoAttributeLengthException(fieldName, maxLength, value.length());
				}
			} catch (PuiDaoAttributeLengthException e) {
				throw e;
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	/**
	 * Get the DAO corresponding to the translation table of this DAO
	 * 
	 * @return The translation DAO
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ITableDao<ITableDto, ITableDto> getTableTranslationDao() {
		if (tableTranslationDao == null) {
			String tableLangName = daoRegistry.getTableLangName(this);
			if (tableLangName != null) {
				Class<? extends IDao> daoLangClass = daoRegistry.getDaoFromEntityName(tableLangName, false);
				if (daoLangClass == null) {
					tableTranslationDao = null;
				} else {
					tableTranslationDao = (ITableDao<ITableDto, ITableDto>) PuiApplicationContext.getInstance()
							.getBean(daoLangClass);
				}
			}
		}

		return tableTranslationDao;
	}

	/**
	 * Get all the languages available in the application.
	 * 
	 * @return The list of all the available languages
	 */
	private List<PuiLanguage> getAllLanguages() {
		List<PuiLanguage> list = new ArrayList<>();

		for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
			list.add(it.next());
		}

		return list;
	}

	@Override
	protected final String addTranslationJoins() {
		StringBuilder query = new StringBuilder();
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		query.append(" LEFT JOIN " + daoRegistry.getTableLangName(this) + " " + SqlUtils.TABLE_LANG_PREFIX + " ON ");
		for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
			String columnName = it.next();
			query.append(
					SqlUtils.TABLE_PREFIX + "." + columnName + " = " + SqlUtils.TABLE_LANG_PREFIX + "." + columnName);
			if (it.hasNext()) {
				query.append(AND);
			}
		}

		return query.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<TPK> getDtoPkClass() {
		if (ITableDto.class.isAssignableFrom(dtoClass)) {
			// is a table
			return (Class<TPK>) dtoClass.getSuperclass();
		} else {
			// is a view
			return (Class<TPK>) dtoClass;
		}
	}

}