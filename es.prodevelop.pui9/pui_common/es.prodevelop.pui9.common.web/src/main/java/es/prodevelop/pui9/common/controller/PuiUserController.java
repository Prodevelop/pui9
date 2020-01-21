package es.prodevelop.pui9.common.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectUserPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonInvalidPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserResetTokenException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserDao;
import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * A controller to managet the users of the application
 * 
 * @generated
 * @author Marc Gil - mgil@prodevelop.es
 */
@Controller
@Api(tags = "PUI User")
@RequestMapping("/puiuser")
public class PuiUserController
		extends AbstractCommonController<IPuiUserPk, IPuiUser, IVPuiUser, IPuiUserDao, IVPuiUserDao, IPuiUserService> {

	private static final String CHANGE_USER_PASSWORDS = "CHANGE_USER_PASSWORDS";

	/**
	 * @generated
	 */
	@Override
	protected String getReadFunctionality() {
		return "READ_PUI_USER";
	}

	/**
	 * @generated
	 */
	@Override
	protected String getWriteFunctionality() {
		return "WRITE_PUI_USER";
	}

	@PuiNoSessionRequired
	@GetMapping(value = "/requestResetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean requestResetPassword(@RequestParam String usrEmail) {
		return getService().requestResetPassword(usrEmail);
	}

	@PuiNoSessionRequired
	@GetMapping(value = "/doResetPassword")
	public void doResetPassword(@RequestParam String resetToken, @RequestParam String newPassword)
			throws PuiCommonUserResetTokenException, PuiServiceUpdateException, PuiCommonInvalidPasswordException {
		getService().doResetPassword(resetToken, newPassword);
	}

	@PuiFunctionality(id = CHANGE_USER_PASSWORDS, value = CHANGE_USER_PASSWORDS)
	@ApiOperation(value = "Change the user password", notes = "Change the user password, providing the old and the new one")
	@GetMapping(value = "/setPassword")
	public void setPassword(@ApiParam(value = "The user", required = true) IPuiUserPk pk,
			@ApiParam(value = "The new password (in plain)", required = true) @RequestParam String newPassword)
			throws PuiCommonIncorrectUserPasswordException, PuiCommonUserNotExistsException, PuiServiceUpdateException,
			PuiCommonInvalidPasswordException {
		getService().doSetPassword(pk, newPassword);
	}

	@ApiOperation(value = "Change the user password", notes = "Change the user password, providing the old and the new one")
	@GetMapping(value = "/changeUserPassword")
	public void changeUserPassword(
			@ApiParam(value = "The old password (in plain)", required = true) @RequestParam String oldPassword,
			@ApiParam(value = "The new password (in plain)", required = true) @RequestParam String newPassword)
			throws PuiCommonIncorrectUserPasswordException, PuiCommonUserNotExistsException, PuiServiceUpdateException,
			PuiCommonInvalidPasswordException {
		getService().setPassword(new PuiUserPk(getSession().getUsr()), oldPassword, newPassword);
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@ApiOperation(value = "Disable a user", notes = "Disable the given user")
	@GetMapping(value = "/disableUser")
	public void disableUser(@ApiParam(value = "The user", required = true) IPuiUserPk pk)
			throws PuiCommonUserNotExistsException {
		getService().disableUser(pk);
	}

	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@ApiOperation(value = "Enable a user", notes = "Enable the given user")
	@GetMapping(value = "/enableUser")
	public void enableUser(@ApiParam(value = "The user", required = true) IPuiUserPk pk)
			throws PuiCommonUserNotExistsException {
		getService().enableUser(pk);
	}

}