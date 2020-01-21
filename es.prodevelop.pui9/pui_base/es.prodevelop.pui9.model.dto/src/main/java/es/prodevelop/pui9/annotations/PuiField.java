package es.prodevelop.pui9.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.prodevelop.pui9.enums.ColumnType;

/**
 * This annotation is useful to set several Column information in the DTO fields
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PuiField {

	/**
	 * The name of the column in the database
	 */
	String columnname();

	/**
	 * If the column is part of the PK of the table
	 */
	boolean ispk() default false;

	/**
	 * If the column value can be null or not
	 */
	boolean nullable() default false;

	/**
	 * The type of the column
	 */
	ColumnType type() default ColumnType.text;

	/**
	 * If the column value is autoincrementable
	 */
	boolean autoincrementable() default false;

	/**
	 * The max length value of the column when it is an String
	 */
	int maxlength() default -1;

	/**
	 * If the column belongs to the translation table
	 */
	boolean islang() default false;

	/**
	 * If the column is a Geometry
	 */
	boolean isgeometry() default false;

	/**
	 * If the column is a sequence in the database, so it will never take part in an
	 * insert or update statement
	 */
	boolean issequence() default false;

}