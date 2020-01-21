package es.prodevelop.pui9.geo.utils.datastore;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import es.prodevelop.pui9.exceptions.PuiException;

public class SHP {

	private String shapeFileName;
	private DataStore dataStore;
	private String typeName;

	public SHP(String shapeFileName) {
		this.shapeFileName = shapeFileName;
	}

	public void open() throws PuiException {
		File file = new File(this.shapeFileName);
		Map<String, Object> map = new HashMap<>();
		try {
			map.put("url", file.toURI().toURL());
			dataStore = DataStoreFinder.getDataStore(map);
			typeName = dataStore.getTypeNames()[0];
		} catch (IOException e) {
			throw new PuiException(e);
		}
	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(Filter filter) throws PuiException {
		try {
			FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);

			if (filter != null) {
				return source.getFeatures(filter);
			}

			return source.getFeatures();
		} catch (IOException e) {
			throw new PuiException(e);
		}
	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> getFeaturesSorted(String sortField, SortOrder sortOrder)
			throws PuiException {
		try {
			FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);

			FilterFactory filterFactory = new FilterFactoryImpl();
			Query query = new Query(typeName);
			SortBy sort = filterFactory.sort(sortField, sortOrder);
			query.setSortBy(new SortBy[] { sort });

			FeatureCollection<SimpleFeatureType, SimpleFeature> sorted = source.getFeatures(query);

			return sorted;
		} catch (IOException e) {
			throw new PuiException(e);
		}
	}

	public void write(String shapeFileName, String typeName, String typeStructure, List<SimpleFeature> features)
			throws PuiException {
		File file = new File(shapeFileName);

		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
		SimpleFeatureType type = null;
		ShapefileDataStore newDataStore;
		try {
			type = DataUtilities.createType(typeName, typeStructure);
			Map<String, Serializable> params = new HashMap<>();
			params.put("url", file.toURI().toURL());
			params.put("create spatial index", Boolean.TRUE);
			newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
			newDataStore.createSchema(type);
		} catch (IOException | SchemaException e) {
			throw new PuiException(e.getMessage());
		}

		/*
		 * Write the features to the shapefile
		 */
		Transaction transaction = new DefaultTransaction("create");

		// String typeName = newDataStore.getTypeNames()[0];
		SimpleFeatureSource featureSource;
		try {
			String newTypeName = newDataStore.getTypeNames()[0];
			featureSource = newDataStore.getFeatureSource(newTypeName);
			/*
			 * The Shapefile format has a couple limitations: - "the_geom" is always first,
			 * and used for the geometry attribute name - "the_geom" must be of type Point,
			 * MultiPoint, MuiltiLineString, MultiPolygon - Attribute names are limited in
			 * length - Not all data types are supported (example Timestamp represented as
			 * Date)
			 * 
			 * Each data store has different limitations so check the resulting
			 * SimpleFeatureType.
			 */

			if (featureSource instanceof SimpleFeatureStore) {
				SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
				/*
				 * SimpleFeatureStore has a method to add features from a
				 * SimpleFeatureCollection object, so we use the ListFeatureCollection class to
				 * wrap our list of features.
				 */
				SimpleFeatureCollection collection = new ListFeatureCollection(type, features);
				featureStore.setTransaction(transaction);
				try {
					featureStore.addFeatures(collection);
					transaction.commit();
				} catch (Exception problem) {
					try {
						transaction.rollback();
					} catch (IOException e) {
						throw new PuiException(e);
					}
				} finally {
					try {
						transaction.close();
					} catch (IOException e) {
						throw new PuiException(e);
					}
				}
			} else {
				try {
					transaction.close();
				} catch (IOException e) {
					throw new PuiException(e);
				}
				throw new PuiException(typeName + " does not support read/write access");
			}
		} catch (IOException e) {
			try {
				transaction.close();
			} catch (IOException e1) {
				throw new PuiException(e1);
			}
			throw new PuiException(e);
		}
	}

	public void close() {
		this.dataStore.dispose();
	}
}
