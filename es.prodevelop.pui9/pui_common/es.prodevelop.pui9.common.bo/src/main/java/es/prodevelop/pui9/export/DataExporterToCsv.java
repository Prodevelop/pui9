package es.prodevelop.pui9.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.csv.CsvWriter;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.ExportType;

/**
 * This components is a utility class to export the data of an entity into a CSV
 * file
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DataExporterToCsv extends AbstractDataExporter {

	@Override
	public ExportType getExportType() {
		return ExportType.csv;
	}

	@Override
	protected InputStream doGenerate(ExportRequest req) {
		List<List<Pair<String, Object>>> data = getData(req);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CsvWriter writer = new CsvWriter(baos, ';', StandardCharsets.UTF_8);

		generateTableHeader(writer, req);
		generateTableContent(writer, req, data);

		writer.close();

		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		try {
			baos.close();
		} catch (IOException e) {
			// do nothing
		}

		return is;
	}

	private void generateTableHeader(CsvWriter writer, ExportRequest req) {
		req.getExportColumns().forEach(ec -> {
			try {
				writer.write(ec.getTitle());
			} catch (IOException e) {
				// do nothing
			}
		});
		try {
			writer.endRecord();
		} catch (IOException e) {
			// do nothing
		}
	}

	private void generateTableContent(CsvWriter writer, ExportRequest req, List<List<Pair<String, Object>>> data) {
		Class<? extends IDto> dtoClass = getDtoClass(req.getModel());

		Map<String, String> mapDateFormat = new HashMap<>();
		req.getExportColumns().forEach(ecd -> mapDateFormat.put(ecd.getName(), ecd.getDateformat()));

		data.forEach(record -> {
			record.forEach(pair -> {
				String value = null;
				if (DtoRegistry.getDateTimeFields(dtoClass).contains(pair.getKey())) {
					value = getInstantAsString(pair.getValue(), mapDateFormat.get(pair.getKey()));
				} else if (DtoRegistry.getFloatingFields(dtoClass).contains(pair.getKey())) {
					value = getString(getBigDecimal(pair.getValue()));
				} else {
					value = getString(pair.getValue());
				}
				try {
					writer.write(value);
				} catch (IOException e) {
					// do nothing
				}
			});
			try {
				writer.endRecord();
			} catch (IOException e) {
				// do nothing
			}
		});
	}

}
