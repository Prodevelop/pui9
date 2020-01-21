package es.prodevelop.pui9.common.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiElasticsearchViewsDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViews;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiElasticsearchViewsPk;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;

/**
 * @generated
 */
@Repository
public class PuiElasticsearchViewsDao extends AbstractTableDao<IPuiElasticsearchViewsPk, IPuiElasticsearchViews>
		implements IPuiElasticsearchViewsDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiElasticsearchViews> findByAppname(String appname) throws PuiDaoFindException {
		return super.findByColumn(IPuiElasticsearchViewsPk.APPNAME_FIELD, appname);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiElasticsearchViews> findByViewname(String viewname) throws PuiDaoFindException {
		return super.findByColumn(IPuiElasticsearchViewsPk.VIEWNAME_FIELD, viewname);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiElasticsearchViews> findByIdentityfields(String identityfields)
			throws PuiDaoFindException {
		return super.findByColumn(IPuiElasticsearchViews.IDENTITY_FIELDS_FIELD, identityfields);
	}
}
