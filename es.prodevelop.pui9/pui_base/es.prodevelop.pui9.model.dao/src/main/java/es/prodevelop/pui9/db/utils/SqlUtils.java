package es.prodevelop.pui9.db.utils;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitorAdapter;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.FromItemVisitor;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.OrderByVisitor;
import net.sf.jsqlparser.statement.select.ParenthesisFromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.TableFunction;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.values.ValuesStatement;

/**
 * Utility class for processing an SQL statement and decorating it with all the
 * faulting aliases
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class SqlUtils {

	public static final String TABLE_PREFIX = "t1";
	public static final String TABLE_LANG_PREFIX = "t2";

	/**
	 * Try to adjust the SQL query in order to add the name of the tables that take
	 * part to the query in order to avoid problems of ambiguous columns that are
	 * present in multiple tables.
	 * 
	 * Visits all the SQL nodes and adds the table name to every column
	 * 
	 * @param dtoClass The type of the DTO
	 * @param query    The real query
	 * @return The modified query
	 */
	public static <T extends IDto> StringBuilder adjustSqlQuery(Class<T> dtoClass, StringBuilder query) {
		try {
			CCJSqlParserManager pm = new CCJSqlParserManager();
			Statement stm = pm.parse(new StringReader(query.toString()));
			if (!(stm instanceof Select)) {
				return query;
			}

			Select select = (Select) stm;
			StatementModifierVisitor smv = new StatementModifierVisitor(dtoClass);
			select.accept(smv);

			return new StringBuilder(stm.toString());
		} catch (Exception e) {
			return query;
		}
	}

	/**
	 * Based on {@link net.sf.jsqlparser.util.deparser.StatementDeParser} class
	 */
	private static class StatementModifierVisitor extends StatementVisitorAdapter {

		private Class<? extends IDto> dtoClass;

		public StatementModifierVisitor(Class<? extends IDto> dtoClass) {
			this.dtoClass = dtoClass;
		}

		@Override
		public void visit(Select select) {
			SelectModifierVisitor smv = new SelectModifierVisitor(dtoClass);
			select.getSelectBody().accept(smv);
		}

	}

	/**
	 * Based on {@link net.sf.jsqlparser.util.deparser.SelectDeParser} class
	 */
	private static class SelectModifierVisitor
			implements SelectVisitor, OrderByVisitor, SelectItemVisitor, FromItemVisitor {
		private ExpressionVisitor expressionVisitor;

		public SelectModifierVisitor(Class<? extends IDto> dtoClass) {
			this.expressionVisitor = new ExpressionModifierVisitor(this, dtoClass);
		}

		@Override
		public void visit(SubSelect subSelect) {
			subSelect.getSelectBody().accept(this);
		}

		@Override
		public void visit(SelectExpressionItem selectExpressionItem) {
			selectExpressionItem.getExpression().accept(expressionVisitor);
		}

		@Override
		public void visit(SubJoin subjoin) {
			subjoin.getLeft().accept(this);
			for (Join join : subjoin.getJoinList()) {
				FromItem fromItem = join.getRightItem();
				fromItem.accept(this);
				if (join.getOnExpression() != null) {
					join.getOnExpression().accept(expressionVisitor);
				}
			}
		}

		@Override
		public void visit(OrderByElement orderBy) {
			orderBy.getExpression().accept(expressionVisitor);
		}

		@Override
		public void visit(PlainSelect plainSelect) {
			if (plainSelect.getDistinct() != null) {
				if (plainSelect.getDistinct().getOnSelectItems() != null) {
					for (Iterator<?> iter = plainSelect.getDistinct().getOnSelectItems().iterator(); iter.hasNext();) {
						SelectItem selectItem = (SelectItem) iter.next();
						selectItem.accept(this);
					}
				}

			}

			for (Iterator<?> iter = plainSelect.getSelectItems().iterator(); iter.hasNext();) {
				SelectItem selectItem = (SelectItem) iter.next();
				selectItem.accept(this);
			}

			if (plainSelect.getFromItem() != null) {
				plainSelect.getFromItem().accept(this);
			}

			if (plainSelect.getJoins() != null) {
				for (Iterator<?> iter = plainSelect.getJoins().iterator(); iter.hasNext();) {
					Join join = (Join) iter.next();
					FromItem fromItem = join.getRightItem();
					fromItem.accept(this);
					if (join.getOnExpression() != null) {
						join.getOnExpression().accept(expressionVisitor);
					}
				}
			}

			if (plainSelect.getWhere() != null) {
				plainSelect.getWhere().accept(expressionVisitor);
			}

			if (plainSelect.getGroupBy() != null && plainSelect.getGroupBy().getGroupByExpressions() != null) {
				for (Iterator<?> iter = plainSelect.getGroupBy().getGroupByExpressions().iterator(); iter.hasNext();) {
					Expression columnReference = (Expression) iter.next();
					columnReference.accept(expressionVisitor);
				}
			}

			if (plainSelect.getHaving() != null) {
				plainSelect.getHaving().accept(expressionVisitor);
			}

			if (plainSelect.getOrderByElements() != null) {
				for (OrderByElement obe : plainSelect.getOrderByElements()) {
					obe.accept(this);
				}
			}
		}

		@Override
		public void visit(SetOperationList setOpList) {
			if (setOpList.getSelects() != null) {
				for (SelectBody sb : setOpList.getSelects()) {
					if (sb instanceof PlainSelect) {
						sb.accept(this);
					}
				}
			}

			if (setOpList.getOrderByElements() != null) {
				for (OrderByElement obe : setOpList.getOrderByElements()) {
					obe.accept(this);
				}
			}
		}

		@Override
		public void visit(Table tableName) {
		}

		@Override
		public void visit(AllColumns allColumns) {
		}

		@Override
		public void visit(AllTableColumns allTableColumns) {
		}

		@Override
		public void visit(LateralSubSelect lateralSubSelect) {
		}

		@Override
		public void visit(ValuesList valuesList) {
		}

		@Override
		public void visit(TableFunction tableFunction) {
		}

		@Override
		public void visit(WithItem withItem) {
		}

		@Override
		public void visit(ParenthesisFromItem aThis) {
		}

		@Override
		public void visit(ValuesStatement aThis) {
		}

	}

	/**
	 * Based on {@link net.sf.jsqlparser.util.deparser.ExpressionDeParser} class
	 */
	private static class ExpressionModifierVisitor extends ExpressionVisitorAdapter {
		private SelectVisitor selectVisitor;
		private List<String> list;
		private List<String> langList;

		public ExpressionModifierVisitor(SelectVisitor selectVisitor, Class<? extends IDto> dtoClass) {
			this.selectVisitor = selectVisitor;
			this.list = DtoRegistry.getColumnNames(dtoClass);
			this.langList = DtoRegistry.getLangColumnNames(dtoClass);
		}

		/**
		 * Add the name of the tables to every column, if it is not set
		 */
		@Override
		public void visit(Column tableColumn) {
			Table table = tableColumn.getTable();
			if (StringUtils.isEmpty(table.getFullyQualifiedName())) {
				if (list.contains(tableColumn.getColumnName())) {
					table.setName(TABLE_PREFIX);
				} else if (langList.contains(tableColumn.getColumnName())) {
					table.setName(TABLE_LANG_PREFIX);
				}
			}
		}

		@Override
		public void visit(SubSelect subSelect) {
			subSelect.getSelectBody().accept(selectVisitor);
		}

	}
}
