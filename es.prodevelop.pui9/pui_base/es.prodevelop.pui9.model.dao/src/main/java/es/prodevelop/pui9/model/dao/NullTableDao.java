package es.prodevelop.pui9.model.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.exceptions.PuiDaoCountException;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.model.dao.interfaces.INullTableDao;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * A Null VDao used when no View is associated with a Table
 */
@Repository
public class NullTableDao extends AbstractTableDao<INullTablePk, INullTable> implements INullTableDao {

	@Override
	public Long count(String column, boolean distinct, FilterBuilder filterBuilder) throws PuiDaoCountException {
		return -1L;
	}

	@Override
	public <N extends Number> N getMaxValue(String columnName, FilterBuilder filterBuilder) {
		return null;
	}

	@Override
	protected List<INullTable> doFindWhere(FilterBuilder filterBuilder, OrderBuilder orderBuilder,
			PuiLanguage language) throws PuiDaoFindException {
		return Collections.emptyList();
	}

}
