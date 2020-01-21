package es.prodevelop.pui9.geo.dto;

import java.lang.reflect.Field;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.locationtech.jts.geom.Geometry;

import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;
import es.prodevelop.pui9.geo.utils.GeoDtoUtil;
import es.prodevelop.pui9.model.dto.AbstractTableDto;
import es.prodevelop.pui9.model.dto.DtoRegistry;

public abstract class AbstractGeoDto extends AbstractTableDto implements IGeoDto {

	private static final long serialVersionUID = 1L;

	@Override
	public Geometry getJtsGeometry() {
		try {
			String fieldName = getGeometryFieldName();
			Field field = DtoRegistry.getJavaFieldFromFieldName(getClass(), fieldName);
			Object value = FieldUtils.readField(field, this, true);
			if (value == null) {
				return null;
			}

			return GeoDtoUtil.parseGeometry(value.toString());
		} catch (Exception e) {
			return null;
		}
	}
}
