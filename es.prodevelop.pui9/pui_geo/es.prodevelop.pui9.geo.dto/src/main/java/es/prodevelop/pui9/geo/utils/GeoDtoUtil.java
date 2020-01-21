package es.prodevelop.pui9.geo.utils;

import org.geotools.geometry.jts.CurvedGeometryFactory;
import org.geotools.geometry.jts.WKTReader2;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

import es.prodevelop.pui9.geo.formats.WKT;

public class GeoDtoUtil {

	public static final String CIRCULARSTRING = "CIRCULARSTRING";
	public static final String COMPOUNDCURVE = "COMPOUNDCURVE";
	public static final String CURVEPOLYGON = "CURVEPOLYGON";
	public static final String GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION";

	private static final String[] types = new String[] { CIRCULARSTRING, COMPOUNDCURVE, CURVEPOLYGON,
			GEOMETRYCOLLECTION };

	private GeoDtoUtil() {
	}

	public static Geometry parseGeometry(String geometryWKT) throws IllegalArgumentException {
		try {
			Geometry jtsGeometry = null;
			if (isCurvedorCollectionGeometry(geometryWKT)) {
				CurvedGeometryFactory curvedfactory = new CurvedGeometryFactory(Double.MAX_VALUE);

				WKTReader2 reader2 = new WKTReader2(curvedfactory);
				jtsGeometry = reader2.read(geometryWKT);
			} else {
				WKT reader = new WKT();
				jtsGeometry = reader.parse(geometryWKT);
			}
			return jtsGeometry;
		} catch (ParseException e) {
			throw new IllegalArgumentException("Bad geometry WKT format");
		}
	}

	public static boolean areEqualsJTSGeometries(String wkt1, String wkt2) {
		try {
			Geometry g1 = parseGeometry(wkt1);
			Geometry g2 = parseGeometry(wkt2);
			return g1.equalsExact(g2);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public static Geometry getGeometryJTS(String geometry) throws IllegalArgumentException {
		return parseGeometry(geometry);
	}

	private static boolean isCurvedorCollectionGeometry(String wkt) {
		for (String type : types) {
			if (wkt.startsWith(type)) {
				return true;
			}
		}

		return false;
	}

}
