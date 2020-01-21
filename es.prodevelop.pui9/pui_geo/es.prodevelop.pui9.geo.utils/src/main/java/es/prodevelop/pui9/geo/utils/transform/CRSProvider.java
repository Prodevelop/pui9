package es.prodevelop.pui9.geo.utils.transform;

import java.util.HashMap;
import java.util.Map;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Class to provide a transform method. It has to be initialized using a central
 * meridian for the working area.
 */
public class CRSProvider {
	private MathTransform transform;

	private static final Map<Integer, CRSProvider> instances = new HashMap<>();

	/**
	 * The WKT for the WGS84 lon/lat projection EPSG:4326
	 */
	public static final String wkt4326 = "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\","
			+ "6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\""
			+ "EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
			+ "UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]]," + "AUTHORITY[\"EPSG\",\"4326\"]]";

	static {
		/* All points will come in lon/lat format */
		Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
	}

	public static CRSProvider getInstance(double lon) {
		Integer cm = getCentralMeridian(lon);

		if (!instances.containsKey(cm)) {
			instances.put(cm, new CRSProvider(cm));
		}
		return instances.get(cm);
	}

	private CRSProvider(int centralMeridian) throws IllegalArgumentException {
		try {
			transform = CRS.findMathTransform(CRS.parseWKT(wkt4326),
					CRS.parseWKT(getTransverseMercatorWKT(centralMeridian)), false);
		} catch (FactoryException e) {
			throw new IllegalArgumentException("Error creating the CRS transformation", e);
		}
	}

	/**
	 * 
	 * @param source geometry to transform in EPSG:4326 (lon,lat)
	 * @return the geoemetry tranformed into TransverseMercator
	 * @throws TransformException
	 */
	public Geometry transverseMercatorTransform(Geometry source) throws TransformException {
		return JTS.transform(source, this.transform);
	}

	/**
	 * 
	 * @param source geometry to transform in TransverseMercator
	 * @return the geoemetry tranformed into EPSG:4326 (lon,lat)
	 * @throws TransformException
	 */
	public Geometry inverseTransverseMercatorTransform(Geometry source) throws TransformException {
		return JTS.transform(source, this.transform.inverse());
	}

	/**
	 * 
	 * @param centralMeridian
	 * @return the Well Known Text for the Transverse Mercator projection
	 */
	public static String getTransverseMercatorWKT(int centralMeridian) {
		return "PROJCS[\"WGS84 / WGS84-TM30\"," + wkt4326
				+ ",UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],PROJECTION[\"Transverse_Mercator\"],"
				+ "PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\"," + centralMeridian
				+ "],PARAMETER[\"scale_factor\",0.9996],PARAMETER[\"false_easting\",500000],"
				+ "PARAMETER[\"false_northing\",0],AUTHORITY[\"EPSG\",\"3042\"],"
				+ "AXIS[\"Easting\",EAST],AXIS[\"Northing\",NORTH]]";
	}

	/**
	 * @param longitude
	 * @return the zone between 1 to 60 of the Earth according to the longitude
	 */
	public static int getZone(double longitude) {
		if (longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException("Longitude out of domain");
		}

		return new Double(Math.min(60, Math.abs((longitude + 180) / 6.0) + 1)).intValue();
	}

	/**
	 * 
	 * @param longitude
	 * @return the central meridian for the zone where the longitude is located
	 */
	public static int getCentralMeridian(double longitude) {
		if (longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException("Longitude out of domain");
		}

		int zone = getZone(longitude);
		return new Double((zone - 0.5) * 6 - 180).intValue();
	}
}
