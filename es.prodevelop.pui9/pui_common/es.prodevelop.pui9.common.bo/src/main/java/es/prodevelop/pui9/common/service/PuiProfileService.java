package es.prodevelop.pui9.common.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiFunctionalityDao;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileDao;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiProfileFunctionalityDao;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiFunctionalityPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfile;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionality;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfileFunctionalityPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiProfilePk;
import es.prodevelop.pui9.common.model.views.dao.interfaces.IVPuiProfileDao;
import es.prodevelop.pui9.common.model.views.dto.interfaces.IVPuiProfile;
import es.prodevelop.pui9.common.service.interfaces.IPuiProfileService;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.order.OrderBuilder;
import es.prodevelop.pui9.service.AbstractService;
import es.prodevelop.pui9.service.MultiValuedAttribute;

/**
 * @generated
 */
@Service
public class PuiProfileService
		extends AbstractService<IPuiProfilePk, IPuiProfile, IVPuiProfile, IPuiProfileDao, IVPuiProfileDao>
		implements IPuiProfileService {

	@Autowired
	private IPuiProfileFunctionalityDao profileFunctionalityDao;

	@Override
	protected void addMultiValuedAttributes() {
		{
			List<Pair<String, String>> localAttributes = new ArrayList<>();
			localAttributes.add(Pair.of(IPuiProfilePk.PROFILE_FIELD, IPuiProfileFunctionalityPk.PROFILE_FIELD));

			List<Pair<String, String>> referencedAttributes = new ArrayList<>();
			referencedAttributes.add(
					Pair.of(IPuiFunctionalityPk.FUNCTIONALITY_FIELD, IPuiProfileFunctionalityPk.FUNCTIONALITY_FIELD));

			addMultiValuedAttribute(new MultiValuedAttribute<>(IPuiProfile.FUNCTIONALITIES_FIELD, localAttributes,
					referencedAttributes, IPuiFunctionalityDao.class, IPuiProfileFunctionalityDao.class));
		}
	}

	@Override
	public List<String> getFunctionalitiesForProfile(String profile) {
		return getFunctionalitiesForProfiles(Collections.singletonList(profile));
	}

	@Override
	public List<String> getFunctionalitiesForProfiles(List<String> profiles) {
		try {
			FilterBuilder filterBuilder = FilterBuilder.newAndFilter()
					.addInString(IPuiProfileFunctionalityPk.PROFILE_COLUMN, profiles);
			OrderBuilder orderBuilder = OrderBuilder
					.newOrder(Order.newOrderAsc(IPuiProfileFunctionalityPk.FUNCTIONALITY_COLUMN));

			List<IPuiProfileFunctionality> list = profileFunctionalityDao.findWhere(filterBuilder, orderBuilder);
			List<String> functionalities = new ArrayList<>();

			for (IPuiProfileFunctionality pf : list) {
				functionalities.add(pf.getFunctionality());
			}

			return functionalities;
		} catch (PuiDaoFindException e) {
			return Collections.emptyList();
		}
	}

}