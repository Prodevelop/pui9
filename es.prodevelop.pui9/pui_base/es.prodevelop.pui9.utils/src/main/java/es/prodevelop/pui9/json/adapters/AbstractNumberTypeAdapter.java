package es.prodevelop.pui9.json.adapters;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Abstract TypeAdapter class for Numeric types, used by GSON to serialize and
 * deserialize objects
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractNumberTypeAdapter<N extends Number> extends TypeAdapter<N> {

	@Override
	public void write(JsonWriter out, N value) throws IOException {
		out.value(value);
	}

	@Override
	public N read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}

		try {
			String result = in.nextString();
			if (StringUtils.isEmpty(result)) {
				return null;
			}
			return parse(result);
		} catch (NumberFormatException e) {
			throw new JsonSyntaxException(e);
		}
	}

	/**
	 * Convert the String into the correct Numeric type
	 * 
	 * @param result The value serialized
	 * @return The value deserialized
	 */
	protected abstract N parse(String result);

}
