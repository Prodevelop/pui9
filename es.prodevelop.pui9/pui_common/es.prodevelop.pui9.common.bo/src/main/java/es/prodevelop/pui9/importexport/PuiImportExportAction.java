package es.prodevelop.pui9.importexport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IllegalFormatConversionException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportDtoColumnErrorException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidColumnException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportInvalidModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportNoModelException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportPkNotIncludedException;
import es.prodevelop.pui9.common.exceptions.PuiCommonImportExportWithErrorsException;
import es.prodevelop.pui9.common.model.dto.PuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexport;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiImportexportPk;
import es.prodevelop.pui9.common.model.dto.interfaces.IPuiModel;
import es.prodevelop.pui9.common.service.interfaces.IPuiImportexportService;
import es.prodevelop.pui9.common.service.interfaces.IPuiModelService;
import es.prodevelop.pui9.components.PuiApplicationContext;
import es.prodevelop.pui9.csv.CsvReader;
import es.prodevelop.pui9.csv.CsvWriter;
import es.prodevelop.pui9.exceptions.PuiDaoFindException;
import es.prodevelop.pui9.exceptions.PuiDaoListException;
import es.prodevelop.pui9.exceptions.PuiException;
import es.prodevelop.pui9.exceptions.PuiServiceException;
import es.prodevelop.pui9.exceptions.PuiServiceGetException;
import es.prodevelop.pui9.exceptions.PuiServiceInsertException;
import es.prodevelop.pui9.exceptions.PuiServiceNewException;
import es.prodevelop.pui9.exceptions.PuiServiceUpdateException;
import es.prodevelop.pui9.file.FileDownload;
import es.prodevelop.pui9.filter.FilterBuilder;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dao.interfaces.IDao;
import es.prodevelop.pui9.model.dao.interfaces.ITableDao;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoFactory;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.ITableDto;
import es.prodevelop.pui9.order.Order;
import es.prodevelop.pui9.search.ExportColumnDefinition;
import es.prodevelop.pui9.search.ExportRequest;
import es.prodevelop.pui9.search.ExportType;
import es.prodevelop.pui9.search.SearchRequest;
import es.prodevelop.pui9.service.interfaces.IService;
import es.prodevelop.pui9.service.registry.ServiceRegistry;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiConstants;
import es.prodevelop.pui9.utils.PuiDateUtil;
import es.prodevelop.pui9.utils.PuiLanguage;

@Component
public class PuiImportExportAction {

	protected static final String CSV_FILE_EXTENSION = ".csv";
	protected static final String JSON_FILE_EXTENSION = ".json";

	@Autowired
	protected DaoRegistry daoRegistry;

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private IPuiModelService modelService;

	@Autowired
	private IPuiImportexportService importExportService;

	@Autowired
	private ImportUtil importUtil;

	@Autowired
	private ExportUtil exportUtil;

	private List<String> modelsCache;

