package es.prodevelop.pui9.elasticsearch.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import es.prodevelop.pui9.common.model.dao.interfaces.IPuiElasticsearchViewsDao;
import es.prodevelop.pui9.common.model.dao.interfaces.IPuiModelDao;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

/**
 * This component process all the indexable views for the application set in the
 * table PUI_ELASTICSEARCH_VIEWS. The indexable views are application dependent,
 * and the identification of each application are set by the "appname" bean in
 * the AbstractAppSpringConfiguration
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticsearchViewsAnalysis {

	@Autowired(required = false)
	private IPuiElasticSearchEnablement elasticSearchEnablement;

	@Autowired
	private ViewsAnalysis viewsAnalysis;

	@Autowired
	private IPuiElasticsearchViewsDao elasticsearchViewsDao;

	@Autowired
	private IPuiModelDao modelDao;

	@Autowired
	private DaoRegistry daoRegistry;

	/**
	 * Set the value using an Spring bean:<br>
	 * <ul>
	 * <li>With Java code:</li> <code>@Bean<br>
	 * public String appname() {<br>
	 * return "your_app_name";<br>
	 * }</code>
	 * <li>With xml configuration:</li>
	 * <code>&ltbean id="appname" class="java.lang.String"&gt<br>
	 * &ltconstructor-arg value="your_app_name"/&gt<br> &lt/bean&gt</code>
	 * </ul>
	 */
	@Qualifier("appname")
	@Autowired(required = false)
	private String appname = "DEFAULT";

	@PostConstruct
	private void postConstruct() {
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ElasticSearch_RefreshIndexableViews", true, 0, 5,
				TimeUnit.MINUTES, this::processIndexableViews);
	}

	@SuppressWarnings("unchecked")
	private void processIndexableViews() {
		if (elasticSearchEnablement != null) {
			Map<String, List<String>> indexableViewsMap = new HashMap<>();
			List<String> elasticSearchModels = new ArrayList<>();
			try {
				elasticsearchViewsDao.findByAppname(appname).forEach(dto -> {
					List<String> identityfields;
					try {
						identityfields = Stream.of(dto.getIdentityfields().split(",", -1)).map(String::trim)
								.map(String::toLowerCase).collect(Collectors.toList());
					} catch (Exception e) {
						identityfields = Collections.emptyList();
					}
					indexableViewsMap.put(dto.getViewname().toLowerCase(), identityfields);
				});

				modelDao.findAll().forEach(model -> {
					if (indexableViewsMap.containsKey(model.getEntity().toLowerCase())
							&& !elasticSearchModels.contains(model.getModel().toLowerCase())) {
						elasticSearchModels.add(model.getModel().toLowerCase());
					}
				});
			} catch (PuiDaoFindException e) {
				// do nothing
			}

			viewsAnalysis.setIndexableViewsMap(indexableViewsMap);
			viewsAnalysis.setElasticSearchList(elasticSearchModels);
			viewsAnalysis.autoGenerateFaultingViews();

			indexableViewsMap.forEach((view, identityfields) -> {
				try {
					Class<? extends IDto> dtoClass = daoRegistry.getDtoFromEntityName(view, false, false);
					if (dtoClass == null) {
						return;
					}

					if (IViewDto.class.isAssignableFrom(dtoClass)) {
						elasticSearchEnablement.addIndexableView((Class<? extends IViewDto>) dtoClass);
					}
				} catch (Exception e) {
					// do nothing
				}
			});
		}
	}

}
