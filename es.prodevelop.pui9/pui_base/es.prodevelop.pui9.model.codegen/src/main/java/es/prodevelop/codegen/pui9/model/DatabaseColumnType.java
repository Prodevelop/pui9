package es.prodevelop.codegen.pui9.model;

import java.sql.Types;

public enum DatabaseColumnType {

	ARRAY(Types.ARRAY),

	CHAR(Types.CHAR),

	NCHAR(Types.NCHAR),

	VARCHAR(Types.VARCHAR),

	NVARCHAR(Types.NVARCHAR),

	LONGVARCHAR(Types.LONGVARCHAR),

	CLOB(Types.CLOB),

	BLOB(Types.BLOB),

	BIT(Types.BIT),

	BOOLEAN(Types.BOOLEAN),

	TINYINT(Types.TINYINT),

	SMALLINT(Types.SMALLINT),

	INTEGER(Types.INTEGER),

	BIGINT(Types.BIGINT),

	NUMERIC(Types.NUMERIC),

	DECIMAL(Types.DECIMAL),

	REAL(Types.REAL),

	DOUBLE(Types.DOUBLE),

	FLOAT(Types.FLOAT),

	DATE(Types.DATE),

	TIME(Types.TIME),

	TIMESTAMP(Types.TIMESTAMP),

	TIMESTAMP_LOCAL_TIMEZONE(-101),

	TIMESTAMP_TIMEZONE(-102),

	STRUCT(Types.STRUCT),

	VARBINARY(Types.VARBINARY),
	
	DISTINCT(Types.DISTINCT),

	OTHER(Types.OTHER);

	public final int typeId;

	private DatabaseColumnType(int typeId) {
		this.typeId = typeId;
	}

	public static DatabaseColumnType getByTypeId(int typeId) {
		for (DatabaseColumnType dct : values()) {
			if (dct.typeId == typeId) {
				return dct;
			}
		}
		return null;
	}

}
