package es.prodevelop.pui9.geo.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.oracle.sdo.GeometryConverter;
import org.geotools.data.oracle.sdo.SDO;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.CompoundRing;
import org.geotools.geometry.jts.CurvePolygon;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;

import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.Datum;
import oracle.sql.NUMBER;
import oracle.sql.STRUCT;

public class CurveGeometryConverter extends GeometryConverter {

	protected final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Used to convert double[] to SDO_ODINATE_ARRAY.
	 * <p>
	 * Will return <code>null</code> as an empty <code>SDO_GEOMETRY</code>
	 * </p>
	 * 
	 * @param geom Map to be represented as a STRUCT
	 * @return STRUCT representing provided Map
	 * @see net.refractions.jspatial.Converter#toDataType(java.lang.Object)
	 *
	 *      POSICIONES FIJAS DE CADA ELEMENTO EN EL OBJETO DATUM QUE SIRVE PARA
	 *      CREAR EL STRUTS
	 **/
	public static final int SDO_GTYPE_DATUM_POS = 0;
	public static final int SDO_SRID_DATUM_POS = 1;
	public static final int SDO_POINT_DATUM_POS = 2;
	public static final int SDO_ELEM_INFO_DATUM_POS = 3;
	public static final int SDO_ORDINATES_DATUM_POS = 4;

	public CurveGeometryConverter(OracleConnection connection) {
		super(connection);
	}

