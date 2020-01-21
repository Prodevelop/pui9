package es.prodevelop.pui9.export;

import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.ExportType;

/**
 * This interface is intended to be implemented by all the data exporters
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IDataExporter {

	/**
	 * Get the export type of this data exporter
	 * 
	 * @return The export type of this data exporter
	 */
	ExportType getExportType();

	/**
	 * Generate the file using the data providen in the given request
	 * 
	 * @param req The request of the export
	 * @return The generated file represented by a {@link FileDownload}
	 */
	FileDownload generate(ExportRequest req);

}
