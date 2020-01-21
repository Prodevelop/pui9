package es.prodevelop.pui9.common.messages;

import java.util.HashMap;
import java.util.Map;

import es.prodevelop.pui9.common.exceptions.PuiCommonAnonymousNotAllowedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonCopyInvalidModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportDtoColumnErrorException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidColumnException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportNoModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportPkNotIncludedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportWithErrorsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectLoginException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectUserPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonInvalidPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoFileException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoSessionException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNotAllowedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserDisabledException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserResetTokenException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserSessionTimeoutException;
import es.prodevelop.pui9.messages.AbstractPuiListResourceBundle;

/**
 * More specific implementation of {@link AbstractPuiListResourceBundle} for PUI
 * Common component
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class PuiCommonResourceBundle extends AbstractPuiListResourceBundle {

	public static final String requestResetPasswordSubject = "requestResetPasswordSubject";

	@Override
	protected Map<Object, String> getMessages() {
		Map<Object, String> messages = new HashMap<>();

		// messages
		messages.put(requestResetPasswordSubject, getRequestResetPasswordSubject());

		// Exceptions
		messages.put(PuiCommonAnonymousNotAllowedException.CODE, getAnonymousNotAllowedMessage_201());
		messages.put(PuiCommonInvalidPasswordException.CODE, getInvalidPasswordMessage_202());
		messages.put(PuiCommonIncorrectUserPasswordException.CODE, getIncorrectUserPasswordMessage_203());
		messages.put(PuiCommonIncorrectLoginException.CODE, getIncorrectLoginMessage_204());
		messages.put(PuiCommonModelException.CODE, getModelMessage_205());
		messages.put(PuiCommonNoFileException.CODE, getNoFileMessage_206());
		messages.put(PuiCommonNoSessionException.CODE, getNoSessionMessage_207());
		messages.put(PuiCommonNotAllowedException.CODE, getNotAllowedMessage_208());
		messages.put(PuiCommonUserSessionTimeoutException.CODE, getUserSessionTimeoutMessage_209());
		messages.put(PuiCommonUserDisabledException.CODE, getUserDisabledMessage_210());
		messages.put(PuiCommonUserNotExistsException.CODE, getUserNotExistsMessage_211());
		messages.put(PuiCommonUserResetTokenException.CODE, getUserResetTokenMessage_212());
		messages.put(PuiCommonImportExportNoModelException.CODE, getImportExportNoModelMessage_213());
		messages.put(PuiCommonImportExportInvalidColumnException.CODE, getImportExportInvalidColumnMessage_214());
		messages.put(PuiCommonImportExportPkNotIncludedException.CODE, getImportExportPkNotIncludedMessage_215());
		messages.put(PuiCommonImportExportWithErrorsException.CODE, getImportExportWithErrorsMessage_216());
		messages.put(PuiCommonImportExportDtoColumnErrorException.CODE, getImportExportDtoErrorMessage_217());
		messages.put(PuiCommonImportExportInvalidModelException.CODE, getImportExportInvalidModelErrorMessage_218());
		messages.put(PuiCommonCopyInvalidModelException.CODE, getCopyInvalidModelErrorMessage_219());

		return messages;
	}

	protected abstract String getRequestResetPasswordSubject();

	protected abstract String getAnonymousNotAllowedMessage_201();

	protected abstract String getInvalidPasswordMessage_202();

	protected abstract String getIncorrectUserPasswordMessage_203();

	protected abstract String getIncorrectLoginMessage_204();

	protected abstract String getModelMessage_205();

	protected abstract String getNoFileMessage_206();

	protected abstract String getNoSessionMessage_207();

	protected abstract String getNotAllowedMessage_208();

	protected abstract String getUserSessionTimeoutMessage_209();

	protected abstract String getUserDisabledMessage_210();

	protected abstract String getUserNotExistsMessage_211();

	protected abstract String getUserResetTokenMessage_212();

	protected abstract String getImportExportNoModelMessage_213();

	protected abstract String getImportExportInvalidColumnMessage_214();

	protected abstract String getImportExportPkNotIncludedMessage_215();

	protected abstract String getImportExportWithErrorsMessage_216();

	protected abstract String getImportExportDtoErrorMessage_217();

	protected abstract String getImportExportInvalidModelErrorMessage_218();

	protected abstract String getCopyInvalidModelErrorMessage_219();

}
