package es.prodevelop.pui9.eventlistener.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.DeleteEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Listener for Delete an element
 */
@Component
public class DeleteAuditListener extends AbstractAuditListener<DeleteEvent> {

	@Override
	protected void fillEventObject(DeleteEvent event, IPuiAudit puiAudit) {
		ITableDto dto = event.getSource();
		String dtoPk = getDtoPK(dto);
		puiAudit.setPk(dtoPk);

		List<OneObject> list = processOneObject(dto);

		String json = GsonSingleton.getSingleton().getGson().toJson(list);
		puiAudit.setDto(json);
	}

}
