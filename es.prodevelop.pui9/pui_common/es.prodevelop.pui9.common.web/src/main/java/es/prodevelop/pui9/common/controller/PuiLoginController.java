package es.prodevelop.pui9.common.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.annotations.PuiNoSessionRequired;
import es.prodevelop.pui9.common.exceptions.PuiCommonAnonymousNotAllowedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectLoginException;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectUserPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonNoSessionException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserDisabledException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiLoginDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiLogin;
import es.prodevelop.pui9.common.service.interfaces.IPuiLoginService;
import es.prodevelop.pui9.controller.AbstractCommonController;
import es.prodevelop.pui9.eventlistener.event.SigninEvent;
import es.prodevelop.pui9.eventlistener.event.SignoutEvent;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.interceptors.CommonInterceptor;
import es.prodevelop.pui9.login.IPuiLogin;
import es.prodevelop.pui9.login.LoginData;
import es.prodevelop.pui9.login.PuiUserInfo;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.INullTableDao;
import es.prodevelop.pui9.model.dto.interfaces.INullTable;
import es.prodevelop.pui9.model.dto.interfaces.INullTablePk;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.utils.PuiRequestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @generated
 */
@Controller
@RequestMapping("/login")
@Api(tags = "PUI Login")
public class PuiLoginController extends
		AbstractCommonController<INullTablePk, INullTable, IVPuiLogin, INullTableDao, IVPuiLoginDao, IPuiLoginService> {

	private static final String LIST_FUNCTIONALITY = "LIST_PUI_SESSIONS";
	private static final String KILL_FUNCTIONALITY = "KILL_PUI_SESSIONS";
	private static final String LOGIN_MODEL = "login";

	@Autowired
	private IPuiLogin puiLogin;

	/**
	 * @generated
	 */
	@Override
	protected String getReadFunctionality() {
		return "LIST_LOGIN";
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

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_LIST, value = METHOD_FUNCTIONALITY_LIST)
	@ApiOperation(value = "List data of a view", notes = "List all the elements that accomplish the given condition")
	@PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse<IVPuiLogin> list(
			@ApiParam(value = "The searching parameters") @RequestBody(required = false) SearchRequest req)
			throws PuiServiceGetException {
		if (req == null) {
			req = new SearchRequest();
		}
		req.setModel(LOGIN_MODEL);

		return super.list(req);
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_LIST, value = METHOD_FUNCTIONALITY_LIST)
	@ApiOperation(value = "Export the grid data", notes = "Export the current grid data")
	@PostMapping(value = "/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload export(@RequestBody(required = false) ExportRequest req) {
		if (req == null) {
			req = new ExportRequest();
		}
		req.setModel(LOGIN_MODEL);

		return super.export(req);
	}

	@PuiNoSessionRequired
	@ApiOperation(value = "Login into the application", notes = "Login into the application using the given credentials")
	@PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
	public PuiUserInfo signin(HttpServletRequest request, @ApiParam(required = true) @RequestBody LoginData loginData,
			@ApiParam(hidden = true) @RequestHeader(value = CommonInterceptor.HEADER_TIMEZONE, required = false) String timezone)
			throws PuiCommonIncorrectLoginException, PuiCommonIncorrectUserPasswordException,
			PuiCommonUserNotExistsException, PuiCommonUserDisabledException, PuiCommonAnonymousNotAllowedException {
		loginData.setIp(PuiRequestUtils.extractIp(request));
		loginData.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		loginData.setTimezone(timezone);

		PuiUserInfo pui = puiLogin.loginUser(loginData);

		PuiUserSession userSession = PuiUserSession.getCurrentSession();

		getEventLauncher().fireAsync(new SigninEvent(userSession));

		return pui;
	}

	@ApiOperation(value = "Logout the application", notes = "Logout the application.")
	@GetMapping(value = "/signout")
	public void signout(@ApiParam(hidden = true) @RequestHeader(value = HttpHeaders.AUTHORIZATION) String authorization)
			throws PuiCommonNoSessionException {
		PuiUserSession userSession = PuiUserSession.getCurrentSession();

		getEventLauncher().fireAsync(new SignoutEvent(userSession));

		puiLogin.logoutUser(authorization);
	}

	@ApiOperation(value = "List all sessions", notes = "List all opened user sessions")
	@PuiFunctionality(id = LIST_FUNCTIONALITY, value = LIST_FUNCTIONALITY)
	@GetMapping(value = "/listSessions", produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse<PuiUserSession> listSessions() {
		List<PuiUserSession> list = puiLogin.getAllSessions();

		SearchResponse<PuiUserSession> resp = new SearchResponse<>();
		resp.setData(list);
		resp.setCurrentPage(1);
		resp.setCurrentRecords(list.size());
		resp.setTotalPages((long) 1);
		resp.setTotalRecords((long) list.size());

		return resp;
	}

	@ApiOperation(value = "Kill given session", notes = "Kill the given session")
	@PuiFunctionality(id = KILL_FUNCTIONALITY, value = KILL_FUNCTIONALITY)
	@PostMapping(value = "killSessions")
	public void killSessions(@RequestBody List<String> jwts) throws PuiCommonNoSessionException {
		if (CollectionUtils.isEmpty(jwts)) {
			return;
		}

		for (String jwt : jwts) {
			puiLogin.logoutUser(jwt);
		}
	}

	@ApiOperation(value = "Keep the session active", notes = "Keep the session active")
	@GetMapping(value = "/keepSessionActive")
	public void keepSessionActive() {
		// do nothing. Simply keeps the session active
	}

}
