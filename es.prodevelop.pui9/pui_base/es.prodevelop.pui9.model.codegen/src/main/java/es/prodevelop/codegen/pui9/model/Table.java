package es.prodevelop.codegen.pui9.model;

import java.util.ArrayList;
import java.util.List;

public class Table extends Entity {

	private transient Table translationTable;
	private transient List<Column> primaryKeys;

	private transient List<String> primaryKeyNames;

	private String translationTableName = "";

	public Table getTranslationTable() {
		return translationTable;
	}

	public List<Column> getPrimaryKeys() {
		if (primaryKeys == null) {
			primaryKeys = new ArrayList<>();
		}
		return primaryKeys;
	}

	public void setTranslationTable(Table translationTable) {
		this.translationTable = translationTable;
		if (translationTable == null) {
			return;
		}

		for (Column traColumn : translationTable.getColumns()) {
			traColumn.setLang(true);
			for (Column column : getColumns()) {
				if (column.getDbName().equalsIgnoreCase(traColumn.getDbName())) {
					traColumn.setLang(false);
					break;
				}
			}
		}
	}

	public String getTranslationTableName() {
		return translationTableName;
	}

	public void setTranslationTableName(String translationTableName) {
		this.translationTableName = translationTableName;
	}

	public List<String> getPrimaryKeyNames() {
		if (primaryKeyNames == null) {
			primaryKeyNames = new ArrayList<>();
		}
		return primaryKeyNames;
	}

	public void addPrimaryKeyName(String primaryKeyName) {
		boolean addedPreviously = false;
		for (String pk : getPrimaryKeyNames()) {
			if (pk.equalsIgnoreCase(primaryKeyName)) {
				addedPreviously = true;
				break;
			}
		}

		if (addedPreviously) {
			// if the pk name is previously added, look for the column and add it
			Column pk = null;
			for (Column column : getPrimaryKeys()) {
				if (column.getDbName().equalsIgnoreCase(primaryKeyName)) {
					pk = column;
					break;
				}
			}
			// if it's not added previously, add it definitely
			if (pk == null) {
				for (Column column : getColumns()) {
					if (column.getDbName().equalsIgnoreCase(primaryKeyName)) {
						getPrimaryKeys().add(column);
						break;
					}
				}
			}
			return;
		} else {
			// if the pk name is not previously added, add it everywhere
			for (Column column : getColumns()) {
				if (column.getDbName().equalsIgnoreCase(primaryKeyName)) {
					column.setPk(true);
					column.setColumnVisibility(ColumnVisibility.completelyhidden);
					getPrimaryKeyNames().add(column.getDbName());
					getPrimaryKeys().add(column);
					break;
				}
			}
		}
	}

	public void setPrimaryKeyNames(List<String> primaryKeyNames) {
		this.primaryKeyNames = primaryKeyNames;
	}

	@Override
	public String toString() {
		return super.toString() + "\nTranslation table: " + translationTableName + "\nPrimary key Names: "
				+ String.join(", ", getPrimaryKeyNames());
	}

}
