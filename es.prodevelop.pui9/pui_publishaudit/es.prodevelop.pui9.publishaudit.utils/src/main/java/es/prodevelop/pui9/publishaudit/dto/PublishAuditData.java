package es.prodevelop.pui9.publishaudit.dto;

import java.util.ArrayList;
import java.util.List;

import es.prodevelop.pui9.model.dto.interfaces.IDto;

public class PublishAuditData {

	private String tablename;
	private PublishAuditOperationTypeEnum type;
	private String topicCode;
	private Integer eventType;
	private IDto olddto;
	private IDto newdto;
	private List<AttributeData> attributes;

	public PublishAuditData(String tablename, PublishAuditOperationTypeEnum type, Integer eventType, IDto olddto,
			IDto dto) {
		this(tablename, type, null, eventType, olddto, dto);
	}

	public PublishAuditData(String tablename, PublishAuditOperationTypeEnum type, String topicCode, Integer eventType,
			IDto olddto, IDto newdto) {
		this.tablename = tablename;
		this.type = type;
		this.topicCode = topicCode;
		this.eventType = eventType;
		this.olddto = olddto;
		this.newdto = newdto;
		this.attributes = new ArrayList<>();
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public PublishAuditOperationTypeEnum getType() {
		return type;
	}

	public void setType(PublishAuditOperationTypeEnum type) {
		this.type = type;
	}

	public String getTopicCode() {
		return topicCode;
	}

	public void setTopicCode(String topicCode) {
		this.topicCode = topicCode;
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public IDto getOlddto() {
		return olddto;
	}

	public void setOlddto(IDto olddto) {
		this.olddto = olddto;
	}

	public IDto getNewdto() {
		return newdto;
	}

	public void setNewdto(IDto dto) {
		this.newdto = dto;
	}

	public List<AttributeData> getAttributes() {
		return attributes;
	}

	public void addAttribute(AttributeData attribute) {
		this.attributes.add(attribute);
	}

	public void setAttributes(List<AttributeData> attributes) {
		this.attributes = attributes;
	}

	public static class AttributeData {
		private String name;
		private Object oldvalue;
		private Object newvalue;

		public AttributeData(String name, Object oldvalue, Object newvalue) {
			super();
			this.name = name;
			this.oldvalue = oldvalue;
			this.newvalue = newvalue;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Object getOldvalue() {
			return oldvalue;
		}

		public void setOldvalue(Object oldvalue) {
			this.oldvalue = oldvalue;
		}

		public Object getNewvalue() {
			return newvalue;
		}

		public void setNewvalue(Object newvalue) {
			this.newvalue = newvalue;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			sb.append(" = {");
			sb.append(oldvalue);
			sb.append(" -> ");
			sb.append(newvalue);
			sb.append("}");

			return sb.toString();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(tablename);
		sb.append(", ");
		sb.append(type);
		sb.append(", ");
		sb.append(attributes);

		return sb.toString();
	}
}
