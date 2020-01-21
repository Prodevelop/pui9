package es.prodevelop.codegen.pui9.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.prodevelop.codegen.pui9.utils.CodegenUtils;

public abstract class Entity {

	private transient PuiConfiguration configuration;
	private transient boolean withGeometry;
	private transient boolean withAutowhere;

	private transient String javaName;

	private String dbName;
	private List<Column> columns;

	public PuiConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(PuiConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isWithGeometry() {
		return withGeometry;
	}

	public boolean isWithAutowhere() {
		return withAutowhere;
	}

	public void setWithAutowhere(boolean withAutowhere) {
		this.withAutowhere = withAutowhere;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
		computeJavaAttributes();
	}

	public String getJavaName() {
		return javaName;
	}

	public List<Column> getColumns() {
		if (columns == null) {
			columns = new ArrayList<>();
		}
		return columns;
	}

	/**
	 * This method adds the new column into the Entity, but if it exists a column
	 * with the same name, it's modified with the values of the given column
	 */
	public void addColumn(Column column) {
		Column existentColumn = null;

		for (Column c : getColumns()) {
			if (column.getDbName().equalsIgnoreCase(c.getDbName())) {
				existentColumn = c;
				break;
			}
		}

		if (existentColumn == null) {
			getColumns().add(column.getPosition(), column);
			existentColumn = column;
		} else {
			existentColumn.setDbType(column.getDbType());
			existentColumn.setDbRawType(column.getDbRawType());
			existentColumn.setDbDefaultValue(column.getDbDefaultValue());
			existentColumn.setDbSize(column.getDbSize());
			existentColumn.setDbDecimals(column.getDbDecimals());
			existentColumn.setSequence(column.isSequence());
			existentColumn.setGeometry(column.isGeometry());
			existentColumn.setPosition(column.getPosition());
		}

		existentColumn.computeJavaAttributes();
		withGeometry |= existentColumn.isGeometry();
	}

	public void removeWrongColumns(List<String> validColumns) {
		getColumns().removeIf(column -> {
			return !validColumns.contains(column.getDbName());
		});
	}
	
	public void reorderColumns() {
		Collections.sort(getColumns());
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public void computeJavaAttributes() {
		setJavaName();
	}

	private void setJavaName() {
		javaName = CodegenUtils.convertEntityNameToJavaName(dbName);
	}

	@Override
	public String toString() {
		return "DB name: " + dbName + "\nJava name: " + javaName + "\nWith geometry: " + withGeometry;
	}

}
