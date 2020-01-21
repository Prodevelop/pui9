package es.prodevelop.pui9.launchers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Connection;
import java.util.Collection;

public interface IReportLauncher {

	/**
	 * Launch the Report using one report element size (details)
	 */
	void launch() throws Exception;

	/**
	 * Launch the Report using the specified number of element size (details)
	 */
	void launch(int reportElementSize) throws Exception;

	/**
	 * Launch the Report using a Database Connection
	 */
	void launch(Connection connection) throws Exception;

	/**
	 * Launch the Report using a Collection
	 */
	void launch(Collection<?> collection) throws Exception;

	/**
	 * Get the resulting Report as Byte Array Output Stream
	 */
	ByteArrayOutputStream getResultAsOutputStream();

	/**
	 * Get the resulting Report as File
	 */
	File getResultAsFile(String folder, boolean includeTimeMillis) throws Exception;
}
