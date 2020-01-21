package es.prodevelop.pui9.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This Annotation is useful to set the name of the Table in the DAO objects
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PuiEntity {

	/**
	 * The name of the table in the database
	 */
	String tablename();

	/**
	 * The name of the translation table associated to the main table
	 */
	String tabletranslationname() default "";

}