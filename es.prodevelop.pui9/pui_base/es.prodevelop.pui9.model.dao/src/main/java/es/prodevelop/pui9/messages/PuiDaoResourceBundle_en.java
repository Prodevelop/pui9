package es.prodevelop.pui9.messages;

/**
 * English Translation for PUI DAO component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoResourceBundle_en extends PuiDaoResourceBundle {

	@Override
	protected String getAttributeLengthMessage_101() {
		return "The maximum length of the attribute ''{0}'' is {1} character(e), and the value has {2} character(s)";
	}

	@Override
	protected String getCountMessage_102() {
		return "Error while performing the count number of registers operation";
	}

	@Override
	protected String getDataAccessMessage_103() {
		return "An error occurred while accessing to the data: {0}";
	}

	@Override
	protected String getDuplicatedMessage_104() {
		return "Duplicated registry";
	}

	@Override
	protected String getFindErrorMessage_105() {
		return "Error while performing the search operation";
	}

	@Override
	protected String getIntegrityOnDeleteMessage_106() {
		return "The registry cannot be deleted because it has related data";
	}

	@Override
	protected String getIntegrityOnInsertMessage_107() {
		return "The registry cannot be inserted due to errors in the related data";
	}

	@Override
	protected String getIntegrityOnUpdateMessage_108() {
		return "The registry cannot be updated due to errors in the related data";
	}

	@Override
	protected String getListMessage_109() {
		return "Error while performing the list operation";
	}

	@Override
	protected String getNullParametersMessage_110() {
		return "The attribute ''{0}'' cannot be null";
	}

	@Override
	protected String getNoNumericExceptionMessage_111() {
		return "The column ''{0}'' should be numeric";
	}
	
	@Override
	protected String getNotExistsExceptionMessage_112() {
		return "Record doesn't exist";
	}

}
