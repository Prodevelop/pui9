package es.prodevelop.pui9.common.service.interfaces;

import java.time.Instant;

import org.springframework.transaction.annotation.Transactional;

import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectUserPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonInvalidPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserResetTokenException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * @generated
 */
public interface IPuiUserService extends IService<IPuiUserPk, IPuiUser, IVPuiUser, IPuiUserDao, IVPuiUserDao> {

	IPuiUser getUserLite(String user);

	/**
	 * Drop the user from the database, instead of disabling it (like the delete
	 * function does)
	 * 
	 * @param pk The user
	 * @throws PuiServiceDeleteException If any exception deleting the user is
	 *                                   thrown
	 */
	@Transactional(rollbackFor = PuiException.class)
	void dropUser(IPuiUserPk pk) throws PuiServiceDeleteException;

	@Transactional(rollbackFor = PuiException.class)
	void disableUser(IPuiUserPk pk) throws PuiCommonUserNotExistsException;

	@Transactional(rollbackFor = PuiException.class)
	void enableUser(IPuiUserPk pk) throws PuiCommonUserNotExistsException;

	@Transactional(rollbackFor = PuiException.class)
	void setLastAccess(IPuiUserPk pk, Instant loginTime, String loginIp) throws PuiCommonUserNotExistsException;

	/**
	 * Request for reseting the password of the user. The request will be available
	 * for 30 minutes. A reset token will be generated and the user will be marked
	 * as pending of new password
	 * 
	 * @param usrEmail The user/email to reset the password
	 * @return True if the request finished correctly; false if the user doesn't
	 *         exist
	 */
	@Transactional(rollbackFor = PuiException.class)
	Boolean requestResetPassword(String usrEmail);

	/**
	 * Do the password reset.
	 * 
	 * @param resetToken  The token to ensure that the user want to reset the
	 *                    password
	 * @param newPassword The new password to be set (in plain)
	 * @throws PuiCommonUserResetTokenException  If the provided token doesn't exist
	 * @throws PuiServiceUpdateException         If fails while updating the
	 *                                           registry in the database
	 * @throws PuiCommonInvalidPasswordException If the password doesn't fit the
	 *                                           security requirements
	 */
	@Transactional(rollbackFor = PuiException.class)
	void doResetPassword(String resetToken, String newPassword)
			throws PuiCommonUserResetTokenException, PuiServiceUpdateException, PuiCommonInvalidPasswordException;

	@Transactional(rollbackFor = PuiException.class)
	void setPassword(IPuiUserPk pk, String oldPassword, String newPassword)
			throws PuiCommonIncorrectUserPasswordException, PuiCommonUserNotExistsException, PuiServiceUpdateException,
			PuiCommonInvalidPasswordException;

	@Transactional(rollbackFor = PuiException.class)
	void doSetPassword(IPuiUserPk user, String newPassword)
			throws PuiServiceUpdateException, PuiCommonInvalidPasswordException;

}