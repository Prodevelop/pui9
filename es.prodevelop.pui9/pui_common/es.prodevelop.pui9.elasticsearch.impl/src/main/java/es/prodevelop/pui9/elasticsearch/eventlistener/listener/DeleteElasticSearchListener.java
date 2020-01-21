package es.prodevelop.pui9.elasticsearch.eventlistener.listener;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.eventlistener.event.DeleteDaoEvent;

/**
 * Listener for ElasticSearch to be fired when a document is deleted from the
 * Database. The corresponding document should be deleted too from the
 * ElasticSearch indexes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DeleteElasticSearchListener extends AbstractElasticSearchListener<DeleteDaoEvent> {

	@Override
	protected DocumentOperationType getOperationType() {
		return DocumentOperationType.delete;
	}

}
