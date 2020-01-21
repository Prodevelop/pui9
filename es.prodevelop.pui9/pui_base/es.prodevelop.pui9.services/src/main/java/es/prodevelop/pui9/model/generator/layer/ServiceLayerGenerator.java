package es.prodevelop.pui9.model.generator.layer;

import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.classpath.PuiDynamicClassLoader;
import es.prodevelop.pui9.model.generator.TemplateFileInfo;
import es.prodevelop.pui9.model.generator.layers.AbstractServiceLayerGenerator;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * This class represents the Service Layer Generator, to generate all the files
 * for the PUI Server.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class ServiceLayerGenerator extends AbstractServiceLayerGenerator {

	@Override
	protected List<Class<?>> getClassesToRegister(List<TemplateFileInfo> templateList) throws Exception {
		List<Class<?>> classes = new ArrayList<>();

		for (TemplateFileInfo tfi : templateList) {
			String className = (tfi.getPackageName() + "." + tfi.getFileName())
					.replace(JavaFileObject.Kind.SOURCE.extension, "");
			Class<?> clazz = PuiDynamicClassLoader.getInstance().loadClass(className);
			if (IService.class.isAssignableFrom(clazz)) {
				if (!clazz.isInterface()) {
					classes.add(clazz);
				}
			}
		}

		return classes;
	}

}
