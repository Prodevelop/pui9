package es.prodevelop.pui9.spring.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class _id_OnApplicationStartedListener {

	private final Log logger = LogFactory.getLog(this.getClass());

	@EventListener
	public void onApplicationEvent(ContextStartedEvent event) {
		logger.info("App started");
	}

}
