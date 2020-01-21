package es.prodevelop.pui9.geo.helpers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.sql.Types;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.oracle.OracleDialect;
import org.geotools.data.oracle.sdo.GeometryConverter;
import org.geotools.data.oracle.sdo.SDOSqlDumper;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.CompoundRing;
import org.geotools.geometry.jts.CurvePolygon;
import org.geotools.jdbc.JDBCDataStore;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;

import oracle.jdbc.OracleConnection;

public class CurveCollectionOracleDialect extends OracleDialect {

	public CurveCollectionOracleDialect(JDBCDataStore dataStore) {
		super(dataStore);
	}

	private Struct testStruct;

	/**
	 * Sentinel value used to mark that the unwrapper lookup happened already, and
	 * an unwrapper was not found
	 */
	private UnWrapper unwrapperNotFount = new UnWrapper() {
		@Override
		public Statement unwrap(Statement statement) {
			throw new UnsupportedOperationException();
		}

		@Override
		public Connection unwrap(Connection conn) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean canUnwrap(Statement st) {
			return false;
		}

		@Override
		public boolean canUnwrap(Connection conn) {
			return false;
		}
	};

	/**
	 * Map of <code>UnWrapper</code> objects keyed by the class of
	 * <code>Connection</code> it is an unwrapper for. This avoids the overhead of
	 * searching the <code>DataSourceFinder</code> service registry at each unwrap.
	 */
	private Map<Class<? extends Connection>, UnWrapper> myUwMap = new HashMap<>();

	@Override
	@SuppressWarnings("rawtypes")
	public void setGeometryValue(Geometry g, int dimension, int srid, Class binding, PreparedStatement ps, int column)
			throws SQLException {
		// Handle the null geometry case.
		// Surprisingly, using setNull(column, Types.OTHER) does not work...
		if (g == null) {
			ps.setNull(column, Types.STRUCT, "MDSYS.SDO_GEOMETRY");
			return;
		}
		OracleConnection ocx;
		if (ps != null) {
			ocx = innerUnwrapConnection(ps.getConnection());
		} else {
			ocx = null;
		}
		CurveGeometryConverter curveConverter = new CurveGeometryConverter(ocx);
		GeometryConverter converter = new GeometryConverter(ocx);
		GeometryConverter collectionConverter = new GeometryCollectionConverter(ocx);

		Struct s = null;
		if (g instanceof CircularString || g instanceof CompoundCurve || g instanceof CircularRing
				|| g instanceof CurvePolygon || g instanceof CompoundRing) {
			s = curveConverter.toSDO(g, srid);
		} else if (g instanceof GeometryCollection
				&& !((g instanceof MultiPolygon) || (g instanceof MultiLineString) || (g instanceof MultiPoint))) {
			s = collectionConverter.toSDO(g, srid);
		} else {
			s = converter.toSDO(g, srid);
		}
		this.setTestStruct(s);
		if (ps != null) {
			ps.setObject(column, s);
		}

		if (LOGGER.isLoggable(Level.FINE)) {
			String sdo;
			try {
				// the dumper cannot translate all types of geometries
				sdo = SDOSqlDumper.toSDOGeom(g, srid);
			} catch (Exception e) {
				sdo = "Could not translate this geometry into a SDO string, " + "WKT representation is: " + g;
			}
			LOGGER.fine("Setting parameter " + column + " as " + sdo);
		}
	}

	/**
	 * Obtains the native oracle connection object given a database connection.
	 */
	private OracleConnection innerUnwrapConnection(Connection cx) throws SQLException {
		if (cx == null) {
			return null;
		}

		if (cx instanceof OracleConnection) {
			return (OracleConnection) cx;
		}

		try {
			// Unwrap the connection multiple levels as necessary to get at the
			// underlying
			// OracleConnection. Maintain a map of UnWrappers to avoid searching
			// the registry every time we need to unwrap.
			Connection testCon = cx;
			Connection toUnwrap;
			do {
				UnWrapper unwrapper = myUwMap.get(testCon.getClass());
				if (unwrapper == null) {
					unwrapper = DataSourceFinder.getUnWrapper(testCon);
					if (unwrapper == null) {
						unwrapper = unwrapperNotFount;
					}
					myUwMap.put(testCon.getClass(), unwrapper);
				}
				if (unwrapper == unwrapperNotFount) {
					// give up and do Java 6 unwrap below
					break;
				}
				toUnwrap = testCon;
				testCon = unwrapper.unwrap(testCon);
				if (testCon instanceof OracleConnection) {
					return (OracleConnection) testCon;
				}
			} while (testCon != null && testCon != toUnwrap);

			if (cx instanceof Wrapper) {
				// try to use java 6 unwrapping
				try {
					Wrapper w = cx;
					if (w.isWrapperFor(OracleConnection.class)) {
						return w.unwrap(OracleConnection.class);
					}
				} catch (SQLException t) {
					// not a mistake, old DBCP versions will throw an Error
					// here, we need to catch
					// it
					LOGGER.log(Level.FINER, "Failed to unwrap connection using java 6 facilities", t);
				}
			}
		} catch (IOException e) {
			throw (SQLException) new SQLException("Could not obtain native oracle connection.").initCause(e);
		}

		throw new SQLException("Could not obtain native oracle connection for " + cx.getClass());
	}

	public Struct getTestStruct() {
		return testStruct;
	}

	public void setTestStruct(Struct testStruct) {
		this.testStruct = testStruct;
	}

}