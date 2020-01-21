package es.prodevelop.pui9.db.helpers;

import java.util.List;
import java.util.Map;

import es.prodevelop.pui9.filter.FilterGroup;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * This interface database helper is intended to be used in the database
 * searches of JDBC approach. It provides useful methods valid for all the
 * database vendors.
 * <p>
 * Specific vendor implementations may be used in the most cases
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public interface IDatabaseHelper {

	String processSearchText(Class<? extends IDto> dtoClass, List<String> fields, String text);

	String processSearchText(Class<? extends IDto> dtoClass, Map<String, String> fieldTextMap);

	/**
	 * Generate the String SQL from the given Filters DTO
	 * 
	 * @param tableDto The DTO class of the search
	 * @param filters  The Filters
	 * @param addAlias Add the alias to the tables
	 * @return The SQL of the given filters
	 */
	String processFilters(Class<? extends IDto> tableDto, FilterGroup filters, boolean addAlias);

	/**
	 * Returns the real SQL that will be executed for pagination
	 * 
	 * @param page  The page to retrieve (starting with 0)
	 * @param size  The number of registries to retrieve
	 * @param query The real query
	 * @return The modified query for pagination
	 */
	String getSqlForPagination(int page, int size, String query);
}
