package es.prodevelop.pui9.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.google.common.base.Objects;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;

@Component
public class PuiDaoAuthenticationProvider extends DaoAuthenticationProvider {

	public static final String ANONYMOUS_USER = "anonymous";

	@Autowired
	protected IPuiVariableService variableService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doAfterPropertiesSet() {
		setUserDetailsService(userDetailsService);
		setPasswordEncoder(new BCryptPasswordEncoder(10));
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		if (Objects.equal(userDetails.getUsername(), ANONYMOUS_USER)) {
			Boolean allowAnonymous = variableService.getVariable(Boolean.class,
					PuiVariableValues.LOGIN_ALLOW_ANONYMOUS.name());
			if (allowAnonymous == null || !allowAnonymous) {
				throw new LockedException(messages
						.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
			}
		}

		super.additionalAuthenticationChecks(userDetails, authentication);
	}

}
