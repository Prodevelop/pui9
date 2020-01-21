package es.prodevelop.pui9.geo.utils;

import org.geotools.util.factory.Hints;

/**
 * Constantes que se utilizan en Agroasesor
 * 
 * @author aromeu
 * 
 */
public class GeoConstants {

	public final static String EPSG_4258 = "EPSG:4258";
	public final static String EPSG_900913 = "EPSG:900913";
	public final static String EPSG_4326 = "EPSG:4326";
	public final static String EPSG_23030 = "EPSG:23030";
	public final static String EPSG_23028 = "EPSG:23028";
	public final static String EPSG_23029 = "EPSG:23029";
	public final static String EPSG_23031 = "EPSG:23031";
	public final static String EPSG_25830 = "EPSG:25830";
	public final static String EPSG_25828 = "EPSG:25828";
	public final static String EPSG_25829 = "EPSG:25829";
	public final static String EPSG_25831 = "EPSG:25831";
	public final static String EPSG_32628 = "EPSG:32628"; // Canarias

	static {
		/* All points will come in lon/lat format */
		Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
	}

	/**
	 * Fuerza a GeoTools para que interprete un par de coordenadas como lon, lat o
	 * X, Y
	 */
	public static void forceXY() {
		System.setProperty("org.geotools.referencing.forceXY", "true");
	}
}
