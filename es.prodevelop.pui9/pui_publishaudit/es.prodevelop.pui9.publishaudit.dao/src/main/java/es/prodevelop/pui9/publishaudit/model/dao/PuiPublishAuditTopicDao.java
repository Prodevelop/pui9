package es.prodevelop.pui9.publishaudit.model.dao;

import org.springframework.stereotype.Repository;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.AbstractTableDao;
import es.prodevelop.pui9.publishaudit.model.dao.interfaces.IPuiPublishAuditTopicDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditTopic;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditTopicPk;

/**
 * @generated
 */
@Repository
public class PuiPublishAuditTopicDao extends AbstractTableDao<IPuiPublishAuditTopicPk, IPuiPublishAuditTopic>
		implements IPuiPublishAuditTopicDao {
	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditTopic> findById(Integer id) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditTopicPk.ID_FIELD, id);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditTopic> findByIdentity(Integer identity) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditTopic.ID_ENTITY_FIELD, identity);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditTopic> findByOptype(String optype) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditTopic.OP_TYPE_FIELD, optype);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditTopic> findByCodtopic(String codtopic) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditTopic.COD_TOPIC_FIELD, codtopic);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditTopic> findByEnabled(Integer enabled) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditTopic.ENABLED_FIELD, enabled);
	}

	/**
	 * @generated
	 */
	@Override
	public java.util.List<IPuiPublishAuditTopic> findByEventtype(Integer eventtype) throws PuiDaoFindException {
		return super.findByColumn(IPuiPublishAuditTopic.EVENT_TYPE_FIELD, eventtype);
	}
}
