package es.prodevelop.pui9.model.generator.layers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.UrlResource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import es.prodevelop.pui9.classpath.PuiDynamicClassLoader;
import es.prodevelop.pui9.model.generator.DynamicClassGenerator;
import es.prodevelop.pui9.model.generator.TemplateFileInfo;

/**
 * This class represents the Controller Layer Generator, to generate all the
 * files for the PUI Server. It is abstract due to special actions are required
 * when getting the classes to be registered within Spring
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractControllerLayerGenerator extends AbstractLayerGenerator {

	public static final String subpackageName = ".controller";

	@Override
	public List<TemplateFileInfo> getTemplateList(boolean isView) {
		TemplateFileInfo web = new TemplateFileInfo("web/Controller.ftl",
				DynamicClassGenerator.filenameWildcard + "Controller" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.packageName + subpackageName);

		List<TemplateFileInfo> list = new ArrayList<>();
		list.add(web);

		return list;
	}

	@Override
	protected List<Class<?>> getClassesToRegister(List<TemplateFileInfo> templateList) throws Exception {
		List<Class<?>> classes = new ArrayList<>();

		for (TemplateFileInfo tfi : templateList) {
			if (!tfi.getIsJava()) {
				continue;
			}
			String className = (tfi.getPackageName() + "." + tfi.getFileName())
					.replace(JavaFileObject.Kind.SOURCE.extension, "");
			Class<?> clazz = PuiDynamicClassLoader.getInstance().loadClass(className);
			classes.add(clazz);
		}

		return classes;
	}

	@Override
	protected Object registerBean(Class<?> clazz) throws Exception {
		String beanName = getBeanName(clazz);
		if (definitionRegistry.containsBeanDefinition(beanName)) {
			throw new BeanCreationException("Bean " + beanName + " already exists");
		}
		MetadataReaderFactory mrf = new CachingMetadataReaderFactory(appContext);
		MetadataReader mr = mrf.getMetadataReader(clazz.getName());
		ScannedGenericBeanDefinition bd = new ScannedGenericBeanDefinition(mr);
		bd.addMetadataAttribute(new BeanMetadataAttribute(
				"org.springframework.context.annotation.ConfigurationClassPostProcessor.configurationClass", "lite"));
		bd.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		bd.setEnforceDestroyMethod(false);
		bd.setEnforceInitMethod(false);

		String className = clazz.getName().replace(".", "/") + JavaFileObject.Kind.CLASS.extension;
		URL url = PuiDynamicClassLoader.getInstance().getResource(className);
		UrlResource ur = new UrlResource(url);
		bd.setSource(ur);
		bd.setResource(ur);

		definitionRegistry.registerBeanDefinition(beanName, bd);

		Object bean = factory.getBean(beanName);
		postCreateBean(bean);

		return bean;
	}

	/**
	 * Do something after creating the bean
	 * 
	 * @param bean The created bean
	 */
	protected abstract void postCreateBean(Object bean);

}
