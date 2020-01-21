package es.prodevelop.pui9.eventlistener.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.InsertEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Listener for Insert an element
 */
@Component
public class InsertAuditListener extends AbstractAuditListener<InsertEvent> {

	@Override
	protected void fillEventObject(InsertEvent event, IPuiAudit puiAudit) {
		ITableDto dto = event.getSource();
		String dtoPk = getDtoPK(dto);
		puiAudit.setPk(dtoPk);

		List<OneObject> list = processOneObject(dto);

		String json = GsonSingleton.getSingleton().getGson().toJson(list);
		puiAudit.setDto(json);
	}

}
