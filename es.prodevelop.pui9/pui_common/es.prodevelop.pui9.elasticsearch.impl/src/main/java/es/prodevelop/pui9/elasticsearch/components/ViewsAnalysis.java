package es.prodevelop.pui9.elasticsearch.components;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.db.DatabaseUtils;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.view.CreateView;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;

/**
 * This class makes an analysis of all the Views of the Database, and builds an
 * structure that represents for each Table, the list of Views where this table
 * is envolved. Then everybody can check the list of tables that participate in
 * a view
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class ViewsAnalysis {

	private final Log logger = LogFactory.getLog(this.getClass());

	private final String[] translation_suffixes = { "_tr", "_tra", "_tran", "_trans", "_translation" };

	/**
	 * View -> Definition
	 */
	private Map<String, String> mapViewCode;

	/**
	 * Table -> (View -> List<TableOrder>)
	 */
	private Map<String, Map<String, List<LinkedList<JoinTableDef>>>> mapInfo;

	private Map<String, List<String>> indexableViewsMap = Collections.emptyMap();
	private List<String> elasticSearchModels = Collections.emptyList();

	@Autowired
	@Qualifier("dataSource")
	private DataSource datasource;

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private DatabaseUtils databaseUtils;

	private boolean isAnalysingViews = true;

	/**
	 * Start the views analysis process
	 */
	@PostConstruct
	private void postConstruct() {
		mapViewCode = new HashMap<>();
		mapInfo = new HashMap<>();

		new Thread(new Runnable() {
			@Override
			public void run() {
				init();
				isAnalysingViews = false;
			}
		}, "PuiThread_ViewsAnalysis").start();
	}

	/**
	 * Get all the views where the given table participates in any way (with from or
	 * join)
	 * 
	 * @param table The table to retrieve its views participation
	 * @return The list of views where the given table participates
	 */
	public Set<String> getViews(String table) {
		while (isAnalysingViews) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		autoGenerateFaultingViews();

		return mapInfo.get(table) != null ? mapInfo.get(table).keySet() : Collections.emptySet();
	}

	/**
	 * Get the list of ordered joins that the given table uses to participate in the
	 * given view. The order returned is in inverse mode, where the last item is the
	 * FROM join
	 * 
	 * @param table The table to retrieve the used joins to participate in the given
	 *              view
	 * @param view  The view where to check the table participation
	 * @return A list of ordered lists with the joins used in the view to make the
	 *         table participate on it. The order returned is in inverse mode, where
	 *         the last item is the FROM join
	 */
	public List<LinkedList<JoinTableDef>> getTableOrder(String table, String view) {
		while (isAnalysingViews) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		autoGenerateFaultingViews();

		return mapInfo.get(table) != null ? mapInfo.get(table).get(view) : new LinkedList<>();
	}

	/**
	 * Get the list of related tables for the given views
	 * 
	 * @param view The view get the related tables
	 * @return The list of tables that participate in the given view
	 */
	public Set<String> getRelatedTables(String view) {
		Set<String> tables = new HashSet<>();

		for (Entry<String, Map<String, List<LinkedList<JoinTableDef>>>> entry : mapInfo.entrySet()) {
			if (entry.getValue().keySet().contains(view)) {
				tables.add(entry.getKey());
			}
		}

		return tables;
	}

	/**
	 * Set the indexable views for this application
	 * 
	 * @param indexableViewsList The list of indexable views
	 */
	public void setIndexableViewsMap(Map<String, List<String>> indexableViewsMap) {
		while (isAnalysingViews) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		this.indexableViewsMap = new HashMap<>();
		indexableViewsMap.forEach((view, identityfields) -> {
			this.indexableViewsMap.put(view.toLowerCase(), identityfields);
		});
	}

	/**
	 * Set the elastic search models
	 * 
	 * @param indexableViewsList The list of indexable views
	 */
	public void setElasticSearchList(List<String> elasticSearchModels) {
		while (isAnalysingViews) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		this.elasticSearchModels = new ArrayList<>();
		elasticSearchModels.forEach((model) -> {
			this.elasticSearchModels.add(model.toLowerCase());
		});
	}

	/**
	 * Get the list of indexable views in Elastic Serach
	 * 
	 * @return The list of indexable views in Elastic Search
	 */
	public Map<String, List<String>> getIndexableViewsMap() {
		return indexableViewsMap;
	}

	/**
	 * Get the list of indexable models in Elastic Search
	 * 
	 * @return The list of indexable models in Elastic Search
	 */
	public List<String> getElasticSearchModels() {
		return elasticSearchModels;
	}

	/**
	 * Before calling this method, make sure that you previously set the indexable
	 * views list ({@link #setIndexableViewsList(List)})
	 */
	public void autoGenerateFaultingViews() {
		while (isAnalysingViews) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		for (String view : mapViewCode.keySet()) {
			if (!indexableViewsMap.containsKey(view.toLowerCase())) {
				continue;
			}

			try {
				// auto-generate the faulting views
				daoRegistry.getDtoFromEntityName(view, false, true);
			} catch (Exception e) {
				continue;
			}
		}
	}

	/**
	 * Get the connection list used to retrieve the views. For each connection, it
	 * will be retrieves all the views
	 * 
	 * @return The list of connections
	 * @throws SQLException If any SQL exception occurs
	 */
	protected List<Connection> getConnection() throws SQLException {
		return Collections.singletonList(datasource.getConnection());
	}

	/**
	 * Initialize the views analysis process. It uses the list of connections of
	 * {@link #getConnection()} to look for all the available views. For each one,
	 * the SQL code will be retrieved and analyzed
	 */
	private void init() {
		logger.info("*** View analysis start");
		long start = System.currentTimeMillis();

		try {
			List<Connection> connList = getConnection();
			for (Connection conn : connList) {
				String sql = getViewsSql(conn);

				try (ResultSet rs = conn.prepareStatement(sql).executeQuery()) {
					while (rs.next()) {
						String viewName = rs.getString(1).toLowerCase();
						String code = rs.getString(2);
						mapViewCode.put(viewName.toLowerCase(), code);
					}
				} finally {
					conn.close();
				}
			}
		} catch (SQLException e) {
			logger.debug(e);
		}

		for (Entry<String, String> entry : mapViewCode.entrySet()) {
			String viewName = entry.getKey();
			String code = entry.getValue();
			if (code == null) {
				continue;
			}

			try {
				CCJSqlParserManager pm = new CCJSqlParserManager();
				Statement stm = pm.parse(new StringReader(code));

				if (stm instanceof CreateView) {
					parseCreateView(viewName, (CreateView) stm);
				} else if (stm instanceof Select) {
					parseSelectBody(viewName, ((Select) stm).getSelectBody());
				}
			} catch (JSQLParserException e) {
				logger.debug(viewName + ": " + e.getCause().getMessage(), e);
			}
		}

		logger.info("*** View analysis finish");
		long end = System.currentTimeMillis();
		logger.info("*** View analysis time: " + (end - start));
	}

	/**
	 * Parse an statement of type Create for the given view
	 * 
	 * @param viewName   The analyzed view
	 * @param createView The Create code
	 */
	private void parseCreateView(String viewName, CreateView createView) {
		if (createView.getSelect().getSelectBody() instanceof PlainSelect) {
			parseSelectBody(viewName, (PlainSelect) createView.getSelect().getSelectBody());
		} else if (createView.getSelect().getSelectBody() instanceof SetOperationList) {
			SetOperationList set = (SetOperationList) createView.getSelect().getSelectBody();
			for (SelectBody selectBody : set.getSelects()) {
				parseSelectBody(viewName, selectBody);
			}
		}
	}

	/**
	 * Parse an statement of type Select for the given view
	 * 
	 * @param viewName   The analyzed view
	 * @param selectBody The Select code
	 */
	private void parseSelectBody(String viewName, SelectBody selectBody) {
		if (selectBody instanceof PlainSelect) {
			parsePlainSelect(viewName, (PlainSelect) selectBody);
		} else if (selectBody instanceof SetOperationList) {
			SetOperationList set = (SetOperationList) selectBody;
			for (SelectBody select : set.getSelects()) {
				parseSelectBody(viewName, select);
			}
		}
	}

	/**
	 * Parse an statement of type Plain Select for the given view. At the end, the
	 * goal is to retrieve the list of tables and views that participate in the
	 * given view in any way: from or join
	 * 
	 * @param viewName    The analyzed view
	 * @param plainSelect The Plain Select code
	 */
	private void parsePlainSelect(String viewName, PlainSelect plainSelect) {
		if (!(plainSelect.getFromItem() instanceof Table)) {
			return;
		}

		Map<String, String> mapAliasToTable = new HashMap<>();
		Map<String, String> mapTableSql = new HashMap<>();
		Map<String, String> columnNameAliasMap = new HashMap<>();

		Table fromTable = (Table) plainSelect.getFromItem();
		String fromTableName = fromTable.getName().toLowerCase();
		String fromTableAlias = fromTable.getAlias() != null ? fromTable.getAlias().getName() : null;
		if (fromTableAlias != null) {
			mapAliasToTable.put(fromTableAlias.toLowerCase(), fromTableName);
		}
		String fromCode = "FROM " + plainSelect.getFromItem();
		mapTableSql.put(fromTableName, fromCode);
		addTable(fromTableName);
		addView(fromTableName, viewName);
		addTableOrderList(fromTableName, viewName);

		for (SelectItem si : plainSelect.getSelectItems()) {
			if (!(si instanceof SelectExpressionItem)) {
				continue;
			}

			SelectExpressionItem sei = (SelectExpressionItem) si;
			if (!(sei.getExpression() instanceof Column)) {
				continue;
			}

			Column c = (Column) sei.getExpression();
			if (c.getTable() == null || c.getTable().getName() == null
					|| c.getTable().getName().toLowerCase().equals(fromTableName)
					|| c.getTable().getName().equals(fromTableAlias)) {
				String name = c.getColumnName().toLowerCase();
				String alias = name;
				if (sei.getAlias() != null) {
					alias = sei.getAlias().getName().toLowerCase();
				}
				columnNameAliasMap.put(name, alias);
			}
		}

		if (plainSelect.getJoins() != null) {
			for (Join join : plainSelect.getJoins()) {
				if (!(join.getRightItem() instanceof Table)) {
					continue;
				}

				Table joinTable = (Table) join.getRightItem();
				String joinTableName = joinTable.getName().toLowerCase();
				String joinCode = join.toString();
				mapTableSql.put(joinTableName, joinCode);
				if (isTranslationTable(joinTableName)) {
					continue;
				}
				if (mapViewCode.containsKey(joinTableName)) {
					logger.info("*** View analysis (view using views): " + viewName + " => " + joinTableName);
				}

				String joinTableAlias = joinTable.getAlias() != null ? joinTable.getAlias().getName() : null;
				if (joinTableAlias != null) {
					mapAliasToTable.put(joinTableAlias.toLowerCase(), joinTableName);
				}
				addTable(joinTableName);
				addView(joinTableName, viewName);
				addTableOrderList(joinTableName, viewName);
				addTableOrder(joinTableName, viewName,
						new JoinTableDef(joinTableName, joinTableAlias, joinCode, columnNameAliasMap));

				EqualsTo eto = null;
				if (join.getOnExpression() instanceof EqualsTo) {
					eto = (EqualsTo) join.getOnExpression();
				} else if (join.getOnExpression() instanceof BinaryExpression) {
					BinaryExpression be = (BinaryExpression) join.getOnExpression();
					if (be.getLeftExpression() instanceof EqualsTo) {
						eto = (EqualsTo) be.getLeftExpression();
					} else if (be.getRightExpression() instanceof EqualsTo) {
						eto = (EqualsTo) be.getRightExpression();
					}
				}

				if (eto == null || !(eto.getLeftExpression() instanceof Column)
						|| !(eto.getRightExpression() instanceof Column)) {
					continue;
				}

				String leftColAlias = ((Column) eto.getLeftExpression()).getTable().getName();
				String rightColAlias = ((Column) eto.getRightExpression()).getTable().getName();

				if (leftColAlias == null || rightColAlias == null) {
					continue;
				}

				if (leftColAlias.equalsIgnoreCase(fromTableAlias != null ? fromTableAlias : fromTableName)
						|| rightColAlias.equalsIgnoreCase(fromTableAlias != null ? fromTableAlias : fromTableName)) {
					String relatedTableName = null;
					if (mapAliasToTable.containsKey(
							fromTableAlias != null ? fromTableAlias.toLowerCase() : fromTableName.toLowerCase())) {
						relatedTableName = mapAliasToTable.get(
								fromTableAlias != null ? fromTableAlias.toLowerCase() : fromTableName.toLowerCase());
					} else {
						relatedTableName = fromTableName;
					}
					addTableOrder(joinTableName, viewName, new JoinTableDef(relatedTableName, fromTableAlias,
							mapTableSql.get(relatedTableName), columnNameAliasMap));
				} else {
					String relatedTableName = null;
					if (leftColAlias.equalsIgnoreCase(joinTableAlias != null ? joinTableAlias : joinTableName)) {
						if (mapAliasToTable.containsKey(rightColAlias.toLowerCase())) {
							relatedTableName = mapAliasToTable.get(rightColAlias.toLowerCase());
						} else {
							relatedTableName = rightColAlias.toLowerCase();
						}
					} else if (rightColAlias
							.equalsIgnoreCase(joinTableAlias != null ? joinTableAlias : joinTableName)) {
						if (mapAliasToTable.containsKey(leftColAlias.toLowerCase())) {
							relatedTableName = mapAliasToTable.get(leftColAlias.toLowerCase());
						} else {
							relatedTableName = leftColAlias.toLowerCase();
						}
					}

					for (JoinTableDef relateds : getTableViewOrder(relatedTableName, viewName)) {
						addTableOrder(joinTableName, viewName, relateds);
					}
				}
			}
		}

		addTableOrder(fromTableName, viewName,
				new JoinTableDef(fromTableName, fromTableAlias, fromCode, columnNameAliasMap));
	}

	/**
	 * Get the SQL statement to retrieve all the views for the given database
	 * connection, depending on the database type
	 * 
	 * @param conn The connection
	 * @return The SQL statement
	 * @throws SQLException if the database is not recognized
	 */
	private String getViewsSql(Connection conn) throws SQLException {
		if (databaseUtils.isOracle()) {
			return getOracleViewsSql();
		} else if (databaseUtils.isSqlServer()) {
			return getSqlServerViewsSql();
		} else if (databaseUtils.isPostgreSql()) {
//			return getPostgreSqlViewsSql();
			throw new SQLException("No supported database PostgreSql");
		} else if (databaseUtils.isHyperSql()) {
			throw new SQLException("No supported database HyperSql");
		} else {
			throw new SQLException("No recognized database");
		}
	}

	/**
	 * Get the SQL statement to retrieve all the views in an Oracle database
	 * 
	 * @return The SQL statement
	 */
	private String getOracleViewsSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("select view_name, text ");
		sb.append("from SYS.USER_VIEWS");
		return sb.toString();
	}

	/**
	 * Get the SQL statement to retrieve all the views in an SqlServer database
	 * 
	 * @return The SQL statement
	 */
	private String getSqlServerViewsSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("select name, object_definition(object_id) as definition ");
		sb.append("from sys.objects ");
		sb.append("where type = 'V'");
		return sb.toString();
	}

	/**
	 * Get the SQL statement to retrieve all the views in a PostgreSql database
	 * 
	 * @return The SQL statement
	 */
	@SuppressWarnings("unused")
	private String getPostgreSqlViewsSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("select table_name, view_definition ");
		sb.append("from information_schema.views ");
		sb.append("where table_schema = any(current_schemas(false))");
		return sb.toString();
	}

	/**
	 * Check if the given table is a translation table or not
	 * 
	 * @param table The table to check
	 * @return true is it's a translation table; false if not
	 */
	private boolean isTranslationTable(String table) {
		for (String suffix : translation_suffixes) {
			if (table.endsWith(suffix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Add the given table to the list of analyzed tables
	 * 
	 * @param table The table to add
	 */
	private void addTable(String table) {
		if (!mapInfo.containsKey(table)) {
			mapInfo.put(table, new HashMap<>());
		}
	}

	/**
	 * Add the given view as participating view for the given table
	 * 
	 * @param table The table where the view is participating
	 * @param view  The view where the table participates
	 */
	private void addView(String table, String view) {
		if (!mapInfo.get(table).containsKey(view)) {
			mapInfo.get(table).put(view, new ArrayList<>());
		}
	}

	/**
	 * Initialize a new table order list
	 * 
	 * @param table
	 * @param view
	 */
	private void addTableOrderList(String table, String view) {
		mapInfo.get(table).get(view).add(new LinkedList<>());
	}

	/**
	 * Add a new tableOrder definition for given table and view
	 * 
	 * @param table      The table where the view is participating
	 * @param view       The view where the table participates
	 * @param tableOrder The tableOrder definition
	 */
	private void addTableOrder(String table, String view, JoinTableDef tableOrder) {
		List<LinkedList<JoinTableDef>> list = mapInfo.get(table).get(view);
		if (!list.get(list.size() - 1).contains(tableOrder)) {
			list.get(list.size() - 1).add(tableOrder);
		}
	}

	/**
	 * Get the ordered list of joins of the given table for the given view
	 * 
	 * @param table The table
	 * @param view  The view
	 * @return The ordered list of joins
	 */
	private LinkedList<JoinTableDef> getTableViewOrder(String table, String view) {
		List<LinkedList<JoinTableDef>> list = mapInfo.get(table).get(view);
		return list.get(list.size() - 1);
	}

	/**
	 * Support class for the information of the SQL Join of the Views in the
	 * database
	 * 
	 * @author Marc Gil - mgil@prodevelop.es
	 */
	public static class JoinTableDef {
		private String tableName;
		private String tableAlias;
		private String joinCode;
		private Map<String, String> nameAliasMap;

		public JoinTableDef(String tableName, String tableAlias, String joinCode, Map<String, String> nameAliasMap) {
			this.tableName = tableName;
			this.tableAlias = tableAlias;
			this.joinCode = joinCode;
			this.nameAliasMap = nameAliasMap;
		}

		/**
		 * Get the table name
		 * 
		 * @return The table name
		 */
		public String getTableName() {
			return tableName;
		}

		/**
		 * Get the alias of the table in the join (if has one)
		 * 
		 * @return The alias of the table in the join
		 */
		public String getTableAlias() {
			return tableAlias;
		}

		/**
		 * The SQL code of the join
		 * 
		 * @return The SQL code of the join
		 */
		public String getJoinCode() {
			return joinCode;
		}

		/**
		 * The map of name-alias of the columns of the view
		 * 
		 * @return A map of name-alias of the columns of the view
		 */
		public Map<String, String> getNameAliasMap() {
			return nameAliasMap;
		}

		@Override
		public String toString() {
			return joinCode;
		}
	}

}
