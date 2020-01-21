package es.prodevelop.pui9.importexport;

import java.io.File;
import java.io.FileFilter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.PuiImportexportPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiImportexportService;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

/**
 * This component has a Timer to periodically check ImportExport table and
 * Files. If there exists any File that is not pointed by any ImportExport, the
 * File will be automatically deleted. Furthermore, if there exist any
 * ImportExport that points to a 'phantom' File, the ImportExport will be
 * deleted too.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class CleanImportExport {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IPuiImportexportService importExportService;

	@PostConstruct
	private void postConstruct() {
		Duration initDelay = Duration.between(LocalDateTime.now(),
				LocalDate.now().plusDays(1).atTime(LocalTime.of(3, 0)));
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("CleanImportExport", true, initDelay.toMinutes(),
				TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES, () -> {
					if (!isEnabled()) {
						return;
					}
					try {
						checkFiles();
						checkDatabase();
					} catch (Exception e) {
						// do nothing
					}
				});
	}

	private boolean isEnabled() {
		return docPathExists() && docPathHasFiles();
	}

	private boolean docPathExists() {
		String basePath = importExportService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);
		return baseFolder.exists();
	}

	private boolean docPathHasFiles() {
		String basePath = importExportService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);
		String[] files = baseFolder.list();
		return files != null && files.length > 0;
	}

	/**
	 * Check every Folder and it's files, and if doesn't exist a
	 */
	private void checkFiles() throws Exception {
		String basePath = importExportService.getBaseDocumentsPath();
		File baseFolder = new File(basePath);

		for (File modelFolder : baseFolder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
			String model = modelFolder.getName();
			for (File pkFolder : modelFolder.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY)) {
				IPuiImportexportPk pk = new PuiImportexportPk(Integer.valueOf(pkFolder.getName()));
				if (!importExportService.exists(pk)) {
					logger.info(
							"ImportExport cleaner: delete file for model '" + model + "' and ID '" + pk.getId() + "'");
					FileUtils.deleteDirectory(pkFolder);
				}
			}

			if (modelFolder.list().length == 0) {
				logger.info("ImportExport cleaner: delete folder from filesystem for model '" + model + "'");
				FileUtils.deleteDirectory(modelFolder);
			}
		}
	}

	/**
	 * Check every Document, and if the referenced file doesn't exist, then delete
	 * the Document from the database
	 */
	private void checkDatabase() throws PuiServiceGetException, PuiServiceDeleteException {
		for (IPuiImportexport importExport : importExportService.getAll()) {
			String importFolder = importExportService.getImportFolder(importExport.getModel(), importExport.getId());
			if (!new File(importFolder).exists()) {
				logger.info("ImportExport cleaner: delete importExport for model '" + importExport.getModel()
						+ "', ID '" + importExport.getId() + "', importTime '" + importExport.getDatetime()
						+ "' and user '" + importExport.getUsr() + "'");
				importExportService.delete(importExport);
			}
		}
	}

}
