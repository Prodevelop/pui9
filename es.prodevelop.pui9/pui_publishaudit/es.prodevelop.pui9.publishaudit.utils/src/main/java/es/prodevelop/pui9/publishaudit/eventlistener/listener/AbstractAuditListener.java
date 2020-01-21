package es.prodevelop.pui9.publishaudit.eventlistener.listener;

import java.util.List;

import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.publishaudit.dto.PublishAuditData;

public abstract class AbstractAuditListener<E extends PuiEvent<ITableDto>> extends AbstractPublishAuditListener<E> {

	@Override
	protected final void doProcess(List<PublishAuditData> data) throws PuiServiceException {
		audit(data);
	}

	protected abstract void audit(List<PublishAuditData> data) throws PuiServiceException;

}
