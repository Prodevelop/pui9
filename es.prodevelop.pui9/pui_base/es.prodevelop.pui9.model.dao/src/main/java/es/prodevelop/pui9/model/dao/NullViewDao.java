package es.prodevelop.pui9.model.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * A Null VDao used when no View is associated with a Table
 */
@Repository
public class NullViewDao extends AbstractViewDao<INullView> implements INullViewDao {

	@Override
	public Long count(String column, boolean distinct, FilterBuilder filterBuilder) throws PuiDaoCountException {
		return -1L;
	}

	@Override
	public <N extends Number> N getMaxValue(String columnName, FilterBuilder filterBuilder) {
		return null;
	}

	@Override
	protected List<INullView> doFindWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder,
			PuiLanguage language) throws PuiDaoFindException {
		return Collections.emptyList();
	}

	@Override
	public SearchResponse<INullView> findForDataGrid(SearchRequest req) throws PuiDaoListException {
		return null;
	}

}
