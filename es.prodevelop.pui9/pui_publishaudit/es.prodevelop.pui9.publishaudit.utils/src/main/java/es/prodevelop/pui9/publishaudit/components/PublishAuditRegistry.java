package es.prodevelop.pui9.publishaudit.components;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.publishaudit.dto.PublishAuditData;
import es.prodevelop.pui9.publishaudit.dto.PublishAuditOperationTypeEnum;
import es.prodevelop.pui9.publishaudit.dto.PublishAuditData.AttributeData;
import es.prodevelop.pui9.publishaudit.dto.PublishEntity;
import es.prodevelop.pui9.publishaudit.dto.PublishField;
import es.prodevelop.pui9.publishaudit.dto.PublishFieldValue;
import es.prodevelop.pui9.publishaudit.dto.PublishTopic;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

@Component
public class PublishAuditRegistry {

	private static final String ANY_VALUE = "ANY_VALUE";
	private static final String NO_VALUE = "NO_VALUE";

	@Autowired
	private RefreshPublishAuditCacheUtil refreshCacheUtil;

	/**
	 * List of entities that will be audit when insert
	 */
	private List<String> auditInsert;

	/**
	 * List of entities that will be audit when update
	 */
	private List<String> auditUpdate;

	/**
	 * List of entities that will be audit when delete
	 */
	private List<String> auditDelete;

	/**
	 * List of entities that will be published
	 */
	private Map<String, PublishEntity> mapPublish;

