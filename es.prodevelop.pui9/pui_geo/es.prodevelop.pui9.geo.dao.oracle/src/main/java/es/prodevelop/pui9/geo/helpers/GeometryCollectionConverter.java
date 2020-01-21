package es.prodevelop.pui9.geo.helpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.oracle.sdo.GeometryConverter;
import org.geotools.data.oracle.sdo.SDO;
import org.geotools.geometry.jts.CompoundCurve;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollectionIterator;

import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;

public class GeometryCollectionConverter extends GeometryConverter {

	public GeometryCollectionConverter(OracleConnection connection) {
		super(connection);
	}

	CurveGeometryConverter curveConverter = new CurveGeometryConverter(connection);

	/**
	 * Used to convert double[] to SDO_ODINATE_ARRAY.
	 * <p>
	 * Will return <code>null</code> as an empty <code>SDO_GEOMETRY</code>
	 * </p>
	 * 
	 * @param geom Map to be represented as a STRUCT
	 * @return STRUCT representing provided Map
	 * @see net.refractions.jspatial.Converter#toDataType(java.lang.Object)
	 */
	@Override
	public STRUCT toSDO(Geometry geomCollection, int srid) throws SQLException {
		STRUCT struct;
		try {
			struct = checkForCurvedGeomsInCollectionAndDoExplodeIfCompoundCurve(geomCollection, srid);
		} catch (Exception e) {
			throw new SQLException(e.getCause());
		}
		return struct;
	}

	@SuppressWarnings("unchecked")
	public STRUCT checkForCurvedGeomsInCollectionAndDoExplodeIfCompoundCurve(Geometry geom, int srid) throws Exception {

		GeometryCollectionIterator geomCollectionIterator = new GeometryCollectionIterator(geom);
		int i = 0;
		geomCollectionIterator.next();
		STRUCT sdoPoint = null;
		Integer sdoSgrid = (srid == SDO.SRID_NULL || srid == 0) ? null : srid;
		Integer sdoGtype = 2004;
		List<Integer> elemInfoList = new ArrayList<>();
		List<Double> ordinatesList = new ArrayList<>();
		int startEnd = 1;

		while (geomCollectionIterator.hasNext()) {
			Geometry geomObject = geom.getGeometryN(i);

			if (geomObject instanceof CompoundCurve) {
				CompoundCurve compoundCurveGeom = (CompoundCurve) geomObject;
				for (int j = 0; j < compoundCurveGeom.getNumGeometries(); j++) {
					Geometry curveOrLine = compoundCurveGeom.getGeometryN(j);
					if (curveConverter.isCurvedGeometry(curveOrLine)) {
						Object[] datum = (Object[]) curveConverter.getDatumObjectFromCurvedGeom(curveOrLine, srid);
						List<Integer> curvedGeomInfoList = (List<Integer>) datum[CurveGeometryConverter.SDO_ELEM_INFO_DATUM_POS];
						curvedGeomInfoList = curveConverter.setStartEndOfGeometry(curvedGeomInfoList, startEnd);
						elemInfoList.addAll(curvedGeomInfoList);
						ordinatesList.addAll((List<Double>) datum[CurveGeometryConverter.SDO_ORDINATES_DATUM_POS]);

					} else {
						ordinatesList.addAll(curveConverter.getOrdinateListFromGeom(curveOrLine, srid));
						elemInfoList.addAll(curveConverter.getElemListFromGeom(curveOrLine, srid, startEnd));
					}
					startEnd = ordinatesList.size() + 1;
				}
			} else if (curveConverter.isCurvedGeometry(geomObject)) {
				Object[] datum = (Object[]) curveConverter.getDatumObjectFromCurvedGeom(geomObject, srid);
				List<Integer> curvedGeomInfoList = (List<Integer>) datum[CurveGeometryConverter.SDO_ELEM_INFO_DATUM_POS];
				curvedGeomInfoList = curveConverter.setStartEndOfGeometry(curvedGeomInfoList, startEnd);
				elemInfoList.addAll(curvedGeomInfoList);
				ordinatesList.addAll((List<Double>) datum[CurveGeometryConverter.SDO_ORDINATES_DATUM_POS]);

			} else {

				ordinatesList.addAll(curveConverter.getOrdinateListFromGeom(geomObject, srid));
				elemInfoList.addAll(curveConverter.getElemListFromGeom(geomObject, srid, startEnd));
			}
			i++;
			startEnd = ordinatesList.size() + 1;
			geomCollectionIterator.next();
		}
		Object[] attributes = new Object[] { sdoGtype, sdoSgrid, sdoPoint, elemInfoList, ordinatesList };
		return curveConverter.createStructFromDatum(attributes);

	}

}