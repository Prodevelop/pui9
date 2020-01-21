package es.prodevelop.pui9.elasticsearch.eventlistener.listener;

import org.springframework.stereotype.Component;

import es.prodevelop.pui9.elasticsearch.enums.DocumentOperationType;
import es.prodevelop.pui9.eventlistener.event.InsertDaoEvent;

/**
 * Listener for ElasticSearch to be fired when a document is inserted into the
 * Database. The corresponding document should be inserted too into the
 * ElasticSearch indexes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class InsertElasticSearchListener extends AbstractElasticSearchListener<InsertDaoEvent> {

	@Override
	protected DocumentOperationType getOperationType() {
		return DocumentOperationType.insert;
	}

}