	/**
	 * Start the Timer that cache all the involved tables
	 */
	@PostConstruct
	private void postConstruct() {
		mapPublish = new HashMap<>();
		auditInsert = new ArrayList<>();
		auditUpdate = new ArrayList<>();
		auditDelete = new ArrayList<>();

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadPublishAudit", true, 0, 1, TimeUnit.HOURS,
				() -> refreshCacheUtil.reloadPublishAudit(PublishAuditRegistry.this));
	}

	public List<PublishAuditData> getPublishDataForInsert(String tableName, IDto dto) {
		tableName = toLower(tableName);
		PublishEntity pe = mapPublish.get(tableName);
		if (pe == null) {
			return Collections.emptyList();
		}

		List<PublishAuditData> listData = new ArrayList<>();
		if (pe.getMapOperationTopics().containsKey(PublishAuditOperationTypeEnum.insert)) {
			List<PublishTopic> listPt = pe.getMapOperationTopics().get(PublishAuditOperationTypeEnum.insert);
			for (PublishTopic pt : listPt) {
				PublishAuditData data = new PublishAuditData(tableName, PublishAuditOperationTypeEnum.insert,
						pt.getTopiccode(), pt.getEventtype(), null, dto);

				// there are modified columns
				if (pt.getMapFieldnameField().isEmpty()) {
					// if no fields were specified, simply include the topic and
					// process the next one
					listData.add(data);
					continue;
				}

				// fields are specified
				boolean allFields = true;
				for (Entry<String, List<PublishField>> entry : pt.getMapFieldnameField().entrySet()) {
					if (!allFields) {
						break;
					}

					String field = entry.getKey();
					List<PublishField> fields = entry.getValue();

					// check if field has specified values
					boolean allValues = true;
					String newValue = getFieldValue(field, dto);
					for (PublishField pf : fields) {
						if (!allValues) {
							break;
						}

						for (PublishFieldValue pfv : pf.getValues()) {
							if (isSameValue(pfv.getNewvalue(), ANY_VALUE) && newValue != null
									|| isSameValue(pfv.getNewvalue(), NO_VALUE) && newValue == null
									|| isSameValue(pfv.getNewvalue(), newValue)) {
								AttributeData ad = new AttributeData(toLower(pf.getFieldname()), null, newValue);
								data.addAttribute(ad);
							}
						}
					}

					if (!allValues || data.getAttributes().isEmpty()) {
						allFields = false;
					}
				}

				if (allFields) {
					listData.add(data);
				}
			}

			return listData;
		} else {
			return Collections.emptyList();
		}
	}

	private boolean isSameValue(String val1, String val2) {
		return StringUtils.equals(toLower(val1), toLower(val2));
	}

	public PublishAuditData getAuditDataForInsert(String tableName, IDto dto) {
		tableName = toLower(tableName);
		PublishAuditData data = null;

		if (auditInsert.contains(tableName)) {
			data = new PublishAuditData(tableName, PublishAuditOperationTypeEnum.insert, null, null, dto);
		}

		return data;
	}

	public List<PublishAuditData> getPublishDataForUpdate(String tableName, IDto dto, IDto oldDto) {
		tableName = toLower(tableName);
		PublishEntity pe = mapPublish.get(tableName);
		if (pe == null) {
			return Collections.emptyList();
		}

		List<PublishAuditData> listData = new ArrayList<>();

		if (pe.getMapOperationTopics().containsKey(PublishAuditOperationTypeEnum.update)) {
			List<PublishTopic> listPt = pe.getMapOperationTopics().get(PublishAuditOperationTypeEnum.update);
			for (PublishTopic pt : listPt) {
				PublishAuditData data = new PublishAuditData(tableName, pt.getOperationtype(), pt.getTopiccode(),
						pt.getEventtype(), oldDto, dto);

				List<String> modifiedColumns = getModifiedColumnNames(dto, oldDto);
				if (modifiedColumns.isEmpty()) {
					// if no columns was modified, don't do nothing
					continue;
				}

				// there are modified columns
				if (pt.getMapFieldnameField().isEmpty()) {
					// if no fields were specified, simply include the topic and
					// process the next one
					listData.add(data);
					continue;
				}

				// fields are specified
				boolean allFields = true;
				for (Entry<String, List<PublishField>> entry : pt.getMapFieldnameField().entrySet()) {
					if (!allFields) {
						break;
					}

					String field = entry.getKey();
					List<PublishField> fields = entry.getValue();
					if (!modifiedColumns.contains(field)) {
						// if a defined field is not included in the modified
						// columns, don't do nothing
						allFields = false;
						continue;
					}

					// check if field has specified values
					boolean allValues = true;
					String oldValue = getFieldValue(field, oldDto);
					String newValue = getFieldValue(field, dto);
					for (PublishField pf : fields) {
						if (!allValues) {
							break;
						}

						for (PublishFieldValue pfv : pf.getValues()) {
							if (isSameValue(pfv.getOldvalue(), ANY_VALUE) && oldValue != null
									|| isSameValue(pfv.getOldvalue(), NO_VALUE) && oldValue == null
									|| isSameValue(pfv.getOldvalue(), oldValue)) {
								if (isSameValue(pfv.getNewvalue(), ANY_VALUE) && newValue != null
										|| isSameValue(pfv.getNewvalue(), NO_VALUE) && newValue == null
										|| isSameValue(pfv.getNewvalue(), newValue)) {
									AttributeData ad = new AttributeData(toLower(pf.getFieldname()), oldValue,
											newValue);
									data.addAttribute(ad);
								}
							}
						}
					}

					if (!allValues || data.getAttributes().isEmpty()) {
						allFields = false;
					}
				}

				if (allFields) {
					listData.add(data);
				}
			}
		} else {
			listData = Collections.emptyList();
		}

		return listData;
	}

	public PublishAuditData getAuditDataForUpdate(String tableName, IDto dto, IDto oldDto) {
		tableName = toLower(tableName);
		PublishAuditData data = null;

		if (auditInsert.contains(tableName)) {
			data = new PublishAuditData(tableName, PublishAuditOperationTypeEnum.update, null, oldDto, dto);
		}

		return data;
	}

	public List<PublishAuditData> getPublishDataForDelete(String tableName, IDto dto) {
		tableName = toLower(tableName);
		PublishEntity pe = mapPublish.get(tableName);
		if (pe == null) {
			return Collections.emptyList();
		}

		List<PublishAuditData> listData = new ArrayList<>();
		if (pe.getMapOperationTopics().containsKey(PublishAuditOperationTypeEnum.delete)) {
			List<PublishTopic> listPt = pe.getMapOperationTopics().get(PublishAuditOperationTypeEnum.delete);
			for (PublishTopic pt : listPt) {
				PublishAuditData data = new PublishAuditData(tableName, PublishAuditOperationTypeEnum.delete,
						pt.getTopiccode(), pt.getEventtype(), dto, null);
				listData.add(data);
			}

			return listData;
		} else {
			return Collections.emptyList();
		}
	}

	public PublishAuditData getAuditDataForDelete(String tableName, IDto dto) {
		tableName = toLower(tableName);
		PublishAuditData data = null;

		if (auditInsert.contains(tableName)) {
			data = new PublishAuditData(tableName, PublishAuditOperationTypeEnum.delete, null, dto, null);
		}

		return data;
	}

	/**
	 * Get the list of modified columns. The names are in lower case
	 */
	private List<String> getModifiedColumnNames(IDto dto, IDto oldDto) {
		if (dto == null || oldDto == null) {
			return Collections.emptyList();
		}

		List<Field> fields = DtoRegistry.getAllJavaFields(dto.getClass());
		List<String> columnNames = new ArrayList<>();
		for (Field field : fields) {
			Object newValue = getFieldValue(field, dto);
			Object oldValue = getFieldValue(field, oldDto);
			if (!Objects.equals(newValue, oldValue)) {
				String columnName = DtoRegistry.getColumnNameFromFieldName(dto.getClass(), field.getName());
				columnNames.add(toLower(columnName));
			}
		}

		return columnNames;
	}

	/**
	 * Get the field value for the given DTO
	 */
	private Object getFieldValue(Field field, IDto dto) {
		try {
			return FieldUtils.readField(field, dto, true);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the value of the given column for the given DTO
	 */
	private String getFieldValue(String columnName, IDto dto) {
		columnName = toLower(columnName);
		Field field = DtoRegistry.getJavaFieldFromColumnName(dto.getClass(), columnName);
		if (field == null) {
			field = DtoRegistry.getJavaFieldFromLangColumnName(dto.getClass(), columnName);
		}
		if (field == null) {
			return null;
		}

		Object val = getFieldValue(field, dto);
		return val != null ? toLower(val.toString()) : null;
	}

	private String toLower(String value) {
		if (value != null) {
			value = value.toLowerCase();
		}

		return value;
	}

	public Map<String, PublishEntity> getMapPublish() {
		return mapPublish;
	}

	public List<String> getAuditInsert() {
		return auditInsert;
	}

	public List<String> getAuditUpdate() {
		return auditUpdate;
	}

	public List<String> getAuditDelete() {
		return auditDelete;
	}

}
