package es.prodevelop.pui9.notifications.eventlistener.event;

import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;

/**
 * Event for new FCM Token unregistered
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class FcmTokenUnregisteredEvent extends PuiEvent<IPuiUserFcm> {

	private static final long serialVersionUID = 1L;

	public FcmTokenUnregisteredEvent(IPuiUserFcm userFcm) {
		super(userFcm, "fcmTokenUnregistered");
	}

}
