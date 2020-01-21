package es.prodevelop.pui9.json.adapters;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import es.prodevelop.pui9.model.dto.DtoFactory;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Type adapter for {@link IDto} type to be used with GSON. Allows to serialize
 * and deserialize {@link IDto} objects that are set as interfaces, looking for
 * the correct implementation class to use in the serialization/deserialization
 * process
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class IDtoTypeAdapter extends TypeAdapter<IDto> {

	public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
		@Override
		@SuppressWarnings("unchecked")
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
			if (!(type.getType() instanceof Class<?>)) {
				return null;
			}

			Class<?> clazz = (Class<?>) type.getType();
			if (!IDto.class.isAssignableFrom(clazz) || !clazz.isInterface()) {
				return null;
			}

			Class<? extends IDto> dtoIface = (Class<? extends IDto>) clazz;
			// try to find the implementation in the Dto Factory cache
			Class<? extends IDto> dtoClass = DtoFactory.getClassFromInterface(dtoIface);
			if (dtoClass == null) {
				// if not exists, try to find the implementation in the loaded classes
				dtoClass = DtoRegistry.getDtoImplementation(dtoIface, false);
				if (dtoClass == null) {
					// if not exists, something strange is happening...
					throw new IllegalStateException();
				}
			}

			return (TypeAdapter<T>) new IDtoTypeAdapter(gson, dtoClass);
		}
	};

	private final Gson gson;
	private final Class<? extends IDto> clazz;

	private IDtoTypeAdapter(Gson gson, Class<? extends IDto> clazz) {
		this.gson = gson;
		this.clazz = clazz;
	}

	@Override
	@SuppressWarnings("unchecked")
	public IDto read(JsonReader in) throws IOException {
		TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) (Object) gson.getAdapter(clazz);
		return (IDto) typeAdapter.read(in);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void write(JsonWriter out, IDto value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}

		TypeAdapter<Object> typeAdapter = (TypeAdapter<Object>) (Object) gson.getAdapter(clazz);

		typeAdapter.write(out, value);
	}
}