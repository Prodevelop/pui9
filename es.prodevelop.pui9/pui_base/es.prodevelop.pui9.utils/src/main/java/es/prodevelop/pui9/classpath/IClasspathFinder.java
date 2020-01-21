package es.prodevelop.pui9.classpath;

import java.io.File;
import java.util.List;

/**
 * A classpath finder to retrieve the list of Jar Files that represents the
 * current classpath of the application.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IClasspathFinder {

	/**
	 * Get the list of JAR files that represents the current classpath of the
	 * application. Should return null or empty if could not retrieve the classpath.
	 * Each File should exists and to be accessible in read mode
	 * 
	 * @return The list of files loaded within this ClassPath
	 */
	List<File> getClasspath();

}
