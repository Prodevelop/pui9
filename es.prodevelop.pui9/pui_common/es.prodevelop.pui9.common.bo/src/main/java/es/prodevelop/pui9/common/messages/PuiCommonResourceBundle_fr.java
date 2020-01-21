package es.prodevelop.pui9.common.messages;

/**
 * French Translation for PUI Common component messages
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiCommonResourceBundle_fr extends PuiCommonResourceBundle {

	@Override
	protected String getRequestResetPasswordSubject() {
		return "Réinitialiser le mot de passe";
	}

	@Override
	protected String getAnonymousNotAllowedMessage_201() {
		return "La navigation anonyme n'est pas autorisée dans l'application";
	}

	@Override
	protected String getInvalidPasswordMessage_202() {
		return "Le mot de passe ne correspond pas aux exigences";
	}

	@Override
	protected String getIncorrectUserPasswordMessage_203() {
		return "L'utilisateur ou le mot de passe fournis sont incorrectes";
	}

	@Override
	protected String getIncorrectLoginMessage_204() {
		return "Erreur lors de la connexion à l'application : mauvaises informations d'identification";
	}

	@Override
	protected String getModelMessage_205() {
		return "La vue ''{0}'' n''est pas inclue dans la table PUI_MODEL";
	}

	@Override
	protected String getNoFileMessage_206() {
		return "Fichier introuvable";
	}

	@Override
	protected String getNoSessionMessage_207() {
		return "Aucune session existe pour le token fourni";
	}

	@Override
	protected String getNotAllowedMessage_208() {
		return "Vous ne disposez pas de permis suffisants";
	}

	@Override
	protected String getUserSessionTimeoutMessage_209() {
		return "La session de l'utilisateur a expiré";
	}

	@Override
	protected String getUserDisabledMessage_210() {
		return "Utilisateur ''{0}'' est désactivé";
	}

	@Override
	protected String getUserNotExistsMessage_211() {
		return "L''utilisateur ''{0}'' n''existe pas";
	}

	@Override
	protected String getUserResetTokenMessage_212() {
		return "N'existe pas d'utilisateur pour le jeton fourni";
	}

	@Override
	protected String getImportExportNoModelMessage_213() {
		return "Attribut 'model' non spécifié";
	}

	@Override
	protected String getImportExportInvalidColumnMessage_214() {
		return "Impossible d''exporter la colonne ''{0}'' car elle n''existe pas dans la base de données";
	}

	@Override
	protected String getImportExportPkNotIncludedMessage_215() {
		return "Colonnes PK non incluses dans l''exportation : ''{0}''";
	}

	@Override
	protected String getImportExportWithErrorsMessage_216() {
		return "L'importation ne peut pas être exécutée car il existe des enregistrements comportant des erreurs. Vérifiez-le";
	}

	@Override
	protected String getImportExportDtoErrorMessage_217() {
		return "Une erreur s'est produite lors du réglage de la colonne ''{0}'' la valeur ''{1}''";
	}

	@Override
	protected String getImportExportInvalidModelErrorMessage_218() {
		return "Le modèle ''{0}'' ne supporte pas l'action d''exportation/importation";
	}

	@Override
	protected String getCopyInvalidModelErrorMessage_219() {
		return "Le modèle ''{0}'' ne supporte pas l'action de copier un enregistrement";
	}

}
