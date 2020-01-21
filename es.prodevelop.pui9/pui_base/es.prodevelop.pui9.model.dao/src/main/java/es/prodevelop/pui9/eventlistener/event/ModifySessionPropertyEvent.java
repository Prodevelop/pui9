package es.prodevelop.pui9.eventlistener.event;

/**
 * Event for modifying a attribute of the session
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class ModifySessionPropertyEvent extends PuiEvent<String> {

	private static final long serialVersionUID = 1L;

	private String extraAttribute;
	private Object oldValue;
	private Object newValue;

	public ModifySessionPropertyEvent(String extraAttribute, Object oldValue, Object newValue) {
		super(extraAttribute, "modify_session_property");
		this.extraAttribute = extraAttribute;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public String getExtraAttribute() {
		return extraAttribute;
	}

	@SuppressWarnings("unchecked")
	public <T> T getOldValue() {
		return (T) oldValue;
	}

	@SuppressWarnings("unchecked")
	public <T> T getNewValue() {
		return (T) newValue;
	}

}
