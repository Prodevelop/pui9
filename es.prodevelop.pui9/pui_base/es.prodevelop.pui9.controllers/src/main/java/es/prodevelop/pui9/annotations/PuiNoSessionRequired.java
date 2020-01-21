package es.prodevelop.pui9.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Controller;

/**
 * This annotation set in a WebService method or in a class marked with the
 * {@link Controller} annotation, make this WebService or the whole controller
 * to be able to be called without starting a session previously in the system
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PuiNoSessionRequired {
}