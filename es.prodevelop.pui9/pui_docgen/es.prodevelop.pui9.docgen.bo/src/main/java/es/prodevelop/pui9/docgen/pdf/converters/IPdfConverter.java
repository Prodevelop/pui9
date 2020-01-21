package es.prodevelop.pui9.docgen.pdf.converters;

import java.io.File;
import java.io.InputStream;

/**
 * An interface for PDF Converters. All the subclasses must register itself to
 * the {@link PdfConverterRegistry}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IPdfConverter {

	/**
	 * Get the File Extension that this converter supports
	 */
	String getFileExtension();

	/**
	 * Converts from a File
	 */
	InputStream convert(File file) throws Exception;

	/**
	 * Converts from an InputStream
	 */
	InputStream convert(InputStream inputStream) throws Exception;

	/**
	 * Converts from a ByteArray
	 */
	InputStream convert(byte[] content) throws Exception;

}
