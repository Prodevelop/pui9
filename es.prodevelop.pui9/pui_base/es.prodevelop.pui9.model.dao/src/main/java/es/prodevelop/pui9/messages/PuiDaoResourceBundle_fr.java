package es.prodevelop.pui9.messages;

/**
 * French Translation for PUI DAO component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDaoResourceBundle_fr extends PuiDaoResourceBundle {

	@Override
	protected String getAttributeLengthMessage_101() {
		return "L''attribut ''{0}'' a une longueur maximale de {1} caractère (s), et sa valeur a {2} caractère (s)";
	}

	@Override
	protected String getCountMessage_102() {
		return "Erreur opératoire au comptage du nombre de registres";
	}

	@Override
	protected String getDataAccessMessage_103() {
		return "Une erreur est survenue lors de l''accès aux données: {0}";
	}

	@Override
	protected String getDuplicatedMessage_104() {
		return "Registre doublé";
	}

	@Override
	protected String getFindErrorMessage_105() {
		return "Erreur opératoire de recherche";
	}

	@Override
	protected String getIntegrityOnDeleteMessage_106() {
		return "Le registre ne peut pas être supprimé car il a des données connexes";
	}

	@Override
	protected String getIntegrityOnInsertMessage_107() {
		return "Le registre ne peut pas être inséré à cause des erreurs aux données liées";
	}

	@Override
	protected String getIntegrityOnUpdateMessage_108() {
		return "Le registre ne peut pas être mis à jour par des erreurs aux données liées";
	}

	@Override
	protected String getListMessage_109() {
		return "Erreur opératoire en effectuant la liste";
	}

	@Override
	protected String getNullParametersMessage_110() {
		return "L''attribut ''{0}'' ne peut pas être nulle";
	}

	@Override
	protected String getNoNumericExceptionMessage_111() {
		return "La colonne ''{0}'' doit être de type numérique";
	}
	
	@Override
	protected String getNotExistsExceptionMessage_112() {
		return "Il n'y a pas de dossier";
	}

}
