package es.prodevelop.pui9.publishaudit.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishTopic {

	private PublishAuditOperationTypeEnum operationtype;
	private String topiccode;
	private Integer eventtype;
	private Map<String, List<PublishField>> mapFieldnameField;

	public PublishTopic(PublishAuditOperationTypeEnum operationtype, String topiccode, Integer eventtype) {
		this.operationtype = operationtype;
		this.topiccode = topiccode;
		this.eventtype = eventtype;
		this.mapFieldnameField = new HashMap<>();
	}

	public PublishAuditOperationTypeEnum getOperationtype() {
		return operationtype;
	}

	public String getTopiccode() {
		return topiccode;
	}

	public Integer getEventtype() {
		return eventtype;
	}

	public Map<String, List<PublishField>> getMapFieldnameField() {
		return mapFieldnameField;
	}

}
