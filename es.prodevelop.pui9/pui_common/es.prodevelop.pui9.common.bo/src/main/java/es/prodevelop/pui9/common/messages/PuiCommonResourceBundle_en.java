package es.prodevelop.pui9.common.messages;

/**
 * English Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiCommonResourceBundle_en extends PuiCommonResourceBundle {

	@Override
	protected String getRequestResetPasswordSubject() {
		return "Reset password";
	}

	@Override
	protected String getAnonymousNotAllowedMessage_201() {
		return "Anonymous navigation is not allowed in the application";
	}

	@Override
	protected String getInvalidPasswordMessage_202() {
		return "Password doesn't fit the requirements";
	}

	@Override
	protected String getIncorrectUserPasswordMessage_203() {
		return "The given user or password are not valid";
	}

	@Override
	protected String getIncorrectLoginMessage_204() {
		return "Error when login the application: bad credentials";
	}

	@Override
	protected String getModelMessage_205() {
		return "The model ''{0}'' is not included in PUI_MODEL";
	}

	@Override
	protected String getNoFileMessage_206() {
		return "Cannot find the requested file";
	}

	@Override
	protected String getNoSessionMessage_207() {
		return "Doesn't exist a session for the given token";
	}

	@Override
	protected String getNotAllowedMessage_208() {
		return "Don't have permission";
	}

	@Override
	protected String getUserSessionTimeoutMessage_209() {
		return "The user session has ended";
	}

	@Override
	protected String getUserDisabledMessage_210() {
		return "User ''{0}'' is disabled";
	}

	@Override
	protected String getUserNotExistsMessage_211() {
		return "The user ''{0}'' doesn''t exist";
	}

	@Override
	protected String getUserResetTokenMessage_212() {
		return "Doesn't exist a user for the provided token";
	}

	@Override
	protected String getImportExportNoModelMessage_213() {
		return "The attribute 'model' was not specified";
	}

	@Override
	protected String getImportExportInvalidColumnMessage_214() {
		return "Could not export the column ''{0}'' because it doesn''t exist in the database";
	}

	@Override
	protected String getImportExportPkNotIncludedMessage_215() {
		return "PK columns not included in the export: ''{0}''";
	}

	@Override
	protected String getImportExportWithErrorsMessage_216() {
		return "The import cannot be executed because there are records with errors. Check it";
	}

	@Override
	protected String getImportExportDtoErrorMessage_217() {
		return "An error occurred when setting to the column ''{0}'' the value ''{1}''";
	}

	@Override
	protected String getImportExportInvalidModelErrorMessage_218() {
		return "The model ''{0}'' doesn''t support export/import action";
	}

	@Override
	protected String getCopyInvalidModelErrorMessage_219() {
		return "The model ''{0}'' doesn''t support copy registry action";
	}

}
