package es.prodevelop.pui9.json;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;

import es.prodevelop.pui9.json.adapters.BigDecimalTypeAdapter;
import es.prodevelop.pui9.json.adapters.DoubleTypeAdapter;
import es.prodevelop.pui9.json.adapters.InstantTypeAdapter;
import es.prodevelop.pui9.json.adapters.IntegerTypeAdapter;
import es.prodevelop.pui9.json.adapters.LocalDateTypeAdapter;
import es.prodevelop.pui9.json.adapters.LongTypeAdapter;
import es.prodevelop.pui9.json.adapters.ObjectTypeAdapter;

/**
 * A singleton class that returns a generic Gson configured properly for the
 * application. Can be used everywhere, ensuring that all the serializations and
 * deserializations will be done using the same Gson. <br>
 * <br>
 * Ensure calling {@link #getGson()} method after registering all the type
 * adapters that you want to use.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class GsonSingleton {

	public static final String DEFAULT_ID = "default";

	private static GsonSingleton instance;

	public static synchronized GsonSingleton getSingleton() {
		if (instance == null) {
			instance = new GsonSingleton();
		}
		return instance;
	}

	private Map<String, GsonBuilder> cacheGsonBuilder;
	private Map<String, Gson> cacheGson;

	private GsonSingleton() {
		cacheGsonBuilder = new HashMap<>();
		cacheGson = new HashMap<>();

		createDefaultGson();
	}

	private Gson createDefaultGson() {
		GsonBuilder builder = createGsonBuilder(DEFAULT_ID);

		builder.registerTypeAdapterFactory(TypeAdapters.newFactory(Instant.class, new InstantTypeAdapter()));
		builder.registerTypeAdapterFactory(TypeAdapters.newFactory(LocalDate.class, new LocalDateTypeAdapter()));
		builder.registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, new LongTypeAdapter()));
		builder.registerTypeAdapterFactory(
				TypeAdapters.newFactory(double.class, Double.class, new DoubleTypeAdapter()));
		builder.registerTypeAdapterFactory(TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter()));
		builder.registerTypeAdapterFactory(TypeAdapters.newFactory(BigDecimal.class, new BigDecimalTypeAdapter()));
		builder.registerTypeAdapterFactory(ObjectTypeAdapter.FACTORY);

		builder.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE, Modifier.STATIC);
		builder.serializeNulls();

		// only in debug mode, set pretty print in order to make easy the
		// debugging for us, poor developers...
		boolean isDebug = ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("jdwp") >= 0;
		if (isDebug) {
			builder.setPrettyPrinting();
		}

		return createGson(DEFAULT_ID);
	}

	/**
	 * Creates a GsonBuilder for the given Gson ID and stores it in cache
	 */
	private synchronized GsonBuilder createGsonBuilder(String gsonId) {
		if (cacheGsonBuilder.containsKey(gsonId)) {
			cacheGsonBuilder.remove(gsonId);
		}
		GsonBuilder builder = new GsonBuilder();
		cacheGsonBuilder.put(gsonId, builder);
		return builder;
	}

	/**
	 * Creates a new Gson for the given ID
	 */
	@SuppressWarnings("unchecked")
	private synchronized Gson createGson(String gsonId) {
		if (cacheGson.containsKey(gsonId)) {
			cacheGson.remove(gsonId);
		}
		Gson gson = getGsonBuilder(gsonId).create();
		try {
			Field field = Gson.class.getDeclaredField("factories");
			field.setAccessible(true);
			List<TypeAdapterFactory> list = (List<TypeAdapterFactory>) field.get(gson);
			List<TypeAdapterFactory> newList = new ArrayList<>();
			for (TypeAdapterFactory taf : list) {
				if (!(taf.equals(com.google.gson.internal.bind.ObjectTypeAdapter.FACTORY))) {
					newList.add(taf);
				}
			}
			field.set(gson, Collections.unmodifiableList(newList));
			field.setAccessible(false);
		} catch (Exception e) {
			// do nothing
		}
		cacheGson.put(gsonId, gson);
		return gson;
	}

	/**
	 * Gets the GsonBuilder for the given Gson ID. If doesn't exist, creates it
	 */
	private GsonBuilder getGsonBuilder(String gsonId) {
		GsonBuilder builder = cacheGsonBuilder.get(gsonId);
		if (builder == null) {
			builder = createGsonBuilder(gsonId);
		}
		return builder;
	}

	/**
	 * Set serialize nulls property to the DEFAULT Gson
	 */
	public void setSerializeNulls() {
		setSerializeNulls(DEFAULT_ID);
	}

	/**
	 * Set serialize nulls property to the Gson of the given ID
	 * 
	 * @param gsonId The gson id
	 */
	public void setSerializeNulls(String gsonId) {
		getGsonBuilder(gsonId).serializeNulls();
		createGson(gsonId);
	}

	/**
	 * Set pretty printing property to the DEFAULT Gson
	 */
	public void setPrettyPrinting() {
		setPrettyPrinting(DEFAULT_ID);
	}

	/**
	 * Set pretty printing property to the Gson of the given ID
	 * 
	 * @param gsonId The gson id
	 */
	public void setPrettyPrinting(String gsonId) {
		getGsonBuilder(gsonId).setPrettyPrinting();
		createGson(gsonId);
	}

	/**
	 * Exclude the fields with given notifiers to the DEFAULT Gson
	 * 
	 * @param modifiers The modifiers
	 */
	public void excludeFieldsWithModifiers(int... modifiers) {
		excludeFieldsWithModifiers(DEFAULT_ID, modifiers);
	}

	/**
	 * Exclude the fields with given notifiers to the Gson of the given ID
	 * 
	 * @param gsonId    The gson id
	 * @param modifiers The modifiers
	 */
	public void excludeFieldsWithModifiers(String gsonId, int... modifiers) {
		getGsonBuilder(gsonId).excludeFieldsWithModifiers(modifiers);
		createGson(gsonId);
	}

	/**
	 * Registers a new type adapter for Gson serialization and deserialization to
	 * the DEFAULT Gson
	 * 
	 * @param type        The type to assign an adapter
	 * @param typeAdapter The registered adapter
	 */
	public void registerTypeAdapter(Type type, Object typeAdapter) {
		registerTypeAdapter(DEFAULT_ID, type, typeAdapter);
	}

	/**
	 * Registers a new type adapter for Gson serialization and deserialization to
	 * the Gson of the given ID
	 * 
	 * @param gsonId      The gson id
	 * @param type        The type to assign an adapter
	 * @param typeAdapter The registered adapter
	 */
	public void registerTypeAdapter(String gsonId, Type type, Object typeAdapter) {
		getGsonBuilder(gsonId).registerTypeAdapter(type, typeAdapter);
		createGson(gsonId);
	}

	/**
	 * Registers a new type adapter factory for Gson serialization and
	 * deserialization to the DEFAULT Gson
	 * 
	 * @param typeAdapterFactory The type adapter factory to be registered
	 */
	public void registerTypeAdapterFactory(TypeAdapterFactory typeAdapterFactory) {
		registerTypeAdapterFactory(DEFAULT_ID, typeAdapterFactory);
	}

	/**
	 * Registers a new type adapter factory for Gson serialization and
	 * deserialization to the Gson of the given ID
	 * 
	 * @param gsonId             The gson id
	 * @param typeAdapterFactory The type adapter factory to be registered
	 */
	public void registerTypeAdapterFactory(String gsonId, TypeAdapterFactory typeAdapterFactory) {
		getGsonBuilder(gsonId).registerTypeAdapterFactory(typeAdapterFactory);
		createGson(gsonId);
	}

	/**
	 * Sets a File Naming Strategy for the DEFAULT Gson
	 * 
	 * @param strategy The file name strategy to be registered
	 */
	public void setFieldNamingStrategy(FieldNamingStrategy strategy) {
		setFieldNamingStrategy(DEFAULT_ID, strategy);
	}

	/**
	 * Sets a File Naming Strategy for the given Gson ID
	 * 
	 * @param gsonId   The gson id
	 * @param strategy The file name strategy to be registered
	 */
	public void setFieldNamingStrategy(String gsonId, FieldNamingStrategy strategy) {
		getGsonBuilder(gsonId).setFieldNamingStrategy(strategy);
		createGson(gsonId);
	}

	/**
	 * Sets an Exclusion Strategy for the DEFAULT Gson
	 * 
	 * @param strategy The exclusion strategy to be registered
	 */
	public void setExclusionStrategies(ExclusionStrategy strategy) {
		setExclusionStrategies(DEFAULT_ID, strategy);
	}

	/**
	 * Sets an Exclusion Strategy for the given Gson ID
	 * 
	 * @param gsonId   The gson id
	 * @param strategy The exclusion strategy to be registered
	 */
	public void setExclusionStrategies(String gsonId, ExclusionStrategy strategy) {
		getGsonBuilder(gsonId).setExclusionStrategies(strategy);
		createGson(gsonId);
	}

	/**
	 * Get the DEFAULT Gson
	 * 
	 * @return The default gson
	 */
	public Gson getGson() {
		return getGson(DEFAULT_ID);
	}

	/**
	 * Get the Gson with the given ID. If doesn't exist, creates it
	 * 
	 * @param gsonId The gson id
	 * @return The gson
	 */
	public Gson getGson(String gsonId) {
		if (!cacheGson.containsKey(gsonId)) {
			createGson(gsonId);
		}
		return cacheGson.get(gsonId);
	}

}
