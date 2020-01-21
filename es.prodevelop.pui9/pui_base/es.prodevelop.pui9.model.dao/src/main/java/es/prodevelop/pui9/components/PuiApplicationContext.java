package es.prodevelop.pui9.components;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Whith this singleton class, it's possible to access to the context of the
 * application in an easy way, and also contains some useful methods to manage
 * beans
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiApplicationContext {

	private static PuiApplicationContext puiContext;

	/**
	 * Use this singleton method only if you can ensure that the application is
	 * completely initialized. Else, it will fail when obtaining the context of the
	 * application
	 */
	public static PuiApplicationContext getInstance() {
		Assert.notNull(puiContext, "PuiApplicationContext only can be used once the bean is created by Spring");
		return puiContext;
	}

	@Autowired
	private ApplicationContext appContext;

	private PuiApplicationContext() {
	}

	@PostConstruct
	private void postConstruct() {
		puiContext = this;
	}

	/**
	 * Get the real Application Context of the application
	 * 
	 * @return The real Application Context
	 */
	public ApplicationContext getAppContext() {
		return appContext;
	}

	/**
	 * Get the corresponding bean for the given Class. If there exists more than one
	 * bean. The first one is returned
	 * 
	 * @param beanClass
	 *            The class that represents the bean
	 * @return The bean object that is instance of the given class
	 */
	public <T> T getBean(Class<T> beanClass) {
		if (beanClass == null) {
			return null;
		}

		T bean = null;

		try {
			bean = appContext.getBean(beanClass);
		} catch (NoUniqueBeanDefinitionException e) {
			Map<String, T> list = appContext.getBeansOfType(beanClass);
			if (list.isEmpty()) {
				return null;
			}
			bean = list.get(StringUtils.uncapitalize(beanClass.getSimpleName()));
		}

		return bean;
	}

	/**
	 * Get the corresponding bean for the given Class. In this case, the Class is
	 * defined with a generic class (multiple instances for different generic
	 * classes can exist)
	 * 
	 * @param beanClass
	 *            The class that represents the bean
	 * @param genericClass
	 *            The generic class that defines the bean
	 * @return The bean object that is instance of the given class
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> beanClass, Class<?> genericClass) {
		if (beanClass == null || genericClass == null) {
			return null;
		}

		T bean = null;
		ResolvableType resolvableType = ResolvableType.forClassWithGenerics(beanClass, genericClass);
		String[] names = appContext.getBeanNamesForType(resolvableType);
		if (names.length > 0) {
			try {
				bean = (T) appContext.getBean(names[0]);
			} catch (NoSuchBeanDefinitionException e) {
			}
		}

		return bean;
	}
}
