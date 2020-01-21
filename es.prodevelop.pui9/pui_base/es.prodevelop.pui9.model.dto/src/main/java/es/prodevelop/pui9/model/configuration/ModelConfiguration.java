package es.prodevelop.pui9.model.configuration;

import java.util.List;

import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.utils.IPuiObject;

public class ModelConfiguration implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private boolean isdefault;
	private String name;
	private String description;
	private List<Order> order;
	private List<FilterCombo> filterCombo;
	private List<Columns> columns;
	private boolean pinColumn;
	private boolean grouped;
	private boolean documentsShowRole;
	private List<String> documentsRoles;
	private boolean documentsShowLanguage;
	private boolean actionImportExport;
	private boolean actionCopy;
	private Integer refreshSeconds;
	private String extra;

	public boolean isIsdefault() {
		return isdefault;
	}

	public void setIsdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public List<FilterCombo> getFilterCombo() {
		return filterCombo;
	}

	public void setFilterCombo(List<FilterCombo> filterCombo) {
		this.filterCombo = filterCombo;
	}

	public List<Columns> getColumns() {
		return columns;
	}

	public void setColumns(List<Columns> columns) {
		this.columns = columns;
	}

	public boolean isPinColumn() {
		return pinColumn;
	}

	public void setPinColumn(boolean pinColumn) {
		this.pinColumn = pinColumn;
	}

	public boolean isGrouped() {
		return grouped;
	}

	public void setGrouped(boolean grouped) {
		this.grouped = grouped;
	}

	public boolean isDocumentsShowRole() {
		return documentsShowRole;
	}

	public void setDocumentsShowRole(boolean documentsShowRole) {
		this.documentsShowRole = documentsShowRole;
	}

	public List<String> getDocumentsRoles() {
		return documentsRoles;
	}

	public void setDocumentsRoles(List<String> documentsRoles) {
		this.documentsRoles = documentsRoles;
	}

	public boolean isDocumentsShowLanguage() {
		return documentsShowLanguage;
	}

	public void setDocumentsShowLanguage(boolean documentsShowLanguage) {
		this.documentsShowLanguage = documentsShowLanguage;
	}

	public boolean isActionImportExport() {
		return actionImportExport;
	}

	public void setActionImportExport(boolean actionImportExport) {
		this.actionImportExport = actionImportExport;
	}

	public boolean isActionCopy() {
		return actionCopy;
	}

	public void setActionCopy(boolean actionCopy) {
		this.actionCopy = actionCopy;
	}

	public Integer getRefreshSeconds() {
		return refreshSeconds;
	}

	public void setRefreshSeconds(Integer refreshSeconds) {
		this.refreshSeconds = refreshSeconds;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

}
