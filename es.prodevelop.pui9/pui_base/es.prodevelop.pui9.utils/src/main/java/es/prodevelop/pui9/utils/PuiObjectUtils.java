package es.prodevelop.pui9.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utility class to manage objects: copy properties, copy objects...
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiObjectUtils {

	private static Map<Class<?>, Map<String, Field>> mapCache = new HashMap<>();

	/**
	 * Copy a whole object into another one
	 * 
	 * @param <T>  The type of the object
	 * @param orig The original object
	 * @return A full copy of the given object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Object> T copyObject(T orig) {
		if (orig == null) {
			return null;
		}

		try {
			T dest = (T) orig.getClass().newInstance();
			copyProperties(dest, orig);
			return dest;
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	/**
	 * Copy all the attributes from the original object to the destination object
	 * 
	 * @param dest The destination object
	 * @param orig The original object
	 */
	public static void copyProperties(Object dest, Object orig) {
		if (dest == null || orig == null) {
			return;
		}

		Map<String, Field> destFields = getFields(dest.getClass());
		Map<String, Field> origFields = getFields(orig.getClass());

		for (Entry<String, Field> entry : destFields.entrySet()) {
			try {
				String destFieldName = entry.getKey();
				Field destField = entry.getValue();

				if (!origFields.containsKey(destFieldName)) {
					continue;
				}

				Field origField = origFields.get(destFieldName);

				if (!destField.getType().equals(origField.getType())) {
					continue;
				}

				Object value = origField.get(orig);
				destField.set(dest, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		}
	}

	/**
	 * Get all the fields from the Class, including the inherited ones
	 * 
	 * @param clazz The class type of the object
	 * @return A map with the name and Field
	 */
	private static Map<String, Field> getFields(Class<?> clazz) {
		if (!mapCache.containsKey(clazz)) {
			Map<String, Field> map = new HashMap<>();

			Class<?> toRegister = clazz;
			while (toRegister != null) {
				List<Field> fields = Arrays.asList(toRegister.getDeclaredFields());
				for (Field field : fields) {
					field.setAccessible(true);
					map.put(field.getName(), field);
				}
				toRegister = toRegister.getSuperclass();
			}

			mapCache.put(clazz, map);
		}

		return mapCache.get(clazz);
	}

	private PuiObjectUtils() {
	}

}
