package es.prodevelop.pui9.model.generator.layers;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.model.generator.TemplateFileInfo;

/**
 * This abstract class represents the layers of the model. Typically, these
 * layers are the following: DTO and DAO layer, Service layer, Controller layer,
 * and Client layer. These classes are used in the generation code. Each layer
 * class, provides information about the templates and generated classes, like
 * how to register them within Spring
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractLayerGenerator {

	@Autowired
	protected PuiApplicationContext context;

	protected AbstractApplicationContext appContext;
	protected DefaultListableBeanFactory factory;
	protected BeanDefinitionRegistry definitionRegistry;

	/**
	 * Load necessary attributes
	 */
	@PostConstruct
	private void postConstruct() {
		appContext = (AbstractApplicationContext) context.getAppContext();
		factory = (DefaultListableBeanFactory) appContext.getBeanFactory();
		definitionRegistry = (BeanDefinitionRegistry) appContext.getAutowireCapableBeanFactory();
	}

	/**
	 * Get the list of templates to be generated for this layer
	 * 
	 * @param isView
	 *            If the templates are for generating a view
	 * @return List of templates to be generated
	 */
	public abstract List<TemplateFileInfo> getTemplateList(boolean isView);

	/**
	 * Registers all the classes of this layer within Spring
	 * 
	 * @param templateList
	 *            List of templates to register the corresponding Java Classes
	 * @throws Exception
	 *             If any error is thrown in the class registration
	 */
	public final void registerClasses(List<TemplateFileInfo> templateList) throws Exception {
		List<Class<?>> classes = getClassesToRegister(templateList);

		for (Class<?> clazz : classes) {
			registerBean(clazz);
		}
	}

	/**
	 * Get the bean name of the given Class to be registered within Spring
	 * 
	 * @param clazz
	 *            The class to be registered
	 * @return The bean name for given class
	 */
	protected String getBeanName(Class<?> clazz) {
		String beanName = clazz.getSimpleName();
		if (clazz.isInterface()) {
			beanName = beanName.substring(1);
		}
		beanName = StringUtils.uncapitalize(beanName);
		if (beanName.startsWith("v")) {
			beanName = StringUtils.capitalize(beanName);
		}
		return beanName;
	}

	/**
	 * Register the given class bean within Spring
	 * 
	 * @param clazz
	 *            The class to be registered
	 * @return The registered bean
	 * @throws Exception
	 *             If any error is thrown in the class registration
	 */
	protected abstract Object registerBean(Class<?> clazz) throws Exception;

	/**
	 * Obtain the list of classes to be registered in Spring
	 * 
	 * @param templateList
	 *            The list of templates to look for Java classes to register
	 * @return The list of classes to be registered
	 * @throws Exception
	 *             If any error is thrown in the class registration
	 */
	protected abstract List<Class<?>> getClassesToRegister(List<TemplateFileInfo> templateList) throws Exception;

}
