package es.prodevelop.pui9.publishaudit.components;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.publishaudit.dto.PublishAuditOperationTypeEnum;
import es.prodevelop.pui9.publishaudit.dto.PublishEntity;
import es.prodevelop.pui9.publishaudit.dto.PublishField;
import es.prodevelop.pui9.publishaudit.dto.PublishFieldValue;
import es.prodevelop.pui9.publishaudit.dto.PublishTopic;
import es.prodevelop.pui9.publishaudit.model.dao.interfaces.IPuiPublishAuditEntityDao;
import es.prodevelop.pui9.publishaudit.model.dao.interfaces.IPuiPublishAuditFieldDao;
import es.prodevelop.pui9.publishaudit.model.dao.interfaces.IPuiPublishAuditFieldValueDao;
import es.prodevelop.pui9.publishaudit.model.dao.interfaces.IPuiPublishAuditTopicDao;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditEntity;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditField;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditFieldValue;
import es.prodevelop.pui9.publishaudit.model.dto.interfaces.IPuiPublishAuditTopic;
import es.prodevelop.pui9.utils.PuiConstants;

/**
 * Cache all the involved tables
 */
@Component
public class RefreshPublishAuditCacheUtil {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	protected IPuiPublishAuditEntityDao publishAuditEntityDao;

	@Autowired
	protected IPuiPublishAuditTopicDao publishAuditTopicDao;

	@Autowired
	protected IPuiPublishAuditFieldDao publishAuditFieldDao;

	@Autowired
	protected IPuiPublishAuditFieldValueDao publishAuditFieldValueDao;

	public void reloadPublishAudit(PublishAuditRegistry par) {
		logger.debug("Refreshing Publish/Audit cache");

		try {
			List<IPuiPublishAuditEntity> listEntities = publishAuditEntityDao.findAll();
			if (listEntities.isEmpty()) {
				return;
			}

			par.getMapPublish().clear();
			par.getAuditInsert().clear();
			par.getAuditUpdate().clear();
			par.getAuditDelete().clear();

			// load entities
			for (IPuiPublishAuditEntity pae : listEntities) {
				if (pae.getAuditinsert().equals(PuiConstants.TRUE_INT)) {
					par.getAuditInsert().add(pae.getEntity());
				}
				if (pae.getAuditupdate().equals(PuiConstants.TRUE_INT)) {
					par.getAuditUpdate().add(pae.getEntity());
				}
				if (pae.getAuditdelete().equals(PuiConstants.TRUE_INT)) {
					par.getAuditDelete().add(pae.getEntity());
				}

				PublishEntity pe = new PublishEntity(pae.getEntity());
				par.getMapPublish().put(pae.getEntity(), pe);

				// load topics
				FilterBuilder filterBuilder = FilterBuilder.newAndFilter()
						.addEquals(IPuiPublishAuditTopic.ID_ENTITY_COLUMN, pae.getId());
				List<IPuiPublishAuditTopic> listTopics = publishAuditTopicDao.findWhere(filterBuilder);
				if (listTopics.isEmpty()) {
					par.getMapPublish().remove(pae.getEntity());
					continue;
				}

				for (IPuiPublishAuditTopic pat : listTopics) {
					if (pat.getEnabled().equals(PuiConstants.FALSE_INT)) {
						continue;
					}

					PublishAuditOperationTypeEnum opType = PublishAuditOperationTypeEnum.valueOf(pat.getOptype());
					if (opType == null) {
						continue;
					}

					if (!pe.getMapOperationTopics().containsKey(opType)) {
						pe.getMapOperationTopics().put(opType, new ArrayList<PublishTopic>());
					}

					PublishTopic pt = new PublishTopic(opType, pat.getCodtopic(), pat.getEventtype());
					pe.getMapOperationTopics().get(opType).add(pt);

					// load fields
					filterBuilder = FilterBuilder.newAndFilter().addEquals(IPuiPublishAuditField.ID_TOPIC_COLUMN,
							pat.getId());

					List<IPuiPublishAuditField> listFields = publishAuditFieldDao.findWhere(filterBuilder);
					for (IPuiPublishAuditField paf : listFields) {
						if (!pt.getMapFieldnameField().containsKey(paf.getField())) {
							pt.getMapFieldnameField().put(paf.getField(), new ArrayList<PublishField>());
						}

						PublishField pf = new PublishField(paf.getField());
						pt.getMapFieldnameField().get(paf.getField()).add(pf);

						// load values
						filterBuilder = FilterBuilder.newAndFilter()
								.addEquals(IPuiPublishAuditFieldValue.ID_FIELD_COLUMN, paf.getId());

						List<IPuiPublishAuditFieldValue> listValues = publishAuditFieldValueDao
								.findWhere(filterBuilder);
						for (IPuiPublishAuditFieldValue pafv : listValues) {
							PublishFieldValue pfv = new PublishFieldValue(pafv.getOldvalue(), pafv.getNewvalue());
							pf.getValues().add(pfv);
						}
					}
				}
			}
		} catch (PuiDaoFindException e) {
			par.getMapPublish().clear();
		}
	}

}
