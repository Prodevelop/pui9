package es.prodevelop.pui9.geo.utils.transform;

import org.locationtech.jts.geom.Point;

import es.prodevelop.pui9.geo.exceptions.PuiGeoCoordinatesException;

/**
 * Transformaciones básicas de coordenadas geográficas a Mercator
 * 
 * @author aromeu
 * 
 */
public interface MTransform {

	double[] toMercator(double[] coords) throws PuiGeoCoordinatesException;

	Point toMercatorAsPoint(double[] coords) throws PuiGeoCoordinatesException;

	double[] toMercator(Point point) throws PuiGeoCoordinatesException;

	Point toMercatorAsPoint(Point point) throws PuiGeoCoordinatesException;

}