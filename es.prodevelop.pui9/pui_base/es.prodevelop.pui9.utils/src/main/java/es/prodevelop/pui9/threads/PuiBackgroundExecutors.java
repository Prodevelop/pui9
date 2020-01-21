package es.prodevelop.pui9.threads;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * This singleton class helps to create backbround executors that will perform
 * actions periodically.
 * <p>
 * An executor could be defined as daemon (daemon thread) or not, depending on
 * the action to be performed.
 * <p>
 * For instance, if it's a executor that only refreshes a cache, this can be
 * defined as daemon thread.
 * <p>
 * In other hand, if you want to execute some task that it's important to be
 * finished, it will not be declared as daemon (but as user thread)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiBackgroundExecutors {

	private static PuiBackgroundExecutors instance;

	public static PuiBackgroundExecutors getSingleton() {
		if (instance == null) {
			instance = new PuiBackgroundExecutors();
		}
		return instance;
	}

	private PuiBackgroundExecutors() {
	}

	/**
	 * Registers a new executor that will be executed on the defined delay (what
	 * means that subsequent executions will take part in the 'delay' timeUnit after
	 * the completion of the previous execution)
	 * 
	 * @param name         The name of the Executor that will be used for the
	 *                     created Thread
	 * @param isDaemon     If it's a Daemon or not
	 * @param initialDelay The initial execution delay before the first execution
	 * @param delay        The delay between subsequent executions
	 * @param unit         The unit of the delays
	 * @param runnable     The runnable to be executed
	 */
	public void registerNewExecutor(String name, boolean isDaemon, long initialDelay, long delay, TimeUnit unit,
			Runnable runnable) {
		Executors
				.newScheduledThreadPool(1,
						new ThreadFactoryBuilder().setDaemon(isDaemon).setNameFormat("PuiExecutor_" + name).build())
				.scheduleWithFixedDelay(runnable, initialDelay, delay, unit);
	}

}
