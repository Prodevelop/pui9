package es.prodevelop.pui9.notifications.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.ApsAlert;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.service.interfaces.IPuiUserFcmService;

/**
 * This component initializes the Firebase Application using the configuration
 * set in the firebase.json file.
 * <p>
 * Access to <a href="https://console.firebase.google.com/">this website</a>
 * configure the firebase project
 * <p>
 * <a href=
 * "https://golb.hplar.ch/2018/01/Sending-Web-push-messages-from-Spring-Boot-to-Browsers.html">This
 * other website</a> contains an example about configuring a new project with
 * Spring and Firebase
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiFcmClient {

	private static final String FIREBASE_JSON_FILENAME = "firebase.json";
	private static final int MAX_TOKENS_PER_REQUEST = 100;
	private static final String TOPIC_NAME_ANTI_PATTERN = "[^a-zA-Z0-9-_.~%]+";
	private static final String TOPIC_NAME_REPLACEMENT = "_";

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	private IPuiUserFcmService userFcmService;

	private PuiFcmClient() {
		// do nothing
	}

	/**
	 * Initializes the Firebase Application with the data in the firebase.json file
	 */
	@PostConstruct
	private void postConstruct() throws IllegalArgumentException {
		try {
			InputStream serviceAccountInputStream = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(FIREBASE_JSON_FILENAME);
			if (serviceAccountInputStream == null) {
				throw new IllegalArgumentException(
						"Firebase Json file not found in the classpath. It should exist at root with the name '"
								+ FIREBASE_JSON_FILENAME + "'");
			}
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccountInputStream)).build();

			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			logger.error("Error initializing Firebase App", e);
		}
	}

	/**
	 * Subscribe the user to the given topic (for all its tokens)
	 * 
	 * @param usr   The user to be subscribed
	 * @param token The token to be subscribed (nullable)
	 * @param topic The topic to be subscribed
	 */
	public void subscribeUserToTopic(String usr, String token, String topic) {
		if (StringUtils.isEmpty(usr) || StringUtils.isEmpty(topic)) {
			return;
		}

		List<String> tokens = new ArrayList<>();
		if (StringUtils.isEmpty(token)) {
			tokens.addAll(getUserTokens(usr));
		} else {
			tokens.add(token);
		}

		subscribeTokensToTopic(topic, tokens);
	}

	/**
	 * Unsubscribe the user from the given topic (for all its tokens)
	 * 
	 * @param usr   The user to be unsubscribe
	 * @param token The token to be unsubscribe (nullable)
	 * @param topic The topic to be unsubscribe
	 */
	public void unsubscribeUserFromTopic(String usr, String token, String topic) {
		if (StringUtils.isEmpty(usr) || StringUtils.isEmpty(topic)) {
			return;
		}

		List<String> tokens = new ArrayList<>();
		if (StringUtils.isEmpty(token)) {
			tokens.addAll(getUserTokens(usr));
		} else {
			tokens.add(token);
		}

		unsubscribeTokensFromTopic(topic, tokens);
	}

	/**
	 * Subscribe the given user tokens to the given topic
	 * 
	 * @param topic  The topic to be subscribed to
	 * @param tokens The list of user tokens to subscribe
	 * 
	 * @return true if the token is subscribed successfully; false if not
	 */
	private void subscribeTokensToTopic(String topic, List<String> tokens) {
		if (StringUtils.isEmpty(topic) || CollectionUtils.isEmpty(tokens)) {
			return;
		}

		topic = variableService.getVariable(PuiVariableValues.FCM_TOPIC_PREFIX.name()) + modifyTopic(topic);
		logger.debug("Subscribe to topic '" + topic + "' the tokens: " + String.join(",", tokens));
		FirebaseMessaging.getInstance().subscribeToTopicAsync(tokens, topic);
	}

	/**
	 * Unsubscribe the given user tokens from the given topic
	 * 
	 * @param topic  The topic to be unsubscribed from
	 * @param tokens The list of user tokens to unsubscribe
	 * 
	 * @return true if the token is unsubscribed successfully; false if not
	 */
	private void unsubscribeTokensFromTopic(String topic, List<String> tokens) {
		if (StringUtils.isEmpty(topic) || CollectionUtils.isEmpty(tokens)) {
			return;
		}

		topic = variableService.getVariable(PuiVariableValues.FCM_TOPIC_PREFIX.name()) + modifyTopic(topic);
		logger.debug("Unsubscribe from topic '" + topic + "' the tokens: " + String.join(",", tokens));
		FirebaseMessaging.getInstance().unsubscribeFromTopicAsync(tokens, topic);
	}

	/**
	 * Send a message to a topic with the given given title, body and data. An
	 * Android, iOS and WebPush notification is created
	 * 
	 * @param topic The topic to send the message
	 * @param title The title of the notification
	 * @param body  The bodyof the notification
	 * @param data  the data to be sent
	 * 
	 * @return true if the message was sent successfully; false if not
	 */
	public void sendToTopic(String topic, String title, String body, Map<String, String> data) {
		Boolean enabled = variableService.getVariable(Boolean.class, PuiVariableValues.FCM_ENABLED.name());
		if (enabled == null || enabled.equals(Boolean.FALSE)) {
			return;
		}

		if (StringUtils.isEmpty(topic)) {
			return;
		}

		topic = variableService.getVariable(PuiVariableValues.FCM_TOPIC_PREFIX.name()) + modifyTopic(topic);

		Message.Builder messageBuilder = Message.builder();

		if (!CollectionUtils.isEmpty(data)) {
			messageBuilder.putAllData(data);
		}
		messageBuilder.setTopic(topic);

		// android
		AndroidNotification android = AndroidNotification.builder().setTitle(title).setBody(body).build();
		messageBuilder.setAndroidConfig(AndroidConfig.builder().setNotification(android).build());

		// ios
		ApsAlert apsAlert = ApsAlert.builder().setTitle(title).setBody(body).build();
		Aps aps = Aps.builder().setAlert(apsAlert).build();
		messageBuilder.setApnsConfig(ApnsConfig.builder().setAps(aps).build());

		// web push
		WebpushNotification webPush = WebpushNotification.builder().setTitle(title).setBody(body).build();
		messageBuilder.setWebpushConfig(WebpushConfig.builder().setNotification(webPush).build());

		logger.debug("Send notification to topic '" + topic + "'");
		FirebaseMessaging.getInstance().sendAsync(messageBuilder.build());
	}

	/**
	 * Send a message to the given user with the given title, body and data. An
	 * Android, iOS and WebPush notification is created
	 * 
	 * @param user  The list of tokens to send the message
	 * @param title The title of the notification
	 * @param body  The bodyof the notification
	 * @param data  the data to be sent
	 * 
	 * @return true if the message was sent successfully; false if not
	 */
	public void sendToUser(String user, String title, String body, Map<String, String> data) {
		Boolean enabled = variableService.getVariable(Boolean.class, PuiVariableValues.FCM_ENABLED.name());
		if (enabled == null || enabled.equals(Boolean.FALSE)) {
			return;
		}

		List<String> tokens = getUserTokens(user);
		logger.debug("Send notification to user '" + user + "'");
		sendToTokens(tokens, title, body, data);
	}

	/**
	 * Send a message to the given tokens with the given title, body and data. An
	 * Android, iOS and WebPush notification is created
	 * 
	 * @param tokens The list of tokens to send the message
	 * @param title  The title of the notification
	 * @param body   The bodyof the notification
	 * @param data   the data to be sent
	 * 
	 * @return true if the message was sent successfully; false if not
	 */
	public void sendToTokens(List<String> tokens, String title, String body, Map<String, String> data) {
		Boolean enabled = variableService.getVariable(Boolean.class, PuiVariableValues.FCM_ENABLED.name());
		if (enabled == null || enabled.equals(Boolean.FALSE)) {
			return;
		}

		if (CollectionUtils.isEmpty(tokens)) {
			return;
		}

		logger.debug("Send notification to tokens: " + String.join(",", tokens));
		AtomicInteger counter = new AtomicInteger(0);
		Collection<List<String>> partitionedTokens = tokens.stream()
				.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / MAX_TOKENS_PER_REQUEST)).values();
		partitionedTokens.forEach(tokenList -> {
			MulticastMessage.Builder multicastMessageBuilder = MulticastMessage.builder().putAllData(data)
					.addAllTokens(tokenList);

			if (!CollectionUtils.isEmpty(data)) {
				multicastMessageBuilder.putAllData(data);
			}

			// android
			AndroidNotification android = AndroidNotification.builder().setTitle(title).setBody(body).build();
			multicastMessageBuilder.setAndroidConfig(AndroidConfig.builder().setNotification(android).build());

			// ios
			ApsAlert apsAlert = ApsAlert.builder().setTitle(title).setBody(body).build();
			Aps aps = Aps.builder().setAlert(apsAlert).build();
			multicastMessageBuilder.setApnsConfig(ApnsConfig.builder().setAps(aps).build());

			// web push
			WebpushNotification webPush = WebpushNotification.builder().setTitle(title).setBody(body).build();
			multicastMessageBuilder.setWebpushConfig(WebpushConfig.builder().setNotification(webPush).build());

			FirebaseMessaging.getInstance().sendMulticastAsync(multicastMessageBuilder.build());
		});
	}

	/**
	 * Validate the list of user tokens
	 * 
	 * @param tokens The list of user tokens to be validated
	 * @return A non-null list with the invalid tokens
	 */
	List<String> validateTokens(List<String> tokens) {
		if (CollectionUtils.isEmpty(tokens)) {
			return Collections.emptyList();
		}

		List<String> badTokens = new ArrayList<>();
		AtomicInteger counter = new AtomicInteger(0);
		Collection<List<String>> partitionedTokens = tokens.stream()
				.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / MAX_TOKENS_PER_REQUEST)).values();
		partitionedTokens.forEach(tokenList -> {
			try {
				BatchResponse br = FirebaseMessaging.getInstance()
						.sendMulticast(MulticastMessage.builder().addAllTokens(tokenList).build(), true);
				IntStream.range(0, br.getResponses().size()).boxed()
						.collect(Collectors.toMap(i -> i, br.getResponses()::get)).entrySet().stream()
						.filter(entry -> entry.getValue().getException() != null)
						.forEach(entry -> badTokens.add(tokenList.get(entry.getKey())));
			} catch (FirebaseMessagingException e) {
				logger.error("Error validating tokens", e);
				Thread.currentThread().interrupt();
			}
		});

		return badTokens;
	}

	/**
	 * By default, topics has a limitations on the name, that should accomplish the
	 * PATTERN "[a-zA-Z0-9-_.~%]+". So the name should be modified replacing all not
	 * matching characters
	 * 
	 * @param topic The original topic name
	 * @return The modified topic name
	 */
	private String modifyTopic(String topic) {
		return topic.replaceAll(TOPIC_NAME_ANTI_PATTERN, TOPIC_NAME_REPLACEMENT);
	}

	private List<String> getUserTokens(String usr) {
		try {
			return userFcmService.getTableDao().findByUsr(usr).stream()
					.collect(Collectors.mapping(IPuiUserFcm::getToken, Collectors.toList()));
		} catch (PuiDaoFindException e) {
			return Collections.emptyList();
		}
	}

}