package es.prodevelop.pui9.model.generator.layers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.model.generator.DynamicClassGenerator;
import es.prodevelop.pui9.model.generator.TemplateFileInfo;

/**
 * This class represents the Client Layer Generator, to generate all the files
 * for the PUI Client
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class ClientLayerGenerator extends AbstractLayerGenerator {

	@Override
	public List<TemplateFileInfo> getTemplateList(boolean isView) {
		List<TemplateFileInfo> list = new ArrayList<>();

		TemplateFileInfo langJs = new TemplateFileInfo("client/LanguageJS.ftl",
				DynamicClassGenerator.resourceWildcard + ".lang.js", null);
		langJs.setIsJava(false);
		list.add(langJs);

		TemplateFileInfo model = new TemplateFileInfo("client/ModelJS.ftl",
				DynamicClassGenerator.resourceWildcard + "Model.js", null);
		model.setIsJava(false);
		list.add(model);

		TemplateFileInfo js = new TemplateFileInfo("client/JS.ftl", DynamicClassGenerator.resourceWildcard + ".js",
				null);
		js.setIsJava(false);
		list.add(js);

		TemplateFileInfo html = new TemplateFileInfo("client/HTML.ftl",
				DynamicClassGenerator.resourceWildcard + "Form.html", null);
		html.setIsJava(false);
		list.add(html);

		return list;
	}

	@Override
	protected final Object registerBean(Class<?> clazz) {
		return null;
	}

	@Override
	protected final List<Class<?>> getClassesToRegister(List<TemplateFileInfo> templateList) throws Exception {
		List<Class<?>> classes = new ArrayList<>();
		return classes;
	}

}
