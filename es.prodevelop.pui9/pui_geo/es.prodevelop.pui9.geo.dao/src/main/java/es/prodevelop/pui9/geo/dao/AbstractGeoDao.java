package es.prodevelop.pui9.geo.dao;

import java.lang.reflect.Field;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiVariableDao;
import es.prodevelop.pui9.common.model.dto.PuiVariablePk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.geo.dao.helpers.IDatabaseGeoHelper;
import es.prodevelop.pui9.geo.dao.helpers.SqlPreparedStatementSetter;
import es.prodevelop.pui9.geo.dao.interfaces.IGeoDao;
import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;
import es.prodevelop.pui9.model.dao.AbstractTableDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;

public abstract class AbstractGeoDao<TPK extends IGeoDto, T extends TPK> extends AbstractTableDao<TPK, T>
		implements IGeoDao<TPK, T> {

	private static final String SRID_DEFAULT_VARIABLE = "SRID_DEFAULT";

	@Autowired
	private IPuiVariableDao variableDao;

	@Autowired
	protected SqlPreparedStatementSetter<TPK, T> preparedStatementSetter;

	@Autowired
	private IDatabaseGeoHelper sqlAdapter;

	private Integer srid;

	/**
	 * SRID is obtained in lazy mode only the first time requested
	 */
	private Integer getSrid() {
		if (srid == null) {
			String tableName = daoRegistry.getEntityName(this);
			srid = getSridFromDatabase(tableName, true);
		}

		return srid;
	}

	protected Integer getSridFromDatabase(String tableName, boolean force) {
		if (srid == null || force) {
			try {
				String sql = "";
				TPK pk = getDtoPkClass().newInstance();

				sql = getSqlAdapter().getSridSql(tableName, pk.getGeometryFieldName());

				try {
					srid = jdbcTemplate.queryForObject(sql, Integer.class);
				} catch (DataAccessException e) {
					// no srid retrieved with the simply query. Let's do the
					// hard one... ;)
					try {
						// check if a default SRID variable is available on
						// CfgVariable table
						IPuiVariable var = variableDao.findOne(new PuiVariablePk(SRID_DEFAULT_VARIABLE));
						String sridAttrVal = var.getValue();
						Assert.notNull(sridAttrVal, "SRID value could not be null");
						srid = Integer.parseInt(sridAttrVal.trim());
					} catch (IllegalArgumentException | PuiDaoFindException e2) {
						// Ups... it doesn't exists? Does the table exists in
						// the database?
						DatabaseMetaData dmd = jdbcTemplate.getDataSource().getConnection().getMetaData();
						ResultSet rs = dmd.getTables(null, null, tableName, null);
						if (rs.next()) {
							// if the table exists, please indicate the srid
							// anywhere!!! :'(
							throw new IllegalArgumentException("Could not get the SRID for table '" + tableName
									+ "'. Please, include 'SRID_DEFAULT' variable to CfgVariable table with the default value");
						} else {
							// if not, consider to remove the Java classes,
							// please...
							logger.error(getClass().getSimpleName() + ": The table '" + tableName
									+ "' doesn't exist. Consider to remove the Java classes");
						}
					}
				}

				return srid;
			} catch (InstantiationException | IllegalAccessException | SQLException e) {
				throw new IllegalArgumentException("Not available SRID value for '" + tableName
						+ "' table. Please, include 'SRID_DEFAULT' variable to CfgVariable table with the default value");
			}
		} else {
			return srid;
		}
	}

	@Override
	public IDatabaseGeoHelper getSqlAdapter() {
		return sqlAdapter;
	}

	@Override
	protected final Object modifyInsertColumnValue(T dto, String columnName, Object value) {
		return modifyColumnValue(dto, columnName, value);
	}

	@Override
	protected final Object modifyUpdateColumnValue(T dto, String columnName, Object value) {
		return modifyColumnValue(dto, columnName, value);
	}

	private Object modifyColumnValue(T dto, String columnName, Object value) {
		if (columnName.equals(dto.getGeometryFieldName()) && value instanceof String) {
			return sqlAdapter.modifyColumnValue(getSrid(), dto, columnName, (String) value);
		} else {
			return value;
		}
	}

	protected final Object modifyInsertColumnValue(String tableName, T dto, String columnName, String value) {
		return modifyColumnValue(tableName, dto, columnName, value);
	}

	public Object modifyUpdateColumnValue(String tableName, T dto, String columnName, String value) {
		return modifyColumnValue(tableName, dto, columnName, value);
	}

	private String modifyColumnValue(String tableName, T dto, String columnName, String value) {
		if (columnName.equals(dto.getGeometryFieldName()) && value instanceof String) {
			Integer srid = getSridFromDatabase(tableName, false);
			return getSqlAdapter().modifyColumnValue(srid, dto, columnName, value);
		}

		return value;
	}

	@Override
	public Integer getSrid(String tableName) {
		return getSrid();
	}

	@Override
	protected void customizeDto(T dto) {
		try {
			String tableName = daoRegistry.getEntityName(this);
			doFillGeometryValue(dto, tableName);
		} catch (IllegalAccessException e) {
		}
	}

	protected void doFillGeometryValue(T dto, String tableName) throws IllegalAccessException {
		TPK dtoPk = dto.createPk();
		String geometryColumn = dto.getGeometryFieldName();
		StringBuilder sql = new StringBuilder();
		List<Object> values = new ArrayList<>();

		sql.append("SELECT ");

		sql.append(getSqlAdapter().fillGeometryValue(dtoPk));

		sql.append(" FROM " + tableName);
		sql.append(" WHERE ");
		for (Iterator<String> it = DtoRegistry.getColumnNames(dtoPk.getClass()).iterator(); it.hasNext();) {
			String next = it.next();
			sql.append(next + " = ?");
			try {
				Field field = DtoRegistry.getJavaFieldFromColumnName(dtoPk.getClass(), next);
				values.add(field.get(dtoPk));
			} catch (Exception e) {
				continue;
			}
			if (it.hasNext()) {
				sql.append(" AND ");
			}
		}

		String value = jdbcTemplate.queryForObject(sql.toString(), values.toArray(), String.class);
		Field geometryField = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), geometryColumn);
		FieldUtils.writeField(geometryField, dto, value, true);
	}

	@Override
	protected void doInsert(List<T> dtoList) throws DataAccessException {
		if (CollectionUtils.isEmpty(dtoList)) {
			return;
		}

		if (dtoList.size() > 1) {
			super.doInsert(dtoList);
			return;
		}

		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(getDtoClass()));
		List<String> sequenceColumns = DtoRegistry.getSequenceFields(getDtoClass());
		columnNames.removeAll(sequenceColumns);

		String tableName = daoRegistry.getEntityName(this);
		Map<String, Field> map = DtoRegistry.getMapFieldsFromColumnName(getDtoClass());

		getSridFromDatabase(tableName, false);

		if (getSqlAdapter().supportsNativeGeometry()) {
			T dto = dtoList.get(0);
			StringBuilder query = new StringBuilder("INSERT INTO ");
			query.append(tableName);
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
				String next = it.next();
				Object value;

				if (next.equals(dto.getGeometryFieldName())) {
					value = modifyInsertColumnValue(tableName, dto, next, "?");
				} else {
					value = modifyInsertColumnValue(dto, next, "?");
				}

				query.append(value);
				if (it.hasNext()) {
					query.append(", ");
				}
			}
			query.append(")");

			jdbcTemplate.update(query.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					preparedStatementSetter.prepareStatement(ps, null, dto, null, columnNames, map, null,
							getSridFromDatabase(tableName, false), getSqlAdapter(), tableName);
				}
			});

		} else {
			super.doInsert(dtoList);
		}
	}

	@Override
	protected void doUpdate(List<T> dtoList) throws DataAccessException {
		String tableName = daoRegistry.getEntityName(this);
		List<String> pkColumnNames = DtoRegistry.getColumnNames(getDtoPkClass());

		List<String> columnNames = new ArrayList<>(DtoRegistry.getColumnNames(getDtoClass()));
		List<String> sequenceColumns = DtoRegistry.getSequenceFields(getDtoClass());
		columnNames.removeAll(sequenceColumns);

		Map<String, Field> map = DtoRegistry.getMapFieldsFromColumnName(getDtoClass());
		Map<String, Field> mapPk = DtoRegistry.getMapFieldsFromColumnName(getDtoPkClass());

		getSridFromDatabase(tableName, false);

		if (getSqlAdapter().supportsNativeGeometry()) {
			T dto = dtoList.get(0);
			StringBuilder query = new StringBuilder("UPDATE ");
			query.append(tableName);
			query.append(" SET ");

			for (Iterator<String> it = columnNames.iterator(); it.hasNext();) {
				String next = it.next();
				Object value;

				if (next.equals(dto.getGeometryFieldName())) {
					value = modifyInsertColumnValue(tableName, dto, next, "?");
				} else {
					value = modifyUpdateColumnValue(dto, next, "?");
				}

				query.append(next + " = " + value);
				if (it.hasNext()) {
					query.append(", ");
				}
			}

			query.append(" WHERE ");

			for (Iterator<String> it = pkColumnNames.iterator(); it.hasNext();) {
				String next = it.next();

				query.append(next + " = ?");
				if (it.hasNext()) {
					query.append(" AND ");
				}
			}

			TPK dtoPk = dtoList.get(0).createPk();
			jdbcTemplate.update(query.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					preparedStatementSetter.prepareStatement(ps, dtoPk, dto, pkColumnNames, columnNames, map, mapPk,
							getSridFromDatabase(tableName, false), getSqlAdapter(), tableName);
				}
			});
		} else {
			super.doUpdate(dtoList);
		}
	}

}
