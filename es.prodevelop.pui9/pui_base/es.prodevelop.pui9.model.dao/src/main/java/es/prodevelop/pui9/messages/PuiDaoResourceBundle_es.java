package es.prodevelop.pui9.messages;

/**
 * Spanish Translation for PUI DAO component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoResourceBundle_es extends PuiDaoResourceBundle {

	@Override
	protected String getAttributeLengthMessage_101() {
		return "El atributo ''{0}'' tiene una longitud máxima de {1} caracter(es), y su valor tiene {2} caracter(es)";
	}

	@Override
	protected String getCountMessage_102() {
		return "Error al realizar la operación de contar el número de registros";
	}

	@Override
	protected String getDataAccessMessage_103() {
		return "Se ha producido un error al acceder a los datos: {0}";
	}

	@Override
	protected String getDuplicatedMessage_104() {
		return "Registro duplicado";
	}

	@Override
	protected String getFindErrorMessage_105() {
		return "Error al realizar la operación de búsqueda";
	}

	@Override
	protected String getIntegrityOnDeleteMessage_106() {
		return "El registro no puede borrarse porque tiene datos relacionados";
	}

	@Override
	protected String getIntegrityOnInsertMessage_107() {
		return "El registro no se puede insertar por errores en datos relacionados";
	}

	@Override
	protected String getIntegrityOnUpdateMessage_108() {
		return "El registro no se puede actualizar por errores en datos relacionados";
	}

	@Override
	protected String getListMessage_109() {
		return "Error al realizar la operación de listado";
	}

	@Override
	protected String getNullParametersMessage_110() {
		return "El atributo ''{0}'' no puede ser nulo";
	}

	@Override
	protected String getNoNumericExceptionMessage_111() {
		return "La columna ''{0}'' debe ser de tipo numérico";
	}
	
	@Override
	protected String getNotExistsExceptionMessage_112() {
		return "No existe el registro";
	}

}
