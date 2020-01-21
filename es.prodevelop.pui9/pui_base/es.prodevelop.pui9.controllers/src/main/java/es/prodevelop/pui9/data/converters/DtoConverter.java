package es.prodevelop.pui9.data.converters;

import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.DtoFactory;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * This class allows to set a @RequestParam parameter in your controllers
 * indicating that the type of this parameter is a DTO. Automatically, value is
 * converted into this DTO using {@link GsonSingleton} class
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class DtoConverter implements GenericConverter {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, IDto.class));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		try {
			Class<? extends IDto> clazz = (Class<? extends IDto>) targetType.getType();
			Class<? extends IDto> dtoClass = null;
			if (clazz.isInterface()) {
				// try to find the implementation in the Dto Factory cache
				dtoClass = DtoFactory.getClassFromInterface((Class<? extends IDto>) clazz);
				if (dtoClass == null) {
					// if not exists, try to find the implementation in the loaded classes
					dtoClass = DtoRegistry.getDtoImplementation(clazz, false);
					if (dtoClass == null) {
						// if not exists, something strange is happening...
						return null;
					}
				}
			} else {
				dtoClass = clazz;
			}

			String val = source.toString();
			return GsonSingleton.getSingleton().getGson().fromJson(val, dtoClass);
		} catch (Exception e) {
			return null;
		}
	}

}