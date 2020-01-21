package es.prodevelop.pui9.eventlistener.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.eventlistener.event.SigninEvent;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.json.GsonSingleton;

/**
 * Listener for Login into application
 */
@Component
public class SigninAuditListener extends AbstractAuditListener<SigninEvent> {

	@Autowired
	private IPuiUserService userService;

	@Override
	protected void process(SigninEvent eventType) throws PuiException {
		super.process(eventType);

		userService.setLastAccess(new PuiUserPk(eventType.getSource().getUsr()), eventType.getSource().getCreation(),
				eventType.getSource().getIp());
	}

	@Override
	protected void fillEventObject(SigninEvent event, IPuiAudit puiAudit) {
		puiAudit.setDatetime(event.getSource().getCreation());
		puiAudit.setModel(daoRegistry.getModelIdFromDto(IPuiUser.class));
		puiAudit.setPk(event.getSource().getUsr());

		puiAudit.setDto(GsonSingleton.getSingleton().getGson().toJson(event.getSource()));
	}

}
