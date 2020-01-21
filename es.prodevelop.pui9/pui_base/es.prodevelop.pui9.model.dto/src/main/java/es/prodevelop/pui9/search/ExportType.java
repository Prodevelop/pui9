package es.prodevelop.pui9.search;

/**
 * The allowed Grid Exporting types
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public enum ExportType {

	excel("xls"),

	csv("csv"),

	pdf("pdf");

	public String extension;

	private ExportType(String extension) {
		this.extension = extension;
	}

}