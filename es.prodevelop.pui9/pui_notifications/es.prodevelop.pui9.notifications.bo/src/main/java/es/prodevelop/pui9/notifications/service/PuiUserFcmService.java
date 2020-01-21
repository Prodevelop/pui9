package es.prodevelop.pui9.notifications.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.login.PuiDaoAuthenticationProvider;
import es.prodevelop.pui9.model.dao.interfaces.INullViewDao;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.notifications.eventlistener.event.FcmTokenRegisteredEvent;
import es.prodevelop.pui9.notifications.eventlistener.event.FcmTokenUnregisteredEvent;
import es.prodevelop.pui9.notifications.exceptions.PuiNotificationsAnonymousNotAllowedException;
import es.prodevelop.pui9.notifications.model.dao.interfaces.IPuiUserFcmDao;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcm;
import es.prodevelop.pui9.notifications.model.dto.interfaces.IPuiUserFcmPk;
import es.prodevelop.pui9.notifications.service.interfaces.IPuiUserFcmService;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

/**
 * @generated
 */
@Service
public class PuiUserFcmService
		extends AbstractService<IPuiUserFcmPk, IPuiUserFcm, INullView, IPuiUserFcmDao, INullViewDao>
		implements IPuiUserFcmService {

	private static final Integer MAX_DAYS_WITHOUT_USE = 30;

	@Autowired
	private PuiFcmClient fcmClient;

	/**
	 * Initializes a background task that is executed every night
	 */
	@PostConstruct
	private void postConstruct() {
		Duration initDelay = Duration.between(LocalDateTime.now(), LocalDate.now().plusDays(1).atStartOfDay());
		Long delay = TimeUnit.DAYS.toMinutes(1);

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("FcmPurgueTokens", true, initDelay.toMinutes(), delay,
				TimeUnit.MINUTES, this::purgueTokens);
	}

	@Override
	protected void beforeInsert(IPuiUserFcm dto) throws PuiServiceException {
		if (dto.getUsr().equals(PuiDaoAuthenticationProvider.ANONYMOUS_USER)) {
			throw new PuiNotificationsAnonymousNotAllowedException();
		}

		dto.setLastuse(Instant.now());
	}

	@Override
	protected void afterInsert(IPuiUserFcm dto) throws PuiServiceException {
		getEventLauncher().fireAsync(new FcmTokenRegisteredEvent(dto));
	}

	@Override
	protected void beforeUpdate(IPuiUserFcm oldDto, IPuiUserFcm dto) throws PuiServiceException {
		if (dto.getUsr().equals(PuiDaoAuthenticationProvider.ANONYMOUS_USER)) {
			throw new PuiNotificationsAnonymousNotAllowedException();
		}

		dto.setLastuse(Instant.now());
	}

	@Override
	protected void afterUpdate(IPuiUserFcm oldDto, IPuiUserFcm dto) throws PuiServiceException {
		if (!oldDto.getUsr().equals(dto.getUsr())) {
			getEventLauncher().fireAsync(new FcmTokenUnregisteredEvent(oldDto));
			getEventLauncher().fireAsync(new FcmTokenRegisteredEvent(dto));
		}
	}

	@Override
	protected void afterDelete(IPuiUserFcm dto) throws PuiServiceException {
		getEventLauncher().fireAsync(new FcmTokenUnregisteredEvent(dto));
	}

	@Override
	public void registerUserFcmToken(IPuiUserFcm userFcm) throws PuiServiceException {
		if (!exists(userFcm.createPk())) {
			insert(userFcm);
		} else {
			update(userFcm);
		}
	}

	@Override
	public void unregisterUserFcmToken(IPuiUserFcmPk userFcmPk) throws PuiServiceException {
		delete(userFcmPk);
	}

	/**
	 * Check all the FCM Tokens, and remove from the database those that are not
	 * valid
	 */
	private void purgueTokens() {
		int from = 0;
		final int size = SearchRequest.NUM_MAX_ROWS;
		List<String> toDelete = new ArrayList<>();

		try {
			List<IPuiUserFcm> list;
			Instant now = Instant.now();
			while (!(list = getTableDao().findAllPagination(null, null, null, from++, size)).isEmpty()) {
				List<String> tokens = list.stream()
						.filter(uf -> ChronoUnit.DAYS.between(uf.getLastuse(), now) > MAX_DAYS_WITHOUT_USE)
						.map(IPuiUserFcm::getToken).collect(Collectors.toList());
				toDelete.addAll(fcmClient.validateTokens(tokens));
			}
		} catch (PuiDaoFindException e) {
			// do nothing
		}

		if (!toDelete.isEmpty()) {
			FilterBuilder filterBuilder = FilterBuilder.newOrFilter();
			toDelete.forEach(token -> filterBuilder.addEquals(IPuiUserFcmPk.TOKEN_COLUMN, token));
			try {
				List<IPuiUserFcm> list = getAllWhere(filterBuilder);
				for (IPuiUserFcm uf : list) {
					delete(uf);
				}
			} catch (PuiServiceException e) {
				// do nothing
			}
		}
	}

}