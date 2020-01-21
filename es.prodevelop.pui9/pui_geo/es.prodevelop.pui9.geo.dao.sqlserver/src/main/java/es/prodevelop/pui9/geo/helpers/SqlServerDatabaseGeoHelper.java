package es.prodevelop.pui9.geo.helpers;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.NotImplementedException;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.geo.dao.helpers.IDatabaseGeoHelper;
import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;

@Component
public class SqlServerDatabaseGeoHelper implements IDatabaseGeoHelper {

	@Override
	public String getSridSql(String tableName, String geometryFieldName) {
		StringBuilder sb = new StringBuilder();
		sb.append("select top 1 ");
		sb.append(geometryFieldName);
		sb.append(".STSrid from ");
		sb.append(tableName);

		return sb.toString();
	}

	@Override
	public String modifyColumnValue(Integer srid, IGeoDto dto, String columnName, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("geometry::STGeomFromText(?, ");
		sb.append(srid);
		sb.append(")");

		return sb.toString();
	}

	@Override
	public String fillGeometryValue(IGeoDto dto) {
		StringBuilder sb = new StringBuilder();
		sb.append(dto.getGeometryFieldName());
		sb.append(".STAsText() as ");
		sb.append(dto.getGeometryFieldName());

		return sb.toString();
	}

	@Override
	public void setGeometryValue(Geometry jtsGeometry, int geometryDimension, Integer srid, PreparedStatement ps,
			int position) throws SQLException {
		throw new NotImplementedException("setGeometryValue is not implemented in " + getClass().getSimpleName());
	}

	@Override
	public boolean supportsNativeGeometry() {
		return false;
	}

	@Override
	public String filterByBoundingBox(String column, Integer Srid) {
		throw new NotImplementedException("filterByBoundingBos is not implemented in " + getClass().getSimpleName());
	}

	@Override
	public String intersectsByPoint2D(String column, Integer srid) {
		throw new NotImplementedException("intersectsByPoint2D is not implemented in " + getClass().getSimpleName());
	}

}
