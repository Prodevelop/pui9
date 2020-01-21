package es.prodevelop.pui9.order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utility class to facilitate creating Order clauses. Preferably use column
 * names insteda of field names
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class OrderBuilder {

	protected List<Order> orders = new ArrayList<>();

	public static OrderBuilder newOrder() {
		return new OrderBuilder();
	}

	public static OrderBuilder newOrder(Order order) {
		return new OrderBuilder().addOrder(order);
	}

	private OrderBuilder() {
		orders = new ArrayList<>();
	}

	/**
	 * Add an Order By with specified order: ASC or DESC. ASC by default is not
	 * specified
	 */
	public OrderBuilder addOrder(Order order) {
		orders.add(order);
		return this;
	}

	public List<Order> getOrders() {
		return orders;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (Iterator<Order> it = orders.iterator(); it.hasNext();) {
			sb.append(it.next());
			if (it.hasNext()) {
				sb.append(", ");
			}
		}

		return sb.toString();
	}

}