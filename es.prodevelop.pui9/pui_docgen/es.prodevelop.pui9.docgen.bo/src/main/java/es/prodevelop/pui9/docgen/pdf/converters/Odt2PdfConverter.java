package es.prodevelop.pui9.docgen.pdf.converters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.TemplateEngineKind;

@Component
public class Odt2PdfConverter extends AbstractPdfConverter {

	@Override
	public String getFileExtension() {
		return "odt";
	}

	@Override
	public InputStream convert(byte[] content) throws Exception {
		IXDocReport report;
		try {
			report = XDocReportRegistry.getRegistry().loadReport(new ByteArrayInputStream(content),
					TemplateEngineKind.Freemarker);
			Options options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF).via(ConverterTypeVia.ODFDOM);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			report.convert(report.createContext(), options, os);

			InputStream is = new ByteArrayInputStream(os.toByteArray());
			os.close();

			return is;
		} catch (XDocReportException e) {
			throw new IOException(e.getMessage());
		}
	}

}
