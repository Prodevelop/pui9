package es.prodevelop.pui9.utils;

import java.util.Collections;
import java.util.Iterator;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;
import org.springframework.web.util.UriComponentsBuilder;

import es.prodevelop.pui9.data.converters.PuiGsonHttpMessageConverter;
import es.prodevelop.pui9.exceptions.PuiException;

/**
 * Utility class to make external request from PUI server to anywhere. POST and
 * GET methods are accepted. It uses the {@link RestTemplate} object of Spring
 * to make the requests
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiExternalRequest {

	private static PuiExternalRequest singleton;
	private static final int defaultTimeout = 10000;

	public static PuiExternalRequest getSingleton() {
		if (singleton == null) {
			singleton = new PuiExternalRequest();
		}
		return singleton;
	}

	private final RestTemplate restTemplate;
	private final HttpHeaders httpHeaders;

	/**
	 * Configure the {@link RestTemplate} object with the valid converters for PUI
	 */
	private PuiExternalRequest() {
		restTemplate = new RestTemplate();
		httpHeaders = new HttpHeaders();

		if (restTemplate.getRequestFactory() instanceof SimpleClientHttpRequestFactory) {
			configureTemplateSetTimeouts(defaultTimeout);
		}

		configureTemplateRemoveJackson();
		configureTemplateSetConverters();
		configureTemplateEncoding();
		configureTemplateHeaders();
	}

	/**
	 * Change the timeout of the request. By default, 10000 milliseconds are set if
	 * the milliseconds parameter is null
	 * 
	 * @param milliseconds
	 */
	public void configureTemplateSetTimeouts(Integer milliseconds) {
		if (milliseconds == null) {
			milliseconds = defaultTimeout;
		}
		SimpleClientHttpRequestFactory factory = (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
		factory.setReadTimeout(milliseconds);
		factory.setConnectTimeout(milliseconds);
	}

	protected void configureTemplateRemoveJackson() {
		for (Iterator<HttpMessageConverter<?>> it = restTemplate.getMessageConverters().iterator(); it.hasNext();) {
			HttpMessageConverter<?> next = it.next();
			if (next instanceof org.springframework.http.converter.json.GsonHttpMessageConverter
					|| next.getClass().getSimpleName().contains("Jackson")
					|| next instanceof AllEncompassingFormHttpMessageConverter) {
				it.remove();
			}
		}
	}

	protected void configureTemplateSetConverters() {
		PuiGsonHttpMessageConverter gsonConverter = new PuiGsonHttpMessageConverter();
		restTemplate.getMessageConverters().add(0, gsonConverter);
	}

	protected void configureTemplateEncoding() {
		DefaultUriBuilderFactory uriFactory = new DefaultUriBuilderFactory();
		uriFactory.setEncodingMode(EncodingMode.VALUES_ONLY);
		restTemplate.setUriTemplateHandler(uriFactory);
	}

	protected void configureTemplateHeaders() {
		httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
	}

	/**
	 * Execute a GET request. This call is synchonous
	 * 
	 * @param url         The URL to be called. If you need to provide parameters,
	 *                    use the "params" attribute instead
	 * @param returnClass The type of the returning object (can be null if no
	 *                    response is expected)
	 * @param params      The list of parameters to be attached to the URL (can be
	 *                    null or empty)
	 * @param headers     The custom headers to be sent
	 * @return The result of the request as an object of the indicated type
	 * @throws PuiException If any exception is thrown in the request
	 */
	public <RETURN> RETURN executeGet(String url, Class<RETURN> returnClass, MultiValueMap<String, String> params,
			HttpHeaders headers) throws PuiException {
		if (!CollectionUtils.isEmpty(params)) {
			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).toUriString();
		}

		HttpEntity<Void> entity = new HttpEntity<>(configureHeaders(headers));

		try {
			ResponseEntity<RETURN> response = restTemplate.exchange(url, HttpMethod.GET, entity, returnClass);
			return response.getBody();
		} catch (RestClientResponseException e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Execute a POST request. This call is synchonous.
	 * 
	 * @param url         The URL to be called. If you need to provide parameters,
	 *                    use the "params" attribute instead
	 * @param returnClass The type of the returning object (can be null if no
	 *                    response is expected)
	 * @param body        The Body of the request (can be null)
	 * @param params      The list of parameters to be attached to the URL (can be
	 *                    null or empty)
	 * @param headers     The custom headers to be sent
	 * @return The result of the request as an object of the indicated type
	 * @throws PuiException If any exception is thrown in the request
	 */
	public <BODY, RETURN> RETURN executePost(String url, Class<RETURN> returnClass, BODY body,
			MultiValueMap<String, String> params, HttpHeaders headers) throws PuiException {
		if (!CollectionUtils.isEmpty(params)) {
			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).toUriString();
		}

		HttpEntity<BODY> entity = new HttpEntity<>(body, configureHeaders(headers));

		try {
			ResponseEntity<RETURN> response = restTemplate.postForEntity(url, entity, returnClass);
			return response.getBody();
		} catch (RestClientResponseException e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Execute a POST request. This call is synchonous.
	 * <p>
	 * This method is distinct from
	 * {@link #executePost(String, Class, Object, MultiValueMap)}, due to you can
	 * specify a parameterized type instead of a concrete type (for instance, a
	 * typed List or Map)
	 * 
	 * @param url        The URL to be called. If you need to provide parameters,
	 *                   use the "params" attribute instead
	 * @param returnType The type of the returning object (can be null if no
	 *                   response is expected)
	 * @param body       The Body of the request (can be null)
	 * @param params     The list of parameters to be attached to the URL (can be
	 *                   null or empty)
	 * @param headers    The custom headers to be sent
	 * @return The result of the request as an object of the indicated type
	 * @throws PuiException If any exception is thrown in the request
	 */
	public <BODY, RETURN> RETURN executePost(String url, ParameterizedTypeReference<RETURN> returnType, BODY body,
			MultiValueMap<String, String> params, HttpHeaders headers) throws PuiException {
		if (!CollectionUtils.isEmpty(params)) {
			url = UriComponentsBuilder.fromHttpUrl(url).queryParams(params).toUriString();
		}

		HttpEntity<BODY> entity = new HttpEntity<>(body, configureHeaders(headers));

		try {
			ResponseEntity<RETURN> response = restTemplate.exchange(url, HttpMethod.POST, entity, returnType);
			return response.getBody();
		} catch (RestClientResponseException e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Configure a new {@link HttpHeaders} using the one provided by the user. If
	 * headers are provided, they override the default headers
	 * 
	 * @param headers The headers provided by the user
	 * @return The new headers to be used
	 */
	private HttpHeaders configureHeaders(final HttpHeaders headers) {
		HttpHeaders newHeaders = headers != null ? headers : new HttpHeaders();

		httpHeaders.forEach(newHeaders::putIfAbsent);
		newHeaders.putIfAbsent(HttpHeaders.ACCEPT_LANGUAGE,
				Collections.singletonList(PuiLanguageUtils.getSessionLanguage().getIsocode()));

		return newHeaders;
	}

}
