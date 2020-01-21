package es.prodevelop.pui9.docgen.enums;

public enum MappingOriginEnum {

	VIEW("V", "Columnas de la vista"),

	SYSTEM("S", "Valores del sistema"),

	TABLE("T", "Definido en tablas del sistema"),

	USER("U", "Definido por el usuario");

	private String code;
	private String description;

	private MappingOriginEnum(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
