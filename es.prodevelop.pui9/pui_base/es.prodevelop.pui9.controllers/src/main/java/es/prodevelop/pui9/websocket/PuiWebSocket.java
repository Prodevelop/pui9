package es.prodevelop.pui9.websocket;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

/**
 * This class allows to send messages over WebSockets to the clients
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiWebSocket {

	@Autowired
	private SimpMessagingTemplate template;

	private List<String> jwts;

	private PuiWebSocket() {
	}

	/**
	 * Init a timer to remove all the sessions at night
	 */
	@PostConstruct
	private void postConstruct() {
		this.jwts = new ArrayList<>();

		Duration initDelay = Duration.between(LocalDateTime.now(), LocalDate.now().plusDays(1).atStartOfDay());
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("WebsocketsCleanerAtNight", true,
				initDelay.toMinutes(), TimeUnit.DAYS.toMinutes(1), TimeUnit.MINUTES, () -> jwts.clear());
	}

	/**
	 * Send a message to the user identified by the given JWT token, for the given
	 * Type and the given Object
	 * 
	 * @param jwt    The JWT session token of the user
	 * @param type   The type of the websocket (to filter the message)
	 * @param object The object to be sent
	 */
	public void sendMessage(String jwt, String type, Object object) {
		if (!StringUtils.isEmpty(jwt) && !StringUtils.isEmpty(type)) {
			template.convertAndSendToUser(jwt, "/response", object, Collections.singletonMap("type", type));
		}
	}

	/**
	 * Broadcast a message to all the available sessions
	 * 
	 * @param type   The type of the websocket (to filter the message)
	 * @param object The object to be sent
	 */
	public void broadcastMessage(String type, Object object) {
		synchronized (jwts) {
			for (String jwt : jwts) {
				sendMessage(jwt, type, object);
			}
		}
	}

	/**
	 * Register the given JWT session (when a user starts a session in the client)
	 * 
	 * @param jwt The JWT session token
	 */
	public void registerJwt(String jwt) {
		if (!jwts.contains(jwt)) {
			jwts.add(jwt);
		}
	}

	/**
	 * Unregister the given JWT session (when a user ends a session in the client)
	 * 
	 * @param jwt The JWT session token
	 */
	public void unregisterJwt(String jwt) {
		if (jwt.contains(jwt)) {
			jwts.remove(jwt);
		}
	}

}
