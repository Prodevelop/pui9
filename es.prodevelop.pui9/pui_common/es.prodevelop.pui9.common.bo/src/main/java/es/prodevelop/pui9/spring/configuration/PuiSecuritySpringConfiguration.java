package es.prodevelop.pui9.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import es.prodevelop.pui9.login.PuiDaoAuthenticationProvider;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

@EnableWebSecurity
@PuiSpringConfiguration
public class PuiSecuritySpringConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private PuiDaoAuthenticationProvider daoAuthenticationProvider;

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		// don't remove this function, because it is used to create the Bean
		return super.authenticationManager();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider);
	}

}
