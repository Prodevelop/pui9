package es.prodevelop.pui9.filter;

/**
 * Filter rule operation enumeration. Values are case sensitive.
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public enum FilterRuleOperation {

	/**
	 * equals
	 */
	eq(" = "),

	/**
	 * equals to today
	 */
	eqt(" = "),

	/**
	 * not equals
	 */
	ne(" <> "),

	/**
	 * not equals to today
	 */
	net(" <> "),

	/**
	 * is null
	 */
	nu(" IS NULL "),

	/**
	 * not null
	 */
	nn(" IS NOT NULL "),

	/**
	 * begins with
	 */
	bw(" LIKE "),

	/**
	 * not begins with
	 */
	bn(" NOT LIKE "),

	/**
	 * contains
	 */
	cn(" LIKE "),

	/**
	 * not contains
	 */
	nc(" NOT LIKE "),

	/**
	 * ends with
	 */
	ew(" LIKE "),

	/**
	 * not ends with
	 */
	en(" NOT LIKE "),

	/**
	 * greater than
	 */
	gt(" > "),

	/**
	 * greater than today
	 */
	gtt(" > "),

	/**
	 * greater or equals than
	 */
	ge(" >= "),

	/**
	 * greater or equals than today
	 */
	get(" >= "),

	/**
	 * less than
	 */
	lt(" < "),

	/**
	 * less than today
	 */
	ltt(" < "),

	/**
	 * less or equals than
	 */
	le(" <= "),

	/**
	 * less or equals than today
	 */
	let(" <= "),

	/**
	 * in
	 */
	in(" IN "),

	/**
	 * not in
	 */
	ni(" NOT IN "),

	/**
	 * between
	 */
	bt(" BETWEEN "),

	/**
	 * not between
	 */
	nbt(" NOT BETWEEN "),

	/**
	 * contains operation for geometry columns (data attribute has the sql)
	 */
	geo_bounding_box(""),

	/**
	 * intersects operation for geometry columns (data attribute has the sql)
	 */
	geo_intersects("");

	public final String op;

	private FilterRuleOperation(String op) {
		this.op = op;
	}

}