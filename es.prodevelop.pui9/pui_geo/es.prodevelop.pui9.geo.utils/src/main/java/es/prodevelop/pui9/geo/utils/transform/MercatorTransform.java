package es.prodevelop.pui9.geo.utils.transform;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Transformación de geográficas a Mercator "a pelo"
 * 
 * @author aromeu
 * 
 */
public class MercatorTransform implements MTransform {

	private static final double DEGREES_PRE_RADIAN = 180.0 / Math.PI;
	private static final double MERCATOR_EARTH_RADIUS = 6378137.0;
	private static final double METERS_PER_EQUATOR_DEGREE = Math.PI * MERCATOR_EARTH_RADIUS / 180.0;

	private GeometryFactory factory = new GeometryFactory();

	@Override
	public double[] toMercator(double[] coords) {
		double lon = coords[0];
		double lat = coords[1];
		double rlat = lat / DEGREES_PRE_RADIAN;
		double y = 0.5 * Math.log((1 + Math.sin(rlat)) / (1 - Math.sin(rlat)));
		double merx = METERS_PER_EQUATOR_DEGREE * lon;
		double mery = METERS_PER_EQUATOR_DEGREE * DEGREES_PRE_RADIAN * y;

		return new double[] { merx, mery };
	}

	@Override
	public Point toMercatorAsPoint(double[] coords) {
		double[] merCoords = toMercator(coords);
		Coordinate coordinate = new Coordinate();
		coordinate.x = merCoords[0];
		coordinate.y = merCoords[1];
		return factory.createPoint(coordinate);
	}

	@Override
	public double[] toMercator(Point point) {
		return toMercator(new double[] { point.getX(), point.getY() });
	}

	@Override
	public Point toMercatorAsPoint(Point point) {
		double[] merCoords = toMercator(point);
		Coordinate coordinate = new Coordinate();
		coordinate.x = merCoords[0];
		coordinate.y = merCoords[1];
		return factory.createPoint(coordinate);
	}

}
