package es.prodevelop.pui9.spring.configuration.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;

/**
 * This annotation should be used by all the Spring configuration files in the
 * PUI application (also the Framework use it)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Configuration
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PuiSpringConfiguration {

}
