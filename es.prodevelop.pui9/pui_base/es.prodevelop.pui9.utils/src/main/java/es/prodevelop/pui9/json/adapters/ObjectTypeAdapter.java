package es.prodevelop.pui9.json.adapters;

import java.io.IOException;
import java.math.BigDecimal;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Type adapter for generic Object type to be used with GSON. Adapts types whose
 * static type is only 'Object'. This Object adapter is used instead of the one
 * provided by GSON due to an error when parsing Numbers: they should be treated
 * as BigDecimals or Integers, and not always as Doubles
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ObjectTypeAdapter extends TypeAdapter<Object> {
	public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
		@SuppressWarnings("unchecked")
		@Override
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
			if (type.getRawType() == Object.class) {
				return (TypeAdapter<T>) new ObjectTypeAdapter(gson);
			}
			return null;
		}
	};

	com.google.gson.internal.bind.ObjectTypeAdapter objectTypeAdapter;

	private ObjectTypeAdapter(Gson gson) {
		this.objectTypeAdapter = (com.google.gson.internal.bind.ObjectTypeAdapter) com.google.gson.internal.bind.ObjectTypeAdapter.FACTORY
				.create(gson, TypeToken.get(Object.class));
	}

	@Override
	public Object read(JsonReader in) throws IOException {
		JsonToken token = in.peek();
		switch (token) {
		case NUMBER:
			String value = in.nextString();
			BigDecimal bd = new BigDecimal(value);
			if (bd.scale() > 0) {
				return bd;
			} else {
				return Integer.valueOf(bd.intValue());
			}
		default:
			return objectTypeAdapter.read(in);
		}
	}

	@Override
	public void write(JsonWriter out, Object value) throws IOException {
		objectTypeAdapter.write(out, value);
	}
}
