package es.prodevelop.pui9.export;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.common.model.dto.PuiModelPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.search.ExportColumnDefinition;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.utils.PuiDateUtil;

public abstract class AbstractDataExporter implements IDataExporter {

	@Autowired
	protected DaoRegistry daoRegistry;

	@Autowired
	private IPuiModelService modelService;

	protected AbstractDataExporter() {
		DataExporterRegistry.getSingleton().registerExporter(this);
	}

	@Override
	public final FileDownload generate(ExportRequest req) {
		try {
			modelService.guessModel(req);
		} catch (PuiServiceGetException e) {
			// do nothing
		}
		if (StringUtils.isEmpty(req.getExportTitle())) {
			req.setExportTitle("title");
		}
		fillColumns(req);
		InputStream is = doGenerate(req);
		return createFileDownload(req, is);
	}

	/**
	 * Do the generation of the export data
	 * 
	 * @param req The request of the export
	 * @return The generated input stream
	 */
	protected abstract InputStream doGenerate(ExportRequest req);

	/**
	 * In case the columns to be exported are not providen, fill the columns list
	 * with all the columns of the table
	 * 
	 * @param req
	 */
	private void fillColumns(ExportRequest req) {
		if (CollectionUtils.isEmpty(req.getExportColumns())) {
			Class<? extends IDto> dtoClass = getDtoClass(req.getModel());
			List<ExportColumnDefinition> columns = new ArrayList<>();
			AtomicInteger index = new AtomicInteger(0);
			DtoRegistry.getColumnNames(dtoClass).forEach(col -> {
				ExportColumnDefinition ecd = ExportColumnDefinition.of(col, col, index.getAndIncrement(), null);
				if (DtoRegistry.getDateTimeFields(dtoClass).contains(col)) {
					ecd.setDateformat(PuiUserSession.getCurrentSession().getDateformat() + " HH:mm:ss");
				}
				columns.add(ecd);
			});
			req.setExportColumns(columns);
		}

		Collections.sort(req.getExportColumns());
	}

	/**
	 * Get the list of data to be exported. This data is represented by a list of
	 * registries, and each registry is represented by itself as a list of pairs,
	 * where each pair is the attribute to be exported (left side) and its value
	 * (right side)
	 * 
	 * @param req The request of the export
	 * @return
	 */
	protected List<List<Pair<String, Object>>> getData(ExportRequest req) {
		Class<? extends IDto> dtoClass = getDtoClass(req.getModel());

		List<List<Pair<String, Object>>> list = new ArrayList<>();
		List<?> data = null;
		try {
			data = modelService.search(req).getData();
		} catch (PuiServiceGetException e) {
			data = Collections.emptyList();
		}

		data.forEach(d -> {
			List<Pair<String, Object>> newData = new ArrayList<>();
			req.getExportColumns().forEach(ec -> {
				Field field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, ec.getName());
				if (field == null) {
					field = DtoRegistry.getJavaFieldFromFieldName(dtoClass, ec.getName());
				}
				Object value;
				try {
					value = field.get(d);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					value = null;
				}
				newData.add(Pair.of(ec.getName(), value));
			});
			list.add(newData);
		});

		return list;
	}

	protected Class<? extends IDto> getDtoClass(String model) {
		IPuiModel puimodel;
		try {
			puimodel = modelService.getByPk(new PuiModelPk(model));
		} catch (PuiServiceGetException e) {
			return null;
		}
		return daoRegistry.getDtoFromEntityName(puimodel.getEntity(), false, false);
	}

	private FileDownload createFileDownload(ExportRequest req, InputStream is) {
		return new FileDownload(is, req.getExportTitle().replaceAll(" ", "_") + "." + getExportType().extension);
	}

	protected ZonedDateTime getCurrentTimeAtUserTimeZone() {
		return PuiDateUtil.getInstantAtZoneId(Instant.now(), PuiUserSession.getCurrentSession().getTimezone());
	}

	protected String getString(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return String.valueOf(((BigDecimal) value).doubleValue());
		}
		return value.toString();
	}

	protected String getInstantAsString(Object value, String dateformat) {
		ZonedDateTime zdt = PuiDateUtil.getInstantAtZoneId((Instant) value,
				PuiUserSession.getCurrentSession().getTimezone());
		if (dateformat == null) {
			dateformat = PuiUserSession.getCurrentSession().getDateformat() + " HH:mm:ss";
		}

		TemporalAccessor ta;
		if (dateformat.toUpperCase().contains("HH")) {
			ta = zdt;
		} else {
			ta = zdt.toLocalDate();
		}
		return PuiDateUtil.temporalAccessorToString(ta,
				DateTimeFormatter.ofPattern(dateformat).withZone(PuiUserSession.getCurrentSession().getTimezone()));
	}

	protected BigDecimal getBigDecimal(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		} else {
			return new BigDecimal(value.toString());
		}
	}

}
