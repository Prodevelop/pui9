package es.prodevelop.pui9.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiImportexportDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.common.service.interfaces.IPuiImportexportService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.service.AbstractService;

/**
 * @generated
 */
@Service
public class PuiImportexportService
		extends AbstractService<IPuiImportexportPk, IPuiImportexport, INullView, IPuiImportexportDao, INullViewDao>
		implements IPuiImportexportService {

	@Autowired
	private IPuiVariableService variableService;

	@Override
	public String getBaseDocumentsPath() {
		String basePath = variableService.getVariable(PuiVariableValues.IMPORTEXPORT_PATH.name());
		if (!basePath.endsWith(File.separator)) {
			basePath += File.separator;
		}

		String tagStart = "[$][{]";
		String tagEnd = "[}]";
		String tagRegex = tagStart + "([^{]*)" + tagEnd;

		List<String> allMatches = new ArrayList<>();
		Matcher m = Pattern.compile(tagRegex).matcher(basePath);
		while (m.find()) {
			allMatches.add(m.group());
		}

		String path = basePath;
		for (String match : allMatches) {
			String prop = match.replaceAll(tagStart, "").replaceAll(tagEnd, "");
			String propVal = System.getProperty(prop);
			if (propVal != null) {
				path = path.replace(match, propVal);
			}
		}

		return path;
	}

	@Override
	public String getImportFolder(String model, Integer id) {
		String importFolder = getBaseDocumentsPath();
		importFolder += model + File.separator + id + File.separator;
		File folders = new File(importFolder);
		folders.mkdirs();

		return importFolder;
	}

}
