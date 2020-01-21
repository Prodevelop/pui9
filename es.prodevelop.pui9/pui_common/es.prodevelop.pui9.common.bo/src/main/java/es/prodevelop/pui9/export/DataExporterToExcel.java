package es.prodevelop.pui9.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.LocaleUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.ExportType;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * This components is a utility class to export the data of an entity into an
 * Excel file
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DataExporterToExcel extends AbstractDataExporter {

	@Override
	public ExportType getExportType() {
		return ExportType.excel;
	}

	@Override
	protected InputStream doGenerate(ExportRequest req) {
		LocaleUtil.setUserTimeZone(TimeZone.getTimeZone(PuiUserSession.getCurrentSession().getTimezone()));

		List<List<Pair<String, Object>>> data = getData(req);

		Workbook workbook = new HSSFWorkbook();
		workbook.createSheet(!StringUtils.isEmpty(req.getExportTitle()) ? req.getExportTitle() : req.getModel());

		generateGeneralHeader(workbook, req);
		generateTableHeader(workbook, req);
		generateTableContent(workbook, req, data);
		adjustColumnsWidth(workbook, req);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = null;
		try {
			workbook.write(baos);
			workbook.close();
			is = new ByteArrayInputStream(baos.toByteArray());
			baos.close();
		} catch (IOException e) {
			// do nothing
		}

		LocaleUtil.resetUserTimeZone();

		return is;
	}

	/**
	 * Generates the header of the Excel, including information like:<br>
	 * <ul>
	 * <li>The name of the model (the exported entity)</li>
	 * <li>The export date</li>
	 * </ul>
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 */
	private void generateGeneralHeader(Workbook workbook, ExportRequest req) {
		CellStyle valueCellStyle = workbook.createCellStyle();
		valueCellStyle.setAlignment(HorizontalAlignment.LEFT);
		valueCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		Font valueFont = workbook.createFont();
		valueFont.setBold(true);
		valueFont.setFontHeightInPoints((short) 14);
		valueCellStyle.setFont(valueFont);

		// model name
		Row row = workbook.getSheetAt(0).createRow(0);
		row.setHeightInPoints(15);

		Cell valueCell = row.createCell(0);
		valueCell.setCellStyle(valueCellStyle);
		valueCell.setCellValue(!StringUtils.isEmpty(req.getExportTitle()) ? req.getExportTitle() : req.getModel());

		// date
		row = workbook.getSheetAt(0).createRow(1);
		row.setHeightInPoints(15);

		valueCell = row.createCell(0);
		valueCell.setCellValue(getInstantAsString(Instant.now(), null));
	}

	/**
	 * Generates the header of the table, including a column for each exportable
	 * column
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 */
	private void generateTableHeader(Workbook workbook, ExportRequest req) {
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerCellStyle.setFont(headerFont);

		int nextRow = 3;

		Row row = workbook.getSheetAt(0).createRow(nextRow);
		for (int i = 0; i < req.getExportColumns().size(); i++) {
			Cell cell = row.createCell(i);
			cell.setCellStyle(headerCellStyle);
			cell.setCellValue(req.getExportColumns().get(i).getTitle());
		}
	}

	/**
	 * Generates the content of the table with the data
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 * @param data     The list of data to be exported
	 */
	private void generateTableContent(Workbook workbook, ExportRequest req, List<List<Pair<String, Object>>> data) {
		Class<? extends IDto> dtoClass = getDtoClass(req.getModel());

		Map<String, String> mapDateFormat = new HashMap<>();
		req.getExportColumns().forEach(ecd -> mapDateFormat.put(ecd.getName(), ecd.getDateformat()));

		Map<String, CellStyle> mapDateCellStyles = new HashMap<>();

		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#"));

		CellStyle doubleStyle = workbook.createCellStyle();
		doubleStyle.setDataFormat(workbook.createDataFormat().getFormat("0#.##"));

		AtomicInteger rownum = new AtomicInteger(4);

		data.forEach(record -> {
			Row row = workbook.getSheetAt(0).createRow(rownum.getAndIncrement());
			AtomicInteger cellnum = new AtomicInteger(0);
			record.forEach(pair -> {
				Cell cell = row.createCell(cellnum.getAndIncrement());
				if (DtoRegistry.getDateTimeFields(dtoClass).contains(pair.getKey())) {
					CellStyle dateStyle;
					if (mapDateCellStyles.containsKey(mapDateFormat.get(pair.getKey()))) {
						dateStyle = mapDateCellStyles.get(mapDateFormat.get(pair.getKey()));
					} else {
						dateStyle = workbook.createCellStyle();
						dateStyle
								.setDataFormat(workbook.createDataFormat().getFormat(mapDateFormat.get(pair.getKey())));
						mapDateCellStyles.put(mapDateFormat.get(pair.getKey()), dateStyle);
					}
					cell.setCellStyle(dateStyle);
					Date value = getDate(pair.getValue());
					if (value != null) {
						cell.setCellValue(value);
					}
				} else if (DtoRegistry.getNumericFields(dtoClass).contains(pair.getKey())) {
					cell.setCellStyle(numberStyle);
					String value = getString(pair.getValue());
					if (value != null) {
						cell.setCellValue(value);
					}
				} else if (DtoRegistry.getFloatingFields(dtoClass).contains(pair.getKey())) {
					cell.setCellStyle(doubleStyle);
					BigDecimal value = getBigDecimal(pair.getValue());
					if (value != null) {
						cell.setCellValue(value.doubleValue());
					}
				} else {
					String value = getString(pair.getValue());
					if (value != null) {
						cell.setCellValue(value);
					}
				}
			});
		});
	}

	/**
	 * Adjust the width of the columns to auto
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 */
	private void adjustColumnsWidth(Workbook workbook, ExportRequest req) {
		for (int i = 0; i < req.getExportColumns().size(); i++) {
			workbook.getSheetAt(0).autoSizeColumn(i);
		}
	}

	private Date getDate(Object value) {
		if (value instanceof Instant) {
			return PuiDateUtil.instantToDate((Instant) value);
		} else {
			return null;
		}
	}

}
