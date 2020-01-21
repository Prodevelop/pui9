package es.prodevelop.pui9.docgen.pdf.converters;

import java.io.File;
import java.io.InputStream;

import org.springframework.util.FileCopyUtils;

public abstract class AbstractPdfConverter implements IPdfConverter {

	protected AbstractPdfConverter() {
		PdfConverterRegistry.getSingleton().registerPdfConverter(this);
	}

	@Override
	public InputStream convert(File file) throws Exception {
		return convert(FileCopyUtils.copyToByteArray(file));
	}

	@Override
	public InputStream convert(InputStream inputStream) throws Exception {
		return convert(FileCopyUtils.copyToByteArray(inputStream));
	}

}
