package es.prodevelop.pui9.notifications.eventlistener.event;

import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;

/**
 * Event for new FCM Token registered
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class FcmTokenRegisteredEvent extends PuiEvent<IPuiUserFcm> {

	private static final long serialVersionUID = 1L;

	public FcmTokenRegisteredEvent(IPuiUserFcm userFcm) {
		super(userFcm, "fcmTokenRegistered");
	}

}
