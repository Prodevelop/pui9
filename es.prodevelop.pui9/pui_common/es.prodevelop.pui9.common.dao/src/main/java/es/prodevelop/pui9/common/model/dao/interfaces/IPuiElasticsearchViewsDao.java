package es.prodevelop.pui9.common.model.dao.interfaces;

import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViews;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViewsPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;

public interface IPuiElasticsearchViewsDao extends ITableDao<IPuiElasticsearchViewsPk, IPuiElasticsearchViews> {
	java.util.List<IPuiElasticsearchViews> findByAppname(String appname) throws PuiDaoFindException;

	java.util.List<IPuiElasticsearchViews> findByViewname(String viewname) throws PuiDaoFindException;

	java.util.List<IPuiElasticsearchViews> findByIdentityfields(String identityfields) throws PuiDaoFindException;
}
