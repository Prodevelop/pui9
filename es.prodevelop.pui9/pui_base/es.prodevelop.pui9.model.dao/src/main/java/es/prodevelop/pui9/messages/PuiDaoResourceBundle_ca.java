package es.prodevelop.pui9.messages;

/**
 * Catalan Translation for PUI DAO component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoResourceBundle_ca extends PuiDaoResourceBundle {

	@Override
	protected String getAttributeLengthMessage_101() {
		return "La propietat ''{0}'' té una longitud màxima de {1} caràcter(s), i el valor proporcionat té un total de {2} caràcter(s)";
	}

	@Override
	protected String getCountMessage_102() {
		return "Error al realitzar l'operació de contar número de registres";
	}

	@Override
	protected String getDataAccessMessage_103() {
		return "S''ha produït un error accedint a les dades: {0}";
	}

	@Override
	protected String getDuplicatedMessage_104() {
		return "Registre duplicat";
	}

	@Override
	protected String getFindErrorMessage_105() {
		return "Error al realitzar l'operació de búsqueda";
	}

	@Override
	protected String getIntegrityOnDeleteMessage_106() {
		return "El registre no es pot esborrar perquè té dades relacionades";
	}

	@Override
	protected String getIntegrityOnInsertMessage_107() {
		return "El registre no es pot inserir per errors en dades relacionades";
	}

	@Override
	protected String getIntegrityOnUpdateMessage_108() {
		return "El registre no es pot actualitzar per errors en dades relacionedes";
	}

	@Override
	protected String getListMessage_109() {
		return "Error al realitzar l'operació de llistat";
	}

	@Override
	protected String getNullParametersMessage_110() {
		return "La propietat ''{0}'' no pot ser nul·la";
	}

	@Override
	protected String getNoNumericExceptionMessage_111() {
		return "La columna ''{0}'' ha de ser de tipus numèric";
	}
	
	@Override
	protected String getNotExistsExceptionMessage_112() {
		return "No existeix el registre";
	}

}
