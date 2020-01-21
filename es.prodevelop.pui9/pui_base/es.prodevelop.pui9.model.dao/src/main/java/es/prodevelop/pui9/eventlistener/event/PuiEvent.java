package es.prodevelop.pui9.eventlistener.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;

import es.prodevelop.pui9.eventlistener.EventLauncher;

/**
 * Abstract implementation for the Spring {@link ApplicationEvent}. Each event
 * is defined by:<br>
 * <ul>
 * <li><b>an Identifier</b>: is a unique identifier based on an String to
 * identify the type of the event</li>
 * <li><b>the Source</b>: the event object that will be passed to each listener
 * that listens to this event</li>
 * </ul>
 * <br>
 * The events are fired using the {@link EventLauncher} class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiEvent<T> extends ApplicationEvent {

	private static final long serialVersionUID = 1L;

	protected final transient Log logger = LogFactory.getLog(getClass());

	private String eventId;

	/**
	 * Create a new Event with the given parameters
	 * 
	 * @param source  The object that provoikes the event to be fired
	 * @param eventId An identifier for the event
	 */
	public PuiEvent(T source, String eventId) {
		super(source);
		this.eventId = eventId;
	}

	public String getEventId() {
		return eventId;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getSource() {
		return (T) super.getSource();
	}

}
