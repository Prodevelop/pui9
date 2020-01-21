package es.prodevelop.pui9.spring.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.sockjs.frame.AbstractSockJsMessageCodec;

import com.google.gson.Gson;

import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;
import es.prodevelop.pui9.websocket.PuiWebSocket;

/**
 * Spring configuration for the PUI WebSockets messaging service. Basically
 * registers an endpoint for the application named <b>endpointpuisocket</b>. The
 * clients should register itself to this endpoint, with the session id for the
 * registered user
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
@EnableWebSocketMessageBroker
public class PuiWebSocketSpringConfiguration implements WebSocketMessageBrokerConfigurer {

	@Autowired
	private PuiWebSocket puiWebSocket;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/user");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/endpointpuisocket").setAllowedOrigins("*").withSockJS()
				.setMessageCodec(new GsonSockJsMessageCodec());
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		ChannelInterceptor interceptor = new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				// store or remove the Session ID in the list depending on the
				// message is for Connecting or Disconnecting
				StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
				StompCommand command = accessor.getCommand();
				List<String> headersJwt = accessor.getNativeHeader("jwt");
				if (!CollectionUtils.isEmpty(headersJwt)) {
					String jwt = headersJwt.get(0);

					switch (command) {
					case CONNECT:
						puiWebSocket.registerJwt(jwt);
						break;
					case DISCONNECT:
						puiWebSocket.unregisterJwt(jwt);
						break;
					default:
						break;
					}
				}

				return message;
			}
		};
		registration.interceptors(interceptor);
	}

	private class GsonSockJsMessageCodec extends AbstractSockJsMessageCodec {

		private Gson gson = new Gson();

		@Override
		public String[] decode(String content) throws IOException {
			return gson.fromJson(content, String[].class);
		}

		@Override
		public String[] decodeInputStream(InputStream content) throws IOException {
			return gson.fromJson(new InputStreamReader(content, StandardCharsets.UTF_8), String[].class);
		}

		@Override
		protected char[] applyJsonQuoting(String content) {
			String str = gson.toJson(content);
			str = str.substring(1, str.length() - 1);
			return str.toCharArray();
		}

	}

}