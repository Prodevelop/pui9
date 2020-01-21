package es.prodevelop.pui9.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import es.prodevelop.pui9.annotations.PuiFunctionality;
import es.prodevelop.pui9.eventlistener.event.DeleteEvent;
import es.prodevelop.pui9.eventlistener.event.GetEvent;
import es.prodevelop.pui9.eventlistener.event.InsertEvent;
import es.prodevelop.pui9.eventlistener.event.ListEvent;
import es.prodevelop.pui9.eventlistener.event.PatchEvent;
import es.prodevelop.pui9.eventlistener.event.TemplateEvent;
import es.prodevelop.pui9.eventlistener.event.UpdateEvent;
import es.prodevelop.pui9.exceptions.PuiServiceDeleteException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.export.DataExporterRegistry;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.interfaces.IViewDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.IPuiSearchAdapter;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.search.SearchResponse;
import es.prodevelop.pui9.service.interfaces.IService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This abstract controller offers all the common operations for entities like:
 * insert, update, delete, get and list for PUI grid.
 */
public abstract class AbstractCommonController<TPK extends ITableDto, T extends TPK, V extends IViewDto, DAO extends ITableDao<TPK, T>, VDAO extends IViewDao<V>, S extends IService<TPK, T, V, DAO, VDAO>>
		extends AbstractPuiController implements IPuiCommonController<TPK, T, V, DAO, VDAO, S> {

	@Autowired
	private DaoRegistry daoRegistry;

	@Autowired
	private IPuiSearchAdapter puiSearchAdapter;

	@Autowired
	private S service;

	@PostConstruct
	private void postConstruct() {
		RequestMapping rm = getClass().getAnnotation(RequestMapping.class);
		if (rm == null) {
			return;
		}

		String modelId;
		String[] value = rm.value();
		if (value.length == 0) {
			return;
		} else {
			modelId = value[0].replace("/", "");
		}

		daoRegistry.registerModelDtos(modelId, getService().getTableDtoPkClass(), getService().getTableDtoClass(),
				getService().getViewDtoClass());
	}

	protected DaoRegistry getDaoRegistry() {
		return daoRegistry;
	}

	public S getService() {
		return service;
	}

	protected DAO getTableDao() {
		return service.getTableDao();
	}

	protected VDAO getViewDao() {
		return service.getViewDao();
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_INSERT, value = METHOD_FUNCTIONALITY_INSERT)
	@ApiOperation(value = "The Template for creating a new element", notes = "Get the template for creating a new element")
	@GetMapping(value = "/template", produces = MediaType.APPLICATION_JSON_VALUE)
	public T template() throws PuiServiceNewException {
		T dto = null;
		if (hasLanguageSupport()) {
			dto = getService().getNew(getLanguage());
		} else {
			dto = getService().getNew();
		}

		fireEventTemplate(dto);
		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_GET, value = METHOD_FUNCTIONALITY_GET)
	@ApiOperation(value = "Obtain an element", notes = "Get the element that matches the given PK")
	@GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
	public T get(@ApiParam(value = "The PK of the element", required = true) TPK dtoPk) throws PuiServiceGetException {
		T dto = null;
		if (hasLanguageSupport()) {
			dto = getService().getByPk(dtoPk, getLanguage());
		} else {
			dto = getService().getByPk(dtoPk);
		}

		fireEventGet(dto);
		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_INSERT, value = METHOD_FUNCTIONALITY_INSERT)
	@ApiOperation(value = "Insert a new element", notes = "Insert a new element into the database")
	@PostMapping(value = "/insert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public T insert(@ApiParam(value = "The data of the element", required = true) @RequestBody T dto)
			throws PuiServiceInsertException {
		getService().insert(dto);
		fireEventInsert(dto);

		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@ApiOperation(value = "Update an element", notes = "Update an existing element in the database")
	@PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public T update(
			@ApiParam(value = "The full data of the element be updated, including non changed values", required = true) @RequestBody T dto)
			throws PuiServiceGetException, PuiServiceUpdateException {
		TPK pk = dto.createPk();
		T oldDto = getService().getByPk(pk, getLanguage());

		getService().update(dto);
		fireEventUpdate(oldDto, dto);

		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_UPDATE, value = METHOD_FUNCTIONALITY_UPDATE)
	@ApiOperation(value = "Patch some attributes of an element", notes = "Update only part of an existing element in the database")
	@PatchMapping(value = "/patch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public T patch(@ApiParam(value = "The PK of the element", required = true) TPK dtoPk,
			@ApiParam(value = "", required = true) @RequestBody Map<String, Object> properties)
			throws PuiServiceGetException, PuiServiceUpdateException {
		T oldDto = getService().getByPk(dtoPk, getLanguage());

		T dto = getService().patch(dtoPk, properties);
		fireEventPatch(oldDto, dto);

		return dto;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_DELETE, value = METHOD_FUNCTIONALITY_DELETE)
	@ApiOperation(value = "Delete an element", notes = "Delete an element from the database")
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public TPK delete(@ApiParam(value = "The PK of the element to be deleted", required = true) TPK dtoPk)
			throws PuiServiceGetException, PuiServiceDeleteException {
		T dto = getService().getByPk(dtoPk, getLanguage());

		if (dto == null) {
			return null;
		}

		TPK deletedDtoPk = getService().delete(dtoPk);
		fireEventDelete(dto);

		return deletedDtoPk;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_LIST, value = METHOD_FUNCTIONALITY_LIST)
	@ApiOperation(value = "List data of a view", notes = "List all the elements that accomplish the given condition")
	@PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse<V> list(
			@ApiParam(value = "The searching parameters") @RequestBody(required = false) SearchRequest req)
			throws PuiServiceGetException {
		if (req == null) {
			req = new SearchRequest();
		}

		req.setViewDtoClass(getViewDao().getDtoClass());
		req.setTableDtoClass(getTableDao().getDtoClass());

		SearchResponse<V> res = puiSearchAdapter.search(req);
		fireEventList(req, res);

		return res;
	}

	@Override
	@PuiFunctionality(id = ID_FUNCTIONALITY_LIST, value = METHOD_FUNCTIONALITY_LIST)
	@ApiOperation(value = "Export the grid data", notes = "Export the current grid data")
	@PostMapping(value = "/export", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileDownload export(@RequestBody(required = false) ExportRequest req) {
		if (req == null) {
			req = new ExportRequest();
		}

		req.setPage(SearchRequest.DEFAULT_PAGE);
		req.setRows(SearchRequest.NUM_MAX_ROWS);

		req.setViewDtoClass(getViewDao().getDtoClass());
		req.setTableDtoClass(getTableDao().getDtoClass());

		return DataExporterRegistry.getSingleton().getExporter(req.getExportType()).generate(req);
	}

	@Override
	public boolean allowGet() {
		return true;
	}

	@Override
	public boolean allowTemplate() {
		return allowInsert();
	}

	@Override
	public boolean allowInsert() {
		return true;
	}

	@Override
	public boolean allowUpdate() {
		return true;
	}

	@Override
	public boolean allowPatch() {
		return allowUpdate();
	}

	@Override
	public boolean allowDelete() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean allowList() {
		// by default, it is allowed if View Dao is not instance of NullViewDao
		Class<IViewDto> dtoViewClass = null;

		try {
			Type superClass = getClass();
			while ((superClass instanceof Class)
					&& ((Class<?>) superClass).getSuperclass().equals(((Class<?>) superClass).getGenericSuperclass())) {
				superClass = ((Class<?>) superClass).getSuperclass();
			}
			superClass = ((Class<?>) superClass).getGenericSuperclass();

			// obtain the dtoViewClass
			for (Type type : ((ParameterizedType) superClass).getActualTypeArguments()) {
				if (IViewDto.class.isAssignableFrom((Class<?>) type)) {
					dtoViewClass = (Class<IViewDto>) type;
					break;
				}
			}
		} catch (Exception e1) {
			// only for PUI controllers
			try {
				first: for (TypeVariable<?> typevar : getClass().getTypeParameters()) {
					for (Type type : typevar.getBounds()) {
						if (!(type instanceof Class)) {
							continue;
						}
						if (IViewDto.class.isAssignableFrom((Class<?>) type)) {
							dtoViewClass = (Class<IViewDto>) type;
							break first;
						}
					}
				}
			} catch (Exception e2) {
				return false;
			}
		}

		return !INullView.class.isAssignableFrom(dtoViewClass);
	}

	@Override
	public boolean allowExport() {
		return allowList();
	}

	protected void fireEventGet(T dto) {
		getEventLauncher().fireAsync(new GetEvent(dto));
	}

	protected void fireEventTemplate(T dto) {
		getEventLauncher().fireAsync(new TemplateEvent(dto));
	}

	protected void fireEventInsert(T dto) {
		getEventLauncher().fireAsync(new InsertEvent(dto));
	}

	protected void fireEventUpdate(T oldDto, T dto) {
		getEventLauncher().fireAsync(new UpdateEvent(dto, oldDto));
	}

	protected void fireEventPatch(T oldDto, T dto) {
		getEventLauncher().fireAsync(new PatchEvent(dto, oldDto));
	}

	protected void fireEventDelete(T dto) {
		getEventLauncher().fireAsync(new DeleteEvent(dto));
	}

	protected void fireEventList(SearchRequest req, SearchResponse<V> res) {
		getEventLauncher().fireAsync(new ListEvent<>(req, res));
	}

	/**
	 * Return if the DAO/DTO objects that manages this controller has language
	 * support
	 */
	protected boolean hasLanguageSupport() {
		return getDaoRegistry().hasLanguageSupport(getTableDao());
	}

}
