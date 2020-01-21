package es.prodevelop.pui9.eventlistener.listener;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.eventlistener.event.SignoutEvent;
import es.prodevelop.pui9.json.GsonSingleton;

/**
 * Listener for Logout from application
 */
@Component
public class SignoutAuditListener extends AbstractAuditListener<SignoutEvent> {

	@Override
	protected void fillEventObject(SignoutEvent event, IPuiAudit puiAudit) {
		puiAudit.setModel(daoRegistry.getModelIdFromDto(IPuiUser.class));
		puiAudit.setPk(event.getSource().getUsr());

		puiAudit.setDto(GsonSingleton.getSingleton().getGson().toJson(event.getSource()));
	}

}
