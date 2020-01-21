package es.prodevelop.pui9.eventlistener.listener;

import java.util.List;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiAudit;
import es.prodevelop.pui9.eventlistener.event.UpdateEvent;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

@Component
public class UpdateAuditListener extends AbstractAuditListener<UpdateEvent> {

	@Override
	protected void fillEventObject(UpdateEvent event, IPuiAudit puiAudit) {
		ITableDto dto = event.getSource();
		String dtoPk = getDtoPK(dto);
		puiAudit.setPk(dtoPk);

		IDto oldDto = event.getOldDto();

		List<TwoObject> list = processTwoObject(oldDto, dto);

		String json = GsonSingleton.getSingleton().getGson().toJson(list);
		puiAudit.setDto(json);
	}

}
