package _artifactid_.messages;

import es.prodevelop.pui9.messages.AbstractPuiMessages;

public class _id_Messages extends AbstractPuiMessages {

	private static _id_Messages singleton;

	public static _id_Messages getSingleton() {
		if (singleton == null) {
			singleton = new _id_Messages();
		}
		return singleton;
	}

	private _id_Messages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return _id_ResourceBundle.class;
	}

}
