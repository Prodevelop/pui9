package es.prodevelop.pui9.data.converters;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.http.converter.json.AbstractJsonHttpMessageConverter;
import org.springframework.stereotype.Component;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import es.prodevelop.pui9.controllers.exceptions.PuiControllersFromJsonException;
import es.prodevelop.pui9.json.GsonSingleton;

/**
 * Own implementation of a Gson Converter, using the {@link GsonSingleton} class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiGsonHttpMessageConverter extends AbstractJsonHttpMessageConverter {

	@Override
	protected Object readInternal(Type resolvedType, Reader reader) throws Exception {
		try {
			return GsonSingleton.getSingleton().getGson().fromJson(reader, resolvedType);
		} catch (JsonSyntaxException | JsonIOException e) {
			throw new PuiControllersFromJsonException(
					e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		}
	}

	@Override
	protected void writeInternal(Object o, Type type, Writer writer) throws Exception {
		try {
			if (type instanceof ParameterizedType) {
				GsonSingleton.getSingleton().getGson().toJson(o, type, writer);
			} else {
				GsonSingleton.getSingleton().getGson().toJson(o, writer);
			}
		} catch (JsonIOException e) {
			throw new PuiControllersFromJsonException(
					e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
		}
	}

}