package es.prodevelop.pui9.geo.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.TopologyException;
import org.locationtech.jts.io.WKTWriter;
import org.locationtech.jts.precision.GeometryPrecisionReducer;
import org.omg.CORBA.portable.ApplicationException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;

import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.geo.exceptions.PuiGeoCoordinatesException;
import es.prodevelop.pui9.geo.utils.transform.CRSProvider;
import es.prodevelop.pui9.geo.utils.transform.JTSTransform;

/**
 * Métodos de utilidades que se pueden hacer falta en cualquier clase de negocio
 * 
 * @author aromeu
 * 
 */
public class GeometryUtils {

	private JTSTransform jtsTransform = new JTSTransform();

	private double[][] WEB_MERCATOR_RATIO = { { 0, 100, 100.7 }, { 5, 100.4, 101.1 }, { 10, 101.6, 102.2 },
			{ 15, 103.5, 104.2 }, { 20, 106.4, 107.1 }, { 25, 110.3, 110.9 }, { 30, 115.4, 116 }, { 35, 122, 122.5 },
			{ 40, 130.4, 130.9 }, { 45, 141.2, 141.7 }, { 50, 155.3, 155.8 }, { 55, 174, 174.4 }, { 60, 199.6, 199.9 },
			{ 65, 236.1, 236.4 }, { 70, 291.6, 291.9 }, { 75, 385.3, 385.5 }, { 80, 574.2, 574.4 } };

	public boolean overlaps(Geometry geom1, Geometry geom2) {
		return geom1.overlaps(geom2);
	}

	public boolean touches(Geometry geom1, Geometry geom2) {
		return geom1.touches(geom2);
	}

	/**
	 * Normaliza una distancia calculada a partir de dos geometrías con coordenadas
	 * en Mercator.
	 * 
	 * Se está utilizando la aproximación de @jldominguez http://www.prodevelop.es
	 * /es/blog/13/12/13/fast-estimation-distancearea-geodetic-coordinates
	 * 
	 * @param distance La distancia calculada a partir de dos geometrías con
	 *                 coordenadas en Mercator
	 * @param lat      La latitud en grados de una de las dos geometrías
	 * @return La distancia normalizada. Puede tener error, pero si son distancias
	 *         pequeñas nos vale
	 */
	public double normalizeDistanceMercator(double distance, double lat) {
		return distance * Math.cos(Math.toRadians(lat));
	}