	@PostConstruct
	private void postConstruct() {
		if (modelsCache != null) {
			return;
		}

		modelsCache = new ArrayList<>();

		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ReloadImportExportActionModels", true, 1, 1,
				TimeUnit.HOURS, () -> reloadModels(true));
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
					.filter(entry -> entry.getValue().getDefaultConfiguration().isActionImportExport())
					.map(Entry::getKey).collect(Collectors.toList()));
		}
	}

	public ImportUtil getImportUtil() {
		return importUtil;
	}

	public ExportUtil getExportUtil() {
		return exportUtil;
	}

	private synchronized void checkModelAvailable(String model) throws PuiCommonImportExportInvalidModelException {
		reloadModels(false);

		if (!modelsCache.contains(model)) {
			throw new PuiCommonImportExportInvalidModelException(model);
		}
	}

	private List<ExportColumnDefinition> getExportableColumns(String model)
			throws PuiCommonImportExportInvalidModelException {
		checkModelAvailable(model);
		Class<? extends ITableDto> dtoClass = getDtoClass(model);
		List<ExportColumnDefinition> columns = new ArrayList<>();

		DtoRegistry.getAllColumnNames(dtoClass).stream().forEach(colName -> {
			String fieldName = DtoRegistry.getFieldNameFromColumnName(dtoClass, colName);
			columns.add(ExportColumnDefinition.of(colName, fieldName, -1, null));
		});

		Collections.sort(columns, new CompareColumns(dtoClass));
		IntStream.range(0, columns.size()).boxed().forEach(i -> columns.get(i).setOrder(i));

		return columns;
	}

	private void checkColumns(String model, List<String> exportColumns)
			throws PuiCommonImportExportPkNotIncludedException, PuiCommonImportExportInvalidColumnException,
			PuiCommonImportExportInvalidModelException {
		Class<? extends ITableDto> dtoClass = getDtoClass(model);
		List<String> columnNames = getExportableColumns(model).stream().map(ExportColumnDefinition::getName)
				.collect(Collectors.toList());
		List<String> pkColumnNames = DtoRegistry.getPkFields(getDtoClass(model)).stream()
				.map(pkField -> DtoRegistry.getColumnNameFromFieldName(dtoClass, pkField)).collect(Collectors.toList());

		// pk is included?
		if (pkColumnNames.size() != exportColumns.stream().filter(pkColumnNames::contains).count()) {
			throw new PuiCommonImportExportPkNotIncludedException(pkColumnNames.toArray(new String[0]));
		}

		// any invalid column?
		Optional<String> ecd = exportColumns.stream().filter(col -> !columnNames.contains(col)).findAny();
		if (ecd.isPresent()) {
			throw new PuiCommonImportExportInvalidColumnException(ecd.get());
		}
	}

	private Class<? extends ITableDto> getDtoClass(String model) {
		Class<? extends ITableDto> dtoClass = daoRegistry.getDtoFromModelId(model, false, false);
		if (dtoClass == null) {
			SearchRequest req = new SearchRequest();
			req.setModel(model);
			IPuiModel puiModel;
			try {
				puiModel = modelService.guessModel(req);
			} catch (PuiServiceGetException e) {
				return null;
			}
			dtoClass = daoRegistry
					.getTableDtoFromViewDto(daoRegistry.getDtoFromEntityName(puiModel.getEntity(), false, false));
		}

		return dtoClass;
	}

	private String asString(Object value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		return value.toString();
	}

	private BigDecimal asBigDecimal(Object value) throws IllegalFormatConversionException {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		} else {
			try {
				return new BigDecimal(value.toString().replace(',', '.'));
			} catch (Exception e) {
				throw new IllegalFormatConversionException('a', Instant.class);
			}
		}
	}

	private Integer stringAsInteger(String value) throws IllegalFormatConversionException {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		try {
			return Integer.valueOf(value);
		} catch (Exception e) {
			throw new IllegalFormatConversionException('a', Instant.class);
		}
	}

	private Instant stringAsInstant(String value) throws IllegalFormatConversionException {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		try {
			return Instant.parse(value);
		} catch (Exception e1) {
			try {
				LocalDateTime ldt = PuiDateUtil.stringToLocalDateTime(value);
				if (ldt == null) {
					throw new IllegalFormatConversionException('a', Instant.class);
				}
				ZonedDateTime zdt = ZonedDateTime.of(ldt, PuiUserSession.getCurrentSession().getTimezone());
				return zdt.toInstant();
			} catch (Exception e2) {
				throw new IllegalFormatConversionException('a', Instant.class);
			}
		}
	}

	private String instantAsString(Object value, DateTimeFormatter dtf) {
		ZonedDateTime zdt = PuiDateUtil.getInstantAtZoneId((Instant) value,
				PuiUserSession.getCurrentSession().getTimezone());
		return PuiDateUtil.temporalAccessorToString(zdt, dtf);
	}

	private class CompareColumns implements Comparator<ExportColumnDefinition> {

		private List<String> pkColumns;

		public CompareColumns(Class<? extends IDto> dtoClass) {
			pkColumns = DtoRegistry.getPkFields(dtoClass).stream()
					.map(pk -> DtoRegistry.getColumnNameFromFieldName(dtoClass, pk)).collect(Collectors.toList());
		}

		@Override
		public int compare(ExportColumnDefinition col1, ExportColumnDefinition col2) {
			if (pkColumns.contains(col1.getName())) {
				return -3;
			} else if (pkColumns.contains(col2.getName())) {
				return 3;
			} else if (IDto.LANG_COLUMN_NAME.equals(col1.getName())) {
				return 2;
			} else if (IDto.LANG_STATUS_COLUMN_NAME.equals(col1.getName())) {
				return 1;
			} else if (IDto.LANG_COLUMN_NAME.equals(col2.getName())) {
				return -2;
			} else if (IDto.LANG_STATUS_COLUMN_NAME.equals(col2.getName())) {
				return -1;
			} else {
				return col1.getOrder().compareTo(col2.getOrder());
			}
		}

	}

	@Component
	public class ExportUtil {

		public List<ExportColumnDefinition> getExportableColumns(String model)
				throws PuiCommonImportExportInvalidModelException {
			return PuiImportExportAction.this.getExportableColumns(model);
		}

		public FileDownload export(ExportRequest req)
				throws PuiCommonImportExportNoModelException, PuiCommonImportExportPkNotIncludedException,
				PuiCommonImportExportInvalidColumnException, PuiCommonImportExportInvalidModelException {
			if (StringUtils.isEmpty(req.getModel())) {
				throw new PuiCommonImportExportNoModelException();
			}

			checkModelAvailable(req.getModel());
			fillColumns(req);
			checkColumns(req.getModel(),
					req.getExportColumns().stream().map(ExportColumnDefinition::getName).collect(Collectors.toList()));
			setOrder(req);

			req.setExportType(ExportType.csv);
			req.setPage(SearchRequest.DEFAULT_PAGE);
			req.setRows(SearchRequest.NUM_MAX_ROWS);
			if (StringUtils.isEmpty(req.getQueryLang())) {
				req.setQueryLang(PuiUserSession.getCurrentSession().getLanguage().getIsocode());
			}

			// backup the columns and order or the original request
			List<ExportColumnDefinition> bakColumns = req.getExportColumns();
			List<Order> bakOrder = req.getOrder();

			// retrieve registries from view
			req.setExportColumns(null);
			req.setOrder(null);
			List<Object> recordsFromView = findFromView(req);

			// retrieve registries from table
			FilterBuilder pkFilterBuilder = mountFilterWithPks(req.getModel(), recordsFromView);
			SearchRequest searchReq = new SearchRequest();
			searchReq.setModel(req.getModel());
			searchReq.setFilter(pkFilterBuilder.asFilterGroup());
			searchReq.setOrder(bakOrder);
			List<Object> recordsFromTable = findFromTable(searchReq);

			InputStream is = generateCsv(req.getModel(), bakColumns, recordsFromTable);
			return new FileDownload(is, req.getModel() + CSV_FILE_EXTENSION);
		}

		private void fillColumns(ExportRequest req) {
			if (CollectionUtils.isEmpty(req.getExportColumns())) {
				req.setExportColumns(new ArrayList<>());
			}

			Class<? extends ITableDto> dtoClass = getDtoClass(req.getModel());

			// include PKs if not included, at the beggining of the list
			DtoRegistry.getPkFields(dtoClass).stream()
					.map(pkField -> DtoRegistry.getColumnNameFromFieldName(dtoClass, pkField))
					.filter(pk -> !req.getExportColumns().stream().map(ExportColumnDefinition::getName)
							.collect(Collectors.toList()).contains(pk))
					.forEach(pk -> req.getExportColumns().add(0, ExportColumnDefinition.of(pk, pk, -1, null)));

			// include mandatory columns
			DtoRegistry.getNotNullFields(dtoClass).stream()
					.map(notNull -> DtoRegistry.getColumnNameFromFieldName(dtoClass, notNull))
					.filter(notNull -> !req.getExportColumns().stream().map(ExportColumnDefinition::getName)
							.collect(Collectors.toList()).contains(notNull))
					.forEach(notNull -> req.getExportColumns()
							.add(ExportColumnDefinition.of(notNull, notNull, -1, null)));

			IntStream.range(0, req.getExportColumns().size()).boxed()
					.forEach(i -> req.getExportColumns().get(i).setOrder(i));

			Collections.sort(req.getExportColumns(), new CompareColumns(dtoClass));
		}

		private void setOrder(ExportRequest req) {
			if (CollectionUtils.isEmpty(req.getOrder())) {
				List<String> pkFieldNames = DtoRegistry.getPkFields(getDtoClass(req.getModel()));
				List<Order> order = new ArrayList<>();
				pkFieldNames.forEach(pk -> order.add(Order.newOrderAsc(pk)));
				req.setOrder(order);
			}
		}

		private List<Object> findFromView(ExportRequest req) {
			try {
				return modelService.search(req).getData();
			} catch (PuiServiceGetException e) {
				return Collections.emptyList();
			}
		}

		@SuppressWarnings("unchecked")
		private FilterBuilder mountFilterWithPks(String model, List<Object> recordsFromView) {
			FilterBuilder filterBuilder = FilterBuilder.newOrFilter();
			if (CollectionUtils.isEmpty(recordsFromView)) {
				return filterBuilder;
			}

			Class<? extends ITableDto> dtoClass = getDtoClass(model);
			List<String> pkFieldNames = DtoRegistry.getPkFields(dtoClass);

			if (pkFieldNames.size() == 1) {
				// the PK is simple
				String pkFieldName = pkFieldNames.get(0);
				Field pkField = DtoRegistry.getJavaFieldFromFieldName((Class<IDto>) recordsFromView.get(0).getClass(),
						pkFieldName);
				boolean isNumeric = DtoRegistry.getNumericFields(dtoClass).contains(pkFieldName);
				boolean isString = DtoRegistry.getStringFields(dtoClass).contains(pkFieldName);
				if (isNumeric) {
					List<Number> pks = new ArrayList<>();
					recordsFromView.forEach(rec -> {
						try {
							pks.add((Number) pkField.get(rec));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// continue
						}
					});
					filterBuilder.addInNumber(pkFieldName, pks);
				} else if (isString) {
					List<String> pks = new ArrayList<>();
					recordsFromView.forEach(rec -> {
						try {
							pks.add((String) pkField.get(rec));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// continue
						}
					});
					filterBuilder.addInString(pkFieldName, pks);
				}
			} else {
				// the PK is composed
				recordsFromView.forEach(rec -> {
					FilterBuilder fbRecord = FilterBuilder.newAndFilter();
					pkFieldNames.forEach(pkName -> {
						Field pkField = DtoRegistry.getJavaFieldFromFieldName((Class<IDto>) rec.getClass(), pkName);
						try {
							fbRecord.addEquals(pkName, pkField.get(rec));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// continue
						}
					});
					filterBuilder.addGroup(fbRecord);
				});
			}

			return filterBuilder;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private List<Object> findFromTable(SearchRequest req) {
			Class<? extends IDao> daoClass = daoRegistry.getDaoFromModelId(req.getModel());
			IDao<?> dao = PuiApplicationContext.getInstance().getBean(daoClass);

			try {
				return (List<Object>) dao.findForDataGrid(req).getData();
			} catch (PuiDaoListException e) {
				return Collections.emptyList();
			}
		}

		private InputStream generateCsv(String model, List<ExportColumnDefinition> exportColumns,
				List<Object> recordsFromTable) {
			List<List<Pair<String, Object>>> data = convertData(model, exportColumns, recordsFromTable);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			CsvWriter writer = new CsvWriter(baos, StandardCharsets.UTF_8);

			generateTableHeader(writer, exportColumns);
			generateTableContent(writer, model, data);

			writer.close();

			InputStream is = new ByteArrayInputStream(baos.toByteArray());
			try {
				baos.close();
			} catch (IOException e) {
				// do nothing
			}

			return is;
		}

		private List<List<Pair<String, Object>>> convertData(String model, List<ExportColumnDefinition> exportColumns,
				List<Object> data) {
			Class<? extends ITableDto> dtoClass = getDtoClass(model);
			List<List<Pair<String, Object>>> list = new ArrayList<>();

			data.forEach(d -> {
				List<Pair<String, Object>> newData = new ArrayList<>();
				exportColumns.forEach(ec -> {
					Field field = DtoRegistry.getJavaFieldFromAllFields(dtoClass, ec.getName());
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

		private void generateTableHeader(CsvWriter writer, List<ExportColumnDefinition> exportColumns) {
			// first row is the name of the column
			exportColumns.forEach(ec -> {
				try {
					writer.write(ec.getName());
				} catch (IOException e) {
					// do nothing
				}
			});
			try {
				writer.endRecord();
			} catch (IOException e) {
				// do nothing
			}

			// second row is the title of the column
			exportColumns.forEach(ec -> {
				try {
					writer.write(ec.getTitle());
				} catch (IOException e) {
					// do nothing
				}
			});
			try {
				writer.endRecord();
			} catch (IOException e) {
				// do nothing
			}
		}

		private void generateTableContent(CsvWriter writer, String model, List<List<Pair<String, Object>>> data) {
			DateTimeFormatter dtf = DateTimeFormatter
					.ofPattern(PuiUserSession.getCurrentSession().getDateformat() + " HH:mm:ss");
			Class<? extends ITableDto> dtoClass = getDtoClass(model);

			data.forEach(record -> {
				record.forEach(pair -> {
					String fieldname = DtoRegistry.getFieldNameFromColumnName(dtoClass, pair.getKey());
					String value = null;
					if (DtoRegistry.getDateTimeFields(dtoClass).contains(fieldname)) {
						value = instantAsString(pair.getValue(), dtf);
					} else if (DtoRegistry.getFloatingFields(dtoClass).contains(fieldname)) {
						value = asString(asBigDecimal(pair.getValue()));
					} else {
						value = asString(pair.getValue());
					}

					try {
						writer.write(value);
					} catch (IOException e) {
						// do nothing
					}
				});

				try {
					writer.endRecord();
				} catch (IOException e) {
					// do nothing
				}
			});
		}

	}

	@Component
	public class ImportUtil {

		private static final String PK_SEPARATOR = "#";
		private static final String NEW_RECORD_PREFIX = "NEW_";

		@Transactional(rollbackFor = PuiException.class)
		public ImportData prepareImport(String model, InputStream is)
				throws PuiCommonImportExportPkNotIncludedException, PuiCommonImportExportInvalidColumnException,
				PuiCommonImportExportInvalidModelException, PuiServiceInsertException {
			checkModelAvailable(model);

			byte[] bytes;
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				IOUtils.copy(is, os);
				bytes = os.toByteArray();
			} catch (IOException e) {
				return null;
			}

			CsvReader reader = new CsvReader(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8, true);

			List<String> columnNames = readColumnNames(reader);
			List<String> columnTitles = readColumnTitles(reader);
			checkColumns(model, columnNames);

			ImportData importData = new ImportData(PuiUserSession.getCurrentSession().getUsr(), model,
					PuiUserSession.getCurrentSession().getLanguage().getIsocode(),
					DtoRegistry.getPkFields(getDtoClass(model)), columnNames, columnTitles);
			readRecords(reader, importData);

			FilterBuilder filterBuilder = mountFilterWithPks(importData);
			List<ITableDto> dtos = findFromTable(model, filterBuilder, importData.getLanguage());
			compareRecords(importData, dtos);
			saveToDatabase(importData);
			copyToFileSystem(new ByteArrayInputStream(bytes), importData);

			return importData;
		}

		@Transactional(rollbackFor = PuiException.class)
		public void performImport(IPuiImportexportPk pk) throws PuiServiceException {
			IPuiImportexport importExport = importExportService.getByPk(pk);
			if (importExport.getExecuted().equals(PuiConstants.TRUE_INT)) {
				throw new PuiServiceException(new Exception("Ya se había ejecutado"));
			}

			String importFolder = importExportService.getImportFolder(importExport.getModel(), importExport.getId());
			String fileName = getFileName(importExport.getDatetime(), importExport.getUsr());

			String jsonFileName = importFolder + fileName + JSON_FILE_EXTENSION;

			String json;
			try {
				json = FileUtils.readFileToString(new File(jsonFileName), StandardCharsets.UTF_8);
			} catch (IOException e) {
				return;
			}

			ImportData importData = GsonSingleton.getSingleton().getGson().fromJson(json, ImportData.class);
			checkImportData(importData);
			executeImport(pk, importData);
		}

		@Transactional(rollbackFor = PuiException.class)
		public void cancelImport(IPuiImportexportPk pk) throws PuiServiceException {
			IPuiImportexport importExport = importExportService.getByPk(pk);

			String importFolder = importExportService.getImportFolder(importExport.getModel(), importExport.getId());
			FileUtils.deleteQuietly(new File(importFolder));

			importExportService.delete(pk);
		}

		private List<String> readColumnNames(CsvReader reader) {
			List<String> columnNames = new ArrayList<>();

			// read the headers in the first row
			try {
				reader.readHeaders();
				columnNames.addAll(Arrays.asList(reader.getHeaders()));
			} catch (IOException e) {
				// do nothing
			}

			return columnNames;
		}

		private List<String> readColumnTitles(CsvReader reader) {
			List<String> columnTitles = new ArrayList<>();

			// read the second row, that contains the title of the columns
			try {
				reader.readRecord();
				columnTitles.addAll(Arrays.asList(reader.getValues()));
			} catch (IOException e) {
				// do nothing
			}

			return columnTitles;
		}

		private void readRecords(CsvReader reader, ImportData importData) {
			Class<? extends ITableDto> dtoClass = getDtoClass(importData.getModel());

			try {
				AtomicReference<String> language = new AtomicReference<>();
				while (reader.readRecord()) {
					ImportDataRecord record = new ImportDataRecord();
					importData.getColumns().forEach(col -> {
						String value;
						try {
							value = reader.get(col);
						} catch (IOException e) {
							return;
						}

						Object val;
						boolean hasError = false;
						try {
							if (DtoRegistry.getDateTimeFields(dtoClass).contains(col)) {
								val = stringAsInstant(value);
							} else if (DtoRegistry.getFloatingFields(dtoClass).contains(col)) {
								val = asBigDecimal(value);
							} else if (DtoRegistry.getNumericFields(dtoClass).contains(col)) {
								val = stringAsInteger(value);
							} else {
								val = asString(value);
							}
						} catch (IllegalFormatConversionException e) {
							val = value;
							hasError = true;
						}

						record.addAttribute(col, new ImportDataAttribute(val, hasError));
						if (hasError) {
							record.setStatus(ImportDataRecordStatus.ERROR);
						}

						if (IDto.LANG_COLUMN_NAME.equals(col) && language.get() == null) {
							language.set(val.toString());
						}
					});

					StringBuilder pkBuilder = new StringBuilder();
					for (Iterator<String> pkIt = importData.getPks().iterator(); pkIt.hasNext();) {
						ImportDataAttribute ida = record.getAttributes().get(pkIt.next());
						if (ida.getValue() != null) {
							pkBuilder.append(ida.getValue());
							if (pkIt.hasNext()) {
								pkBuilder.append(PK_SEPARATOR);
							}
						} else {
							pkBuilder = new StringBuilder(NEW_RECORD_PREFIX);
							break;
						}
					}

					if (pkBuilder.toString().equals(NEW_RECORD_PREFIX)) {
						if (record.getStatus().equals(ImportDataRecordStatus.ERROR)) {
							record.setStatus(ImportDataRecordStatus.NEW_ERROR);
						} else {
							record.setStatus(ImportDataRecordStatus.NEW);
						}
					}

					importData.addRecord(pkBuilder.toString(), record);
				}

				if (language.get() != null) {
					importData.setLanguage(language.get());
				}

				reader.close();
			} catch (IOException e) {
				// do nothing
			}
		}

		private FilterBuilder mountFilterWithPks(ImportData importData) {
			Class<? extends ITableDto> dtoClass = getDtoClass(importData.getModel());
			FilterBuilder filterBuilder = FilterBuilder.newOrFilter();
			if (CollectionUtils.isEmpty(importData.getRecords())) {
				return filterBuilder;
			}

			if (importData.getPks().size() == 1) {
				// the PK is simple
				String pkName = importData.getPks().get(0);
				boolean isNumeric = DtoRegistry.getNumericFields(dtoClass).contains(pkName);
				boolean isString = DtoRegistry.getStringFields(dtoClass).contains(pkName);
				if (isNumeric) {
					List<Number> pks = new ArrayList<>();
					importData.getRecords().keySet().stream().filter(pk -> !pk.equals(NEW_RECORD_PREFIX))
							.forEach(pk -> pks.add(Integer.parseInt(pk)));
					filterBuilder.addInNumber(pkName, pks);
				} else if (isString) {
					List<String> pks = new ArrayList<>();
					importData.getRecords().keySet().forEach(pks::add);
					filterBuilder.addInString(pkName, pks);
				}
			} else {
				// the PK is composed
				importData.getRecords().keySet().stream().filter(pk -> !pk.equals(NEW_RECORD_PREFIX)).forEach(pkRec -> {
					FilterBuilder fbRecord = FilterBuilder.newAndFilter();
					String[] pkValues = pkRec.split(PK_SEPARATOR);
					for (int i = 0; i < pkValues.length; i++) {
						fbRecord.addEquals(importData.getPks().get(i), pkValues[i]);
					}
					filterBuilder.addGroup(fbRecord);
				});
			}

			return filterBuilder;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private List<ITableDto> findFromTable(String model, FilterBuilder filterBuilder, String language) {
			Class<? extends IDao> daoClass = daoRegistry.getDaoFromModelId(model);
			IDao<?> dao = PuiApplicationContext.getInstance().getBean(daoClass);
			try {
				return (List<ITableDto>) dao.findWhere(filterBuilder, new PuiLanguage(language));
			} catch (PuiDaoFindException e) {
				return Collections.emptyList();
			}
		}

		private void compareRecords(ImportData importData, List<ITableDto> dtos) {
			DateTimeFormatter dtf = DateTimeFormatter
					.ofPattern(PuiUserSession.getCurrentSession().getDateformat() + " HH:mm:ss");
			Class<? extends ITableDto> dtoClass = getDtoClass(importData.getModel());

			for (ITableDto dto : dtos) {
				StringBuilder pkBuilder = new StringBuilder();
				for (String pk : importData.getPks()) {
					if (pkBuilder.length() > 0) {
						pkBuilder.append(PK_SEPARATOR);
					}
					try {
						pkBuilder.append(DtoRegistry.getJavaFieldFromColumnName(dtoClass, pk).get(dto));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						pkBuilder = null;
						break;
					}
				}

				if (pkBuilder == null) {
					continue;
				}

				ImportDataRecord idr = importData.getRecords().get(pkBuilder.toString());
				if (idr == null) {
					continue;
				}

				boolean recordHasChanged = false;
				boolean recordHasErrror = idr.getStatus().equals(ImportDataRecordStatus.ERROR);
				for (String attr : idr.getAttributes().keySet()) {
					ImportDataAttribute ida = idr.getAttributes().get(attr);

					Object dtoAttrValue;
					Field field;
					field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, attr);
					if (field == null) {
						field = DtoRegistry.getJavaFieldFromLangColumnName(dtoClass, attr);
					}
					try {
						dtoAttrValue = field.get(dto);
						if (DtoRegistry.getDateTimeFields(dtoClass).contains(field.getName())) {
							String str = instantAsString(dtoAttrValue, dtf);
							dtoAttrValue = stringAsInstant(str);
						} else if (DtoRegistry.getStringFields(dtoClass).contains(field.getName())) {
							if (StringUtils.isEmpty(dtoAttrValue)) {
								dtoAttrValue = null;
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						ida.setStatus(ImportDataAttributeStatus.ERROR);
						recordHasErrror = true;
						break;
					}

					if (ida.getStatus().equals(ImportDataAttributeStatus.ERROR)) {
						ida.setOldValue(dtoAttrValue);
						continue;
					}

					if (dtoAttrValue == null && ida.getValue() == null) {
						ida.setStatus(ImportDataAttributeStatus.UNMODIFIED);
					} else if (dtoAttrValue == null && ida.getValue() != null) {
						if (field.getType().equals(ida.getValue().getClass())) {
							ida.setStatus(ImportDataAttributeStatus.MODIFIED);
							ida.setOldValue(dtoAttrValue);
							recordHasChanged = true;
						} else {
							ida.setStatus(ImportDataAttributeStatus.ERROR);
							recordHasErrror = true;
							break;
						}
					} else if (dtoAttrValue != null && ida.getValue() == null) {
						if (DtoRegistry.getNotNullFields(dtoClass).contains(field.getName())) {
							ida.setStatus(ImportDataAttributeStatus.ERROR);
							recordHasErrror = true;
							break;
						} else {
							ida.setStatus(ImportDataAttributeStatus.MODIFIED);
							ida.setOldValue(dtoAttrValue);
							recordHasChanged = true;
						}
					} else if (dtoAttrValue != null && ida.getValue() != null) {
						if (Objects.equals(dtoAttrValue, ida.getValue())) {
							ida.setStatus(ImportDataAttributeStatus.UNMODIFIED);
						} else if (dtoAttrValue.getClass().equals(ida.getValue().getClass())) {
							ida.setStatus(ImportDataAttributeStatus.MODIFIED);
							ida.setOldValue(dtoAttrValue);
							recordHasChanged = true;
						} else {
							ida.setStatus(ImportDataAttributeStatus.ERROR);
							recordHasErrror = true;
							break;
						}
					}
				}

				if (recordHasErrror) {
					idr.setStatus(ImportDataRecordStatus.ERROR);
				} else if (recordHasChanged) {
					idr.setStatus(ImportDataRecordStatus.MODIFIED);
				} else {
					idr.setStatus(ImportDataRecordStatus.UNMODIFIED);
				}
			}

			importData.getRecords().values().forEach(rec -> {
				switch (rec.getStatus()) {
				case UNMODIFIED:
					importData.addUnmodifiedRecord();
					break;
				case MODIFIED:
					importData.addModifiedRecord();
					break;
				case NEW:
					importData.addNewRecord();
					break;
				case NEW_ERROR:
					importData.addNewWithErrorsRecord();
					break;
				case ERROR:
					importData.addErrorRecord();
					break;
				}
			});
		}

		private void copyToFileSystem(InputStream is, ImportData importData) {
			String csv;
			try {
				csv = IOUtils.toString(is, StandardCharsets.UTF_8);
			} catch (IOException e) {
				// do nothing
				return;
			}
			String json = GsonSingleton.getSingleton().getGson().toJson(importData);

			String importFolder = importExportService.getImportFolder(importData.getModel(), importData.getId());
			String fileName = getFileName(importData.getImportTime(), importData.getUser());

			String csvFileName = importFolder + fileName + CSV_FILE_EXTENSION;
			String jsonFileName = importFolder + fileName + JSON_FILE_EXTENSION;
			try {
				FileUtils.write(new File(csvFileName), csv, StandardCharsets.UTF_8);
				FileUtils.write(new File(jsonFileName), json, StandardCharsets.UTF_8);
			} catch (IOException e) {
				// do nothing
			}
		}

		private void saveToDatabase(ImportData importData) throws PuiServiceInsertException {
			String fileName = getFileName(importData.getImportTime(), importData.getUser());

			IPuiImportexport importExport = new PuiImportexport();
			importExport.setModel(importData.getModel());
			importExport.setUsr(importData.getUser());
			importExport.setDatetime(importData.getImportTime());
			importExport.setFilenamecsv(fileName + CSV_FILE_EXTENSION);
			importExport.setFilenamejson(fileName + JSON_FILE_EXTENSION);
			importExport.setExecuted(PuiConstants.FALSE_INT);

			importExport = importExportService.insert(importExport);
			importData.setId(importExport.getId());
		}

		private void checkImportData(ImportData importData) throws PuiCommonImportExportWithErrorsException {
			if (importData.getErrorRecords() > 0) {
				throw new PuiCommonImportExportWithErrorsException();
			}

			Class<? extends ITableDto> dtoClass = getDtoClass(importData.getModel());
			for (ImportDataRecord record : importData.getRecords().values()) {
				if (record.getStatus().equals(ImportDataRecordStatus.ERROR)) {
					throw new PuiCommonImportExportWithErrorsException();
				}
				if (record.getStatus().equals(ImportDataRecordStatus.MODIFIED)
						|| record.getStatus().equals(ImportDataRecordStatus.NEW)) {
					record.getAttributes().forEach((field, attr) -> {
						if (DtoRegistry.getDateTimeFields(dtoClass).contains(field)) {
							attr.setValue(stringAsInstant((String) attr.getValue()));
						}
						if (DtoRegistry.getFloatingFields(dtoClass).contains(field)
								&& attr.getValue() instanceof Integer) {
							attr.setValue(new BigDecimal((Integer) attr.getValue()));
						}
					});
				}
			}
		}

		private void executeImport(IPuiImportexportPk pk, ImportData importData) throws PuiServiceException {
			for (ImportDataRecord record : importData.getRecords().values()) {
				if (record.getStatus().equals(ImportDataRecordStatus.MODIFIED)) {
					modifyRecord(importData.getModel(), importData.getLanguage(), importData.getPks(), record);
				} else if (record.getStatus().equals(ImportDataRecordStatus.NEW)) {
					createRecord(importData.getModel(), record);
				}
			}

			importExportService.patch(pk,
					Collections.singletonMap(IPuiImportexport.EXECUTED_FIELD, PuiConstants.TRUE_INT));
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void modifyRecord(String model, String language, List<String> pkColumns, ImportDataRecord record)
				throws PuiCommonImportExportDtoColumnErrorException, PuiServiceUpdateException {
			Class<? extends IService> serviceClass = serviceRegistry.getServiceFromModel(model);
			IService service = PuiApplicationContext.getInstance().getBean(serviceClass);
			Class<? extends ITableDao<ITableDto, ITableDto>> daoClass = daoRegistry.getDaoFromModelId(model);
			Class<? extends ITableDto> dtoPkClass = daoRegistry.getDtoFromDao(daoClass, true);
			ITableDto dtoPk = DtoFactory.createInstanceFromInterface(dtoPkClass);

			String columnNameError = null;
			Object columnValueError = null;
			for (String pkColumn : pkColumns) {
				Field field = DtoRegistry.getJavaFieldFromColumnName(dtoPkClass, pkColumn);
				try {
					field.set(dtoPk, record.getAttributes().get(pkColumn).getValue());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					columnNameError = pkColumn;
					columnValueError = record.getAttributes().get(pkColumn).getValue();
					throw new PuiCommonImportExportDtoColumnErrorException(columnNameError, columnValueError);
				}
			}

			Map<String, Object> attributes = new HashMap<>();
			for (Entry<String, ImportDataAttribute> entry : record.getAttributes().entrySet()) {
				if (!entry.getValue().getStatus().equals(ImportDataAttributeStatus.MODIFIED)) {
					continue;
				}

				attributes.put(entry.getKey(), entry.getValue().getValue());
			}

			if (daoRegistry.hasLanguageSupport(daoClass)) {
				attributes.put(IDto.LANG_COLUMN_NAME, language);
			}

			service.patch(dtoPk, attributes);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void createRecord(String model, ImportDataRecord record) throws PuiServiceInsertException {
			Class<? extends IService> serviceClass = serviceRegistry.getServiceFromModel(model);
			IService service = PuiApplicationContext.getInstance().getBean(serviceClass);
			Class<? extends ITableDto> dtoClass = getDtoClass(model);
			ITableDto dto;
			try {
				dto = service.getNew();
			} catch (PuiServiceNewException e) {
				throw new PuiServiceInsertException(e);
			}

			for (Entry<String, ImportDataAttribute> entry : record.getAttributes().entrySet()) {
				Field field;
				field = DtoRegistry.getJavaFieldFromColumnName(dtoClass, entry.getKey());
				if (field == null) {
					field = DtoRegistry.getJavaFieldFromLangColumnName(dtoClass, entry.getKey());
				}

				try {
					field.set(dto, entry.getValue().getValue());
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new PuiServiceInsertException(new PuiServiceException(e));
				}
			}

			service.insert(dto);
		}

		private String getFileName(Instant importTime, String user) {
			String importTimeStr = PuiDateUtil.temporalAccessorToString(importTime,
					DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
			return importTimeStr + "_" + user;
		}

	}

}
