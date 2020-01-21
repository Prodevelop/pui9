package es.prodevelop.pui9.model.generator.layers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import org.springframework.beans.BeanMetadataAttribute;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.UrlResource;

import es.prodevelop.pui9.classpath.PuiDynamicClassLoader;
import es.prodevelop.pui9.model.generator.DynamicClassGenerator;
import es.prodevelop.pui9.model.generator.TemplateFileInfo;

/**
 * This class represents the Service Layer Generator, to generate all the files
 * for the PUI Server. It is abstract due to special action are required when
 * registering the bean within Spring
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractServiceLayerGenerator extends AbstractLayerGenerator {

	public static final String subpackageName = ".service";
	public static final String subpackageInterfacesName = subpackageName + ".interfaces";

	@Override
	public List<TemplateFileInfo> getTemplateList(boolean isView) {
		TemplateFileInfo ibo = new TemplateFileInfo("bo/IService.ftl",
				"I" + DynamicClassGenerator.filenameWildcard + "Service" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.packageName + subpackageInterfacesName);
		TemplateFileInfo bo = new TemplateFileInfo("bo/Service.ftl",
				DynamicClassGenerator.filenameWildcard + "Service" + JavaFileObject.Kind.SOURCE.extension,
				DynamicClassGenerator.packageName + subpackageName);

		List<TemplateFileInfo> list = new ArrayList<>();
		list.add(ibo);
		list.add(bo);

		return list;
	}

	@Override
	protected Object registerBean(Class<?> clazz) throws Exception {
		String beanName = getBeanName(clazz);
		if (definitionRegistry.containsBeanDefinition(beanName)) {
			throw new BeanCreationException("Bean " + beanName + " already exists");
		}
		RootBeanDefinition rbd = new RootBeanDefinition(clazz);
		rbd.addMetadataAttribute(new BeanMetadataAttribute(
				"org.springframework.context.annotation.ConfigurationClassPostProcessor.configurationClass", "lite"));
		rbd.setEnforceDestroyMethod(false);
		rbd.setEnforceInitMethod(false);
		rbd.setScope(ConfigurableBeanFactory.SCOPE_SINGLETON);
		ResolvableType rt = ResolvableType.forClass(clazz);
		rt.getInterfaces();
		rbd.setTargetType(rt);

		String className = clazz.getName().replace(".", "/") + JavaFileObject.Kind.CLASS.extension;
		URL url = PuiDynamicClassLoader.getInstance().getResource(className);
		UrlResource ur = new UrlResource(url);
		rbd.setSource(ur);
		rbd.setResource(ur);

		definitionRegistry.registerBeanDefinition(beanName, rbd);

		// sometimes an strange error occurs, and it's necessary to get the bean twice
		// because first time it's created
		Object bean = null;
		try {
			bean = factory.getBean(beanName);
		} catch (Exception e) {
			bean = factory.getBean(beanName);
		}

		return bean;
	}

}
