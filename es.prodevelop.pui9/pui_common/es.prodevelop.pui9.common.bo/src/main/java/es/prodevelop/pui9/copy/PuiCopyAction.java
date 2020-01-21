package es.prodevelop.pui9.copy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import es.prodevelop.pui9.common.exceptions.PuiCommonCopyInvalidModelException;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.exceptions.PuiServiceCopyRegistryException;
import es.prodevelop.pui9.model.dto.DtoFactory;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.service.registry.ServiceRegistry;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;

@Component
public class PuiCopyAction {

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private IPuiModelService modelService;

	private List<String> modelsCache;

	@PostConstruct
	private void postConstruct() {
		if (modelsCache != null) {
			return;
		}

		modelsCache = new ArrayList<>();
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadCopyActionModels", true, 1, 1, TimeUnit.HOURS,
				() -> reloadModels(true));
	}

	/**
	 * Reload the models cache
	 */
	public synchronized void reloadModels(boolean force) {
		if (force || CollectionUtils.isEmpty(modelsCache)) {
			if (force) {
				modelService.reloadModels(true);
			}
			modelsCache.clear();
			modelsCache.addAll(modelService.getOriginalPuiModelConfigurations().entrySet().stream()
					.filter(entry -> entry.getValue().getDefaultConfiguration().isActionCopy()).map(Entry::getKey)
					.collect(Collectors.toList()));
		}
	}

	private void checkModelAvailable(String model) throws PuiCommonCopyInvalidModelException {
		reloadModels(false);

		if (!modelsCache.contains(model)) {
			throw new PuiCommonCopyInvalidModelException(model);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends ITableDto> T performCopy(String model, Map<String, Object> pk)
			throws PuiServiceCopyRegistryException, PuiCommonCopyInvalidModelException {
		checkModelAvailable(model);

		Class<? extends IService> serviceClass = serviceRegistry.getServiceFromModel(model);
		IService service = PuiApplicationContext.getInstance().getBean(serviceClass);

		Class<? extends ITableDto> dtoPkClass = service.getTableDtoPkClass();

		ITableDto dtoPk = DtoFactory.createInstanceFromInterface(dtoPkClass, pk);

		return (T) service.copy(dtoPk);
	}

}
