package es.prodevelop.pui9.model.configuration;

import es.prodevelop.pui9.enums.ColumnType;
import es.prodevelop.pui9.enums.ColumnVisibility;

/**
 * The column configuration for each model. For each column it's specified its
 * name (the name is the Java field name); the title for translate the column in
 * the client (concatenate the name of the model, a '.' and the name of the
 * column); the type of the column (numeric, text, date...); if it belongs to
 * the PK of the model; and the visibility (visible, hidden or completelyhidden)
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiModelColumn {

	private String name;
	private String title;
	private ColumnType type;
	private boolean isPk;
	private ColumnVisibility visibility;

	public PuiModelColumn(String name, String title, ColumnType type, boolean isPk, ColumnVisibility visibility) {
		this.name = name;
		this.title = title;
		this.type = type;
		this.isPk = isPk;
		this.visibility = visibility;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public ColumnType getType() {
		return type;
	}

	public boolean isPk() {
		return isPk;
	}

	public ColumnVisibility getVisibility() {
		return visibility;
	}

}
