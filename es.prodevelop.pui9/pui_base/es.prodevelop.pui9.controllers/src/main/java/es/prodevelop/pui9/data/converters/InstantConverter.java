package es.prodevelop.pui9.data.converters;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * This class allows to set a @RequestParam parameter in your controllers
 * indicating that the type of this parameter is a {@link Instant}.
 * Automatically, the value is converted into a Date using the
 * {@link PuiDateUtil} class (allows multiple formats)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class InstantConverter implements GenericConverter {

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Instant.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}
		String val = new String(source.toString().getBytes(), StandardCharsets.UTF_8);
		return PuiDateUtil.stringToInstant(val);
	}

}