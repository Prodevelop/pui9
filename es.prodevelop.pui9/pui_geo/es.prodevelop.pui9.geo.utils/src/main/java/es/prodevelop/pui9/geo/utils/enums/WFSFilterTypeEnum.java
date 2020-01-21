package es.prodevelop.pui9.geo.utils.enums;

/**
 * Enumeración de todos los tipos de filtros que se pueden aplicar con WFSFilter
 * 
 * @author aromeu
 * 
 */
public enum WFSFilterTypeEnum {

	bbox("bbox"),

	beyond("beyond"),

	contains("contains"),

	crosses("crosses"),

	disjoint("disjoint"),

	dwithin("dwithin"),

	equal("equal"),

	intersects("intersects"),

	overlaps("overlaps"),

	touches("touches"),

	within("within");

	private String nombre;

	private WFSFilterTypeEnum(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

}
