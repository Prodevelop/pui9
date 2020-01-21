package es.prodevelop.pui9.publishaudit.eventlistener.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import es.prodevelop.pui9.eventlistener.event.PuiEvent;
import es.prodevelop.pui9.eventlistener.event.UpdateDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.publishaudit.components.PublishAuditRegistry;
import es.prodevelop.pui9.publishaudit.dto.PublishAuditData;

public abstract class AbstractPublishAuditListener<E extends PuiEvent<ITableDto>> extends PuiListener<E> {

	@Autowired
	protected PublishAuditRegistry registry;

	@Autowired
	protected DaoRegistry daoRegistry;

	@Override
	protected boolean passFilter(E event) {
		return event.getSource() != null;
	}

	@Override
	protected final void process(E event) throws PuiException {
		ITableDto dto = event.getSource();

		String tableName = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dto.getClass()));
		ITableDto oldDto = null;
		if (event instanceof UpdateDaoEvent) {
			oldDto = ((UpdateDaoEvent) event).getOldDto();
		}

		List<PublishAuditData> data = getPublishAuditData(tableName, oldDto, dto);
		if (data != null) {
			doProcess(data);
		}
	}

	protected abstract void doProcess(List<PublishAuditData> data) throws PuiServiceException;

	protected abstract List<PublishAuditData> getPublishAuditData(String tableName, IDto oldDto, IDto dto);

}
