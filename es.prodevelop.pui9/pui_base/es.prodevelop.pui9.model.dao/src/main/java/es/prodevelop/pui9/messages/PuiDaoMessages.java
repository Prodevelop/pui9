package es.prodevelop.pui9.messages;

/**
 * Utility class to get internationalized messages for PUI DAO
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoMessages extends AbstractPuiMessages {

	private static PuiDaoMessages singleton;

	public static PuiDaoMessages getSingleton() {
		if (singleton == null) {
			singleton = new PuiDaoMessages();
		}
		return singleton;
	}

	private PuiDaoMessages() {
	}

	@Override
	protected Class<?> getResourceBundleClass() {
		return PuiDaoResourceBundle.class;
	}

}
