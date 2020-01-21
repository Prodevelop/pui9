package es.prodevelop.pui9.notifications.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.notifications.model.dao.interfaces.IPuiUserFcmDao;
import es.prodevelop.pui9.notifications.model.dto.PuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;
import es.prodevelop.pui9.notifications.service.interfaces.IPuiUserFcmService;
import io.swagger.annotations.Api;

/**
 * @generated
 */
@Controller
@Api(tags = "PUI Notifications")
@RequestMapping("/puiuserfcm")
public class PuiUserFcmController extends
		AbstractCommonController<IPuiUserFcmPk, IPuiUserFcm, INullView, IPuiUserFcmDao, INullViewDao, IPuiUserFcmService> {
	/**
	 * @generated
	 */
	@Override
	protected String getReadFunctionality() {
		return null;
	}

	/**
	 * @generated
	 */
	@Override
	protected String getWriteFunctionality() {
		return null;
	}

	@Override
	public boolean allowList() {
		return false;
	}

	@Override
	public boolean allowGet() {
		return false;
	}

	@Override
	public boolean allowInsert() {
		return false;
	}

	@Override
	public boolean allowUpdate() {
		return false;
	}

	@Override
	public boolean allowDelete() {
		return false;
	}

	@GetMapping(value = "/registerFcmToken")
	public void registerFcmToken(@RequestParam String fcmToken) throws PuiServiceException {
		IPuiUserFcm userFcm = new PuiUserFcm();
		userFcm.setUsr(getSession().getUsr());
		userFcm.setToken(fcmToken);

		getService().registerUserFcmToken(userFcm);
	}

	@GetMapping(value = "/unregisterFcmToken")
	public void unregisterFcmToken(@RequestParam String fcmToken) throws PuiServiceException {
		IPuiUserFcm userFcm = new PuiUserFcm();
		userFcm.setUsr(getSession().getUsr());
		userFcm.setToken(fcmToken);

		getService().unregisterUserFcmToken(userFcm);
	}

}