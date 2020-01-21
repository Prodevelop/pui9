package es.prodevelop.pui9.eventlistener.listener;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.eventlistener.event.VariableUpdatedEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.mail.PuiMailSender;

/**
 * Listener fired when the variable 'DOCUMENTS_THUMBNAILS_VALUES' is updated
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class UpdateMailVariablesListener extends PuiListener<VariableUpdatedEvent> {

	private static final String MAIL_SMTP_HOST_VAR = "MAIL_SMTP_HOST";
	private static final String MAIL_SMTP_PORT_VAR = "MAIL_SMTP_PORT";
	private static final String MAIL_SMTP_USER_VAR = "MAIL_SMTP_USER";
	private static final String MAIL_SMTP_PASS_VAR = "MAIL_SMTP_PASS";
	private static final String MAIL_SMTP_AUTH_VAR = "MAIL_SMTP_AUTH";
	private static final String MAIL_SMTP_STARTTLS_ENABLE_VAR = "MAIL_SMTP_STARTTLS_ENABLE";
	private static final String MAIL_FROM_VAR = "MAIL_FROM";

	@Override
	protected boolean passFilter(VariableUpdatedEvent event) {
		if (event.getSource().getVariable().equals(MAIL_SMTP_HOST_VAR)
				&& !event.getOldValue().equals(event.getSource().getValue())) {
			return true;
		} else if (event.getSource().getVariable().equals(MAIL_SMTP_PORT_VAR)
				&& !event.getOldValue().equals(event.getSource().getValue())) {
			return true;
		} else if (event.getSource().getVariable().equals(MAIL_SMTP_USER_VAR)
				&& !event.getOldValue().equals(event.getSource().getValue())) {
			return true;
		} else if (event.getSource().getVariable().equals(MAIL_SMTP_PASS_VAR)
				&& !event.getOldValue().equals(event.getSource().getValue())) {
			return true;
		} else if (event.getSource().getVariable().equals(MAIL_SMTP_AUTH_VAR)
				&& !event.getOldValue().equals(event.getSource().getValue())) {
			return true;
		} else if (event.getSource().getVariable().equals(MAIL_SMTP_STARTTLS_ENABLE_VAR)
				&& !event.getOldValue().equals(event.getSource().getValue())) {
			return true;
		} else if (event.getSource().getVariable().equals(MAIL_FROM_VAR)
				&& !event.getOldValue().equals(event.getSource().getValue())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void process(VariableUpdatedEvent event) throws PuiException {
		PuiMailSender.getSingleton().configureSender();
	}

}
