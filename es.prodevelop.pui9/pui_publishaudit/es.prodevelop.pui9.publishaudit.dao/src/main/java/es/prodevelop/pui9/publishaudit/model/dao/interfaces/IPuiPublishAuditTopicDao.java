package es.prodevelop.pui9.publishaudit.model.dao.interfaces;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditTopic;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditTopicPk;

/**
 * @generated
 */
public interface IPuiPublishAuditTopicDao extends ITableDao<IPuiPublishAuditTopicPk, IPuiPublishAuditTopic> {
	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditTopic> findById(Integer id) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditTopic> findByIdentity(Integer identity) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditTopic> findByOptype(String optype) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditTopic> findByCodtopic(String codtopic) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditTopic> findByEnabled(Integer enabled) throws PuiDaoFindException;

	/**
	 * @generated
	 */
	java.util.List<IPuiPublishAuditTopic> findByEventtype(Integer eventtype) throws PuiDaoFindException;
}
