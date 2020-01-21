package es.prodevelop.pui9.common.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.exceptions.PuiCommonIncorrectUserPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonInvalidPasswordException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserNotExistsException;
import es.prodevelop.pui9.common.exceptions.PuiCommonUserResetTokenException;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileDao;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserDao;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiUserProfileDao;
import es.prodevelop.pui9.common.model.dto.PuiUserPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfilePk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUser;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiUserProfilePk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiVariable;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiUserDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiUser;
import es.prodevelop.pui9.common.service.interfaces.IPuiUserService;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.eventlistener.event.PasswordResetEvent;
import es.prodevelop.pui9.eventlistener.event.RequestResetPasswordEvent;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoSaveException;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceExistsException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.service.MultiValuedAttribute;
import es.prodevelop.pui9.utils.PuiConstants;

/**
 * @generated
 */
@Service
public class PuiUserService extends AbstractService<IPuiUserPk, IPuiUser, IVPuiUser, IPuiUserDao, IVPuiUserDao>
		implements IPuiUserService {

	@Autowired
	private IPuiVariableService variableService;

	private BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder(10);
	private Cache<String, List<IPuiUser>> cache;

	@PostConstruct
	private void postConstruct() {
		cache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();
	}

	@Override
	protected void addMultiValuedAttributes() {
		{
			List<Pair<String, String>> localAttributes = new ArrayList<>();
			localAttributes.add(Pair.of(IPuiUserPk.USR_FIELD, IPuiUserProfilePk.USR_FIELD));

			List<Pair<String, String>> referencedAttributes = new ArrayList<>();
			referencedAttributes.add(Pair.of(IPuiProfilePk.PROFILE_FIELD, IPuiUserProfilePk.PROFILE_FIELD));

			addMultiValuedAttribute(new MultiValuedAttribute<>(IPuiUser.PROFILES_FIELD, localAttributes,
					referencedAttributes, IPuiProfileDao.class, IPuiUserProfileDao.class));
		}
	}

	@Override
	public IPuiUser insert(IPuiUser dto) throws PuiServiceInsertException {
		boolean exists;
		try {
			exists = exists(dto.createPk());
		} catch (PuiServiceExistsException e) {
			throw new PuiServiceInsertException(e);
		}

		if (exists) {
			if (dto.getDisabled().equals(PuiConstants.TRUE_INT)) {
				dto.setDisabled(PuiConstants.FALSE_INT);
				try {
					update(dto);
				} catch (PuiServiceUpdateException e) {
					throw new PuiServiceInsertException(e);
				}
			}

			return dto;
		} else {
			if (!StringUtils.isEmpty(dto.getPassword())) {
				try {
					checkPassword(dto.getPassword());
				} catch (PuiCommonInvalidPasswordException e) {
					throw new PuiServiceInsertException(e);
				}

				String password = bCryptEncoder.encode(dto.getPassword());
				dto.setPassword(password);
			}
			return super.insert(dto);
		}
	}

	@Override
	public IPuiUserPk delete(IPuiUserPk dtoPk) throws PuiServiceDeleteException {
		// avoid to delete physically the user. Set it deleted logically
		try {
			List<IPuiUser> list = getTableDao().findByUsr(dtoPk.getUsr());
			if (list.isEmpty()) {
				return null;
			}

			IPuiUser dto = list.get(0);
			dto.setDisabled(PuiConstants.TRUE_INT);
			update(dto);

			return dtoPk;
		} catch (PuiDaoFindException e) {
			throw new PuiServiceDeleteException(e);
		} catch (PuiServiceUpdateException e) {
			throw new PuiServiceDeleteException(e);
		}
	}

	@Override
	public void dropUser(IPuiUserPk pk) throws PuiServiceDeleteException {
		super.delete(pk);
	}

	@Override
	protected void beforeUpdate(IPuiUser oldDto, IPuiUser dto) throws PuiServiceException {
		dto.setPassword(oldDto.getPassword());
	}

	@Override
	protected void afterInsert(IPuiUser dto) throws PuiServiceException {
		dto.setPassword(null);
	}

	@Override
	protected void afterUpdate(IPuiUser oldDto, IPuiUser dto) throws PuiServiceException {
		dto.setPassword(null);
	}

	@Override
	public IPuiUser getUserLite(String user) {
		try {
			List<IPuiUser> list = getTableDao().findByUsr(user);
			return list.isEmpty() ? null : list.get(0);
		} catch (PuiDaoFindException e) {
			return null;
		}
	}

	@Override
	public void disableUser(IPuiUserPk pk) throws PuiCommonUserNotExistsException {
		try {
			IPuiUser puiUser = getTableDao().findOne(pk);
			if (puiUser == null) {
				throw new PuiCommonUserNotExistsException(pk.getUsr());
			}
			puiUser.setDisabled(PuiConstants.TRUE_INT);
			puiUser.setDisableddate(Instant.now());
			getTableDao().save(puiUser);
		} catch (PuiDaoFindException | PuiDaoSaveException e) {
			// do nothing
		}
	}

	@Override
	public void enableUser(IPuiUserPk pk) throws PuiCommonUserNotExistsException {
		try {
			IPuiUser puiUser = getTableDao().findOne(pk);
			if (puiUser == null) {
				throw new PuiCommonUserNotExistsException(pk.getUsr());
			}
			puiUser.setDisabled(PuiConstants.FALSE_INT);
			puiUser.setDisableddate(null);
			getTableDao().save(puiUser);
		} catch (PuiDaoFindException | PuiDaoSaveException e) {
			// do nothing
		}
	}

	@Override
	public void setLastAccess(IPuiUserPk pk, Instant loginTime, String loginIp) throws PuiCommonUserNotExistsException {
		try {
			IPuiUser puiUser = getTableDao().findOne(pk);
			if (puiUser == null) {
				throw new PuiCommonUserNotExistsException(pk.getUsr());
			}
			puiUser.setLastaccesstime(loginTime);
			puiUser.setLastaccessip(loginIp);
			getTableDao().save(puiUser);
		} catch (PuiDaoFindException | PuiDaoSaveException e) {
			// do nothing
		}
	}

	@Override
	public Boolean requestResetPassword(String usrEmail) {
		// try to find the user by the "usr" property
		List<IPuiUser> list;
		try {
			list = getTableDao().findByUsr(usrEmail);
		} catch (PuiDaoFindException e1) {
			list = Collections.emptyList();
		}
		if (CollectionUtils.isEmpty(list)) {
			// if the value is the email, try to find the user by the "email" property
			try {
				list = getTableDao().findByEmail(usrEmail);
			} catch (PuiDaoFindException e) {
				list = Collections.emptyList();
			}
		}

		if (CollectionUtils.isEmpty(list)) {
			return false;
		}

		String random = RandomStringUtils.randomAlphanumeric(100);
		cache.put(random, list);
		list.forEach(puiUser -> {
			puiUser.setResetpasswordtoken(random);
			try {
				patch(puiUser.createPk(),
						Collections.singletonMap(IPuiUser.RESET_PASSWORD_TOKEN_FIELD, puiUser.getResetpasswordtoken()));
				getEventLauncher().fireAsync(new RequestResetPasswordEvent(puiUser));
			} catch (PuiServiceUpdateException e) {
				// do nothing
			}
		});

		return true;
	}

	@Override
	public void doResetPassword(String resetToken, String newPassword)
			throws PuiCommonUserResetTokenException, PuiServiceUpdateException, PuiCommonInvalidPasswordException {
		List<IPuiUser> list = cache.getIfPresent(resetToken);
		if (CollectionUtils.isEmpty(list)) {
			throw new PuiCommonUserResetTokenException();
		}

		for (IPuiUser puiUser : list) {
			puiUser = patch(puiUser.createPk(), Collections.singletonMap(IPuiUser.RESET_PASSWORD_TOKEN_FIELD, null));
			doSetPassword(puiUser, newPassword);
			cache.invalidate(resetToken);
			getEventLauncher().fireAsync(new PasswordResetEvent(puiUser));
		}
	}

	@Override
	public void setPassword(IPuiUserPk pk, String oldPassword, String newPassword)
			throws PuiCommonIncorrectUserPasswordException, PuiCommonUserNotExistsException, PuiServiceUpdateException,
			PuiCommonInvalidPasswordException {
		if (StringUtils.isEmpty(oldPassword)) {
			oldPassword = null;
		}

		if (pk == null) {
			pk = new PuiUserPk(getSession().getUsr());
		}

		IPuiUser user;
		try {
			user = getTableDao().findOne(pk);
		} catch (PuiDaoFindException e1) {
			throw new PuiCommonUserNotExistsException(pk.getUsr());
		}

		if (!bCryptEncoder.matches(oldPassword, user.getPassword())) {
			throw new PuiCommonIncorrectUserPasswordException();
		}

		doSetPassword(user, newPassword);
	}

	@Override
	public void doSetPassword(IPuiUserPk user, String newPassword)
			throws PuiServiceUpdateException, PuiCommonInvalidPasswordException {
		if (StringUtils.isEmpty(newPassword)) {
			throw new PuiCommonInvalidPasswordException();
		}

		if (user.getClass().equals(PuiUserPk.class)) {
			try {
				user = getTableDao().findOne(user);
			} catch (PuiDaoFindException e) {
				// do nothing
			}
		}

		checkPassword(newPassword);

		String newPasswordBCrypt = newPassword != null ? bCryptEncoder.encode(newPassword) : null;

		try {
			((IPuiUser) user).setPassword(newPasswordBCrypt);
			getTableDao().save((IPuiUser) user);
		} catch (PuiDaoSaveException e) {
			throw new PuiServiceUpdateException(e);
		}
	}

	private void checkPassword(String password) throws PuiCommonInvalidPasswordException {
		String regex = variableService.getVariable(String.class, PuiVariableValues.PASSWORD_PATTERN.name());
		if (StringUtils.isEmpty(regex) || regex.equals(IPuiVariable.VARIABLE_WITH_NO_VALUE)) {
			return;
		}

		if (!password.matches(regex)) {
			throw new PuiCommonInvalidPasswordException();
		}
	}

}