package es.prodevelop.codegen.pui9.model;

import java.util.ArrayList;
import java.util.List;

public enum DatabaseType {

	ORACLE("Oracle", "oracle", "Oracle", "sdo_geometry", 1521),

	SQL_SERVER("SQL Server", "sqlserver", "Microsoft SQL Server", "geometry", 1433),

	POSTGRE_SQL("Postgre SQL", "postgresql", "PostgreSQL", "geometry", 5432),

	HSQLDB("HyperSql DB", "hsqldb", "HyperSQL", "", 9001);

	public String name;
	public String innerName;
	public String officialName;
	public String geometryColumnName;
	public int defaultPort;

	private DatabaseType(String name, String innerName, String officialName, String geometryColumnName,
			int defaultPort) {
		this.name = name;
		this.innerName = innerName;
		this.officialName = officialName;
		this.geometryColumnName = geometryColumnName;
		this.defaultPort = defaultPort;
	}

	public static DatabaseType getByName(String name) {
		for (DatabaseType dte : values()) {
			if (dte.name.equals(name)) {
				return dte;
			}
		}
		return null;
	}

	public static DatabaseType getByOfficialName(String officialName) {
		for (DatabaseType dte : values()) {
			if (dte.officialName.equals(officialName)) {
				return dte;
			}
		}
		return null;
	}

	public static String[] getAllForGenerateCode() {
		List<String> names = new ArrayList<>();
		for (DatabaseType dte : values()) {
			if (dte.equals(HSQLDB)) {
				continue;
			}
			names.add(dte.name);
		}

		return names.toArray(new String[0]);
	}

	public static String[] getAllForNewProject() {
		List<String> names = new ArrayList<>();
		for (DatabaseType dte : values()) {
			names.add(dte.name);
		}

		return names.toArray(new String[0]);
	}

}
