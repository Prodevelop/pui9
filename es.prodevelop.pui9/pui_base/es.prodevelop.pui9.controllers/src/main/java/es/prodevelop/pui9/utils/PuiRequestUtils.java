package es.prodevelop.pui9.utils;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.google.common.net.HttpHeaders;

/**
 * Utility class to extract information from the Request
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiRequestUtils {

	private PuiRequestUtils() {
	}

	/**
	 * Extract the real client IP from the request
	 * 
	 * @param request The request
	 * @return The client IP
	 */
	public static String extractIp(HttpServletRequest request) {
		String ip = Optional.ofNullable(request.getHeader(HttpHeaders.X_FORWARDED_FOR)).orElse(request.getRemoteAddr());
		if (ip.contains(",")) {
			ip = ip.split(",")[0];
		}
		if (ip.contains(":")) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

}
