package es.prodevelop.pui9.json.adapters;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.base.CaseFormat;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Type adapter for Class type to be used with GSON. Allows to serialize and
 * deserialize classes that conforms to {@link IDto} objects
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class IDtoClassTypeAdapter extends TypeAdapter<Class<?>> {

	@SuppressWarnings("unchecked")
	@Override
	public void write(JsonWriter out, Class<?> value) throws IOException {
		if (value == null || !IDto.class.isAssignableFrom(value)) {
			out.nullValue();
			return;
		}

		Class<IDto> dtoClass = (Class<IDto>) value;

		out.beginObject();

		out.name("name");
		out.value(dtoClass.getSimpleName().toLowerCase());
		out.name("classname");
		out.value(dtoClass.getCanonicalName());
		out.name("fields");

		out.beginArray();

		Map<String, Field> map = DtoRegistry.getMapFieldNameToJavaField(dtoClass);
		List<Method> methods = new ArrayList<>();
		methods.addAll(Arrays.asList(dtoClass.getMethods()));
		Collections.sort(methods, new MethodComparator());

		for (Method method : methods) {
			boolean nonBooleanAccessor = method.getName().startsWith("get");
			boolean booleanAccessor = method.getName().startsWith("is");
			if ((nonBooleanAccessor || booleanAccessor) && !method.getName().equals("getClass")
					&& method.getParameterTypes().length == 0 && method.getReturnType() != null) {
				try {
					out.beginObject();
					String name = method.getName().substring(nonBooleanAccessor ? 3 : 2);
					name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, name);
					PuiField puiField = null;
					PuiViewColumn puiViewColumn = null;
					try {
						puiField = map.get(name).getAnnotation(PuiField.class);
						puiViewColumn = map.get(name).getAnnotation(PuiViewColumn.class);
					} catch (Exception e) {
					}
					out.name("name");
					out.value(name);
					out.name("type");
					out.value(method.getGenericReturnType().toString().replace("class", "").trim());
					out.name("ispk");
					out.value(puiField != null ? puiField.ispk() : false);
					out.name("autoincrementable");
					out.value(puiField != null ? puiField.autoincrementable() : false);
					out.name("nullable");
					out.value(puiField != null ? puiField.nullable() : false);
					out.name("maxlength");
					out.value(puiField != null ? puiField.maxlength() : -1);
					if (puiViewColumn != null) {
						out.name("visibility");
						out.value(puiViewColumn.visibility().name());
						out.name("order");
						out.value(puiViewColumn.order());
					}
				} catch (Exception e) {
					throw new IOException("problem writing json: " + e);
				} finally {
					out.endObject();
				}
			}
		}
		out.endArray();

		out.endObject();
	}

	@Override
	public Class<?> read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		} else {
			throw new UnsupportedOperationException(
					"Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
		}
	}

	private class MethodComparator implements Comparator<Method> {
		@Override
		public int compare(Method o1, Method o2) {
			return o1.getName().compareTo(o2.getName());
		}
	}
}