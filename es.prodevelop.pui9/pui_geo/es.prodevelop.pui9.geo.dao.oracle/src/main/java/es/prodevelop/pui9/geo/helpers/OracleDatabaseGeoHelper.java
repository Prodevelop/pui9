package es.prodevelop.pui9.geo.helpers;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.jdbc.JDBCDataStore;
import org.locationtech.jts.geom.Geometry;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.geo.dao.helpers.IDatabaseGeoHelper;
import es.prodevelop.pui9.geo.dto.interfaces.IGeoDto;

@Component
public class OracleDatabaseGeoHelper implements IDatabaseGeoHelper {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private JDBCDataStore ds;
	private CurveCollectionOracleDialect dialect;
	private static final Integer GEOMETRY_TYPE_2D_POINT_CODE = 2001;
	private static final Integer GEOMETRY_TYPE_2D_POLYGON_CODE = 2003;
	private static final String GEOMETRY_SDO_ELEM_INFO_UNIC_POLYGON_WITHOUT_HOLES = "1,1003,3";

	@PostConstruct
	public void postConstruct() {
		ds = new JDBCDataStore();
		dialect = new CurveCollectionOracleDialect(ds);
	}

	@PreDestroy
	public void preDestroy() {
		if (ds != null) {
			ds.dispose();
		}
	}

	@Override
	public String getSridSql(String tableName, String geometryFieldName) {
		StringBuilder sb = new StringBuilder();
		sb.append("select srid from user_sdo_geom_metadata where lower(table_name) = '");
		sb.append(tableName.toLowerCase());
		sb.append("'");

		return sb.toString();
	}

	@Override
	public String modifyColumnValue(Integer srid, IGeoDto dto, String columnName, String value) {
		return value;
	}

	@Override
	public String fillGeometryValue(IGeoDto dto) {
		StringBuilder sb = new StringBuilder();
		sb.append("SDO_UTIL.TO_WKTGEOMETRY(");
		sb.append(dto.getGeometryFieldName());
		sb.append(") as ");
		sb.append(dto.getGeometryFieldName());

		return sb.toString();
	}

	@Override
	public void setGeometryValue(Geometry jtsGeometry, int geometryDimension, Integer srid, PreparedStatement ps,
			int position) throws SQLException {
		dialect.setGeometryValue(jtsGeometry, geometryDimension, srid, null, ps, position);
	}

	@Override
	public boolean supportsNativeGeometry() {
		return true;
	}

	@Override
	public String filterByBoundingBox(String column, Integer srid) {
		StringBuilder sb = new StringBuilder();
		sb.append("lower(SDO_ANYINTERACT(");
		sb.append(column);
		sb.append(",MDSYS.SDO_GEOMETRY(");
		sb.append(GEOMETRY_TYPE_2D_POLYGON_CODE);
		sb.append(",");
		sb.append(srid);
		sb.append(", NULL, MDSYS.SDO_ELEM_INFO_ARRAY(");
		sb.append(GEOMETRY_SDO_ELEM_INFO_UNIC_POLYGON_WITHOUT_HOLES);
		sb.append("),MDSYS.SDO_ORDINATE_ARRAY(?,?,?,?)))) = 'true'");

		return sb.toString();
	}

	@Override
	public String intersectsByPoint2D(String column, Integer srid) {
		StringBuilder sb = new StringBuilder();
		sb.append("lower(SDO_ANYINTERACT(");
		sb.append(column);
		sb.append(", MDSYS.SDO_GEOMETRY(");
		sb.append(GEOMETRY_TYPE_2D_POINT_CODE);
		sb.append(",");
		sb.append(srid);
		sb.append(", MDSYS.SDO_POINT_TYPE(?, ?, NULL), NULL,NULL))) = 'true'");

		return sb.toString();
	}

	public CurveCollectionOracleDialect getDialect() {
		return this.dialect;
	}

}
