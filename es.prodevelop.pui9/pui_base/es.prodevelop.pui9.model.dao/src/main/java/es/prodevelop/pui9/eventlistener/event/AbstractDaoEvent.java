package es.prodevelop.pui9.eventlistener.event;

import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Special PUI Event for DAO Actions. Created in the {@link ITableDao}
 * implementation classes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDaoEvent<T extends ITableDto> extends PuiEvent<T> {

	private static final long serialVersionUID = 1L;

	private Long transactionId;

	public AbstractDaoEvent(T dto, String eventTypeId) {
		super(dto, eventTypeId);
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

}
