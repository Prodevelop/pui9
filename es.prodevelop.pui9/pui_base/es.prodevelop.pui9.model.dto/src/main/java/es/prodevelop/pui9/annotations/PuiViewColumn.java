package es.prodevelop.pui9.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.prodevelop.pui9.enums.ColumnVisibility;

/**
 * This annotation is useful to set several Column information in the View DTO
 * fields
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PuiViewColumn {

	/**
	 * The column order in the grid
	 */
	int order();

	/**
	 * The column visibility
	 */
	ColumnVisibility visibility() default ColumnVisibility.visible;

}