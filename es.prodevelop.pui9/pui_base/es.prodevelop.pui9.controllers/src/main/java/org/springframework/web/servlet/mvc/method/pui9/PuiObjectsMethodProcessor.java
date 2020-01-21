package org.springframework.web.servlet.mvc.method.pui9;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;

import es.prodevelop.pui9.exceptions.PuiExceptionDto;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.utils.IPuiObject;

/**
 * This class treats all the returning values of the Web Services, before
 * returning the response to the clients
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiObjectsMethodProcessor extends AbstractMessageConverterMethodProcessor {

	private List<Class<?>> supportedTypes;
	private Map<Class<?>, Boolean> supportedTypeCache;

	public PuiObjectsMethodProcessor(List<HttpMessageConverter<?>> converters) {
		super(converters);

		supportedTypes = new ArrayList<>();
		supportedTypes.add(IPuiObject.class);
		supportedTypes.add(Collection.class);
		supportedTypes.add(Map.class);

		supportedTypeCache = new HashMap<>();
	}

	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		Class<?> type = returnType.getParameterType();

		if (!supportedTypeCache.containsKey(type)) {
			Boolean supported = Boolean.FALSE;
			for (Class<?> supportedType : supportedTypes) {
				if (supportedType.isAssignableFrom(type)) {
					supported = Boolean.TRUE;
					break;
				}
			}

			if (!supported) {
				supported = BeanUtils.isSimpleProperty(type) || Void.class.equals(type) || void.class.equals(type);
			}

			supportedTypeCache.put(type, supported);
		}

		return supportedTypeCache.get(type);
	}

	@Override
	public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);

		ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
		ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

		if (returnValue instanceof FileDownload) {
			FileDownload fd = (FileDownload) returnValue;

			String contentType = new MimetypesFileTypeMap().getContentType(fd.getFilename());
			MediaType mediaType = MediaType.parseMediaType(contentType);
			outputMessage.getHeaders().setContentType(mediaType);

			ContentDisposition cd;
			if (fd.isDownloadable()) {
				cd = ContentDisposition.builder("attachment").filename(fd.getFilename()).build();
			} else {
				cd = ContentDisposition.builder("inline").build();
			}
			outputMessage.getHeaders().setContentDisposition(cd);

			byte[] content;
			try {
				if (fd.getFile() != null) {
					content = FileCopyUtils.copyToByteArray(fd.getFile());
				} else if (fd.getInputStream() != null) {
					content = FileCopyUtils.copyToByteArray(fd.getInputStream());
				} else {
					content = new byte[0];
				}
			} catch (Exception e) {
				content = new byte[0];
			}

			outputMessage.getHeaders().setContentLength(content.length);

			returnValue = content;
		} else {
			if (returnValue instanceof PuiExceptionDto) {
				PuiExceptionDto ex = (PuiExceptionDto) returnValue;
				outputMessage.getServletResponse().setStatus(ex.getStatusCode());
				outputMessage.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			}
			outputMessage.getHeaders().setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
		}

		writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);

		outputMessage.flush();
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return null;
	}

}