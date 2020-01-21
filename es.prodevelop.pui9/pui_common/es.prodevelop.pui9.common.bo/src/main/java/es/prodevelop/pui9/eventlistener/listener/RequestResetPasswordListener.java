package es.prodevelop.pui9.eventlistener.listener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.messages.PuiCommonMessages;
import es.prodevelop.pui9.common.messages.PuiCommonResourceBundle;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.eventlistener.event.RequestResetPasswordEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.mail.PuiMailSender;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * Listener fired when a user request to reset the password. It sends a
 * verification email to the user with a URL to generate a new password. This
 * URL is available only for 30 minutes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class RequestResetPasswordListener extends PuiListener<RequestResetPasswordEvent> {

	private static final String LANG = "{lang}";
	private static final String TEMPLATE_NAME = "request_reset_password_" + LANG + ".html";

	@Autowired
	protected IPuiVariableService variableService;

	@Override
	protected void process(RequestResetPasswordEvent event) throws PuiException {
		IPuiUser puiUser = event.getSource();
		PuiLanguage lang;
		if (!StringUtils.isEmpty(puiUser.getLanguage())) {
			lang = new PuiLanguage(puiUser.getLanguage());
		} else {
			lang = PuiLanguageUtils.getDefaultLanguage();
		}

		String mailSubjectLabelId = variableService
				.getVariable(PuiVariableValues.PASSWORD_CHANGE_MAIL_SUBJECT_LABEL_ID.name());
		if (StringUtils.isEmpty(mailSubjectLabelId)) {
			mailSubjectLabelId = PuiCommonResourceBundle.requestResetPasswordSubject;
		}

		String templateName = TEMPLATE_NAME.replace(LANG, lang.getIsocode());

		String to = puiUser.getEmail();
		String subject = PuiCommonMessages.getSingleton().getString(mailSubjectLabelId, lang);

		String baseUrl = variableService.getVariable(PuiVariableValues.BASE_CLIENT_URL.name());
		String resetPasswordUrl = "/resetPassword";
		String params = "?resetToken=" + puiUser.getResetpasswordtoken();
		String url = baseUrl + resetPasswordUrl + params;

		Map<String, Object> map = new HashMap<>();
		map.put("username", puiUser.getName());
		map.put("url", url);
		String email = PuiMailSender.getSingleton().compileTemplateFromClasspath(templateName, map);
		if (email == null) {
			throw new PuiException("No template found for reseting the password. The file " + TEMPLATE_NAME
					+ " is not found in the classpath. Please, add it to the resources folder on your web project");
		}

		try {
			PuiMailSender.getSingleton().send(to, subject, email, true);
		} catch (Exception e) {
			throw new PuiException(e);
		}
	}

}
