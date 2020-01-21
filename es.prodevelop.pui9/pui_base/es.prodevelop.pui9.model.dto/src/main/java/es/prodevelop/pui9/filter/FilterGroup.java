package es.prodevelop.pui9.filter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.utils.PuiDateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This class represents a Filter to execute a customized search against the
 * database. The {@link SearchRequest} uses this kind of Filters so specify the
 * user search. See also {@link FilterBuilder} class to help building custom
 * filters
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@ApiModel(value = "Filter group object")
public class FilterGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "Operation", position = -1, required = true)
	private FilterGroupOperation groupOp = FilterGroupOperation.and;

	@ApiModelProperty(value = "Groups list", position = -1, required = false)
	private List<FilterGroup> groups = new ArrayList<>();

	@ApiModelProperty(value = "Rules list", position = -1, required = true)
	private List<FilterRule> rules = new ArrayList<>();

	/**
	 * Create a Filter from the Dto PK provided with the values of the Primary Key
	 * of the registry
	 * 
	 * @param dto The table Dto registry
	 * @return A filter of the PK of the given Table Dto
	 */
	public static FilterGroup createFilterForDtoPk(ITableDto dto) {
		if (dto == null) {
			return null;
		}

		ITableDto dtoPk = dto.createPk();
		FilterGroup filter = new FilterGroup();
		for (String fieldName : DtoRegistry.getAllFields(dtoPk.getClass())) {
			try {
				Field field = DtoRegistry.getJavaFieldFromFieldName(dtoPk.getClass(), fieldName);
				Object value = field.get(dtoPk);
				FilterRule rule = new FilterRule(fieldName, FilterRuleOperation.eq, value);
				filter.getRules().add(rule);
			} catch (Exception e) {
				// do nothing
			}
		}

		return !StringUtils.isEmpty(filter.toString()) ? filter : null;
	}

	/**
	 * Create a single Filter using the provided information: field, operation and
	 * value
	 * 
	 * @param field     The field name of the registry
	 * @param operation The operation type. See {@link FilterRuleOperation} enum
	 * @param value     The value
	 * @return The single Filter
	 */
	public static FilterGroup createSingleFilter(String field, FilterRuleOperation operation, Object value) {
		return new FilterGroup().addRule(new FilterRule(field, operation, value));
	}

	/**
	 * Create a Filter from a json
	 * 
	 * @param json The filter as json
	 * @return The filter object
	 */
	public static FilterGroup fromJson(String json) {
		return GsonSingleton.getSingleton().getGson().fromJson(json, FilterGroup.class);
	}

	/**
	 * Creates a FiltersDto element with the default operation
	 * {@link FilterGroupOperation#and}
	 */
	public FilterGroup() {
		this(FilterGroupOperation.and);
	}

	/**
	 * Creates a FiltersDto element with the given {@link FilterGroupOperation}
	 * operation
	 */
	public FilterGroup(FilterGroupOperation groupOp) {
		this.groupOp = groupOp;
	}

	/**
	 * Get the group operation. This operation is used for the whole group and rules
	 * 
	 * @return The Operation
	 */
	public FilterGroupOperation getGroupOp() {
		return groupOp;
	}

	/**
	 * Set the group operation
	 * 
	 * @param groupOp The operation for the whole group and rules
	 */
	public void setGroupOp(FilterGroupOperation groupOp) {
		this.groupOp = groupOp;
	}

	public List<FilterGroup> getGroups() {
		if (groups == null) {
			groups = new ArrayList<>();
		}
		return groups;
	}

	public void setGroups(List<FilterGroup> groups) {
		this.groups = groups;
	}

	public FilterGroup addGroup(FilterGroup group) {
		getGroups().add(group);
		return this;
	}

	public List<FilterRule> getRules() {
		if (rules == null) {
			rules = new ArrayList<>();
		}
		return rules;
	}

	public void setRules(List<FilterRule> rules) {
		this.rules = rules;
	}

	public FilterGroup addRule(FilterRule rule) {
		getRules().add(rule);
		return this;
	}

	public String toJson() {
		return GsonSingleton.getSingleton().getGson().toJson(this);
	}

	@Override
	public String toString() {
		if (getRules().isEmpty() && getGroups().isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		for (Iterator<FilterRule> it = getRules().iterator(); it.hasNext();) {
			FilterRule next = it.next();
			sb.append(next.toString());
			if (it.hasNext()) {
				sb.append("(" + groupOp + ") ");
			}
		}

		if (getGroups() != null && !getGroups().isEmpty()) {
			if (!getRules().isEmpty()) {
				sb.append(" " + groupOp + " (");
			} else {
				sb.append("(");
			}

			for (Iterator<FilterGroup> it = getGroups().iterator(); it.hasNext();) {
				FilterGroup next = it.next();
				sb.append(next.toString());
				if (it.hasNext()) {
					sb.append(" " + groupOp + " ");
				}
			}
			sb.append(")");
		}

		return sb.toString();
	}

	public static void modifyDateFilters(Class<? extends IDto> dtoClass, FilterGroup filter) {
		if (filter == null) {
			return;
		}

		filter.getRules().stream().filter(rule -> isDate(dtoClass, rule.getField()) && rule.getData() != null
				&& !StringUtils.isEmpty(rule.getData().toString())).forEach(FilterGroup::modifyDateFilters);

		filter.getGroups().forEach(group -> modifyDateFilters(dtoClass, group));
	}

	@SuppressWarnings("unchecked")
	public static void modifyDateFilters(FilterRule rule) {
		String value = rule.getData().toString();
		boolean hasHour = PuiDateUtil.stringHasHours(value);
		boolean hasMinutes = PuiDateUtil.stringHasMinutes(value);
		boolean hasSeconds = PuiDateUtil.stringHasSeconds(value);
		Instant instant = PuiDateUtil.stringToInstant(value);
		List<Instant> range = new ArrayList<>();

		switch (rule.getOp()) {
		case eq:
			if (hasSeconds) {
				range.add(getInstantAtStartOfDay(instant, false, false, false));
				range.add(getInstantAtEndOfDay(instant, false, false, false));
			} else if (hasMinutes) {
				range.add(getInstantAtStartOfDay(instant, false, false, true));
				range.add(getInstantAtEndOfDay(instant, false, false, true));
			} else if (hasHour) {
				range.add(getInstantAtStartOfDay(instant, false, true, true));
				range.add(getInstantAtEndOfDay(instant, false, true, true));
			} else {
				range.add(getInstantAtStartOfDay(instant, true, true, true));
				range.add(getInstantAtEndOfDay(instant, true, true, true));
			}

			rule.setOp(FilterRuleOperation.bt);
			rule.setData(range);
			break;
		case eqt:
			instant = Instant.now();
			instant = instant.plus(Integer.parseInt(value), ChronoUnit.DAYS);
			range.add(getInstantAtStartOfDay(instant, true, true, true));
			range.add(getInstantAtEndOfDay(instant, true, true, true));

			rule.setOp(FilterRuleOperation.bt);
			rule.setData(range);
			break;
		case ne:
			if (hasSeconds) {
				range.add(getInstantAtStartOfDay(instant, false, false, false));
				range.add(getInstantAtEndOfDay(instant, false, false, false));
			} else if (hasMinutes) {
				range.add(getInstantAtStartOfDay(instant, false, false, true));
				range.add(getInstantAtEndOfDay(instant, false, false, true));
			} else if (hasHour) {
				range.add(getInstantAtStartOfDay(instant, false, true, true));
				range.add(getInstantAtEndOfDay(instant, false, true, true));
			} else {
				range.add(getInstantAtStartOfDay(instant, true, true, true));
				range.add(getInstantAtEndOfDay(instant, true, true, true));
			}

			rule.setOp(FilterRuleOperation.nbt);
			rule.setData(range);
			break;
		case net:
			instant = Instant.now();
			instant = instant.plus(Integer.parseInt(value), ChronoUnit.DAYS);
			range.add(getInstantAtStartOfDay(instant, true, true, true));
			range.add(getInstantAtEndOfDay(instant, true, true, true));

			rule.setOp(FilterRuleOperation.nbt);
			rule.setData(range);
			break;
		case lt:
			if (hasSeconds) {
				instant = getInstantAtStartOfDay(instant, false, false, false);
			} else if (hasMinutes) {
				instant = getInstantAtStartOfDay(instant, false, false, true);
			} else if (hasHour) {
				instant = getInstantAtStartOfDay(instant, false, true, true);
			} else {
				instant = getInstantAtStartOfDay(instant, true, true, true);
			}

			rule.setData(instant);
			break;
		case ltt:
			instant = Instant.now();
			instant = instant.plus(Integer.parseInt(value), ChronoUnit.DAYS);
			instant = getInstantAtStartOfDay(instant, true, true, true);

			rule.setOp(FilterRuleOperation.lt);
			rule.setData(instant);
			break;
		case le:
			if (hasSeconds) {
				instant = getInstantAtEndOfDay(instant, false, false, false);
			} else if (hasMinutes) {
				instant = getInstantAtEndOfDay(instant, false, false, true);
			} else if (hasHour) {
				instant = getInstantAtEndOfDay(instant, false, true, true);
			} else {
				instant = getInstantAtEndOfDay(instant, true, true, true);
			}

			rule.setData(instant);
			break;
		case let:
			instant = Instant.now();
			instant = instant.plus(Integer.parseInt(value), ChronoUnit.DAYS);
			instant = getInstantAtEndOfDay(instant, true, true, true);

			rule.setOp(FilterRuleOperation.le);
			rule.setData(instant);
			break;
		case gt:
			if (hasSeconds) {
				instant = getInstantAtEndOfDay(instant, false, false, false);
			} else if (hasMinutes) {
				instant = getInstantAtEndOfDay(instant, false, false, true);
			} else if (hasHour) {
				instant = getInstantAtEndOfDay(instant, false, true, true);
			} else {
				instant = getInstantAtEndOfDay(instant, true, true, true);
			}

			rule.setData(instant);
			break;
		case gtt:
			instant = Instant.now();
			instant = instant.plus(Integer.parseInt(value), ChronoUnit.DAYS);
			instant = getInstantAtEndOfDay(instant, true, true, true);

			rule.setOp(FilterRuleOperation.gt);
			rule.setData(instant);
			break;
		case ge:
			if (hasSeconds) {
				instant = getInstantAtStartOfDay(instant, false, false, false);
			} else if (hasMinutes) {
				instant = getInstantAtStartOfDay(instant, false, false, true);
			} else if (hasHour) {
				instant = getInstantAtStartOfDay(instant, false, true, true);
			} else {
				instant = getInstantAtStartOfDay(instant, true, true, true);
			}

			rule.setData(instant);
			break;
		case get:
			instant = Instant.now();
			instant = instant.plus(Integer.parseInt(value), ChronoUnit.DAYS);
			instant = getInstantAtStartOfDay(instant, true, true, true);

			rule.setOp(FilterRuleOperation.ge);
			rule.setData(instant);
			break;
		case bt:
			if (rule.getData() instanceof List && ((List<String>) rule.getData()).size() == 2) {
				List<String> list = (List<String>) rule.getData();
				String lower = list.get(0);
				String upper = list.get(1);
				hasHour = PuiDateUtil.stringHasHours(lower) && PuiDateUtil.stringHasHours(upper);
				hasMinutes = PuiDateUtil.stringHasMinutes(lower) && PuiDateUtil.stringHasMinutes(upper);
				hasSeconds = PuiDateUtil.stringHasSeconds(lower) && PuiDateUtil.stringHasSeconds(upper);
				Instant lowerInstant = PuiDateUtil.stringToInstant(lower);
				Instant upperInstant = PuiDateUtil.stringToInstant(upper);

				if (hasSeconds) {
					range.add(getInstantAtStartOfDay(lowerInstant, false, false, false));
					range.add(getInstantAtEndOfDay(upperInstant, false, false, false));
				} else if (hasMinutes) {
					range.add(getInstantAtStartOfDay(lowerInstant, false, false, true));
					range.add(getInstantAtEndOfDay(upperInstant, false, false, true));
				} else if (hasHour) {
					range.add(getInstantAtStartOfDay(lowerInstant, false, true, true));
					range.add(getInstantAtEndOfDay(upperInstant, false, true, true));
				} else {
					range.add(getInstantAtStartOfDay(lowerInstant, true, true, true));
					range.add(getInstantAtEndOfDay(upperInstant, true, true, true));
				}

				rule.setData(range);
			}
			break;
		case nbt:
			if (rule.getData() instanceof List && ((List<String>) rule.getData()).size() == 2) {
				List<String> list = (List<String>) rule.getData();
				String lower = list.get(0);
				String upper = list.get(1);
				hasHour = PuiDateUtil.stringHasHours(lower) && PuiDateUtil.stringHasHours(upper);
				hasMinutes = PuiDateUtil.stringHasMinutes(lower) && PuiDateUtil.stringHasMinutes(upper);
				hasSeconds = PuiDateUtil.stringHasSeconds(lower) && PuiDateUtil.stringHasSeconds(upper);
				Instant lowerInstant = PuiDateUtil.stringToInstant(lower);
				Instant upperInstant = PuiDateUtil.stringToInstant(upper);

				if (hasSeconds) {
					range.add(getInstantAtStartOfDay(lowerInstant, false, false, false));
					range.add(getInstantAtEndOfDay(upperInstant, false, false, false));
				} else if (hasMinutes) {
					range.add(getInstantAtStartOfDay(lowerInstant, false, false, true));
					range.add(getInstantAtEndOfDay(upperInstant, false, false, true));
				} else if (hasHour) {
					range.add(getInstantAtStartOfDay(lowerInstant, false, true, true));
					range.add(getInstantAtEndOfDay(upperInstant, false, true, true));
				} else {
					range.add(getInstantAtStartOfDay(lowerInstant, true, true, true));
					range.add(getInstantAtEndOfDay(upperInstant, true, true, true));
				}

				rule.setData(range);
			}
			break;
		default:
			break;
		}
	}

	private static boolean isDate(Class<? extends IDto> dtoClass, String field) {
		if (DtoRegistry.getFieldNameFromColumnName(dtoClass, field) != null) {
			field = DtoRegistry.getFieldNameFromColumnName(dtoClass, field);
		}
		return DtoRegistry.getDateTimeFields(dtoClass).contains(field);
	}

	private static Instant getInstantAtStartOfDay(Instant instant, boolean setHour, boolean setMinute,
			boolean setSecond) {
		if (instant == null) {
			return null;
		}

		LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
		if (setHour) {
			ldt = ldt.withHour(0);
		}
		if (setMinute) {
			ldt = ldt.withMinute(0);
		}
		if (setSecond) {
			ldt = ldt.withSecond(0);
		}
		ldt = ldt.with(ChronoField.MILLI_OF_SECOND, 0);

		return ldt.toInstant(ZoneOffset.UTC);
	}

	private static Instant getInstantAtEndOfDay(Instant instant, boolean setHour, boolean setMinute,
			boolean setSecond) {
		if (instant == null) {
			return null;
		}

		LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
		if (setHour) {
			ldt = ldt.withHour(23);
		}
		if (setMinute) {
			ldt = ldt.withMinute(59);
		}
		if (setSecond) {
			ldt = ldt.withSecond(59);
		}
		ldt = ldt.with(ChronoField.MILLI_OF_SECOND, 999);

		return ldt.toInstant(ZoneOffset.UTC);
	}

}