	public double normalizeDistanceMercatorXurxosanzMethod(double distance, double lat) {
		int posInArray = (int) Math.round(Math.abs(lat) / 5);

		try {
			double[] WEB_MERCATOR_RATIO_2 = this.WEB_MERCATOR_RATIO[posInArray];

			return distance * WEB_MERCATOR_RATIO_2[1] / 100;
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Normaliza un área calculada a partir de una geometría con las coordenadas en
	 * Mercator
	 * 
	 * Se está utilizando la aproximación de @jldominguez http://www.prodevelop.es
	 * /es/blog/13/12/13/fast-estimation-distancearea-geodetic-coordinates
	 * 
	 * @param area El área calculada a partir de una geometría con las coordenadas
	 *             en Mercator
	 * @param lat  La latitud en grados del centroide de la geometría
	 * @return El área normalizada. Puede tener error, pero si son áreas pequeñas
	 *         nos sirve
	 */
	public double normalizeAreaMercator(double area, double lat) {
		return area * Math.pow(Math.cos(Math.toRadians(lat)), 2);
	}

	/**
	 * Convierte las dos geometrías a Mercator desde 4258, calcula la differencia y
	 * obtiene el área normalizada
	 * 
	 * @param geom
	 * @param geom2
	 * @return
	 * @throws ApplicationException
	 */
	public double calculateDifferenceAreaNormalized(Geometry geom, Geometry geom2) throws PuiGeoCoordinatesException {
		Geometry geomMercator = jtsTransform.transformFrom4258ToMercator(geom);
		Geometry geom2Mercator = jtsTransform.transformFrom4258ToMercator(geom2);

		Geometry difference = geomMercator.difference(geom2Mercator);
		return this.normalizeAreaMercator(difference.getArea(), geom.getCentroid().getY());

	}

	/**
	 * Convierte las dos geometrías a Mercator desde 4258, calcula la intersección y
	 * obtiene el área normalizada
	 * 
	 * @param geom
	 * @param geom2
	 * @return
	 */
	public double calculateIntersectionAreaNormalized(Geometry geom, Geometry geom2) throws PuiGeoCoordinatesException {
		Geometry geomMercator = jtsTransform.transformFrom4258ToMercator(geom);
		Geometry geom2Mercator = jtsTransform.transformFrom4258ToMercator(geom2);

		Geometry intersection = null;
		try {
			intersection = geomMercator.intersection(geom2Mercator);
		} catch (TopologyException e) {
			PrecisionModel pm = new PrecisionModel(1000000);
			GeometryPrecisionReducer gpr = new GeometryPrecisionReducer(pm);
			geomMercator = gpr.reduce(geomMercator);
			geom2Mercator = gpr.reduce(geom2Mercator);
			intersection = geomMercator.intersection(geom2Mercator);
		}
		return this.normalizeAreaMercator(intersection.getArea(), geom.getCentroid().getY());

	}

	/**
	 * Calculates the distance of two points in geographical coordinates using UTM
	 * transformations
	 * 
	 * @param from
	 * @param to
	 * @return
	 * @throws PuiException
	 */
	public double calculateDistanceUTM(Point from, Point to) throws PuiException {
		try {
			Geometry toUTM = CRSProvider.getInstance(to.getX()).transverseMercatorTransform(to);
			Geometry fromUTM = CRSProvider.getInstance(from.getX()).transverseMercatorTransform(from);
			return fromUTM.distance(toUTM);
		} catch (MismatchedDimensionException | TransformException e) {
			throw new PuiException(e);
		}
	}

	/**
	 * Calculates the distance of two points in UTM. No coordinate transformation is
	 * performed
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public double calculateDistance(Point from, Point to) {
		return from.distance(to);
	}

	public LineString pointsToLine(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
		GeometryFactory factory = new GeometryFactory();
		int size = collection.size();

		// aseguramos que la línea va a tener al menos 2 puntos
		if (size < 2) {
			size = 2;
		}

		Coordinate[] coords = new Coordinate[size];
		int i = 0;

		Coordinate coord = null;
		Geometry geom;
		try (FeatureIterator<SimpleFeature> features = collection.features()) {

			while (features.hasNext()) {
				SimpleFeature feature = features.next();
				geom = (Geometry) feature.getDefaultGeometryProperty().getValue();
				coord = geom.getCoordinate();
				if (coord != null) {
					coords[i++] = new Coordinate(coord);
				}
			}
		}

		while (i < size) {
			if (coord == null) {
				coord = new Coordinate(0, 0);
			}
			coords[i++] = coord;
		}

		return factory.createLineString(coords);
	}

	public LineString pointsToLine(List<SimpleFeature> collection) {
		GeometryFactory factory = new GeometryFactory();
		int size = collection.size();

		Coordinate[] coords = new Coordinate[size];
		int i = 0;

		Coordinate coord = null;
		Geometry geom;
		Iterator<SimpleFeature> features = collection.iterator();

		while (features.hasNext()) {
			SimpleFeature feature = features.next();
			geom = (Geometry) feature.getDefaultGeometryProperty().getValue();
			coord = geom.getCoordinate();
			if (coord != null) {
				coords[i++] = new Coordinate(coord);
			}
		}

		while (i < size) {
			coords[i++] = coord;
		}

		return factory.createLineString(coords);
	}

	/**
	 * Merge array JTS polygon and create a WKT multiPolygon
	 * 
	 * @param polygons
	 * @return
	 */
	public String mergeArrayPolygons(List<Geometry> polygons) {
		int totalGeoms = 0;
		for (int i = 0; i < polygons.size(); i++) {
			totalGeoms += polygons.get(i).getNumGeometries();
		}
		Polygon[] polys = new Polygon[totalGeoms];
		List<Geometry> geoms = new ArrayList<>();
		for (int i = 0; i < polygons.size(); i++) {
			List<Geometry> subGeoms = getSubGeometries(polygons.get(i));
			for (int j = 0; j < subGeoms.size(); j++) {
				geoms.add(subGeoms.get(j));
			}
		}
		geoms.toArray(polys);

		GeometryFactory factory = new GeometryFactory();

		Geometry mergePolygon = new MultiPolygon(polys, factory);
		WKTWriter writer = new WKTWriter();
		return writer.write(mergePolygon);
	}

	/**
	 * Get subgeometries list of a multi geometry
	 * 
	 * @param geom
	 * @return
	 */
	public List<Geometry> getSubGeometries(Geometry geom) {
		List<Geometry> subGeoms = new ArrayList<>();
		int numGeometries = geom.getNumGeometries();

		for (int i = 0; i < numGeometries; i++) {
			Geometry g = geom.getGeometryN(i);
			subGeoms.add(g);
		}

		return subGeoms;
	}

	/**
	 * Transform JTS geometry to a WKT polygon. If the geometry is not a polygon, it
	 * creates a buffer over the geometry and it is returned
	 * 
	 * @param geometry
	 * @return
	 */
	public Geometry transformGeometryToPolygon(Geometry geometry) {
		double distance = 0.001;
		String gtype = geometry.getGeometryType();
		boolean eq = gtype.equals("Polygon");
		boolean eq2 = gtype.equals("MultiPolygon");
		Geometry poly = null;
		if (!eq && !eq2) {
			poly = geometry.buffer(distance);
		} else {
			poly = geometry;
		}
		return poly;
	}

}
