package es.prodevelop.pui9.classpath;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

/**
 * Class path registry used to retrieve the classpath of the application. You
 * can use this class when you want to obtain the list of classpath files
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiClasspathFinderRegistry {

	private static PuiClasspathFinderRegistry instance;

	public static PuiClasspathFinderRegistry getInstance() {
		if (instance == null) {
			instance = new PuiClasspathFinderRegistry();
		}
		return instance;
	}

	private List<IClasspathFinder> classpathFinders;

	private PuiClasspathFinderRegistry() {
		classpathFinders = new ArrayList<>();
	}

	/**
	 * Retrieves the list of files that are used by the application classpath
	 * 
	 * @return The list of files that take part of the application classpath
	 */
	public List<File> getClasspath() {
		if (classpathFinders.isEmpty()) {
			Set<Class<? extends IClasspathFinder>> set = new Reflections("es.prodevelop.pui9")
					.getSubTypesOf(IClasspathFinder.class);
			for (Class<? extends IClasspathFinder> clazz : set) {
				try {
					IClasspathFinder cpf = clazz.newInstance();
					classpathFinders.add(cpf);
				} catch (InstantiationException | IllegalAccessException e) {
				}
			}
		}

		for (IClasspathFinder finder : classpathFinders) {
			List<File> list = finder.getClasspath();
			if (list != null && !list.isEmpty()) {
				return list;
			}
		}

		return Collections.emptyList();
	}

}
