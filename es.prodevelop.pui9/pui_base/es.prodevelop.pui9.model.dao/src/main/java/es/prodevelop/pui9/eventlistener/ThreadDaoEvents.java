package es.prodevelop.pui9.eventlistener;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;

import es.prodevelop.pui9.eventlistener.event.AbstractDaoEvent;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

/**
 * This class acts like a cache of Actions to be executed after a transaction is
 * completed correctly.<br>
 * <br>
 * It is called in the {@link ITableDao} after an Insert, Update or Delete is
 * performed to add the event. All the events are stored and processed in the
 * same order<br>
 * <br>
 * Then, when the Transaction ends correctly (See implementations of
 * {@link AbstractPlatformTransactionManager} in PUI for JDBC approach), it is
 * called again in order to process all the saved Events
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class ThreadDaoEvents {

	private static final AtomicLong seq = new AtomicLong(0);

	private ThreadLocal<PriorityBlockingQueue<OrderedEvent>> threadLocal = new ThreadLocal<>();

	@Autowired
	private EventLauncher eventLauncher;

	private ThreadDaoEvents() {
	}

	/**
	 * Initialize a new queue of Events for current Thread, if no one is created
	 * before (in case of nested transactions) (Typically called when creating new
	 * transactions)
	 */
	public void initialize() {
		if (threadLocal.get() == null) {
			threadLocal.set(new PriorityBlockingQueue<>());
		}
	}

	/**
	 * Add a new Event Type to be processed after the transaction
	 * 
	 * @param eventType The event type
	 */
	public void addEventType(AbstractDaoEvent<?> eventType) {
		if (threadLocal.get() == null) {
			return;
		}

		threadLocal.get().put(new OrderedEvent(eventType));
	}

	/**
	 * Process all the stored Events in the same order as stored. Also, these events
	 * are processed in a Synchronized way.<br>
	 * <br>
	 * Typically called when the commit of the transaction is performed
	 * 
	 * @param transactionId The ID of the transaction that fired this event (could
	 *                      be null)
	 */
	public void process(Long transactionId) {
		if (threadLocal.get() == null) {
			return;
		}

		final PriorityBlockingQueue<OrderedEvent> queue = threadLocal.get();
		if (queue.isEmpty()) {
			remove();
			return;
		}

		try {
			OrderedEvent threadEvent = queue.take();
			threadEvent.event.setTransactionId(transactionId);
			eventLauncher.fireSync(threadEvent.event);
		} catch (InterruptedException e) {
			// do nothing
		}

		remove();
	}

	/**
	 * Erase all the event for current Thread. Called after processing the events or
	 * then a rollback is done in the transaction
	 */
	public void remove() {
		if (threadLocal.get() == null) {
			return;
		}

		threadLocal.remove();
	}

	/**
	 * Inner class for storing the events using an order
	 * 
	 * @author Marc Gil - mgil@prodevelop.es
	 */
	private class OrderedEvent implements Comparable<OrderedEvent> {
		private final long seqNum;
		private AbstractDaoEvent<?> event;

		public OrderedEvent(AbstractDaoEvent<?> event) {
			this.seqNum = seq.getAndIncrement();
			this.event = event;
		}

		@Override
		public int compareTo(OrderedEvent o) {
			return seqNum < o.seqNum ? -1 : 1;
		}

		@Override
		public String toString() {
			return seqNum + " :: " + event.getEventId() + " :: " + event.getSource();
		}
	}

}
