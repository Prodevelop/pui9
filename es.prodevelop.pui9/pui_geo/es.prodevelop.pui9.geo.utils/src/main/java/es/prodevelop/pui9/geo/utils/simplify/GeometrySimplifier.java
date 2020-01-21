package es.prodevelop.pui9.geo.utils.simplify;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

public class GeometrySimplifier {

	public Geometry simplify(Geometry geometry, double distanceTolerance) {
		return simplify(geometry, distanceTolerance, true);
	}

	public Geometry simplify(Geometry geometry, double distanceTolerance, boolean preserveTopology) {
		if (geometry == null) {
			return null;
		}

		if (!preserveTopology) {
			return DouglasPeuckerSimplifier.simplify(geometry, distanceTolerance);
		}

		TopologyPreservingSimplifier tps = new TopologyPreservingSimplifier(geometry);
		tps.setDistanceTolerance(distanceTolerance);
		return tps.getResultGeometry();
	}
}
