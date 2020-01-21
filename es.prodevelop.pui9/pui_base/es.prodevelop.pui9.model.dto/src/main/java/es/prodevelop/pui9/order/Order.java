package es.prodevelop.pui9.order;

import es.prodevelop.pui9.utils.IPuiObject;

public class Order implements IPuiObject {

	private static final long serialVersionUID = 1L;

	private String column;
	private OrderDirection direction = OrderDirection.asc;

	public static Order newOrderAsc(String column) {
		return newOrder(column, OrderDirection.asc);
	}

	public static Order newOrderDesc(String column) {
		return newOrder(column, OrderDirection.desc);
	}

	public static Order newOrder(String column, OrderDirection direction) {
		return new Order(column, direction != null ? direction : OrderDirection.asc);
	}

	public Order() {
	}

	public Order(String column) {
		this(column, OrderDirection.asc);
	}

	public Order(String column, OrderDirection direction) {
		this.column = column;
		this.direction = direction;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public OrderDirection getDirection() {
		return direction;
	}

	public void setDirection(OrderDirection direction) {
		this.direction = direction;
	}

	@Override
	public String toString() {
		return column + " " + direction;
	}

}
