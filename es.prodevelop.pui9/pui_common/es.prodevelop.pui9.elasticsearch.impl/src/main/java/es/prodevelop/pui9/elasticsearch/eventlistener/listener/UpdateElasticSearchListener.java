package es.prodevelop.pui9.elasticsearch.eventlistener.listener;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.eventlistener.event.UpdateDaoEvent;

/**
 * Listener for ElasticSearch to be fired when a document is updated into the
 * Database. The corresponding document should be updated too into the
 * ElasticSearch indexes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class UpdateElasticSearchListener extends AbstractElasticSearchListener<UpdateDaoEvent> {

	@Override
	protected DocumentOperationType getOperationType() {
		return DocumentOperationType.update;
	}

}
