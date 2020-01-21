package es.prodevelop.pui9.model.dto;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.reflections.Reflections;

import es.prodevelop.pui9.annotations.PuiField;
import es.prodevelop.pui9.annotations.PuiViewColumn;
import es.prodevelop.pui9.classpath.PuiDynamicClassLoader;
import es.prodevelop.pui9.enums.ColumnVisibility;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * This class is a Registry of DTOs, where all the classes are mapped to get
 * useful attributes in a lazy way. This registry is filled once when the
 * application starts
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class DtoRegistry {

	private static List<Class<? extends IDto>> registeredDtos;
	private static Map<Class<? extends IDto>, List<Field>> mapAllFields;
	private static Map<Class<? extends IDto>, List<String>> mapAllFieldNames;
	private static Map<Class<? extends IDto>, List<String>> mapAllColumnNames;
	private static Map<Class<? extends IDto>, List<String>> mapColumnNames;
	private static Map<Class<? extends IDto>, List<String>> mapLangColumnNames;
	private static Map<Class<? extends IDto>, List<String>> mapLangFieldNames;
	private static Map<Class<? extends IDto>, List<String>> mapAutoincrementable;
	private static Map<Class<? extends IDto>, List<String>> mapNotNull;
	private static Map<Class<? extends IDto>, List<String>> mapGeometry;
	private static Map<Class<? extends IDto>, List<String>> mapSequenceFields;
	private static Map<Class<? extends IDto>, List<String>> mapSequenceColumns;
	private static Map<Class<? extends IDto>, List<String>> mapPk;
	private static Map<Class<? extends IDto>, List<String>> mapNumericFieldsFromDto;
	private static Map<Class<? extends IDto>, List<String>> mapFloatingFieldsFromDto;
	private static Map<Class<? extends IDto>, List<String>> mapDateTimeFieldsFromDto;
	private static Map<Class<? extends IDto>, List<String>> mapStringFieldsFromDto;
	private static Map<Class<? extends IDto>, List<String>> mapBooleanFieldsFromDto;
	private static Map<Class<? extends IDto>, Map<String, Integer>> mapFieldNameMaxLength;
	private static Map<Class<? extends IDto>, Map<String, String>> mapFieldColumn;
	private static Map<Class<? extends IDto>, Map<String, String>> mapColumnField;
	private static Map<Class<? extends IDto>, Map<String, Field>> mapJavaFieldFromAllFieldName;
	private static Map<Class<? extends IDto>, Map<String, Field>> mapJavaFieldNameField;
	private static Map<Class<? extends IDto>, Map<String, Field>> mapLangJavaFieldName;
	private static Map<Class<? extends IDto>, Map<String, Field>> mapJavaFieldColumnName;
	private static Map<Class<? extends IDto>, Map<String, Field>> mapLangJavaFieldColumnName;
	private static Map<Class<? extends IDto>, Map<String, Integer>> mapColumnSqlDataType;
	private static Map<Class<? extends IDto>, Map<String, ColumnVisibility>> mapColumnVisibility;
	private static Map<String, Map<String, Integer>> mapColumnSqlDataTypeByTableName;

	static {
		registeredDtos = new ArrayList<>();
		mapAllFields = new HashMap<>();
		mapAllFieldNames = new HashMap<>();
		mapAllColumnNames = new HashMap<>();
		mapColumnNames = new HashMap<>();
		mapLangColumnNames = new HashMap<>();
		mapLangFieldNames = new HashMap<>();
		mapAutoincrementable = new HashMap<>();
		mapNotNull = new HashMap<>();
		mapGeometry = new HashMap<>();
		mapSequenceFields = new HashMap<>();
		mapSequenceColumns = new HashMap<>();
		mapPk = new HashMap<>();
		mapNumericFieldsFromDto = new HashMap<>();
		mapFloatingFieldsFromDto = new HashMap<>();
		mapDateTimeFieldsFromDto = new HashMap<>();
		mapStringFieldsFromDto = new HashMap<>();
		mapBooleanFieldsFromDto = new HashMap<>();
		mapFieldNameMaxLength = new HashMap<>();
		mapFieldColumn = new HashMap<>();
		mapColumnField = new HashMap<>();
		mapJavaFieldFromAllFieldName = new HashMap<>();
		mapJavaFieldNameField = new HashMap<>();
		mapLangJavaFieldName = new HashMap<>();
		mapJavaFieldColumnName = new HashMap<>();
		mapLangJavaFieldColumnName = new HashMap<>();
		mapColumnSqlDataType = new HashMap<>();
		mapColumnVisibility = new HashMap<>();
		mapColumnSqlDataTypeByTableName = new HashMap<>();
	}

	/**
	 * Registers a DTO in the System
	 * 
	 * @param dtoClass The DTO class to be registered
	 */
	@SuppressWarnings("unchecked")
	public static void registerDto(Class<? extends IDto> dtoClass) {
		if (registeredDtos.contains(dtoClass)) {
			return;
		}
		registeredDtos.add(dtoClass);
		mapAllFields.put(dtoClass, new ArrayList<>());
		mapAllFieldNames.put(dtoClass, new ArrayList<>());
		mapAllColumnNames.put(dtoClass, new ArrayList<>());
		mapColumnNames.put(dtoClass, new ArrayList<>());
		mapLangColumnNames.put(dtoClass, new ArrayList<>());
		mapLangFieldNames.put(dtoClass, new ArrayList<>());
		mapAutoincrementable.put(dtoClass, new ArrayList<>());
		mapNotNull.put(dtoClass, new ArrayList<>());
		mapGeometry.put(dtoClass, new ArrayList<>());
		mapSequenceFields.put(dtoClass, new ArrayList<>());
		mapSequenceColumns.put(dtoClass, new ArrayList<>());
		mapPk.put(dtoClass, new ArrayList<>());
		mapNumericFieldsFromDto.put(dtoClass, new ArrayList<>());
		mapFloatingFieldsFromDto.put(dtoClass, new ArrayList<>());
		mapDateTimeFieldsFromDto.put(dtoClass, new ArrayList<>());
		mapStringFieldsFromDto.put(dtoClass, new ArrayList<>());
		mapBooleanFieldsFromDto.put(dtoClass, new ArrayList<>());
		mapFieldNameMaxLength.put(dtoClass, new HashMap<>());
		mapFieldColumn.put(dtoClass, new HashMap<>());
		mapColumnField.put(dtoClass, new HashMap<>());
		mapJavaFieldFromAllFieldName.put(dtoClass, new HashMap<>());
		mapJavaFieldNameField.put(dtoClass, new HashMap<>());
		mapLangJavaFieldName.put(dtoClass, new HashMap<>());
		mapJavaFieldColumnName.put(dtoClass, new HashMap<>());
		mapLangJavaFieldColumnName.put(dtoClass, new HashMap<>());
		mapColumnVisibility.put(dtoClass, new HashMap<>());

		registerFields(dtoClass, dtoClass);
		registerSuperclass(dtoClass, dtoClass.getSuperclass());
		for (Class<?> iface : dtoClass.getInterfaces()) {
			if (iface.getSimpleName().contains(dtoClass.getSimpleName())) {
				DtoFactory.registerInterface((Class<? extends IDto>) iface, (Class<? extends AbstractDto>) dtoClass);
			}
		}
	}

	/**
	 * Register the superclass of the given DTO
	 * 
	 * @param mainDtoClass  The main DTO class
	 * @param superDtoClass The DTO superclass
	 */
	private static void registerSuperclass(Class<? extends IDto> mainDtoClass, Class<?> superDtoClass) {
		if (superDtoClass != null) {
			registerFields(mainDtoClass, superDtoClass);
			registerSuperclass(mainDtoClass, superDtoClass.getSuperclass());
		}
	}

	/**
	 * Register the fields of the DTO, always keeping the reference to the main DTO
	 * class
	 * 
	 * @param mainDtoClass  The main DTO class
	 * @param superDtoClass A possible superclass of the DTO
	 */
	private static void registerFields(Class<? extends IDto> mainDtoClass, Class<?> superDtoClass) {
		List<Field> fields = Arrays.asList(superDtoClass.getDeclaredFields());
		Collections.sort(fields, new PuiFieldsComparator());

		for (Field field : fields) {
			field.setAccessible(true);
			PuiField puiField = field.getAnnotation(PuiField.class);
			if (puiField != null) {
				String fieldName = field.getName();
				String columnName = puiField.columnname();
				boolean autoincrementable = puiField.autoincrementable();
				boolean nullable = puiField.nullable();
				boolean ispk = puiField.ispk();
				int maxlength = puiField.maxlength();
				boolean islang = puiField.islang();
				boolean isGeom = puiField.isgeometry();
				boolean isSequence = puiField.issequence();

				mapAllFields.get(mainDtoClass).add(field);
				mapAllFieldNames.get(mainDtoClass).add(fieldName);
				mapAllColumnNames.get(mainDtoClass).add(columnName);
				mapFieldColumn.get(mainDtoClass).put(fieldName, columnName);
				mapColumnField.get(mainDtoClass).put(columnName, fieldName);
				mapJavaFieldFromAllFieldName.get(mainDtoClass).put(fieldName, field);
				mapJavaFieldFromAllFieldName.get(mainDtoClass).put(columnName, field);

				if (autoincrementable) {
					mapAutoincrementable.get(mainDtoClass).add(fieldName);
				}
				if (!nullable) {
					mapNotNull.get(mainDtoClass).add(fieldName);
				}
				if (isGeom) {
					mapGeometry.get(mainDtoClass).add(fieldName);
				}
				if (isSequence) {
					mapSequenceFields.get(mainDtoClass).add(fieldName);
					mapSequenceColumns.get(mainDtoClass).add(columnName);
				}
				if (ispk) {
					mapPk.get(mainDtoClass).add(fieldName);
				}
				if (islang) {
					mapLangColumnNames.get(mainDtoClass).add(columnName);
					mapLangFieldNames.get(mainDtoClass).add(fieldName);
					mapLangJavaFieldName.get(mainDtoClass).put(fieldName, field);
					mapLangJavaFieldColumnName.get(mainDtoClass).put(columnName, field);
				}
				if ((ispk && islang) || !islang) {
					mapColumnNames.get(mainDtoClass).add(columnName);
					mapJavaFieldNameField.get(mainDtoClass).put(fieldName, field);
					mapJavaFieldColumnName.get(mainDtoClass).put(columnName, field);
				}
				mapFieldNameMaxLength.get(mainDtoClass).put(fieldName, maxlength);
				mapFieldNameMaxLength.get(mainDtoClass).put(columnName, maxlength);

				switch (puiField.type()) {
				case text:
					// text or object
					mapStringFieldsFromDto.get(mainDtoClass).add(fieldName);
					mapStringFieldsFromDto.get(mainDtoClass).add(columnName);
					break;
				case numeric:
					// integer or long
					mapNumericFieldsFromDto.get(mainDtoClass).add(fieldName);
					mapNumericFieldsFromDto.get(mainDtoClass).add(columnName);
					break;
				case decimal:
					// double or big decimal
					mapFloatingFieldsFromDto.get(mainDtoClass).add(fieldName);
					mapFloatingFieldsFromDto.get(mainDtoClass).add(columnName);
					break;
				case datetime:
					// datetime
					mapDateTimeFieldsFromDto.get(mainDtoClass).add(fieldName);
					mapDateTimeFieldsFromDto.get(mainDtoClass).add(columnName);
					break;
				case logic:
					// boolean
					mapBooleanFieldsFromDto.get(mainDtoClass).add(fieldName);
					mapBooleanFieldsFromDto.get(mainDtoClass).add(columnName);
					break;
				}

				PuiViewColumn puiViewColumn = field.getAnnotation(PuiViewColumn.class);
				if (puiViewColumn != null) {
					mapColumnVisibility.get(mainDtoClass).put(columnName, puiViewColumn.visibility());
					mapColumnVisibility.get(mainDtoClass).put(fieldName, puiViewColumn.visibility());
				}
			}
		}
	}

	/**
	 * Registers a SQL column data type
	 * 
	 * @param dtoClass    The main DTO class
	 * @param columnName  The column to be registered
	 * @param sqlDataType The SQL datatype of the column
	 */
	public static void registerSqlColumnDataType(Class<? extends IDto> dtoClass, String columnName, int sqlDataType) {
		if (!mapColumnSqlDataType.containsKey(dtoClass)) {
			mapColumnSqlDataType.put(dtoClass, new HashMap<String, Integer>());
		}

		mapColumnSqlDataType.get(dtoClass).put(columnName, sqlDataType);
	}

	/**
	 * Registers a SQL column data type
	 * 
	 * @param tableName   The table of the column
	 * @param columnName  The column to be registered
	 * @param sqlDataType The SQL datatype of the column
	 */
	public static void registerSqlColumnDataTypeByTableName(String tableName, String columnName, int sqlDataType) {
		if (mapColumnSqlDataTypeByTableName.containsKey(tableName)) {
			mapColumnSqlDataTypeByTableName.get(tableName).put(columnName, sqlDataType);
		} else {
			Map<String, Integer> map = new HashMap<>();
			map.put(columnName, sqlDataType);
			mapColumnSqlDataTypeByTableName.put(tableName, map);
		}
	}

	/**
	 * Check if the given Dto class was registered or not. This is because sometimes
	 * one can define a class as Dto without really belonging it to a table or view
	 * 
	 * @param dtoClass The DTO class
	 * @return true if the DTO class is registered; false if not
	 */
	public static boolean isRegistered(Class<? extends IDto> dtoClass) {
		return registeredDtos.contains(dtoClass);
	}

	/**
	 * Get all the Field names of the given DTO class (same order as the entity in
	 * the Database) (including lang table columns)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names as String
	 */
	public static List<String> getAllFields(Class<? extends IDto> dtoClass) {
		return mapAllFieldNames.get(dtoClass);
	}

	/**
	 * Get all the Lang Field names of the DTO
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names of the related lang table
	 */
	public static List<String> getLangFieldNames(Class<? extends IDto> dtoClass) {
		return mapLangFieldNames.get(dtoClass);
	}

	/**
	 * Get all Fields from the given class in the order indicated in the
	 * {@link PuiViewColumn} annotation for Views
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of Java Fields
	 */
	public static List<Field> getAllJavaFields(Class<? extends IDto> dtoClass) {
		return mapAllFields.get(dtoClass);
	}

	/**
	 * Get all the Column names of the DTO (same order as the Table in the Database)
	 * (not including lang table columns)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of column names (not including lang table columns)
	 */
	public static List<String> getColumnNames(Class<? extends IDto> dtoClass) {
		return mapColumnNames.get(dtoClass);
	}

	/**
	 * Get all the Column names of the DTO (same order as the Table in the Database)
	 * (including lang table columns)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of column names (including lang table columns)
	 */
	public static List<String> getAllColumnNames(Class<? extends IDto> dtoClass) {
		return mapAllColumnNames.get(dtoClass);
	}

	/**
	 * Get all the Lang Column names of the DTO
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of column names of the related lang table
	 */
	public static List<String> getLangColumnNames(Class<? extends IDto> dtoClass) {
		return mapLangColumnNames.get(dtoClass);
	}

	/**
	 * Get all field names of the DTO marked as autoincrementable in the
	 * {@link PuiField} annotation (same order as the Table in the Database)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names marked as autoincrementable in the
	 *         {@link PuiField} annotation
	 */
	public static List<String> getAutoincrementableFieldNames(Class<? extends IDto> dtoClass) {
		return mapAutoincrementable.get(dtoClass);
	}

	/**
	 * Get all the fields names of the DTO marked as not null in the
	 * {@link PuiField} annotation (same order as the Table in the Database)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names marked as not null in the {@link PuiField}
	 *         annotation
	 */
	public static List<String> getNotNullFields(Class<? extends IDto> dtoClass) {
		return mapNotNull.get(dtoClass);
	}

	/**
	 * Get all field names of the DTO marked as geometry in the {@link PuiField}
	 * annotation (same order as the Table in the Database)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names marked as geometry in the {@link PuiField}
	 *         annotation
	 */
	public static List<String> getGeomFields(Class<? extends IDto> dtoClass) {
		return mapGeometry.get(dtoClass);
	}

	/**
	 * Get all the field names of the DTO marked as sequence in the {@link PuiField}
	 * annotation (same order as the Table in the Database)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names marked as sequence in the {@link PuiField}
	 *         annotation
	 */
	public static List<String> getSequenceFields(Class<? extends IDto> dtoClass) {
		return mapSequenceFields.get(dtoClass);
	}

	/**
	 * Get all the column names of the DTO marked as sequence in the
	 * {@link PuiField} annotation (same order as the Table in the Database)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of column names marked as sequence in the {@link PuiField}
	 *         annotation
	 */
	public static List<String> getSequenceColumns(Class<? extends IDto> dtoClass) {
		return mapSequenceColumns.get(dtoClass);
	}

	/**
	 * Get all the field names of the DTO marked as pk in the {@link PuiField}
	 * annotation (same order as the Table in the Database)
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names marked as pk in the {@link PuiField}
	 *         annotation
	 */
	public static List<String> getPkFields(Class<? extends IDto> dtoClass) {
		return mapPk.get(dtoClass);
	}

	/**
	 * Get the list of Field names that are of Numeric type (Integer, Long)
	 * according the {@link PuiField#type()} field
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names of type Numeric
	 */
	public static List<String> getNumericFields(Class<? extends IDto> dtoClass) {
		return mapNumericFieldsFromDto.get(dtoClass);
	}

	/**
	 * Get the list of Field names that are of Floating type (Double, BigDecimal)
	 * according the {@link PuiField#type()} field
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names of type Floating
	 */
	public static List<String> getFloatingFields(Class<? extends IDto> dtoClass) {
		return mapFloatingFieldsFromDto.get(dtoClass);
	}

	/**
	 * Get the list of Field names that are of {@link Instant} type according the
	 * {@link Field#type()} field
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names of type Date
	 */
	public static List<String> getDateTimeFields(Class<? extends IDto> dtoClass) {
		return mapDateTimeFieldsFromDto.get(dtoClass);
	}

	/**
	 * Get the list of Field names that are of String type (String) according the
	 * {@link PuiField#type()} field
	 * 
	 * @param dtoClass The DTO class
	 * @return The list of field names of type String
	 */
	public static List<String> getStringFields(Class<? extends IDto> dtoClass) {
		return mapStringFieldsFromDto.get(dtoClass);
	}

	/**
	 * Get the list of Field names that are of Boolean type (Boolean) according the
	 * {@link PuiField#type()} field
	 * 
	 * @param dtoClass The DTO class
	 * 
	 * @return The list of field names of type Boolean
	 */
	public static List<String> getBooleanFields(Class<? extends IDto> dtoClass) {
		return mapBooleanFieldsFromDto.get(dtoClass);
	}

	/**
	 * Returns a map for all the length restrictions for each field name of the
	 * given DTO, according to the {@link PuiField#maxlength()} field
	 * 
	 * @param dtoClass The DTO class
	 * @return A map with all the field names and the corresponding max value length
	 */
	public static Map<String, Integer> getFieldNamesMaxLength(Class<? extends IDto> dtoClass) {
		return mapFieldNameMaxLength.get(dtoClass);
	}

	/**
	 * Return the max length of a field name, according to the
	 * {@link PuiField#maxlength()} field (only applicable for String type columns)
	 * 
	 * @param dtoClass  The DTO class
	 * @param fieldName The field name to get its max length
	 * @return The max length of the given field
	 */
	public static Integer getFieldMaxLength(Class<? extends IDto> dtoClass, String fieldName) {
		return mapFieldNameMaxLength.get(dtoClass).get(fieldName);
	}

	/**
	 * Get the column name from the given field name
	 * 
	 * @param dtoClass  The DTO class
	 * @param fieldName The field name to get its corresponding column name
	 * @return The corresponding column name of the field name
	 */
	public static String getColumnNameFromFieldName(Class<? extends IDto> dtoClass, String fieldName) {
		return mapFieldColumn.get(dtoClass).get(fieldName);
	}

	/**
	 * Get the field name from the given column name
	 * 
	 * @param dtoClass   The DTO class
	 * @param columnName The column name to get its corresponding field name
	 * @return The corresponding field name of the column name
	 */
	public static String getFieldNameFromColumnName(Class<? extends IDto> dtoClass, String columnName) {
		return mapColumnField.get(dtoClass).get(columnName);
	}

	/**
	 * Get a map with all the field names and the corresponding Java field of the
	 * given DTO class
	 * 
	 * @param dtoClass The DTO class
	 * @return The map with all the field names and the corresponding Java fields
	 */
	public static Map<String, Field> getMapFieldNameToJavaField(Class<? extends IDto> dtoClass) {
		return mapJavaFieldFromAllFieldName.get(dtoClass);
	}

	/**
	 * Get the Java field that represents the given field name (including lang
	 * fields)
	 * 
	 * @param dtoClass  The DTO class
	 * @param fieldName The field name
	 * @return The associated Java field
	 */
	public static Field getJavaFieldFromAllFields(Class<? extends IDto> dtoClass, String fieldName) {
		return mapJavaFieldFromAllFieldName.get(dtoClass).get(fieldName);
	}

	/**
	 * Get the Field of the given Field name (not including lang fields)
	 * 
	 * @param dtoClass  The DTO class
	 * @param fieldName The field name
	 * @return The associated Java field
	 */
	public static Field getJavaFieldFromFieldName(Class<? extends IDto> dtoClass, String fieldName) {
		return mapJavaFieldNameField.get(dtoClass).get(fieldName);
	}

	/**
	 * Get the Java field of the given field name (only lang fields)
	 * 
	 * @param dtoClass  The DTO class
	 * @param fieldName The field name
	 * @return The associated Java field
	 */
	public static Field getJavaFieldFromLangFieldName(Class<? extends IDto> dtoClass, String fieldName) {
		return mapLangJavaFieldName.get(dtoClass).get(fieldName);
	}

	/**
	 * Get the Java field of the given column name (not including lang fields)
	 * 
	 * @param dtoClass   The DTO class
	 * @param columnName The column name
	 * @return The associated Java Field
	 */
	public static Field getJavaFieldFromColumnName(Class<? extends IDto> dtoClass, String columnName) {
		return mapJavaFieldColumnName.get(dtoClass).get(columnName);
	}

	/**
	 * Get the Java field of the given column name (only lang fields)
	 * 
	 * @param dtoClass
	 * @param columnName The column name
	 * @return The associated Java field
	 */
	public static Field getJavaFieldFromLangColumnName(Class<? extends IDto> dtoClass, String columnName) {
		return mapLangJavaFieldColumnName.get(dtoClass).get(columnName);
	}

	/**
	 * Returns a map with all the field names and their corresponding Java Field
	 * (not including lang fields)
	 * 
	 * @param dtoClass The DTO class
	 * @return The map with the field names and the Java fields (not including lang
	 *         fields)
	 */
	public static Map<String, Field> getMapFieldsFromFieldName(Class<? extends IDto> dtoClass) {
		return mapJavaFieldNameField.get(dtoClass);
	}

	/**
	 * Returns a map with all the field names and their corresponding Java Field
	 * (only lang fields)
	 * 
	 * @param dtoClass The DTO class
	 * @return The map with the field names and the Java fields (only lang fields)
	 */
	public static Map<String, Field> getLangMapFieldsFromFieldName(Class<? extends IDto> dtoClass) {
		return mapLangJavaFieldName.get(dtoClass);
	}

	/**
	 * Returns a map with all the column names and their corresponding Java Field
	 * (not including lang fields)
	 * 
	 * @param dtoClass The DTO Class
	 * @return The map with the column names and the Java fields (not including lang
	 *         fields)
	 */
	public static Map<String, Field> getMapFieldsFromColumnName(Class<? extends IDto> dtoClass) {
		return mapJavaFieldColumnName.get(dtoClass);
	}

	/**
	 * Returns a map with all the column names and their corresponding Java Field
	 * (only lang fields)
	 * 
	 * @param dtoClass The DTO class
	 * @return The map with the column names and the Java fields (only lang fields)
	 */
	public static Map<String, Field> getLangMapFieldsFromColumnName(Class<? extends IDto> dtoClass) {
		return mapLangJavaFieldColumnName.get(dtoClass);
	}

	/**
	 * Get all the column visibility for all the columns/fields of the given dto
	 * class. Values are set in the {@link PuiViewColumn#visibility()} field
	 * 
	 * @param dtoClass The DTO class
	 * @return The map with all the columns/fields and its visibility
	 */
	public static Map<String, ColumnVisibility> getAllColumnVisibility(Class<? extends IDto> dtoClass) {
		return mapColumnVisibility.get(dtoClass);
	}

	/**
	 * Get the column visibility of the given column/field. Value is set in the
	 * {@link PuiViewColumn#visibility()} field
	 * 
	 * @param dtoClass   The DTO class
	 * @param columnName The column/field name
	 * @return The visibility for the given column/field
	 */
	public static ColumnVisibility getColumnVisibility(Class<? extends IDto> dtoClass, String columnName) {
		return mapColumnVisibility.get(dtoClass).get(columnName);
	}

	/**
	 * Returns the SQL data type for the column name
	 * 
	 * @param dtoClass   The DTO class
	 * @param columnName The column name
	 * @return The SQL data type
	 */
	public static int getColumnSqlDataType(Class<? extends IDto> dtoClass, String columnName) {
		return mapColumnSqlDataType.get(dtoClass).get(columnName);
	}

	/**
	 * Returns the SQL data type given the tableName (this is used when a single Dao
	 * is used for more than one table)
	 * 
	 * @param tableName  The table name
	 * @param columnName The column name
	 * @return The SQL data type
	 */
	public static int getColumnSqlDataType(String tableName, String columnName) {
		return mapColumnSqlDataTypeByTableName.get(tableName).get(columnName);
	}

	private static Map<Class<? extends IDto>, Class<? extends IDto>> mapInterfaceImplementation = new HashMap<>();
	private static Map<String, Reflections> mapPackageReflections = new HashMap<>();

	/**
	 * Get the DTO implementation class of a given DTO interface
	 * 
	 * @param dtoClass     The DTO interface or class
	 * @param refreshCache If set to true, the cache of DTO classes will be erased
	 *                     and created again
	 * @return The implementing class of the given interface. If the given class is
	 *         not an interface, it returns the same class
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IDto> Class<T> getDtoImplementation(Class<T> dtoClass, boolean refreshCache) {
		if (refreshCache) {
			mapPackageReflections.clear();
		}

		if (dtoClass == null) {
			return null;
		}

		if (!dtoClass.isInterface()) {
			return dtoClass;
		}

		if (!mapInterfaceImplementation.containsKey(dtoClass)) {
			String packageName = dtoClass.getPackage().getName();
			String[] splits = packageName.split("\\.", -1);

			if (splits.length >= 3) {
				packageName = splits[0] + "." + splits[1] + "." + splits[2];
			} else {
				packageName = packageName.substring(0, packageName.lastIndexOf("."));
			}

			if (!mapPackageReflections.containsKey(packageName)) {
				mapPackageReflections.put(packageName,
						new Reflections(packageName, PuiDynamicClassLoader.getInstance()));
			}
			Reflections reflections = mapPackageReflections.get(packageName);

			for (Class<? extends IDto> clazz : reflections.getSubTypesOf(dtoClass)) {
				if (clazz.isInterface()) {
					continue;
				}

				if (Arrays.asList(clazz.getInterfaces()).contains(dtoClass)) {
					mapInterfaceImplementation.put(dtoClass, clazz);
					break;
				}
			}
		}

		return (Class<T>) mapInterfaceImplementation.get(dtoClass);
	}

	/**
	 * A comparator class for the PUI DTO fields
	 * 
	 * @author Marc Gil - mgil@prodevelop.es
	 */
	private static class PuiFieldsComparator implements Comparator<Field> {
		@Override
		public int compare(Field o1, Field o2) {
			PuiViewColumn puiCol1 = o1.getAnnotation(PuiViewColumn.class);
			PuiViewColumn puiCol2 = o2.getAnnotation(PuiViewColumn.class);

			if (puiCol1 != null && puiCol2 != null) {
				return Integer.valueOf(puiCol1.order()).compareTo(Integer.valueOf(puiCol2.order()));
			} else if (puiCol1 == null && puiCol2 != null) {
				return -1;
			} else if (puiCol1 != null && puiCol2 == null) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	private DtoRegistry() {
	}
}
