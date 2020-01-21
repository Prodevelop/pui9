package es.prodevelop.pui9.elasticsearch.eventlistener.listener;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;

import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.elasticsearch.synchronization.PuiElasticSearchLiveSynchronization;
import es.prodevelop.pui9.eventlistener.event.AbstractDaoEvent;
import es.prodevelop.pui9.eventlistener.listener.PuiListener;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * Abstract listener for synchronizing registries with ElasticSearch. See the
 * {@link PuiElasticSearchLiveSynchronization} class
 * 
 * @param <T> Event Type of type {@link AbstractDaoEvent}
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractElasticSearchListener<E extends AbstractDaoEvent<ITableDto>> extends PuiListener<E> {

	@Autowired
	protected PuiElasticSearchLiveSynchronization liveSynch;

	@Override
	protected boolean passFilter(E event) {
		IDto dto = event.getSource();
		if (!(dto instanceof ITableDto)) {
			// if it's not a Table DTO... false
			return false;
		}

		Field langField = DtoRegistry.getJavaFieldFromFieldName(dto.getClass(), IDto.LANG_FIELD_NAME);
		if (langField == null) {
			// if no lang field is available... true
			return true;
		}

		langField = DtoRegistry.getJavaFieldFromLangFieldName(dto.getClass(), IDto.LANG_FIELD_NAME);
		// if has lang field, but it's not a translate table... true
		return langField != null;
	}

	@Override
	protected final void process(E event) throws PuiException {
		ITableDto dtoPk = event.getSource().createPk();
		DocumentOperationType operation = getOperationType();
		Long transactionId = event.getTransactionId();
		PuiUserSession userSession = getUserSession();

		liveSynch.queueOperation(dtoPk, operation, transactionId, userSession != null ? userSession.getJwt() : null);
	}

	/**
	 * Get the operation type
	 * 
	 * @return The operation type
	 */
	protected abstract DocumentOperationType getOperationType();
}
