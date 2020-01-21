package es.prodevelop.pui9.service.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.service.interfaces.IService;

/**
 * This is a Registry for all the Services. It brings some useful methods to
 * manage the Services of PUI
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
@SuppressWarnings("rawtypes")
public class ServiceRegistry {

	@Autowired
	private DaoRegistry daoRegistry;

	private List<Class<? extends IService>> registeredServices;
	private Map<Class<? extends IDto>, Class<? extends IService>> mapServiceFromDto;
	private Map<String, Class<? extends IService>> mapServiceFromModel;
	private Map<Class<? extends ITableDto>, Class<? extends IViewDto>> mapViewDtoFromTableDto;
	private Map<Class<? extends IViewDto>, Class<? extends ITableDto>> mapTableDtoFromViewDto;

	private ServiceRegistry() {
		registeredServices = new ArrayList<>();
		mapServiceFromDto = new HashMap<>();
		mapServiceFromModel = new HashMap<>();
		mapViewDtoFromTableDto = new HashMap<>();
		mapTableDtoFromViewDto = new HashMap<>();
	}

	/**
	 * Registers a Service
	 */
	@SuppressWarnings("unchecked")
	public void registerService(IService service) {
		Class<? extends IService> serviceInterface = findInterface(service.getClass());

		if (registeredServices.contains(serviceInterface)) {
			return;
		}
		registeredServices.add(serviceInterface);
		daoRegistry.relatedDaos(service.getTableDao().getDaoClass(), service.getViewDao().getDaoClass());
		daoRegistry.relatedDtos(service.getTableDao().getDtoPkClass(), service.getTableDao().getDtoClass(),
				service.getViewDao().getDtoClass());

		Service serviceAnnotation = service.getClass().getAnnotation(Service.class);
		if (serviceAnnotation != null) {
			String value = serviceAnnotation.value();
			if (StringUtils.isEmpty(value)) {
				value = service.getClass().getSimpleName();
			}
			if (value.endsWith("Service")) {
				value = value.substring(0, value.indexOf("Service"));
			}
			mapServiceFromModel.put(value.toLowerCase(), serviceInterface);
		}

		mapServiceFromDto.put(service.getTableDtoClass(), serviceInterface);
		mapServiceFromDto.put(service.getViewDtoClass(), serviceInterface);
		mapViewDtoFromTableDto.put(service.getTableDtoClass(), service.getViewDtoClass());
		mapViewDtoFromTableDto.put(service.getTableDtoPkClass(), service.getViewDtoClass());
		mapTableDtoFromViewDto.put(service.getViewDtoClass(), service.getTableDtoClass());
	}

	@SuppressWarnings("unchecked")
	private Class<? extends IService> findInterface(Class<? extends IService> serviceClass) {
		for (Class<?> clazz : serviceClass.getInterfaces()) {
			if (IService.class.isAssignableFrom(clazz)) {
				return (Class<? extends IService>) clazz;
			}
		}

		return null;
	}

	/**
	 * Get all the registered Services
	 */
	public List<Class<? extends IService>> getAllServices() {
		return registeredServices;
	}

	/**
	 * Get the ID related with the given DTO class
	 */
	public Class<? extends IService> getServiceFromDto(Class<? extends IDto> clazz) {
		return mapServiceFromDto.get(clazz);
	}

	/**
	 * Get the Service class represented by the given Model
	 */
	public Class<? extends IService> getServiceFromModel(String model) {
		return mapServiceFromModel.get(model);
	}

	/**
	 * Get the VDto class associated with the given TDto class
	 */
	public Class<? extends IViewDto> getViewDtoFromTableDto(Class<? extends ITableDto> tdtoClass) {
		return mapViewDtoFromTableDto.get(tdtoClass);
	}

	/**
	 * Get the TDto class associated with the given VDto class
	 */
	public Class<? extends ITableDto> getTableDtoFromViewDto(Class<? extends IViewDto> vdtoClass) {
		return mapTableDtoFromViewDto.get(vdtoClass);
	}

}
