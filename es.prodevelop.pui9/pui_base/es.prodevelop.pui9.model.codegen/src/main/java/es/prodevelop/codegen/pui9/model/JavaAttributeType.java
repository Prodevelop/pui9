package es.prodevelop.codegen.pui9.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public enum JavaAttributeType {

	BOOLEAN(Boolean.class),

	CHARACTER(Character.class),

	BYTE(Byte.class),

	STRING(String.class),

	INTEGER(Integer.class),

	LONG(Long.class),

	SHORT(Short.class),

	BIGDECIMAL(BigDecimal.class),

	DATETIME(Instant.class),

	LIST(List.class);

	public Class<?> clazz;

	private JavaAttributeType(Class<?> clazz) {
		this.clazz = clazz;
	}

}
