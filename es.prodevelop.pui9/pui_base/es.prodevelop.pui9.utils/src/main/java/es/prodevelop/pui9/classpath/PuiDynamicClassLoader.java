package es.prodevelop.pui9.classpath;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * This class loader is a wrapper of the class loader used in the application.
 * This is of type {@link URLClassLoader}, and it's used to wrap class loaders
 * that are not of this type (for instance, when using JBoss)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDynamicClassLoader extends URLClassLoader {

	private static PuiDynamicClassLoader instance;

	public static PuiDynamicClassLoader getInstance() {
		if (instance == null) {
			instance = new PuiDynamicClassLoader();
		}
		return instance;
	}

	private PuiDynamicClassLoader() {
		super(new URL[] {}, Thread.currentThread().getContextClassLoader());
	}

	public void addURL(URL url) {
		super.addURL(url);
	}

}