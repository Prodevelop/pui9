package es.prodevelop.pui9.geo.helpers;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.geo.dao.helpers.IDatabaseGeoHelper;
import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;

@Component
public class PostgreSqlDatabaseGeoHelper implements IDatabaseGeoHelper {

	@Override
	public String getSridSql(String tableName, String geometryFieldName) {
		StringBuilder sb = new StringBuilder();
		sb.append("select srid from geometry_columns where lower(f_table_name) = '");
		sb.append("tableName.toLowerCase()");
		sb.append("'");

		return sb.toString();
	}

	@Override
	public String modifyColumnValue(Integer srid, IGeoDto dto, String columnName, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("ST_GeomFromText(?, ");
		sb.append(srid);
		sb.append(")");

		return sb.toString();
	}

	@Override
	public String fillGeometryValue(IGeoDto dto) {
		StringBuilder sb = new StringBuilder();
		sb.append("ST_AsText(");
		sb.append(dto.getGeometryFieldName());
		sb.append(") as ");
		sb.append(dto.getGeometryFieldName());

		return sb.toString();
	}

	@Override
	public void setGeometryValue(Geometry jtsGeometry, int geometryDimension, Integer srid, PreparedStatement ps,
			int position) throws SQLException {
	}

	@Override
	public boolean supportsNativeGeometry() {
		return false;
	}

	@Override
	public String filterByBoundingBox(String column, Integer srid) {
		StringBuilder sb = new StringBuilder();
		sb.append("ST_Intersects(");
		sb.append(column);
		sb.append(",ST_MakeEnvelope(?,?,?,?,");
		sb.append(srid);
		sb.append(")) = true");

		return sb.toString();
	}

	@Override
	public String intersectsByPoint2D(String column, Integer srid) {
		StringBuilder sb = new StringBuilder();
		sb.append("ST_Intersects(");
		sb.append(column);
		sb.append(",ST_SetSRID(ST_MakePoint(?,?),");
		sb.append(srid);
		sb.append(")) = true");

		return sb.toString();
	}

}
