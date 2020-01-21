package es.prodevelop.pui9.geo.dao.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

@Component
public class SqlPreparedStatementSetter<TPK extends IGeoDto, T extends TPK> {

	private static final int GEOMETRY_DIMENSION = 2;

	private static final int NOT_FOUND_SQL_DATA_TYPE = -99999999;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	protected DaoRegistry daoRegistry;

	@Autowired
	private void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public void prepareStatement(PreparedStatement ps, TPK dtoPk, T dto, List<String> pkColumnNames,
			List<String> columnNames, Map<String, Field> map, Map<String, Field> mapPk, Integer srid,
			IDatabaseGeoHelper sqlAdapter, String tableName) {
		int position = 1;

		if (columnNames != null && dto != null) {
			for (String columnName : columnNames) {
				try {
					Field field = map.get(columnName);
					Object val = FieldUtils.readField(field, dto, true);
					if (field.getName().equals(dto.getGeometryFieldName())) {
						sqlAdapter.setGeometryValue(dto.getJtsGeometry(), GEOMETRY_DIMENSION, srid, ps, position++);
					} else {
						invokeMethod(ps, dto, field, processValue(val), position++, columnName, tableName);
					}
				} catch (Exception e) {
					continue;
				}
			}
		}

		if (pkColumnNames != null && dtoPk != null) {
			for (String pkColumnName : pkColumnNames) {
				Field field = mapPk.get(pkColumnName);
				try {
					Object value = FieldUtils.readField(field, dtoPk, true);
					invokeMethod(ps, dtoPk, field, value, position++, pkColumnName, tableName);
				} catch (Exception e1) {
					try {
						Object value = FieldUtils.readField(field, dto, true);
						invokeMethod(ps, dtoPk, field, value, position++, pkColumnName, tableName);
					} catch (Exception e2) {
						continue;
					}
				}
			}
		}
	}

	public void invokeMethod(PreparedStatement ps, TPK dto, Field field, Object val, int position, String columnName,
			String tableName) {
		try {
			Method setMethod = ps.getClass().getMethod("set" + extractType(field),
					new Class[] { int.class, extractGetMethodType(field) });
			setMethod.setAccessible(true);
			val = processValue(val);
			if (val == null) {
				int sqlDataType = getSqlDataType(columnName, tableName, dto);
				ps.setNull(position, sqlDataType);
			} else {
				setMethod.invoke(ps, position, val);
			}
		} catch (Exception e) {
		}
	}

	private int getSqlDataType(String columnName, String tableName, TPK dto) {
		int sqlDataType = NOT_FOUND_SQL_DATA_TYPE;
		try {
			sqlDataType = DtoRegistry.getColumnSqlDataType(tableName, columnName);
		} catch (Exception e) {
			registerColumnSqlDataTypes(tableName, dto.getClass());
			sqlDataType = DtoRegistry.getColumnSqlDataType(tableName, columnName);
		}

		return sqlDataType;
	}

	private Object processValue(Object val) {
		if (val == null) {
			return val;
		}

		if (val instanceof Instant) {
			val = Timestamp.from((Instant) val);
		}

		if (val instanceof Integer) {
			val = ((Integer) val).intValue();
		}

		return val;
	}

	private Class<?> extractGetMethodType(Field field) {
		Class<?> type = field.getType();
		if (type.equals(Instant.class)) {
			type = Timestamp.class;
		}

		if (type.equals(Integer.class)) {
			type = int.class;
		}

		if (type.equals(Long.class)) {
			type = long.class;
		}

		if (type.equals(Double.class)) {
			type = double.class;
		}

		if (type.equals(java.lang.Float.class)) {
			type = float.class;
		}

		return type;
	}

	private String extractType(Field field) {
		String fullType = StringUtils.capitalize(field.getType().toString());
		String[] parts = fullType.split("\\.");

		String type = parts[parts.length - 1];

		if (type.equals("Integer")) {
			type = "Int";
		}

		return type;
	}

	private void registerColumnSqlDataTypes(String tableName, Class<? extends IDto> dtoClass) {
		Connection conn = null;
		ResultSet columnsResultSet = null;
		int dataType = -99999999;

		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			columnsResultSet = conn.getMetaData().getColumns(null, null, tableName, "%");
			if (columnsResultSet.getRow() == 0) {
				columnsResultSet.close();
				columnsResultSet = conn.getMetaData().getColumns(null, null, tableName.toUpperCase(), "%");
			}

			while (columnsResultSet.next()) {
				String columnName = columnsResultSet.getString("COLUMN_NAME");
				dataType = columnsResultSet.getInt("DATA_TYPE");
				DtoRegistry.registerSqlColumnDataType(dtoClass, columnName.toLowerCase(), dataType);
				DtoRegistry.registerSqlColumnDataTypeByTableName(tableName, columnName.toLowerCase(), dataType);
			}
		} catch (Exception ignore) {
		} finally {
			try {
				columnsResultSet.close();
				conn.close();
			} catch (Exception ignore) {
			}
		}
	}

}
