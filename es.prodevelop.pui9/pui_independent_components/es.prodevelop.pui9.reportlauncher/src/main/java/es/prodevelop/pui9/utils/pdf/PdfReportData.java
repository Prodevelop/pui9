package es.prodevelop.pui9.utils.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import es.prodevelop.pui9.utils.AbstractReportData;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class PdfReportData extends AbstractReportData {

	private Map<String, Object> parameters;

	public PdfReportData() {
		this(null, null);
	}

	public PdfReportData(String reportPath, String reportName) {
		this(reportPath, reportName, null);
	}

	public PdfReportData(String reportPath, String reportName, Map<String, Object> parameters) {
		super(reportPath, reportName);
		if (parameters == null) {
			parameters = new HashMap<>();
		}
		this.parameters = parameters;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	@SuppressWarnings("unchecked")
	public <T> T getParameter(String key) {
		return (T) parameters.get(key);
	}

	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}

	public void addString(String key, String value) {
		addParameter(key, value);
	}

	public void addInteger(String key, Integer value) {
		addParameter(key, value);
	}

	public void addDouble(String key, Double value) {
		addParameter(key, value);
	}

	public void addBigDecimal(String key, BigDecimal value) {
		addParameter(key, value);
	}

	public void addBoolean(String key, Boolean value) {
		addParameter(key, value);
	}

	public void addDate(String key, Instant value) {
		addParameter(key, value);
	}

	public void addList(String key, List<?> value) {
		addParameter(key, value);
	}

	public void addCollection(String key, Collection<?> value) {
		addParameter(key, value);
	}

	public void addImage(String key, BufferedImage value) {
		addParameter(key, value);
	}

	public void addQrImage(String key, String value) {
		BufferedImage bi = generateQRCode(value);
		addImage(key, bi);
	}

	public BufferedImage generateQRCode(String value) {
		try {
			File file = QRCode.from(value).to(ImageType.PNG).withSize(250, 250).file();
			BufferedImage bi = ImageIO.read(file);
			return bi;
		} catch (IOException e) {
			return null;
		}
	}
}
