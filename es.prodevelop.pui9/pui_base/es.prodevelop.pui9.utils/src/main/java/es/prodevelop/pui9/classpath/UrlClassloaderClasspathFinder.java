package es.prodevelop.pui9.classpath;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class retrieves the classpath when using a Classloader of URLClassLoader
 * type (for instance, when using Tomcat)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class UrlClassloaderClasspathFinder implements IClasspathFinder {

	private List<File> classPath;

	@Override
	public List<File> getClasspath() {
		if (classPath == null) {
			classPath = new ArrayList<>();

			if (!(Thread.currentThread().getContextClassLoader() instanceof URLClassLoader)) {
				return classPath;
			}

			URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();

			try {
				for (URL url : classLoader.getURLs()) {
					try {
						File file = new File(url.toURI());
						if (file.exists() && file.canRead()) {
							classPath.add(file);
						}
					} catch (URISyntaxException e) {
					}
				}
			} catch (Exception e) {
			}
		}

		return classPath;
	}

}
