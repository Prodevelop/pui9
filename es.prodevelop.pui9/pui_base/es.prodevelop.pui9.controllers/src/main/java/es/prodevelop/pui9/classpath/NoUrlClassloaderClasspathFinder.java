package es.prodevelop.pui9.classpath;

import java.io.File;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.context.WebApplicationContext;

import es.prodevelop.pui9.components.PuiApplicationContext;

/**
 * This class retrieves the classpath when using a Classloader of any type
 * distinct to URLClassLoader type (for instance, when using JBoss)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class NoUrlClassloaderClasspathFinder implements IClasspathFinder {

	@Override
	public List<File> getClasspath() {
		List<File> classpath = new ArrayList<>();

		if (Thread.currentThread().getContextClassLoader() instanceof URLClassLoader) {
			return classpath;
		}

		WebApplicationContext context = (WebApplicationContext) PuiApplicationContext.getInstance().getAppContext();
		Set<String> jarList = context.getServletContext().getResourcePaths("/WEB-INF/lib");

		for (String jar : jarList) {
			jar = context.getServletContext().getRealPath(jar);
			File file = new File(jar);
			String extension = FilenameUtils.getExtension(jar);
			if (!file.exists() || !file.canRead()) {
				continue;
			}
			if ((file.isFile() && extension.equalsIgnoreCase("jar")) || file.isDirectory()) {
				classpath.add(file);
			}
		}

		return classpath;
	}

}
