package es.prodevelop.pui9.filter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to make easier creating Where clauses
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class FilterBuilder {

	protected FilterGroup filters;

	/**
	 * Create a new group with global 'and' operation
	 * 
	 * @return The Filter Builder
	 */
	public static FilterBuilder newAndFilter() {
		return new FilterBuilder(FilterGroupOperation.and);
	}

	/**
	 * Create a new group with global 'or' operation
	 * 
	 * @return The Filter Builder
	 */
	public static FilterBuilder newOrFilter() {
		return new FilterBuilder(FilterGroupOperation.or);
	}

	/**
	 * Create a new group with the given group
	 * 
	 * @param filters The group to be added
	 * @return The Filter Builder
	 */
	public static FilterBuilder newFilter(FilterGroup filters) {
		return new FilterBuilder(filters);
	}

	/**
	 * Create a new group with the specified group operation
	 * 
	 * @param op The group operation
	 */
	private FilterBuilder(FilterGroupOperation op) {
		this(new FilterGroup(op));
	}

	/**
	 * Create a new group with the specified filter group
	 * 
	 * @param filters The filter group
	 */
	protected FilterBuilder(FilterGroup filters) {
		this.filters = filters;
	}

	/**
	 * Add an "Equals" operation for an Object value: "column = ?" (better use
	 * methods with concret value type)
	 * 
	 * @param column The column name
	 * @param value  The Object value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEquals(String column, Object value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.eq, value));
		return this;
	}

	/**
	 * Add an "Equals" operation for an String value: "column = ?"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEquals(String column, String value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.eq, value));
		return this;
	}

	/**
	 * Add an "Equals" operation for a Number value: "column = ?"
	 * 
	 * @param column The column name
	 * @param value  The Number value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEquals(String column, Number value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.eq, value));
		return this;
	}

	/**
	 * Add an "Equals" operation for a LocalDate value: "column = ?". This case, the
	 * real operation is a "between" operation from the start of the day to the end
	 * of the day
	 * 
	 * @param column The column name
	 * @param value  The LocalDate value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEquals(String column, LocalDate value) {
		addBetween(column, value, value);
		return this;
	}

	/**
	 * Add a "Not Equals" operation for an Object value: "column <> ?" (better use
	 * methods with concret value type)
	 * 
	 * @param column The column name
	 * @param value  The Object value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEquals(String column, Object value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.ne, value));
		return this;
	}

	/**
	 * Add a "Not Equals" operation for an String value: "column <> ?"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEquals(String column, String value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.ne, value));
		return this;
	}

	/**
	 * Add a "Not Equals" operation for a Number value: "column <> ?"
	 * 
	 * @param column The column name
	 * @param value  The Number value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEquals(String column, Number value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.ne, value));
		return this;
	}

	/**
	 * Add an "Not Equals" operation for a LocalDate value "column = ?". This case,
	 * the real operation is a "not between" operation from the start of the day to
	 * the end of the day
	 * 
	 * @param column The column name
	 * @param value  The LocalDate
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEquals(String column, LocalDate value) {
		addNotBetween(column, value, value);
		return this;
	}

	/**
	 * Add a "Is Null" opeartion: "column IS NULL"
	 * 
	 * @param column The column name
	 * @return The current Filter Builder
	 */
	public FilterBuilder addIsNull(String column) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.nu, null));
		return this;
	}

	/**
	 * Add a "Is Not Null" operation: "column IS NOT NULL"
	 * 
	 * @param column The column name
	 * @return The current Filter Builder
	 */
	public FilterBuilder addIsNotNull(String column) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.nn, null));
		return this;
	}

	/**
	 * Add a "Begins With" operation: "column LIKE '?%'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBeginWith(String column, String value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.bw, value));
		return this;
	}

	/**
	 * Add a "Not Begins With" operation: "column NOT LIKE '?%'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotBeginWith(String column, String value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.bn, value));
		return this;
	}

	/**
	 * Add a "Ends With" operation: "column LIKE '%?'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addEndsWith(String column, String value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.ew, value));
		return this;
	}

	/**
	 * Add a "Not Ends With" operation: "column NOT LIKE '%?'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotEndsWith(String column, String value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.en, value));
		return this;
	}

	/**
	 * Add a "Contains" operation: "column LIKE '%?%'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addContains(String column, String value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.cn, value));
		return this;
	}

	/**
	 * Add a "Not Contains" operation: "column NOT LIKE '%?%'"
	 * 
	 * @param column The column name
	 * @param value  The String value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotContains(String column, String value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.nc, value));
		return this;
	}

	/**
	 * Add a "Lower Than" operation for a Number value: "column < ?"
	 * 
	 * @param column The column name
	 * @param value  The Number value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addLowerThan(String column, Number value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.lt, value));
		return this;
	}

	/**
	 * Add a "Lower Than" operation for a LocalDate value: "column < ?"
	 * 
	 * @param column The column name
	 * @param value  The LocalDate value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addLowerThan(String column, LocalDate value) {
		Instant to = Instant.from(value.atStartOfDay().atZone(ZoneId.systemDefault()));
		filters.addRule(new FilterRule(column, FilterRuleOperation.lt, to));
		return this;
	}

	/**
	 * Add a "Lower or Equals Than" operation for a Number value: "column <= ?"
	 * 
	 * @param column The column name
	 * @param value  The Number value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addLowerEqualsThan(String column, Number value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.le, value));
		return this;
	}

	/**
	 * Add a "Lower or Equals Than" operation for a LocalDate value: "column <= ?"
	 * 
	 * @param column The column name
	 * @param value  The LocalDate value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addLowerEqualsThan(String column, LocalDate value) {
		Instant to = Instant.from(
				value.atTime(LocalTime.MAX).with(ChronoField.MILLI_OF_SECOND, 999).atZone(ZoneId.systemDefault()));
		filters.addRule(new FilterRule(column, FilterRuleOperation.le, to));
		return this;
	}

	/**
	 * Add a "Greater Than" operation for a Number value: "column > ?"
	 * 
	 * @param column The column name
	 * @param value  The Number value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGreaterThan(String column, Number value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.gt, value));
		return this;
	}

	/**
	 * Add a "Greater Than" operation for a LocalDate value: "column > ?"
	 * 
	 * @param column The column name
	 * @param value  The LocalDate value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGreaterThan(String column, LocalDate value) {
		Instant from = Instant.from(
				value.atTime(LocalTime.MAX).with(ChronoField.MILLI_OF_SECOND, 999).atZone(ZoneId.systemDefault()));
		filters.addRule(new FilterRule(column, FilterRuleOperation.gt, from));
		return this;
	}

	/**
	 * Add a "Greater or Equals Than" operation for a Number value: "column >= ?"
	 * 
	 * @param column The column name
	 * @param value  The Number value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGreaterEqualsThan(String column, Number value) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.ge, value));
		return this;
	}

	/**
	 * Add a "Greater or Equals Than" operation for a LocalDate value: "column >= ?"
	 * 
	 * @param column The column name
	 * @param value  The LocalDate value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGreaterEqualsThan(String column, LocalDate value) {
		Instant from = Instant.from(value.atStartOfDay().atZone(ZoneId.systemDefault()));
		filters.addRule(new FilterRule(column, FilterRuleOperation.ge, from));
		return this;
	}

	/**
	 * Add a "Between" operation for a Number value: "column between {lower} and
	 * {upper}"
	 * 
	 * @param column The column name
	 * @param lower  The lower Number value
	 * @param upper  The upper Number value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBetween(String column, Number lower, Number upper) {
		List<Object> values = new ArrayList<>();
		values.add(lower);
		values.add(upper);
		filters.addRule(new FilterRule(column, FilterRuleOperation.bt, values));
		return this;
	}

	/**
	 * Add a "Between" operation for a LocalDate value: "column between {lower} and
	 * {upper}"
	 * 
	 * @param column The column name
	 * @param lower  The lower LocalDate value
	 * @param upper  The upper LocalDate value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addBetween(String column, LocalDate lower, LocalDate upper) {
		List<Object> values = new ArrayList<>();
		values.add(Instant.from(lower.atStartOfDay().atZone(ZoneId.systemDefault())));
		values.add(Instant.from(
				upper.atTime(LocalTime.MAX).with(ChronoField.MILLI_OF_SECOND, 999).atZone(ZoneId.systemDefault())));
		filters.addRule(new FilterRule(column, FilterRuleOperation.bt, values));
		return this;
	}

	/**
	 * Add a "Not Between" operation for a Number value: "column not between {lower}
	 * and {upper}"
	 * 
	 * @param column The column name
	 * @param lower  The lower Number value
	 * @param upper  The upper Number value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotBetween(String column, Number lower, Number upper) {
		List<Object> values = new ArrayList<>();
		values.add(lower);
		values.add(upper);
		filters.addRule(new FilterRule(column, FilterRuleOperation.nbt, values));
		return this;
	}

	/**
	 * Add a "Not Between" operation for a LocalDate value: "column not between
	 * {lower} and {upper}"
	 * 
	 * @param column The column name
	 * @param lower  The lower LocalDate value
	 * @param upper  The upper LocalDate value
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotBetween(String column, LocalDate lower, LocalDate upper) {
		List<Object> values = new ArrayList<>();
		values.add(Instant.from(lower.atStartOfDay().atZone(ZoneId.systemDefault())));
		values.add(Instant.from(
				upper.atTime(LocalTime.MAX).with(ChronoField.MILLI_OF_SECOND, 999).atZone(ZoneId.systemDefault())));
		filters.addRule(new FilterRule(column, FilterRuleOperation.nbt, values));
		return this;
	}

	/**
	 * Add a "In" operation for an String value: "column in (val1, val2, val3)"
	 * 
	 * @param column     The column name
	 * @param collection The String value list
	 * @return The current Filter Builder
	 */
	public FilterBuilder addInString(String column, List<String> collection) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.in, collection));
		return this;
	}

	/**
	 * Add a "In" operation for a Number value: "column in (val1, val2, val3)"
	 * 
	 * @param column     The column name
	 * @param collection The Number value list
	 * @return The current Filter Builder
	 */
	public FilterBuilder addInNumber(String column, List<? extends Number> collection) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.in, collection));
		return this;
	}

	/**
	 * Add a "Not In" operation for an String value: "column not in (val1, val2,
	 * val3)"
	 * 
	 * @param column The column name
	 * @param values The String value list
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotInString(String column, List<String> values) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.ni, values));
		return this;
	}

	/**
	 * Add a "Not In" operation for a Number value: "column not in (val1, val2,
	 * val3)"
	 * 
	 * @param column The column name
	 * @param values The Number value list
	 * @return The current Filter Builder
	 */
	public FilterBuilder addNotInNumber(String column, List<? extends Number> values) {
		filters.addRule(new FilterRule(column, FilterRuleOperation.ni, values));
		return this;
	}

	/**
	 * Add a group to the filter
	 */
	/**
	 * Add a group to the current filter
	 * 
	 * @param filterBuilder The group to be added
	 * @return The current Filter Builder
	 */
	public FilterBuilder addGroup(FilterBuilder filterBuilder) {
		if (filterBuilder != null) {
			filters.addGroup(filterBuilder.asFilterGroup());
		}
		return this;
	}

	/**
	 * Return the builtin filter group object
	 * 
	 * @return The Filter group
	 */
	public FilterGroup asFilterGroup() {
		return filters;
	}

	/**
	 * Check if the builtin filter is empty or not
	 * 
	 * @return true if it's empty; false if not
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(filters.toString());
	}

	@Override
	public String toString() {
		return filters != null ? filters.toString() : "*empty_filters*";
	}

}