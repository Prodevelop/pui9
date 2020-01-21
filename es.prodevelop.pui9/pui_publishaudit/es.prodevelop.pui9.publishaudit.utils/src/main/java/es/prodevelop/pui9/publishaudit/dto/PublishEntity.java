package es.prodevelop.pui9.publishaudit.dto;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PublishEntity {

	private String entityname;
	private Map<PublishAuditOperationTypeEnum, List<PublishTopic>> mapOperationTopics;

	public PublishEntity(String entityname) {
		this.entityname = entityname;
		this.mapOperationTopics = new EnumMap<>(PublishAuditOperationTypeEnum.class);
	}

	public String getEntityname() {
		return entityname;
	}

	public Map<PublishAuditOperationTypeEnum, List<PublishTopic>> getMapOperationTopics() {
		return mapOperationTopics;
	}

}
