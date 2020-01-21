package es.prodevelop.pui9.geo.utils;

import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.filter.FilterGroupOperation;
import es.prodevelop.pui9.filter.FilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;
import es.prodevelop.pui9.geo.dao.helpers.IDatabaseGeoHelper;

public class GeoFilterBuilder extends FilterBuilder {

	public static GeoFilterBuilder newFilter(IDatabaseGeoHelper sqlAdapter) {
		return new GeoFilterBuilder(sqlAdapter);
	}

	public static GeoFilterBuilder newFilter(IDatabaseGeoHelper sqlAdapter, FilterGroupOperation op) {
		return new GeoFilterBuilder(sqlAdapter, op);
	}

	public static GeoFilterBuilder newFilter(IDatabaseGeoHelper sqlAdapter, FilterGroup filters) {
		return new GeoFilterBuilder(sqlAdapter, filters);
	}

	private IDatabaseGeoHelper sqlAdapter;

	private GeoFilterBuilder(IDatabaseGeoHelper sqlAdapter) {
		this(sqlAdapter, FilterGroupOperation.and);
	}

	private GeoFilterBuilder(IDatabaseGeoHelper sqlAdapter, FilterGroupOperation op) {
		this(sqlAdapter, new FilterGroup(op));
	}

	private GeoFilterBuilder(IDatabaseGeoHelper sqlAdapter, FilterGroup filters) {
		super(filters);
		this.sqlAdapter = sqlAdapter;
	}

	public GeoFilterBuilder filterByBoundingBox(String column, Integer srid) {
		filters.addRule(new FilterRule(null, FilterRuleOperation.geo_bounding_box,
				sqlAdapter.filterByBoundingBox(column, srid)));
		return this;
	}

	public GeoFilterBuilder intersectsByPoint2D(String column, Integer srid) {
		filters.addRule(
				new FilterRule(null, FilterRuleOperation.geo_intersects, sqlAdapter.intersectsByPoint2D(column, srid)));
		return this;
	}

}
