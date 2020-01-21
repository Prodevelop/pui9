package es.prodevelop.pui9.eventlistener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.eventlistener.event.PuiEvent;

/**
 * This class allows to fire an Event using a synchronized or asynchronized way
 * (depending on the needs of the application)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class EventLauncher {

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private PuiApplicationEventMulticaster multicaster;

	private SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();

	private EventLauncher() {
	}

	/**
	 * Fire an event in an Asynchronous way
	 * 
	 * @param event The event to be fired
	 */
	public void fireAsync(PuiEvent<?> event) {
		multicaster.setTaskExecutor(executor);
		publisher.publishEvent(event);
	}

	/**
	 * Fire an event in an Synchronous way
	 * 
	 * @param event The event to be fired
	 */
	public void fireSync(PuiEvent<?> event) {
		multicaster.setTaskExecutor(null);
		publisher.publishEvent(event);
	}

}
