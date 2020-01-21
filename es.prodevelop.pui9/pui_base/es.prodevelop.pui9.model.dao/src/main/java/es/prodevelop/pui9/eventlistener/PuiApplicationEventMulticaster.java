package es.prodevelop.pui9.eventlistener;

import java.util.concurrent.Executor;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * This class represents the multicaster event for pulbishing the events through
 * the application. If a task executor is specified
 * ({@link #setTaskExecutor(Executor)}), the events will be processed a
 * asynchrous way; if not, in a synchronous way
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component(value = "applicationEventMulticaster")
public class PuiApplicationEventMulticaster extends SimpleApplicationEventMulticaster {

	private PuiApplicationEventMulticaster() {
	}

	@Override
	public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
		ResolvableType type = (eventType != null ? eventType : ResolvableType.forInstance(event));
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) {
			Executor executor = getTaskExecutor();
			if (executor != null) {
				executor.execute(() -> {
					SecurityContextHolder.getContext().setAuthentication(auth);
					invokeListener(listener, event);
				});
			} else {
				invokeListener(listener, event);
			}
		}
	}

}
