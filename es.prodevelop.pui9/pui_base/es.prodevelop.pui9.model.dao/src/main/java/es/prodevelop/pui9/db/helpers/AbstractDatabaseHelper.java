package es.prodevelop.pui9.db.helpers;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.db.utils.SqlUtils;
import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.filter.FilterRule;
import es.prodevelop.pui9.filter.FilterRuleOperation;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * This is an abstract implementation shared for all the database types.
 * Nevertheless, there are some specific methods that are specific for each
 * database vendor
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractDatabaseHelper implements IDatabaseHelper {

	private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";

	protected static final String DATE_FORMAT = "{DATE_FORMAT}";
	protected static final String COLUMNNAME = "_columnname_";
	protected static final String OP = "_op_";
	protected static final String VALUE = "_value_";
	protected static final String BEGINNING = "_beginning_";
	protected static final String END = "_end_";
	protected static final String AND = " AND ";
	protected static final String OR = " OR ";

	@Autowired
	private DaoRegistry daoRegistry;

	public String processSearchText(Class<? extends IDto> dtoClass, List<String> fields, String text) {
		Map<String, String> fieldTextMap = new HashMap<>();
		fields.forEach(field -> fieldTextMap.put(field, text));
		return processSearchText(dtoClass, fieldTextMap);
	}

	public String processSearchText(Class<? extends IDto> dtoClass, Map<String, String> fieldTextMap) {
		StringBuilder sb = new StringBuilder();

		boolean hasLangSupport = daoRegistry
				.hasLanguageSupport(daoRegistry.getDaoFromModelId(daoRegistry.getModelIdFromDto(dtoClass)));

		for (Iterator<Entry<String, String>> it = fieldTextMap.entrySet().iterator(); it.hasNext();) {
			Entry<String, String> next = it.next();
			String field = next.getKey();
			String text = next.getValue();
			if (StringUtils.isEmpty(field) || StringUtils.isEmpty(text)) {
				continue;
			}

			String sqlColumn = String.valueOf(field);

			if (!DtoRegistry.getColumnNames(dtoClass).contains(sqlColumn)) {
				// if the columns list doesn't contain the given column, check if it was a field
				// and get the corresponding column name
				sqlColumn = DtoRegistry.getColumnNameFromFieldName(dtoClass, sqlColumn);
			}

			if (sqlColumn == null) {
				continue;
			}

			if (hasLangSupport && DtoRegistry.getLangColumnNames(dtoClass).contains(sqlColumn)) {
				sqlColumn = SqlUtils.TABLE_LANG_PREFIX + "." + sqlColumn;
			} else {
				sqlColumn = SqlUtils.TABLE_PREFIX + "." + sqlColumn;
			}

			text = text.toLowerCase();
			boolean isLargeStringField = isLargeStringField(dtoClass, field);

			String op = null;
			if (isString(dtoClass, field)) {
				op = getSqlTextOperation(sqlColumn, isLargeStringField, FilterRuleOperation.cn.op, text, true, true);
			} else if (isNumber(dtoClass, field)) {
				op = getSqlNumberOperationAsString(sqlColumn, FilterRuleOperation.cn.op, text, true, true);
			} else if (isDate(dtoClass, field)) {
				text = text.replace("/", "-");
				text = text.replace(".", "-");
				op = getSqlDateOperationAsString(sqlColumn, text);
			}

			if (op != null) {
				sb.append("(" + op + ")");
			}

			if (it.hasNext()) {
				sb.append(OR);
			}
		}

		return sb.toString();
	}

	@Override
	public String processFilters(Class<? extends IDto> dtoClass, FilterGroup filters, boolean addAlias) {
		if (filters == null) {
			return null;
		}

		List<String> ruleList = new ArrayList<>();
		List<String> filterList = new ArrayList<>();

		// rules
		for (FilterRule rule : filters.getRules()) {
			String r = processRule(dtoClass, rule, addAlias);
			if (r != null) {
				ruleList.add(r);
			}
		}

		// groups
		for (FilterGroup filter : filters.getGroups()) {
			String f = processFilters(dtoClass, filter, addAlias);
			if (f != null) {
				filterList.add(f);
			}
		}

		StringBuilder sb = new StringBuilder();

		// rules
		if (!ruleList.isEmpty()) {
			sb.append("(");
		}
		for (Iterator<String> it = ruleList.iterator(); it.hasNext();) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(" " + filters.getGroupOp() + " ");
			}
		}
		if (!ruleList.isEmpty()) {
			sb.append(")");
		}

		// groups
		if (!filterList.isEmpty()) {
			if (!ruleList.isEmpty()) {
				sb.append(" " + filters.getGroupOp());
			}
			sb.append(" (");
		}
		for (Iterator<String> it = filterList.iterator(); it.hasNext();) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(" " + filters.getGroupOp() + " ");
			}
		}
		if (!filterList.isEmpty()) {
			sb.append(")");
		}

		if (StringUtils.isEmpty(sb.toString())) {
			return null;
		} else {
			return sb.toString();
		}
	}

	@SuppressWarnings("unchecked")
	private String processRule(Class<? extends IDto> dtoClass, FilterRule rule, boolean addAlias) {
		if (rule == null) {
			return null;
		}

		Field field = DtoRegistry.getJavaFieldFromAllFields(dtoClass, rule.getField());
		if (field == null) {
			return null;
		}

		boolean hasLangSupport = daoRegistry
				.hasLanguageSupport(daoRegistry.getDaoFromModelId(daoRegistry.getModelIdFromDto(dtoClass)));
		String fieldName = rule.getField();
		String sqlColumn = rule.getField();

		if (!DtoRegistry.getColumnNames(dtoClass).contains(sqlColumn)) {
			// if the columns list doesn't contain the given column, check if it was a field
			// and get the corresponding column name
			sqlColumn = DtoRegistry.getColumnNameFromFieldName(dtoClass, sqlColumn);
		}

		if (hasLangSupport && rule.getField().equalsIgnoreCase(IDto.LANG_COLUMN_NAME)) {
			sqlColumn = (addAlias ? SqlUtils.TABLE_LANG_PREFIX + "." : "") + sqlColumn;
		} else {
			sqlColumn = (addAlias ? SqlUtils.TABLE_PREFIX + "." : "") + sqlColumn;
		}

		String textValue = parseValueToString(rule.getData());
		Instant instantValue = parseValueToInstant(rule.getData());
		ZoneId zoneId = ZoneId.systemDefault();
		if (PuiUserSession.getCurrentSession() != null) {
			zoneId = PuiUserSession.getCurrentSession().getTimezone();
		}
		ZonedDateTime zdt = instantValue != null ? instantValue.atZone(zoneId) : null;
		Number numberValue = parseValueToNumber(rule.getData());
		boolean isLargeStringField = isLargeStringField(dtoClass, rule.getField());
		boolean isNull = StringUtils.isEmpty(textValue) && zdt == null && numberValue == null;
		if (isNull) {
			numberValue = Integer.valueOf(0);
		}

		String sqlOp;
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> values;

		switch (rule.getOp()) {
		case eq:
			if (isNull) {
				sqlBuilder.append(sqlColumn + FilterRuleOperation.nu.op);
			} else {
				sqlOp = FilterRuleOperation.eq.op;
				if (isString(dtoClass, fieldName)) {
					sqlBuilder.append(sqlColumn + sqlOp + "'" + textValue + "'");
				} else if (isNumber(dtoClass, fieldName)) {
					sqlBuilder.append(sqlColumn + sqlOp + numberValue);
				} else if (isDate(dtoClass, fieldName)) {
					sqlBuilder.append(sqlColumn + sqlOp + getSqlDateOperation(zdt));
				}
			}
			break;
		case ne:
			if (isNull) {
				sqlBuilder.append(sqlColumn + FilterRuleOperation.nn.op);
			} else {
				sqlOp = FilterRuleOperation.ne.op;
				if (isString(dtoClass, fieldName)) {
					sqlBuilder.append(sqlColumn + sqlOp + "'" + textValue + "'");
				} else if (isNumber(dtoClass, fieldName)) {
					sqlBuilder.append(sqlColumn + sqlOp + numberValue);
				} else if (isDate(dtoClass, fieldName)) {
					sqlBuilder.append(sqlColumn + sqlOp + getSqlDateOperation(zdt));
				}
			}
			break;
		case bw:
			sqlOp = FilterRuleOperation.bw.op;
			if (isString(dtoClass, fieldName)) {
				sqlBuilder.append(getSqlTextOperation(sqlColumn, isLargeStringField, sqlOp, textValue.toLowerCase(),
						false, true));
			}
			break;
		case bn:
			sqlOp = FilterRuleOperation.bn.op;
			if (isString(dtoClass, fieldName)) {
				sqlBuilder.append(getSqlTextOperation(sqlColumn, isLargeStringField, sqlOp, textValue.toLowerCase(),
						false, true));
			}
			break;
		case ew:
			sqlOp = FilterRuleOperation.ew.op;
			if (isString(dtoClass, fieldName)) {
				sqlBuilder.append(getSqlTextOperation(sqlColumn, isLargeStringField, sqlOp, textValue.toLowerCase(),
						true, false));
			}
			break;
		case en:
			sqlOp = FilterRuleOperation.en.op;
			if (isString(dtoClass, fieldName)) {
				sqlBuilder.append(getSqlTextOperation(sqlColumn, isLargeStringField, sqlOp, textValue.toLowerCase(),
						true, false));
			}
			break;
		case cn:
			sqlOp = FilterRuleOperation.cn.op;
			if (isString(dtoClass, fieldName)) {
				sqlBuilder.append(
						getSqlTextOperation(sqlColumn, isLargeStringField, sqlOp, textValue.toLowerCase(), true, true));
			}
			break;
		case nc:
			sqlOp = FilterRuleOperation.nc.op;
			if (isString(dtoClass, fieldName)) {
				sqlBuilder.append(
						getSqlTextOperation(sqlColumn, isLargeStringField, sqlOp, textValue.toLowerCase(), true, true));
			}
			break;
		case lt:
			sqlOp = FilterRuleOperation.lt.op;
			if (isNumber(dtoClass, fieldName)) {
				sqlBuilder.append(sqlColumn + sqlOp + numberValue);
			} else if (isDate(dtoClass, fieldName)) {
				sqlBuilder.append(sqlColumn + sqlOp + getSqlDateOperation(zdt));
			}
			break;
		case le:
			sqlOp = FilterRuleOperation.le.op;
			if (isNumber(dtoClass, fieldName)) {
				sqlBuilder.append(sqlColumn + sqlOp + numberValue);
			} else if (isDate(dtoClass, fieldName)) {
				sqlBuilder.append(sqlColumn + sqlOp + getSqlDateOperation(zdt));
			}
			break;
		case gt:
			sqlOp = FilterRuleOperation.gt.op;
			if (isNumber(dtoClass, fieldName)) {
				sqlBuilder.append(sqlColumn + sqlOp + numberValue);
			} else if (isDate(dtoClass, fieldName)) {
				sqlBuilder.append(sqlColumn + sqlOp + getSqlDateOperation(zdt));
			}
			break;
		case ge:
			sqlOp = FilterRuleOperation.ge.op;
			if (isNumber(dtoClass, fieldName)) {
				sqlBuilder.append(sqlColumn + sqlOp + numberValue);
			} else if (isDate(dtoClass, fieldName)) {
				sqlBuilder.append(sqlColumn + sqlOp + getSqlDateOperation(zdt));
			}
			break;
		case bt:
			sqlOp = FilterRuleOperation.bt.op;
			if (rule.getData() instanceof List && ((List<Object>) rule.getData()).size() == 2) {
				values = (List<Object>) rule.getData();
				Object from = values.get(0);
				Object to = values.get(1);
				if (isNumber(dtoClass, fieldName)) {
					Number lower = parseValueToNumber(from);
					Number upper = parseValueToNumber(to);

					sqlBuilder.append(sqlColumn + sqlOp + lower + AND + upper);
				} else if (isDate(dtoClass, fieldName) && from instanceof Instant && to instanceof Instant) {
					String lower = getSqlDateOperation((Instant) from);
					String upper = getSqlDateOperation((Instant) to);

					sqlBuilder.append(sqlColumn + sqlOp + lower + AND + upper);
				}
			}
			break;
		case nbt:
			sqlOp = FilterRuleOperation.nbt.op;
			if (rule.getData() instanceof List && ((List<Object>) rule.getData()).size() == 2) {
				values = (List<Object>) rule.getData();
				Object from = values.get(0);
				Object to = values.get(1);
				if (isNumber(dtoClass, fieldName)) {
					Number lower = parseValueToNumber(from);
					Number upper = parseValueToNumber(to);

					sqlBuilder.append(sqlColumn + sqlOp + lower + AND + upper);
				} else if (isDate(dtoClass, fieldName) && from instanceof Instant && to instanceof Instant) {
					String lower = getSqlDateOperation((Instant) from);
					String upper = getSqlDateOperation((Instant) to);

					sqlBuilder.append(sqlColumn + sqlOp + lower + AND + upper);
				}
			}
			break;
		case in:
			if (rule.getData() instanceof List) {
				values = (List<Object>) rule.getData();
			} else if (rule.getData() instanceof String) {
				values = new ArrayList<>();
				for (String val : rule.getData().toString().split(",")) {
					values.add(val.trim());
				}
			} else {
				values = null;
			}
			if (!CollectionUtils.isEmpty(values)) {
				sqlOp = FilterRuleOperation.in.op;
				sqlBuilder.append(sqlColumn + sqlOp + " (");
				for (Iterator<Object> it = values.iterator(); it.hasNext();) {
					Object next = it.next();
					if (isString(dtoClass, fieldName)) {
						String strVal = (String) next;
						if (strVal.startsWith("'") && strVal.endsWith("'")) {
							sqlBuilder.append(strVal);
						} else {
							sqlBuilder.append("'" + strVal + "'");
						}
					} else if (isNumber(dtoClass, fieldName)) {
						sqlBuilder.append(parseValueToNumber(next));
					}

					if (it.hasNext()) {
						sqlBuilder.append(",");
					}
				}
				sqlBuilder.append(")");
			}
			break;
		case ni:
			if (rule.getData() instanceof List) {
				values = (List<Object>) rule.getData();
			} else if (rule.getData() instanceof String) {
				values = new ArrayList<>();
				for (String val : rule.getData().toString().split(",")) {
					values.add(val.trim());
				}
			} else {
				values = null;
			}
			if (!CollectionUtils.isEmpty(values)) {
				sqlOp = FilterRuleOperation.ni.op;
				sqlBuilder.append(sqlColumn + sqlOp + " (");
				for (Iterator<Object> it = values.iterator(); it.hasNext();) {
					Object next = it.next();
					if (isString(dtoClass, fieldName)) {
						String strVal = (String) next;
						if (strVal.startsWith("'") && strVal.endsWith("'")) {
							sqlBuilder.append(strVal);
						} else {
							sqlBuilder.append("'" + strVal + "'");
						}
					} else if (isNumber(dtoClass, fieldName)) {
						sqlBuilder.append(parseValueToNumber(next));
					}

					if (it.hasNext()) {
						sqlBuilder.append(",");
					}
				}
				sqlBuilder.append(")");
			}
			break;
		case nu:
			sqlBuilder.append(sqlColumn + FilterRuleOperation.nu.op);
			break;
		case nn:
			sqlBuilder.append(sqlColumn + FilterRuleOperation.nn.op);
			break;
		case geo_bounding_box:
			sqlBuilder.append("'" + textValue + "'");
			break;
		case geo_intersects:
			sqlBuilder.append("'" + textValue + "'");
			break;
		case eqt:
		case net:
		case ltt:
		case let:
		case gtt:
		case get:
			// these case is not supported here. These filters should only be sent from the
			// client, and they're treated previously in the SearchRequest class, calling
			// FilterGroup#modifyDateFilters method
			break;
		}

		String sql = sqlBuilder.toString();
		return StringUtils.isEmpty(sql) ? null : sql;
	}

	/**
	 * Returns the value as String to be used with text fields
	 * 
	 * @param value The value
	 * @return The value as String
	 */
	private String parseValueToString(Object value) {
		if (value == null) {
			return "";
		}

		String text;
		if (value instanceof String) {
			text = (String) value;
			if (StringUtils.isEmpty(text)) {
				return "";
			}

			if (text.contains("'")) {
				text = text.replace("'", "''");
			}
		} else {
			text = value.toString();
		}

		return text;
	}

	/**
	 * Returns the value as Number to be used with text fields
	 * 
	 * @param value The value
	 * @return The value as Number
	 */
	private Number parseValueToNumber(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof String) {
			String val = (String) value;
			val = val.replace(",", ".");

			try {
				// is Integer?
				return Integer.parseInt(val);
			} catch (Exception e1) {
				try {
					// is Long?
					return Long.parseLong(val);
				} catch (Exception e2) {
					try {
						// is Double?
						return Double.parseDouble(val);
					} catch (Exception e3) {
						try {
							// is BigDecimal?
							return new BigDecimal(val);
						} catch (Exception e4) {
							return null;
						}
					}
				}
			}
		} else if (value instanceof Number) {
			return (Number) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the value as Date
	 */
	private Instant parseValueToInstant(Object value) {
		if (value instanceof Instant) {
			return (Instant) value;
		} else if (value instanceof String) {
			String val = (String) value;
			if (val.startsWith("\"")) {
				val = val.substring(1);
			}
			if (val.endsWith("\"")) {
				val = val.substring(0, val.length() - 1);
			}

			return PuiDateUtil.stringToInstant(val);
		} else {
			return null;
		}
	}

	/**
	 * Get the Sql for searching a number as string (convert the Number into a
	 * String)
	 */
	private String getSqlNumberOperationAsString(String column, String operation, String value, boolean beginning,
			boolean end) {
		return getSqlConvertNumberIntoString().replace(COLUMNNAME, column) + " " + operation
				+ (beginning ? " '%" : " '") + value + (end ? "%'" : "'");
	}

	/**
	 * Get the vendor specific Sql for converting a Number into a String
	 */
	protected abstract String getSqlConvertNumberIntoString();

	/**
	 * Get the Sql for searching a date as string (convert the Date into a String)
	 */
	private String getSqlDateOperationAsString(String field, String value) {
		return getSqlConvertDateIntoString().replace(COLUMNNAME, field) + FilterRuleOperation.cn.op + "'%" + value
				+ "%'";
	}

	/**
	 * Get the vendor specific Sql for converting a Date into a String
	 */
	protected abstract String getSqlConvertDateIntoString();

	/**
	 * Get the Sql for converting an String into a Date
	 */
	private String getSqlDateOperation(TemporalAccessor instant) {
		return getSqlConvertStringIntoDate().replace(VALUE,
				PuiDateUtil.temporalAccessorToString(instant, PuiDateUtil.utcFormatter));
	}

	/**
	 * Get the vendor specific Sql for converting a String into Date
	 */
	protected abstract String getSqlConvertStringIntoDate();

	/**
	 * Get the SQL for searching Text, removing the accents and making the lowercase
	 * operation
	 */
	private String getSqlTextOperation(String column, boolean isLargeStringField, String operation, String value,
			boolean beginning, boolean end) {
		return getSqlTextOperation(isLargeStringField).replace(COLUMNNAME, column).replace(OP, operation)
				.replace(VALUE, value).replace(BEGINNING, beginning ? "%" : "").replace(END, end ? "%" : "");
	}

	/**
	 * Get the vendor specific Sql for converting a String by removing the accents
	 * and converting to lowercase
	 */
	protected abstract String getSqlTextOperation(boolean isLargeStringField);

	/**
	 * Check if the given field is a text field
	 */
	private boolean isString(Class<? extends IDto> dtoClass, String field) {
		if (DtoRegistry.getFieldNameFromColumnName(dtoClass, field) != null) {
			field = DtoRegistry.getFieldNameFromColumnName(dtoClass, field);
		}
		return DtoRegistry.getStringFields(dtoClass).contains(field);
	}

	/**
	 * Check if the given field is an Clob or very large field in the database
	 */
	private boolean isLargeStringField(Class<? extends IDto> dtoClass, String field) {
		if (DtoRegistry.getFieldNameFromColumnName(dtoClass, field) != null) {
			field = DtoRegistry.getFieldNameFromColumnName(dtoClass, field);
		}
		Integer length = DtoRegistry.getFieldMaxLength(dtoClass, field);
		return length == null || length == -1 || length >= (32 * 1024);
	}

	/**
	 * Check if the given field is a numeric field
	 */
	private boolean isNumber(Class<? extends IDto> dtoClass, String field) {
		if (DtoRegistry.getFieldNameFromColumnName(dtoClass, field) != null) {
			field = DtoRegistry.getFieldNameFromColumnName(dtoClass, field);
		}
		return DtoRegistry.getNumericFields(dtoClass).contains(field)
				|| DtoRegistry.getFloatingFields(dtoClass).contains(field);
	}

	/**
	 * Check if the given field is a date field
	 */
	private boolean isDate(Class<? extends IDto> dtoClass, String field) {
		if (DtoRegistry.getFieldNameFromColumnName(dtoClass, field) != null) {
			field = DtoRegistry.getFieldNameFromColumnName(dtoClass, field);
		}
		return DtoRegistry.getDateTimeFields(dtoClass).contains(field);
	}

	protected String adaptDateFormatToUser(String baseDateFormat) {
		String dateFormat = DEFAULT_DATE_FORMAT;
		if (PuiUserSession.getCurrentSession() != null) {
			dateFormat = PuiUserSession.getCurrentSession().getDateformat();
		}
		return baseDateFormat.replace(DATE_FORMAT, dateFormat).replace("/", "-").replace(".", "-");
	}

}
