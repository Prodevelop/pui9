package es.prodevelop.pui9.geo.dao.helpers;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.locationtech.jts.geom.Geometry;

import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;

public interface IDatabaseGeoHelper {

	String getSridSql(String tableName, String geometryFieldName);

	String modifyColumnValue(Integer srid, IGeoDto dto, String columnName, String value);

	String fillGeometryValue(IGeoDto dto);

	void setGeometryValue(Geometry jtsGeometry, int geometryDimension, Integer srid, PreparedStatement ps, int position)
			throws SQLException;

	boolean supportsNativeGeometry();

	String filterByBoundingBox(String column, Integer srid);

	String intersectsByPoint2D(String column, Integer srid);

}
