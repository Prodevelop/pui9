package es.prodevelop.pui9.data.converters;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.json.GsonSingleton;

/**
 * This class allows to set a @RequestParam parameter in your controllers
 * indicating that the type of this parameter is a Map<String,Object>.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class MapConverter implements GenericConverter {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Map.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		try {
			Class<?> clazz = targetType.getType();
			return GsonSingleton.getSingleton().getGson().fromJson(source.toString(), clazz);
		} catch (Exception e) {
			return null;
		}
	}

}