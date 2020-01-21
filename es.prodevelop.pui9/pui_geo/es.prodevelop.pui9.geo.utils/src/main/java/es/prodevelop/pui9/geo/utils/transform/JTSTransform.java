package es.prodevelop.pui9.geo.utils.transform;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.omg.CORBA.portable.ApplicationException;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import es.prodevelop.pui9.geo.exceptions.PuiGeoCoordinatesException;
import es.prodevelop.pui9.geo.utils.GeoConstants;

/**
 * Reproyección de geometrías. Implementa {@link MTransform} para tener
 * disponibles métodos para transformar de coordenadas geográficas a Mercator
 * 
 * @author aromeu
 * 
 */
public class JTSTransform implements MTransform {

	protected final Log logger = LogFactory.getLog(this.getClass());

	private GeometryFactory factory = new GeometryFactory();

	public JTSTransform() {
		GeoConstants.forceXY();
	}

	/**
	 * Reproyecta una geometría de un SRS origen a otro destino. Los SRS deben estar
	 * disponibles en GeoTools
	 * 
	 * @param sourceSRSCode      El código del SRS de origen
	 * @param destinationSRSCode El código del SRS destino
	 * @param geometry           La geometría a reproyectar
	 * @return La geometría reproyectada
	 * @throws ApplicationException Las excepciones propias de GeoTools se devuelven
	 *                              como {@link ApplicationException}
	 */
	public Geometry transform(String sourceSRSCode, String destinationSRSCode, Geometry geometry)
			throws PuiGeoCoordinatesException {
		CoordinateReferenceSystem sourceCRS;
		try {
			sourceCRS = CRS.decode(sourceSRSCode);
			CoordinateReferenceSystem targetCRS = CRS.decode(destinationSRSCode);

			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

			return JTS.transform(geometry, transform);
		} catch (FactoryException | MismatchedDimensionException | TransformException e) {
			throw new PuiGeoCoordinatesException(e);
		}
	}

	/**
	 * Método de utilidad para la transformación que más se utiliza de EPSG:4258 a
	 * EPSG:900913
	 * 
	 * @param geometry La geometría a reproyectar
	 * @return La geometría reproyectada
	 * @throws ApplicationException Las excepciones propias de GeoTools se devuelven
	 *                              como {@link ApplicationException}
	 */
	public Geometry transformFrom4258ToMercator(Geometry geometry) throws PuiGeoCoordinatesException {
		return this.transform(GeoConstants.EPSG_4258, GeoConstants.EPSG_900913, geometry);
	}

	@Override
	public double[] toMercator(double[] coords) throws PuiGeoCoordinatesException {
		Point transformedPoint = this.toMercatorAsPoint(coords);

		return new double[] { transformedPoint.getX(), transformedPoint.getY() };
	}

	@Override
	public Point toMercatorAsPoint(double[] coords) throws PuiGeoCoordinatesException {
		Coordinate coordinate = new Coordinate();
		coordinate.x = coords[0];
		coordinate.y = coords[1];

		Geometry point = factory.createPoint(coordinate);

		return (Point) this.transform(GeoConstants.EPSG_4258, GeoConstants.EPSG_900913, point);
	}

	@Override
	public double[] toMercator(Point point) throws PuiGeoCoordinatesException {
		return this.toMercator(new double[] { point.getX(), point.getY() });
	}

	@Override
	public Point toMercatorAsPoint(Point point) throws PuiGeoCoordinatesException {
		return this.toMercatorAsPoint(new double[] { point.getX(), point.getY() });
	}
}
