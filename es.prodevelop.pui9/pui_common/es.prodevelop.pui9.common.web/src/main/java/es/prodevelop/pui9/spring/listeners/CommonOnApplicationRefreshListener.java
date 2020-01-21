package es.prodevelop.pui9.spring.listeners;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Lifecycle;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.messages.AbstractPuiMessages;
import es.prodevelop.pui9.spring.configuration.AbstractAppSpringConfiguration;

@Component
public class CommonOnApplicationRefreshListener {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private AbstractAppSpringConfiguration appConfig;

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (!event.getApplicationContext().equals(PuiApplicationContext.getInstance().getAppContext())) {
			return;
		}

		// obtain all the packages to scan
		List<String> packageScan = new ArrayList<>();
		packageScan.add("es.prodevelop");
		ComponentScan componentScan = ClassUtils.getUserClass(appConfig).getAnnotation(ComponentScan.class);
		if (componentScan != null) {
			packageScan.addAll(Arrays.asList(componentScan.basePackages()));
		}

		// force to load all the messages of the application
		new Reflections(packageScan.toArray()).getSubTypesOf(AbstractPuiMessages.class).forEach(pm -> {
			try {
				pm.getMethod(AbstractPuiMessages.GET_SINGLETON_METHOD_NAME).invoke(null);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// do nothing
			}
		});

		logger.info("PUI9 started. Triggering 'ContextStartedEvent' over the application");

		if (PuiApplicationContext.getInstance().getAppContext() instanceof Lifecycle) {
			((Lifecycle) PuiApplicationContext.getInstance().getAppContext()).start();
		}
	}
}