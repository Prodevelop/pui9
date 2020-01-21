package es.prodevelop.codegen.pui9.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class PuiConfiguration {

	private transient Table selectedTable;
	private transient View selectedView;

	private String projectId;
	private String pui9Version;
	private DatabaseConnection database;
	private ServerConfiguration server;
	private ClientConfiguration client;
	private List<Table> tables;
	private List<View> views;

	public Table getSelectedTable() {
		return selectedTable;
	}

	public void setSelectedTable(Table selectedTable) {
		this.selectedTable = selectedTable;
		if (selectedTable != null) {
			server.setReadFunctionality("READ_" + selectedTable.getDbName().toUpperCase());
			server.setWriteFunctionality("WRITE_" + selectedTable.getDbName().toUpperCase());
		}
	}

	public View getSelectedView() {
		return selectedView;
	}

	public void setSelectedView(View selectedView) {
		this.selectedView = selectedView;
		if (selectedView != null) {
			if (StringUtils.isEmpty(server.getReadFunctionality())) {
				server.setReadFunctionality("READ_" + selectedView.getDbName().toUpperCase());
			}
			if (StringUtils.isEmpty(server.getWriteFunctionality())) {
				server.setWriteFunctionality("WRITE_" + selectedView.getDbName().toUpperCase());
			}
		}
	}

	public Table findTable(String name) {
		for (Table table : tables) {
			if (table.getDbName().equals(name)) {
				return table;
			}
		}
		return null;
	}

	public View findView(String name) {
		for (View view : views) {
			if (view.getDbName().equals(name)) {
				return view;
			}
		}
		return null;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPui9Version() {
		return pui9Version;
	}

	public void setPui9Version(String pui9Version) {
		this.pui9Version = pui9Version;
	}

	public DatabaseConnection getDatabase() {
		return database;
	}

	public void setDatabase(DatabaseConnection database) {
		this.database = database;
	}

	public ServerConfiguration getServer() {
		return server;
	}

	public void setServer(ServerConfiguration server) {
		this.server = server;
	}

	public ClientConfiguration getClient() {
		return client;
	}

	public void setClient(ClientConfiguration client) {
		this.client = client;
	}

	public List<Table> getTables() {
		if (tables == null) {
			tables = new ArrayList<>();
		}
		return tables;
	}

	public void addTable(Table table) {
		getTables().add(table);
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public List<View> getViews() {
		if (views == null) {
			views = new ArrayList<>();
		}
		return views;
	}

	public void addView(View view) {
		getViews().add(view);
	}

	public void setViews(List<View> views) {
		this.views = views;
	}

	@Override
	public String toString() {
		return "Project ID: " + projectId + "\nPUI9 Version: " + pui9Version + "\nDatabase: " + database
				+ (selectedTable != null ? "\n\nTable:\n" + selectedTable : "")
				+ (selectedView != null ? "\n\nView:\n" + selectedView : "");
	}

}
