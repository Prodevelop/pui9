package es.prodevelop.pui9.export;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

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
public class DataExporterToPdf extends AbstractDataExporter {

	@Override
	public ExportType getExportType() {
		return ExportType.pdf;
	}

	@Override
	protected InputStream doGenerate(ExportRequest req) {
		List<List<Pair<String, Object>>> data = getData(req);

		Document document = new Document(PageSize.A4.rotate());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PdfWriter writer;
		try {
			writer = PdfWriter.getInstance(document, baos);
			writer.setPageEvent(new FooterPageEvent());
		} catch (DocumentException e) {
			return null;
		}

		document.open();

		generateGeneralHeader(document, req);
		generateTable(document, req, data);

		document.close();

		InputStream is = null;
		try {
			writer.close();
			is = new ByteArrayInputStream(baos.toByteArray());
			baos.close();
		} catch (IOException e) {
			// do nothing
		}

		return is;
	}

	/**
	 * Generates the header of the PDF, including information like:<br>
	 * <ul>
	 * <li>The name of the model (the exported entity)</li>
	 * <li>The export date</li>
	 * </ul>
	 * 
	 * @param document The document to be used in the export
	 * @param req      The request of the export
	 */
	private void generateGeneralHeader(Document document, ExportRequest req) {
		PdfPTable table = new PdfPTable(1);
		table.setWidthPercentage(100);
		table.setSpacingBefore(5f);
		table.setSpacingAfter(5f);

		Paragraph text = new Paragraph(
				!StringUtils.isEmpty(req.getExportTitle()) ? req.getExportTitle() : req.getModel(),
				FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD));
		PdfPCell cell = new PdfPCell(text);
		cell.setBorderWidth(0f);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		text = new Paragraph(getInstantAsString(Instant.now(), null),
				FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD));
		cell = new PdfPCell(text);
		cell.setBorderWidth(0f);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		try {
			document.add(table);
		} catch (DocumentException e) {
			// do nothing
		}
	}

	/**
	 * Generates the header of the table, including a column for each exportable
	 * column
	 * 
	 * @param workbook The workbook to be used in the export
	 * @param req      The request of the export
	 */
	private void generateTable(Document document, ExportRequest req, List<List<Pair<String, Object>>> data) {
		Class<? extends IDto> dtoClass = getDtoClass(req.getModel());

		Map<String, String> mapDateFormat = new HashMap<>();
		req.getExportColumns().forEach(ecd -> mapDateFormat.put(ecd.getName(), ecd.getDateformat()));

		PdfPTable table = new PdfPTable(req.getExportColumns().size());
		table.setWidthPercentage(100);
		table.setSpacingBefore(5f);
		table.setSpacingAfter(5f);

		Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD);
		req.getExportColumns().forEach(ecd -> {
			Paragraph text = new Paragraph(ecd.getTitle(), headerFont);
			PdfPCell cell = new PdfPCell(text);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			table.addCell(cell);
		});

		Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL);
		data.forEach(record -> record.forEach(pair -> {
			String value = null;
			if (DtoRegistry.getDateTimeFields(dtoClass).contains(pair.getKey())) {
				value = getInstantAsString(pair.getValue(), mapDateFormat.get(pair.getKey()));
				if (value == null) {
					value = "";
				}
			} else if (DtoRegistry.getFloatingFields(dtoClass).contains(pair.getKey())) {
				value = getString(getBigDecimal(pair.getValue()));
			} else {
				value = getString(pair.getValue());
			}

			Paragraph text = new Paragraph(value, cellFont);
			PdfPCell cell = new PdfPCell(text);
			table.addCell(cell);
		}));

		try {
			document.add(table);
		} catch (DocumentException e) {
			// do nothing
		}
	}

	private class FooterPageEvent extends PdfPageEventHelper {

		private PdfTemplate template;
		private Image total;

		@Override
		public void onOpenDocument(PdfWriter writer, Document document) {
			template = writer.getDirectContent().createTemplate(30, 16);
			try {
				total = Image.getInstance(template);
				total.setRole(PdfName.ARTIFACT);
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			addFooter(writer);
		}

		private void addFooter(PdfWriter writer) {
			PdfPTable footer = new PdfPTable(2);
			try {
				// set defaults
				footer.setWidths(new int[] { 24, 2 });
				footer.setTotalWidth(PageSize.A4.rotate().getWidth() - 70);
				footer.setLockedWidth(true);
				footer.getDefaultCell().setFixedHeight(40);
				footer.getDefaultCell().setBorder(Rectangle.TOP);
				footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

				// add current page count
				footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
				footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()),
						new Font(Font.FontFamily.HELVETICA, 8)));

				// add placeholder for total page count
				PdfPCell totalPageCount = new PdfPCell(total);
				totalPageCount.setBorder(Rectangle.TOP);
				totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
				footer.addCell(totalPageCount);

				// write page
				PdfContentByte canvas = writer.getDirectContent();
				canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
				footer.writeSelectedRows(0, -1, 34, 30, canvas);
				canvas.endMarkedContentSequence();
			} catch (DocumentException de) {
				throw new ExceptionConverter(de);
			}
		}

		@Override
		public void onCloseDocument(PdfWriter writer, Document document) {
			int totalLength = String.valueOf(writer.getPageNumber()).length();
			int totalWidth = totalLength * 5;
			ColumnText.showTextAligned(template, Element.ALIGN_RIGHT,
					new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)),
					totalWidth, 6, 0);
		}

	}

}
