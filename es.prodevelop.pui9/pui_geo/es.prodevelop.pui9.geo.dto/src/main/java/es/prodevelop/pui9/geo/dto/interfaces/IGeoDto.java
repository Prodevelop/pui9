package es.prodevelop.pui9.geo.dto.interfaces;

import org.locationtech.jts.geom.Geometry;

import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import io.swagger.annotations.ApiModelProperty;

public interface IGeoDto extends ITableDto {

	/**
	 * Get the name of the geometry column
	 */
	@ApiModelProperty(hidden = true)
	String getGeometryFieldName();

	/**
	 * Get the geometry value parsed as JST Geometry object
	 */
	@ApiModelProperty(hidden = true)
	Geometry getJtsGeometry();
}
