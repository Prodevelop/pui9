package es.prodevelop.pui9.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is useful to indicate the functionality that the user should
 * have to be able to consume this web service
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PuiFunctionality {

	/**
	 * A simple ID to identify the functionality
	 * 
	 * @return The ID
	 */
	String id();

	/**
	 * The functionality value. Due to compatibility with the abstract controllers
	 * of PUI, The value could be intelligent:<br>
	 * <ul>
	 * <li>Directly the name of the functionality, or an String constant:
	 * "my_functionality", "MyOwnClass.MY_EDIT_FUNCTIONALITY_CONST"</li>
	 * <li>The name of a method in the same Controller that returns an String
	 * containing the value of the functionality: "getEditFunctionality"</li>
	 * </ul>
	 * 
	 * @return
	 */
	String value();

	/**
	 * Indicate if should check the functionality or not. By default, check it.
	 * Useful when a webservice is rewrited
	 * 
	 * @return
	 */
	boolean check() default true;

}