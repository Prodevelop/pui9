package es.prodevelop.pui9.model.dto;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * This Factory class is a utility class to manage DTOs, allowing to retrieve
 * the concret class of an interface of type {@link IDto}. See
 * {@link DtoRegistry} class for more information about utility methods with DTO
 * objects and classes
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DtoFactory {

	private static Map<Class<? extends IDto>, Class<? extends AbstractDto>> mapInterfaceToClass;
	private static final Log logger = LogFactory.getLog(DtoFactory.class);

	static {
		mapInterfaceToClass = new HashMap<>();
	}

	/**
	 * Registers a DTO
	 * 
	 * @param iface
	 *            The interface of the {@link IDto}
	 * @param clazz
	 *            The class of the {@link IDto}
	 */
	public static <T extends IDto, T2 extends AbstractDto> void registerInterface(Class<T> iface, Class<T2> clazz) {
		if (iface.isInterface() && !mapInterfaceToClass.containsKey(iface)) {
			mapInterfaceToClass.put(iface, clazz);
		}
	}

	/**
	 * Return the concrete {@link IDto} class associated to the given {@link IDto}
	 * interface
	 * 
	 * @param iface
	 *            The interface of the {@link IDto}
	 * @return The class of the {@link IDto}
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IDto> Class<? extends AbstractDto> getClassFromInterface(Class<T> iface) {
		if (iface.isInterface()) {
			return mapInterfaceToClass.get(iface);
		} else {
			return (Class<? extends AbstractDto>) iface;
		}
	}

	/**
	 * Return an instance that extends from the given {@link IDto} interface
	 * 
	 * @param iface
	 *            The interface representing the {@link IDto}
	 * @return The {@link IDto} instance object
	 */
	public static <T extends IDto> T createInstanceFromInterface(Class<T> iface) {
		return createInstanceFromInterface(iface, (Object) null);
	}

	/**
	 * Return an instance that extends from the given {@link IDto} interface
	 * 
	 * @param iface
	 *            The interface representing the {@link IDto}
	 * @param attrValues
	 *            The attributes map with the values to assign for each attribute
	 * @return The {@link IDto} instance object
	 */
	public static <T extends IDto> T createInstanceFromInterface(Class<T> iface, Map<String, Object> attrValues) {
		T dto = createInstanceFromInterface(iface, (Object) null);
		fillInstance(dto, attrValues);

		return dto;
	}

	/**
	 * Return an instance that extends from the given {@link IDto} interface
	 * 
	 * @param iface
	 *            The interface representing the {@link IDto}
	 * @param args
	 *            The constructor arguments (can be null, but be sure that the
	 *            {@link IDto} class contains a default constructor with no
	 *            parameters)
	 * @return The {@link IDto} instance object
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IDto> T createInstanceFromInterface(Class<T> iface, Object... args) {
		Class<T> clazz = null;
		if (iface.isInterface()) {
			clazz = (Class<T>) getClassFromInterface(iface);
			if (clazz == null) {
				// if not exists, try to find the implementation in the loaded classes
				clazz = DtoRegistry.getDtoImplementation(iface, false);
				if (clazz == null) {
					// if not exists, something strange is happening...
					throw new IllegalStateException();
				}
			}
		} else {
			clazz = iface;
		}

		try {
			T instance;
			if (args != null && args[0] != null) {
				List<Class<?>> params = new ArrayList<>();
				for (Object arg : args) {
					params.add(arg.getClass());
				}

				Constructor<T> construct = clazz.getConstructor(params.toArray(new Class<?>[0]));
				instance = construct.newInstance(args);
			} else {
				Constructor<T> construct = clazz.getConstructor();
				instance = construct.newInstance();
			}
			return instance;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
			return null;
		}
	}

	/**
	 * Fill the instance with the given attributes
	 * 
	 * @param dto
	 *            The instance object
	 * @param attrValues
	 *            The attributes map with the values to assign for each attribute
	 */
	private static <T extends IDto> void fillInstance(T dto, Map<String, Object> attrValues) {
		if (MapUtils.isEmpty(attrValues)) {
			return;
		}

		try {
			BeanUtils.populate(dto, attrValues);
		} catch (IllegalAccessException | InvocationTargetException e1) {
			logger.error("Error while populating the dto with a map values");
		}
	}

	private DtoFactory() {
	}
}
