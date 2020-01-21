package es.prodevelop.pui9.common.messages;

/**
 * Spanish Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiCommonResourceBundle_es extends PuiCommonResourceBundle {

	@Override
	protected String getRequestResetPasswordSubject() {
		return "Restablecer contraseña";
	}

	@Override
	protected String getAnonymousNotAllowedMessage_201() {
		return "No se permite la navegación anónima en la aplicación";
	}

	@Override
	protected String getInvalidPasswordMessage_202() {
		return "El password no cumple con los requisitos";
	}

	@Override
	protected String getIncorrectUserPasswordMessage_203() {
		return "El usuario o el password suministrados son incorrectos";
	}

	@Override
	protected String getIncorrectLoginMessage_204() {
		return "Error al hacer login: datos de acceso incorrectos";
	}

	@Override
	protected String getModelMessage_205() {
		return "El modelo ''{0}'' no está incluido en PUI_MODEL";
	}

	@Override
	protected String getNoFileMessage_206() {
		return "No se encuentra el fichero solicitado";
	}

	@Override
	protected String getNoSessionMessage_207() {
		return "No existe una sesión para el token proporcionado";
	}

	@Override
	protected String getNotAllowedMessage_208() {
		return "No dispone de suficientes permisos";
	}

	@Override
	protected String getUserSessionTimeoutMessage_209() {
		return "La sesión de usuario ha caducado";
	}

	@Override
	protected String getUserDisabledMessage_210() {
		return "El usuario ''{0}'' está deshabilitado";
	}

	@Override
	protected String getUserNotExistsMessage_211() {
		return "El usuario ''{0}'' no existe";
	}

	@Override
	protected String getUserResetTokenMessage_212() {
		return "No existe un usuario para el token proporcionado";
	}

	@Override
	protected String getImportExportNoModelMessage_213() {
		return "No se ha especificado el atributo 'model'";
	}

	@Override
	protected String getImportExportInvalidColumnMessage_214() {
		return "La columna ''{0}'' no se puede exportar porque no existe en la tabla de base de datos";
	}

	@Override
	protected String getImportExportPkNotIncludedMessage_215() {
		return "No se ha incluido las columnas PK en la exportación: ''{0}''";
	}

	@Override
	protected String getImportExportWithErrorsMessage_216() {
		return "La importación no se puede ejecutar porque hay registros con errores. Revísela";
	}

	@Override
	protected String getImportExportDtoErrorMessage_217() {
		return "Se ha producido un error al establecer a la columna ''{0}'' el valor ''{1}''";
	}

	@Override
	protected String getImportExportInvalidModelErrorMessage_218() {
		return "El modelo ''{0}'' no soporta la acción de exportar/importar";
	}

	@Override
	protected String getCopyInvalidModelErrorMessage_219() {
		return "El modelo ''{0}'' no soporta la acción de copiar registro";
	}

}
