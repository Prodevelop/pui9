package es.prodevelop.pui9.service;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;

import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.exceptions.PuiDaoDeleteException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;

/**
 * This class is used in the {@link AbstractService} classes to configure the
 * multivalued attributes. For each multivalued attribute configuration, you
 * must provide:
 * <ul>
 * <li>The Java Field name of the multivalued attribute</li>
 * <li>A list of pairs of table Java Fields, indicating at left the Java Field
 * of the local table and at right the Java Field of the multivalued table used
 * to make the relationship between the two tables (local and multivalued)</li>
 * <li>A list of pairs of table Java Fields, indicating at left the Java Field
 * of the referenced table and at right the Java Field of the multivalued table
 * used to make the relationship between the two tables (referenced and
 * multivalued)</li>
 * <li>The DAO class of the referenced table: A DAO that extends from
 * ITableDao</li>
 * <li>The DAO class of the multivalued table: A DAO that extends from
 * ITableDao</li>
 * </ul>
 * 
 * @param <LTD>   Local Table DTO
 * @param <RTDPK> Referenced Table DTO PK
 * @param <RTD>   Referenced Table DTO
 * @param <RD>    Referenced DAO
 * @param <MTDPK> Multivalued Table DTO PK
 * @param <MTD>   Multivalued Table DTO
 * @param <MD>    Multivalued Table DAO
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class MultiValuedAttribute<LTD extends ITableDto, RTDPK extends ITableDto, RTD extends RTDPK, RD extends ITableDao<RTDPK, RTD>, MTDPK extends ITableDto, MTD extends MTDPK, MD extends ITableDao<MTDPK, MTD>> {

	private String fieldName;
	private List<Pair<String, String>> localFields;
	private List<Pair<String, String>> referencedFields;
	private Class<RD> referencedDaoClass;
	private Class<MD> multivaluedDaoClass;

	private RD referencedDao;
	private MD multivaluedDao;

	public MultiValuedAttribute(String fieldName, List<Pair<String, String>> localFields,
			List<Pair<String, String>> referencedFields, Class<RD> referencedDaoClass, Class<MD> multivaluedDaoClass) {
		this.fieldName = fieldName;
		this.localFields = localFields;
		this.referencedFields = referencedFields;
		this.referencedDaoClass = referencedDaoClass;
		this.multivaluedDaoClass = multivaluedDaoClass;
	}

	/**
	 * Populate the multivalued field of the given DTO with the values
	 * 
	 * @param local The registry to populate the multivalued field
	 */
	void populate(LTD local) {
		List<RTD> referencedList = getReferencedFromDatabase(local);
		setFieldValue(local, fieldName, referencedList);
	}

	/**
	 * Insert all the multivalued registries
	 * 
	 * @param local The local registry
	 * @throws PuiServiceInsertException If any exception is thrown while inserting
	 *                                   the multivalued registries
	 */
	void insert(LTD local) throws PuiServiceInsertException {
		for (RTD referenced : getReferencedFromField(local)) {
			MTD multivalued = createMultivalued(local, referenced);
			try {
				getMultivaluedDao().save(multivalued);
				afterInsertMultivalued(local, referenced, multivalued);
			} catch (PuiDaoSaveException e) {
				throw new PuiServiceInsertException(e);
			}
		}
	}

	/**
	 * Update all the multivalued registries. The way to manage the multivalued
	 * registries, is to delete and insert them again
	 * 
	 * @param local The registry to update its multivalued registries
	 * @throws PuiServiceUpdateException If any exception is thrown while updating
	 *                                   the multivalued registries
	 */
	void update(LTD local) throws PuiServiceUpdateException {
		try {
			delete(local, false);
			insert(local);
		} catch (PuiServiceDeleteException | PuiServiceInsertException e) {
			throw new PuiServiceUpdateException(e);
		}
	}

	/**
	 * Delete all the multivalued registries
	 * 
	 * @param local The registry to delete its multivalued registries
	 * @param force If true, all the multivalued registries will be deleted (delete
	 *              action); if not, only those that are not present in the list of
	 *              the DTO (update action)
	 * @throws PuiServiceUpdateException If any exception is thrown while deleting
	 *                                   the multivalued registries
	 */
	void delete(LTD local, boolean force) throws PuiServiceDeleteException {
		List<RTD> referencedFromFieldList = getReferencedFromField(local);
		List<RTD> referencedFromDbList = getReferencedFromDatabase(local);
		first: for (RTD referencedDb : referencedFromDbList) {
			if (!force) {
				for (RTD referencedLocal : referencedFromFieldList) {
					if (areEqualsReferenced(referencedDb, referencedLocal)) {
						continue first;
					}
				}
			}

			MTD multivalued = createMultivalued(local, referencedDb);
			if (multivalued == null) {
				continue;
			}

			try {
				beforeDeleteMultivalued(local, referencedDb, multivalued);
				getMultivaluedDao().delete(multivalued.createPk());
				afterDeleteMultivalued(local, referencedDb, multivalued);
			} catch (PuiDaoDeleteException e) {
				throw new PuiServiceDeleteException(e);
			}
		}
	}

	/**
	 * Do something after create a multivalued registry
	 * 
	 * @param local       The local registry
	 * @param referenced  The referenced registry
	 * @param multivalued The multivalued registry
	 */
	protected void afterCreateMultivalued(LTD local, RTD referenced, MTD multivalued) {
		// nothing to do
	}

	/**
	 * Do something after inserting a multivalued registry
	 * 
	 * @param local       The local registry
	 * @param referenced  The referenced registry
	 * @param multivalued The multivalued registry
	 */
	protected void afterInsertMultivalued(LTD local, RTD referenced, MTD multivalued) {
		// nothing to do
	}

	/**
	 * Do something before deleting a multivalued registry
	 * 
	 * @param local       The local registry
	 * @param referenced  The referenced registry
	 * @param multivalued The multivalued registry
	 */
	protected void beforeDeleteMultivalued(LTD local, RTD referenced, MTD multivalued) {
		// nothing to do
	}

	/**
	 * Do something after deleting a multivalued registry
	 * 
	 * @param local       The local registry
	 * @param referenced  The referenced registry
	 * @param multivalued The multivalued registry
	 */
	protected void afterDeleteMultivalued(LTD local, RTD referenced, MTD multivalued) {
		// nothing to do
	}

	/**
	 * Get the list of referenced values for the given DTO
	 * 
	 * @param local The DTO that we want to get the referenced list of values
	 * @return The list of referenced registries in the other side of the
	 *         multivalued relationship
	 */
	private List<RTD> getReferencedFromDatabase(LTD local) {
		List<MTD> multivaluedList;
		if (hasValidFieldValues(local)) {
			multivaluedList = getMultivaluedListFromDatabase(local);
		} else {
			multivaluedList = Collections.emptyList();
		}

		return getReferencedListFromMultivaluedList(multivaluedList);
	}

	/**
	 * Get the list of referenced values from the field of the DTO
	 * 
	 * @param local The DTO that we want to get the referenced list of values
	 * @return The list of referenced registries in the other side of the
	 *         multivalued relationship
	 */
	@SuppressWarnings("unchecked")
	private List<RTD> getReferencedFromField(LTD local) {
		List<RTD> referencedList = (List<RTD>) getFieldValue(local, fieldName);
		if (referencedList == null) {
			referencedList = Collections.emptyList();
		}
		return referencedList;
	}

	/**
	 * Get the list of registries of the multivalued table for the given DTO
	 * 
	 * @param local The DTO to obtain its related multivalued registries
	 * @return The list of related multivalued registries
	 */
	private List<MTD> getMultivaluedListFromDatabase(LTD local) {
		FilterBuilder filterBuilder = FilterBuilder.newAndFilter();

		for (Pair<String, String> localField : localFields) {
			Object value = getFieldValue(local, localField.getLeft());
			if (value == null) {
				break;
			}

			filterBuilder.addEquals(localField.getRight(), value);
		}

		List<MTD> multivaluedList;
		try {
			multivaluedList = getMultivaluedDao().findWhere(filterBuilder);
		} catch (PuiDaoFindException e) {
			multivaluedList = Collections.emptyList();
		}

		return multivaluedList;
	}

	/**
	 * Get the list of referenced registries from the given multivalued list
	 * 
	 * @param multivaluedList The list of multivalued registries
	 * @return The list of referenced registries
	 */
	private List<RTD> getReferencedListFromMultivaluedList(List<MTD> multivaluedList) {
		FilterBuilder filterBuilder = FilterBuilder.newOrFilter();

		for (MTD multivalued : multivaluedList) {
			FilterBuilder filterBuilder2 = FilterBuilder.newAndFilter();
			for (Pair<String, String> refencedField : referencedFields) {
				Object value = getFieldValue(multivalued, refencedField.getRight());
				filterBuilder2.addEquals(refencedField.getLeft(), value);
			}
			filterBuilder.addGroup(filterBuilder2);
		}

		List<RTD> referencedList = Collections.emptyList();
		if (filterBuilder.isEmpty()) {
			return referencedList;
		}

		try {
			referencedList = getReferencedDao().findWhere(filterBuilder);
		} catch (PuiDaoFindException e) {
			// do nothing
		}

		return referencedList;
	}

	/**
	 * Check if the two given referenced objects are the same or not (based on the
	 * PK)
	 * 
	 * @param refOne One referenced object
	 * @param refTwo Other referenced object
	 * @return true if are equals; false if not
	 */
	private boolean areEqualsReferenced(ITableDto refOne, ITableDto refTwo) {
		boolean equals = true;
		for (Pair<String, String> referencedField : referencedFields) {
			Object valOne = getFieldValue(refOne, referencedField.getLeft());
			Object valTwo = getFieldValue(refTwo, referencedField.getLeft());
			equals &= Objects.equals(valOne, valTwo);
		}
		return equals;
	}

	/**
	 * Check if the given DTO has valid field values to participate in the
	 * multivalued table
	 * 
	 * @param local The DTO
	 * @return true if all the attributes of the DTO that participates in the
	 *         multivalued table have any value; false if not
	 */
	private boolean hasValidFieldValues(ITableDto local) {
		boolean valid = true;

		for (Pair<String, String> localField : localFields) {
			Object value = getFieldValue(local, localField.getLeft());
			valid &= value != null;
		}

		return valid;
	}

	/**
	 * Create a Multivalued registry from the local and referenced registry
	 * 
	 * @param local      The local registry
	 * @param referenced The referenced registry
	 * @return The multivalued registry
	 */
	private MTD createMultivalued(LTD local, RTD referenced) {
		try {
			MTD multivalued = getMultivaluedDao().getDtoClass().newInstance();
			for (Pair<String, String> localField : localFields) {
				Object value = getFieldValue(local, localField.getLeft());
				setFieldValue(multivalued, localField.getRight(), value);
			}
			for (Pair<String, String> referencedField : referencedFields) {
				Object value = getFieldValue(referenced, referencedField.getLeft());
				setFieldValue(multivalued, referencedField.getRight(), value);
			}

			afterCreateMultivalued(local, referenced, multivalued);
			return multivalued;
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
	}

	/**
	 * Get the value of the given field for the given registry
	 * 
	 * @param dto       The registry to retrieve a value
	 * @param fieldName The field to retrieve its value
	 * @return The value of the field
	 */
	private Object getFieldValue(ITableDto dto, String fieldName) {
		try {
			Field field = getField(dto.getClass(), fieldName);
			return FieldUtils.readField(field, dto);
		} catch (Exception e) {
			return null;
		}
	}

	private Field getField(Class<? extends ITableDto> dtoClass, String fieldName) {
		try {
			Field field = DtoRegistry.getJavaFieldFromFieldName(dtoClass, fieldName);
			if (field == null) {
				field = FieldUtils.getDeclaredField(dtoClass, fieldName, true);
			}
			return field;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Set a value to a field
	 * 
	 * @param dto       The registry to be set a value
	 * @param fieldName The field to set a new value
	 * @param value     The value to be set
	 */
	private void setFieldValue(ITableDto dto, String fieldName, Object value) {
		try {
			FieldUtils.writeField(dto, fieldName, value, true);
		} catch (Exception e) {
			// do nothing
		}
	}

	/**
	 * Obtain the bean of the referenced table
	 * 
	 * @return The referenced table bean
	 */
	private RD getReferencedDao() {
		if (referencedDao == null) {
			referencedDao = PuiApplicationContext.getInstance().getBean(referencedDaoClass);
		}
		return referencedDao;
	}

	/**
	 * Obtain the bean of the multivalued table
	 * 
	 * @return The multivalued table bean
	 */
	private MD getMultivaluedDao() {
		if (multivaluedDao == null) {
			multivaluedDao = PuiApplicationContext.getInstance().getBean(multivaluedDaoClass);
		}
		return multivaluedDao;
	}

	@Override
	public String toString() {
		return "Multivalued field: " + fieldName;
	}

}