	public STRUCT toSDO(Geometry geom, int srid) throws SQLException {
		if (geom == null) {
			return asEmptyDataType();
		}

		try {
			if (geom instanceof CircularString) {
				// LINES
				return createStructFromDatum((Object[]) toCircularString(geom, srid));
			} else if (geom instanceof CompoundCurve) {
				// LINES
				return createStructFromDatum((Object[]) toCompoundCurve(geom, srid));
			} else if (geom instanceof CircularRing) {
				// LINES
				return createStructFromDatum((Object[]) toCircularRing(geom, srid));
			} else if (geom instanceof CurvePolygon) {
				// POLYGONS
				return createStructFromDatum((Object[]) toCurvePolygon(geom, srid));
			} else if (geom instanceof CompoundRing) {
				// POLYGONS
				return createStructFromDatum((Object[]) toCompoundRing(geom, srid, false));
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new SQLException(e.getCause());
		}
	}

	/**
	 * Constructs the datum object made from native java objects so that can be used
	 * by others easily
	 * 
	 * @param datum
	 * @return STRUCT
	 */
	protected Object getDatumObjectFromCurvedGeom(Geometry geom, int srid) throws SQLException {
		if (geom == null) {
			return asEmptyDataType();
		}

		if (geom instanceof CircularString) {
			// LINES
			return toCircularString(geom, srid);
		} else if (geom instanceof CompoundCurve) {
			// LINES
			return toCompoundCurve(geom, srid);
		} else if (geom instanceof CircularRing) {
			// LINES
			return toCircularRing(geom, srid);
		} else if (geom instanceof CurvePolygon) {
			// POLYGONS
			return toCurvePolygon(geom, srid);
		} else if (geom instanceof CompoundRing) {
			// POLYGONS
			return toCompoundRing(geom, srid, false);
		} else {
			return null;
		}
	}

	/**
	 * Constructs a struct object given the datum object made from native java
	 * objects
	 * 
	 * @param datum
	 * @return STRUCT
	 */
	@SuppressWarnings("unchecked")
	protected STRUCT createStructFromDatum(Object[] datum) throws Exception {
		try {
			STRUCT sdoPoint = null;
			NUMBER sdoSgrid = new NUMBER(datum[SDO_SRID_DATUM_POS]);
			NUMBER sdoGtype = new NUMBER(datum[SDO_GTYPE_DATUM_POS]);
			List<Integer> elemInfo = (List<Integer>) datum[SDO_ELEM_INFO_DATUM_POS];
			int[] elemInfoIntArray = new int[elemInfo.size()];
			int i = 0;
			for (Integer integer : elemInfo) {
				elemInfoIntArray[i] = integer.intValue();
				i++;
			}
			ARRAY sdoElemInfo = toARRAY(elemInfoIntArray, "MDSYS.SDO_ELEM_INFO_ARRAY");

			List<Double> ordinateList = (List<Double>) datum[SDO_ORDINATES_DATUM_POS];
			double[] ordinateArray = new double[ordinateList.size()];
			i = 0;
			for (Double doub : ordinateList) {
				ordinateArray[i] = doub.doubleValue();
				i++;
			}

			ARRAY sdoOrdinates = toARRAY(ordinateArray, "MDSYS.SDO_ORDINATE_ARRAY");

			Datum attributes[] = new Datum[] { sdoGtype, sdoSgrid, sdoPoint, sdoElemInfo, sdoOrdinates };
			return toSTRUCT(attributes, DATATYPE);
		} catch (Exception e) {
			String message = "Error creando STRUCT a partir de datum";
			logger.error(message + e.getStackTrace());
			throw new Exception(message);
		}
	}

	private Object toCircularString(Geometry geom, int srid) {
		// ARCOS
		STRUCT sdoPoint = null;

		Integer sdoSrig = (srid == SDO.SRID_NULL || srid == 0) ? null : srid;
		int dim = geom.getDimension();

		Integer sdoGtype = 2002;
		if (dim == 3) {
			sdoGtype = 3002;
		}
		List<Integer> elemInfo = new ArrayList<>();
		elemInfo.add(1);
		elemInfo.add(2);
		elemInfo.add(2);
		CircularString csGeom = (CircularString) geom;

		double[] ordinates = csGeom.getControlPoints();
		List<Double> ordinatesList = new ArrayList<>();

		for (int i = 0; i < ordinates.length; i++) {
			ordinatesList.add(ordinates[i]);
		}

		return new Object[] { sdoGtype, sdoSrig, sdoPoint, elemInfo, ordinatesList };
	}

	private Object toCompoundCurve(Geometry geom, int srid) {
		// CURVAS Y RECTAS
		STRUCT sdoPoint = null;
		Integer sdoSgrid = (srid == SDO.SRID_NULL || srid == 0) ? null : srid;
		Integer sdoGtype = 2002;

		int ngeoms = geom.getNumGeometries();
		List<Integer> elemInfo = new ArrayList<>();
		elemInfo.add(1);
		elemInfo.add(4);
		elemInfo.add(2);
		if (ngeoms > 2) {
			elemInfo.set(2, ngeoms);
		}

		List<Double> coordsList = new ArrayList<>();
		List<Integer> startEnd = new ArrayList<>();

		for (int i = 0; i < ngeoms; i++) {
			Geometry subgeom = geom.getGeometryN(i);

			Coordinate[] arrayCoords = subgeom.getCoordinates();
			if (subgeom instanceof CircularString) {
				CircularString cgeom = (CircularString) subgeom;
				double[] controlPoints = cgeom.getControlPoints();
				arrayCoords = new Coordinate[(int) ((controlPoints.length + 1) / 2)];
				int count = 0;
				for (int j = 0; j < controlPoints.length; j += 2) {
					double x = controlPoints[j];
					double y = controlPoints[j + 1];
					Coordinate nc = new Coordinate(x, y);
					arrayCoords[count] = nc;
					count++;
				}
			}

			if (startEnd.isEmpty()) {
				startEnd.add(1);
			} else {
				startEnd.add(coordsList.size() - 1);
			}

			for (int j = 0; j < arrayCoords.length; j++) {
				Coordinate c = arrayCoords[j];
				if (coordsList.isEmpty()) {
					coordsList.add(Double.valueOf(c.x));
					coordsList.add(Double.valueOf(c.y));

				} else {
					double lastX = coordsList.get(coordsList.size() - 2);
					double lastY = coordsList.get(coordsList.size() - 1);
					if (lastX != c.x || lastY != c.y) {
						coordsList.add(Double.valueOf(c.x));
						coordsList.add(Double.valueOf(c.y));
					}
				}
			}
		}

		for (int i = 0; i < ngeoms; i++) {
			Geometry subgeom = geom.getGeometryN(i);

			if (subgeom instanceof CircularString) {
				elemInfo.add(startEnd.get(i));
				elemInfo.add(2);
				elemInfo.add(2);
			} else {
				elemInfo.add(startEnd.get(i));
				elemInfo.add(2);
				elemInfo.add(1);
			}
		}
		List<Double> ordinatesList = new ArrayList<>();
		for (int i = 0; i < coordsList.size(); i++) {
			ordinatesList.add(coordsList.get(i));
		}

		return new Object[] { sdoGtype, sdoSgrid, sdoPoint, elemInfo, ordinatesList };
	}

	private Object toCircularRing(Geometry geom, int srid) {
		// CIRCUNFERENCIAS
		STRUCT sdoPoint = null;
		Integer sdoSrid = (srid == SDO.SRID_NULL || srid == 0) ? null : srid;
		Integer sdoGtype = 2002;

		List<Integer> elemInfo = new ArrayList<>();
		elemInfo.add(1);
		elemInfo.add(2);
		elemInfo.add(2);

		double ordinates[] = ((CircularRing) geom).getControlPoints();
		List<Double> ordinatesList = new ArrayList<>();

		for (int i = 0; i < ordinates.length; i++) {
			ordinatesList.add(ordinates[i]);
		}

		return new Object[] { sdoGtype, sdoSrid, sdoPoint, elemInfo, ordinatesList };
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////
	// POLYGONS
	// ///////////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////////////

	private Object toCompoundRing(Geometry geom, int srid, boolean isInnerRing) {
		List<Integer> elemInfo = new ArrayList<>();
		List<Double> ordinatesList = new ArrayList<>();
		STRUCT sdoPoint = null;
		Integer sdoSrid = (srid == SDO.SRID_NULL || srid == 0) ? null : srid;
		Integer sdoGtype = 2003;

		// POLIGONO CON ARCOS

		int ngeoms = geom.getNumGeometries();
		if (!isInnerRing) {
			elemInfo.add(1);
			elemInfo.add(1005);
			elemInfo.add(ngeoms);
		}

		List<Integer> startEnd = new ArrayList<>();

		for (int i = 0; i < ngeoms; i++) {
			Geometry subgeom = geom.getGeometryN(i);

			Coordinate[] arrayCoords = subgeom.getCoordinates();
			if (subgeom instanceof CircularString) {
				CircularString cgeom = (CircularString) subgeom;
				double[] controlPoints = cgeom.getControlPoints();
				arrayCoords = new Coordinate[controlPoints.length / 2];
				int count = 0;
				for (int j = 0; j < controlPoints.length; j += 2) {
					double x = controlPoints[j];
					double y = controlPoints[j + 1];
					Coordinate nc = new Coordinate(x, y);
					arrayCoords[count] = nc;
					count++;
				}
			}

			if (startEnd.isEmpty()) {
				startEnd.add(1);
			} else {
				startEnd.add(ordinatesList.size() + 1);
			}

			for (int j = 0; j < arrayCoords.length; j++) {
				Coordinate c = arrayCoords[j];
				if (ordinatesList.isEmpty()) {
					ordinatesList.add(Double.valueOf(c.x));
					ordinatesList.add(Double.valueOf(c.y));
				} else {
					double lastX = ordinatesList.get(ordinatesList.size() - 2);
					double lastY = ordinatesList.get(ordinatesList.size() - 1);
					if (lastX != c.x || lastY != c.y) {
						ordinatesList.add(Double.valueOf(c.x));
						ordinatesList.add(Double.valueOf(c.y));
					} else {
						startEnd.set(i, ordinatesList.size() - 1);
					}
				}
			}
		}

		for (int i = 0; i < ngeoms; i++) {
			Geometry subgeom = geom.getGeometryN(i);

			if (subgeom instanceof CircularString) {
				elemInfo.add(startEnd.get(i));
				elemInfo.add(2);
				elemInfo.add(2);
			} else {
				elemInfo.add(startEnd.get(i));
				elemInfo.add(2);
				elemInfo.add(1);
			}
		}

		return new Object[] { sdoGtype, sdoSrid, sdoPoint, elemInfo, ordinatesList };
	}

	@SuppressWarnings("unchecked")
	private Object toCurvePolygon(Geometry geom, int srid) {
		STRUCT sdoPoint = null;
		Integer sdoSrid = (srid == SDO.SRID_NULL || srid == 0) ? null : srid;
		Integer sdoGtype = 2003;

		CurvePolygon cg = (CurvePolygon) geom;
		LineString exteriorRing = cg.getExteriorRing();
		int ngeoms = cg.getNumGeometries();
		List<Integer> elemInfo = new ArrayList<>();

		if (exteriorRing instanceof CircularRing && ngeoms == 1) {
			// CIRCULO Y ...

			elemInfo.add(1);
			elemInfo.add(1003);
			elemInfo.add(2);

			double ordinates[] = ((CircularRing) exteriorRing).getControlPoints();

			List<Double> ordinatesList = new ArrayList<>();

			for (int i = 0; i < ordinates.length; i++) {
				ordinatesList.add(ordinates[i]);
			}

			return new Object[] { sdoGtype, sdoSrid, sdoPoint, elemInfo, ordinatesList };
		} else {
			CurvePolygon curvePolygonGeom = (CurvePolygon) geom;
			if (ngeoms != 1) {
				return null;
			}

			Geometry exteriorGeom = curvePolygonGeom.getExteriorRing();
			if (exteriorGeom instanceof CompoundRing) {
				return toCompoundRing(exteriorGeom, srid, false);
			} else {
				// then only can be a straight outter polygon with curved hole
				elemInfo.add(1);
				elemInfo.add(1003);
				elemInfo.add(1);
				int numberOfVertices = exteriorGeom.getNumPoints();
				Coordinate[] geomCoordiantes = exteriorGeom.getCoordinates();
				List<Double> ordinatesList = new ArrayList<>();
				for (int i = 0; i < numberOfVertices; i++) {
					ordinatesList.add(geomCoordiantes[i].x);
					ordinatesList.add(geomCoordiantes[i].y);
				}
				int startEnd = numberOfVertices * 2 + 1;
				for (int i = 0; i < cg.getNumInteriorRing(); i++) {
					Geometry innerRing = cg.getInteriorRingN(i);
					Object[] innerRingAttributes = null;
					boolean isCompound = false;
					boolean isInnerRing = true;
					if (innerRing instanceof CompoundRing) {
						innerRingAttributes = (Object[]) toCompoundRing(innerRing, srid, isInnerRing);
						isCompound = true;

					} else if (innerRing instanceof CircularRing) {
						innerRingAttributes = (Object[]) toCircularRing(innerRing, srid);
					} else {
						List<Integer> innerStraightRingElemInfoList = new ArrayList<>();
						List<Double> innerRingOrdinateList = new ArrayList<>();

						innerStraightRingElemInfoList.add(1);
						innerStraightRingElemInfoList.add(2003);
						innerStraightRingElemInfoList.add(1);
						Coordinate[] innerRingOrdinates = ((LinearRing) innerRing).getCoordinates();
						for (int j = 0; j < innerRingOrdinates.length; j++) {
							innerRingOrdinateList.add(innerRingOrdinates[j].x);
							innerRingOrdinateList.add(innerRingOrdinates[j].y);

						}
						innerRingAttributes = new Object[] { null, null, null, innerStraightRingElemInfoList,
								innerRingOrdinateList };
					}
					List<Integer> innerRingElemInfo = (List<Integer>) innerRingAttributes[SDO_ELEM_INFO_DATUM_POS];
					innerRingElemInfo = changeTypetoInteriorPolygon(isCompound, innerRingElemInfo);
					elemInfo.addAll(setStartEndOfGeometry(innerRingElemInfo, startEnd));
					ordinatesList.addAll((List<Double>) innerRingAttributes[SDO_ORDINATES_DATUM_POS]);
					startEnd = ordinatesList.size() + 1;

				}

				return new Object[] { sdoGtype, sdoSrid, sdoPoint, elemInfo, ordinatesList };
			}
		}
	}

	/**
	 * changes the descriptor of a polygon of a geometry depending if it is compound
	 * (hole) is used only for a curvepolygon that is hole of other polygon
	 * 
	 * @param isCompound        indicates if it is a hole or not
	 * @param innerRingElemInfo the element info of the geometry
	 * @return List<Integers>
	 */
	private List<Integer> changeTypetoInteriorPolygon(boolean isCompound, List<Integer> innerRingElemInfo) {
		if (!isCompound) {
			return innerRingElemInfo;
		} else {
			List<Integer> compoundCurveExplodedElemInfo = new ArrayList<>();
			Integer numberOfTriplets = innerRingElemInfo.size() / 3;
			compoundCurveExplodedElemInfo.add(1);
			compoundCurveExplodedElemInfo.add(2005);
			compoundCurveExplodedElemInfo.add(numberOfTriplets);
			compoundCurveExplodedElemInfo.addAll(innerRingElemInfo);
			return compoundCurveExplodedElemInfo;
		}
	}

	/**
	 * sets the firsts elements of all triplets in a info_array of
	 * curvedGeomInfoList sequence, used for geometry collections or compounds
	 * polygons
	 * 
	 * @param curvedGeomInfoList the element info array of a curvedGeometry
	 * 
	 * @param startEnd           this is the current length of the ordinates array
	 *                           of the geometry
	 * @return List<Integers>
	 */
	protected List<Integer> setStartEndOfGeometry(List<Integer> curvedGeomInfoList, int startEnd) {
		if (curvedGeomInfoList.size() == 3) {
			curvedGeomInfoList.set(0, startEnd);
		} else {
			Integer numberOfTriplets = curvedGeomInfoList.size() / 3;
			for (int i = 0; i < numberOfTriplets; i++) {
				if (i == 0) {
					curvedGeomInfoList.set(0, startEnd);
				} else if (i == 1 && curvedGeomInfoList.get(4) == 2005) {
					curvedGeomInfoList.set(3, curvedGeomInfoList.get(3) + startEnd - 1);
				} else if (i == 1) {
					curvedGeomInfoList.set(3, startEnd);
				} else {
					curvedGeomInfoList.set(i * 3, curvedGeomInfoList.get(i * 3) + startEnd - 1);
				}
			}

			if (curvedGeomInfoList.get(4) == 2003) {
				curvedGeomInfoList.set(3, curvedGeomInfoList.get(6));
			}
		}

		return curvedGeomInfoList;
	}

	protected boolean isCurvedGeometry(Geometry g) {
		return g instanceof CircularString || g instanceof CircularRing || g instanceof CurvePolygon
				|| g instanceof CompoundRing || g instanceof CompoundCurve;
	}

	/**
	 * Calculates the a list of Integers that defines the geometry
	 * element_info_array of sdo_geometry in oracle sequence
	 * 
	 * @param geomObject
	 * @param srid
	 * @param startEnd   this is the current length of the ordinates array, to set
	 *                   the first element of the element info
	 * @return List<Integers>
	 * @throws SQLException
	 */
	protected List<Integer> getElemListFromGeom(Geometry geomObject, int srid, int startEnd) throws SQLException {
		List<Integer> theList = new ArrayList<>();
		if (geomObject instanceof Point) {
			theList.add(startEnd);
			theList.add(1);
			theList.add(1);
			return theList;
		}

		STRUCT struct = super.toSDO(geomObject, srid);
		ARRAY elemInfo = (ARRAY) struct.getOracleAttributes()[SDO_ELEM_INFO_DATUM_POS];
		int[] elemInfoArray = elemInfo.getIntArray();
		for (int i = 0; i < elemInfo.length(); i++) {
			if (i == 0) {
				theList.add(startEnd);
			} else {
				theList.add(elemInfoArray[i]);
			}
		}

		return theList;
	}

	/**
	 * Calculates the a list of doubles that defines the geometry coordinates
	 * sequence
	 * 
	 * @param geomObject
	 * @param srid
	 * @return List<Double>
	 * @throws SQLException
	 */
	protected List<Double> getOrdinateListFromGeom(Geometry geomObject, int srid) throws SQLException {
		List<Double> theList = new ArrayList<>();
		if (geomObject instanceof Point) {
			Point point = (Point) geomObject;
			theList.add(point.getX());
			theList.add(point.getY());
			return theList;
		}

		STRUCT struct = super.toSDO(geomObject, srid);
		ARRAY ordinateArray = (ARRAY) struct.getOracleAttributes()[SDO_ORDINATES_DATUM_POS];
		double[] ordinateDoubleArray = ordinateArray.getDoubleArray();
		for (int i = 0; i < ordinateDoubleArray.length; i++) {
			theList.add(ordinateDoubleArray[i]);
		}

		return theList;
	}

}
