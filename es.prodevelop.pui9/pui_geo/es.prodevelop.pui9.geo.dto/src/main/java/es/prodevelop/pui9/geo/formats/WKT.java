package es.prodevelop.pui9.geo.formats;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * Wrapper de {@link WKTReader} para converter geometrías en WKT a
 * {@link Geometry}
 * 
 * @author aromeu
 * 
 */
public class WKT {

	private WKTReader reader;
	private WKTWriter writer;

	public WKT() {
		reader = new WKTReader(JTSFactoryFinder.getGeometryFactory());
		writer = new WKTWriter();
	}

	/**
	 * Convierte una geometría en WKT a un objeto {@link Geometry} de JTS
	 * 
	 * @param wkt El string WKT
	 * @return Un objeto {@link Geometry} de JTS
	 * @throws ParseException
	 */
	public Geometry parse(String wkt) throws ParseException {
		return reader.read(wkt);
	}

	public String toWKT(Geometry geometry) {
		return writer.write(geometry);
	}
}
