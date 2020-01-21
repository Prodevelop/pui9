package es.prodevelop.pui9.export;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.search.ExportType;

/**
 * A registry with the available data exporters
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DataExporterRegistry {

	private static DataExporterRegistry singleton;

	public static DataExporterRegistry getSingleton() {
		if (singleton == null) {
			singleton = new DataExporterRegistry();
		}
		return singleton;
	}

	private Map<ExportType, IDataExporter> map;

	private DataExporterRegistry() {
		map = new HashMap<>();
	}

	/**
	 * Register a new data exporter
	 * 
	 * @param dataExporter The data exporter to be registered
	 */
	public void registerExporter(IDataExporter dataExporter) {
		if (map.containsKey(dataExporter.getExportType())) {
			throw new IllegalArgumentException(
					"There exists more than one data exporter for '" + dataExporter.getExportType() + "' type");
		}
		map.put(dataExporter.getExportType(), dataExporter);
	}

	/**
	 * Get the registered exporter for the given type
	 * 
	 * @param exportType The export type
	 * @return The data exporter associated with the given type
	 */
	public IDataExporter getExporter(ExportType exportType) {
		return map.get(exportType);
	}

}
