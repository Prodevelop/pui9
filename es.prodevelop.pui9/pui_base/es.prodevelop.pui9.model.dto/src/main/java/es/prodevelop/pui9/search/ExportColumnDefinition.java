package es.prodevelop.pui9.search;

import org.apache.commons.lang3.StringUtils;

import es.prodevelop.pui9.utils.IPuiObject;

/**
 * A class representing the exportable column, specifying the name, the
 * translated title and the order of the column
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ExportColumnDefinition implements IPuiObject, Comparable<ExportColumnDefinition> {

	private static final long serialVersionUID = 1L;

	public static ExportColumnDefinition of(String name, String title, Integer order, String dateformat) {
		return new ExportColumnDefinition(name, title, order, dateformat);
	}

	private String name;
	private String title;
	private Integer order;
	private String dateformat;

	private ExportColumnDefinition(String name, String title, Integer order, String dateformat) {
		this.name = name;
		this.title = title;
		this.order = order;
		this.dateformat = dateformat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getOrder() {
		return order != null ? order : 0;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getDateformat() {
		return dateformat;
	}

	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}

	@Override
	public int compareTo(ExportColumnDefinition o) {
		return this.getOrder().compareTo(o.getOrder());
	}

	@Override
	public String toString() {
		return (order != null ? order : "") + "::" + name + "::" + (StringUtils.isEmpty(title) ? "" : title);
	}

}
