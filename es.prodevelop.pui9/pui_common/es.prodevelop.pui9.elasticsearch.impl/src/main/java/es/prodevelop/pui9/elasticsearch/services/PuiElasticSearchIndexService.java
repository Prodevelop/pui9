package es.prodevelop.pui9.elasticsearch.services;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.elasticsearch.ResourceAlreadyExistsException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.IndexNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import es.prodevelop.pui9.elasticsearch.enums.ESLanguages;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCountException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchCreateIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchDeleteIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchExistsIndexException;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.elasticsearch.services.interfaces.IPuiElasticSearchIndexService;
import es.prodevelop.pui9.json.GsonSingleton;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.INullView;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.threads.PuiBackgroundExecutors;
import es.prodevelop.pui9.utils.PuiLanguage;

/**
 * Implementation for the API to manage Indexes for ElasticSaerch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@Component
public class PuiElasticSearchIndexService extends AbstractPuiElasticSearchService
		implements IPuiElasticSearchIndexService {

	private static final String NULL_VIEW = "NullView";

	private static final String PROP_NUMBER_OF_SHARDS = "index.number_of_shards";
	private static final String PROP_NUMBER_OF_REPLICAS = "index.number_of_replicas";
	private static final String PROP_MAX_RESULT_WINDOW = "index.max_result_window";

	private static final Integer NUMBER_OF_SHARDS = 1;
	private static final Integer NUMBER_OF_REPLICAS = 0;
	private static final Long MAX_RESULT_WINDOW = 10000000L;

	private Map<String, MappingType> indexCache = new HashMap<>();

	private Type countObjectType = new TypeToken<List<CountObject>>() {
		private static final long serialVersionUID = 1L;
	}.getType();

	@PostConstruct
	private void postConstruct() {
		PuiBackgroundExecutors.getSingleton().registerNewExecutor("ElasticSearch_RefreshIndexes", true, 0, 10,
				TimeUnit.MINUTES, this::refreshIndexesCache);
	}

	public String getIndexPrefix() {
		return super.getIndexPrefix();
	}

	@Override
	public void createIndex(Class<? extends IViewDto> dtoClass)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchCreateIndexException {
		createIndex(dtoClass, null);
	}

	@Override
	public void createIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchCreateIndexException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			throw new PuiElasticSearchCreateIndexException(NULL_VIEW);
		}

		if (!puiElasticSearchEnablement.isViewIndexable(dtoClass)) {
			return;
		}

		List<String> indexes;
		if (language != null) {
			indexes = Collections.singletonList(getIndexForLanguage(dtoClass, language));
		} else {
			indexes = getIndexesForDto(dtoClass);
		}

		Gson gson = new GsonBuilder().create();

		for (String index : indexes) {
			try {
				CreateIndexRequest request = new CreateIndexRequest(index);
				request.settings(Settings.builder().put(PROP_NUMBER_OF_SHARDS, NUMBER_OF_SHARDS)
						.put(PROP_NUMBER_OF_REPLICAS, NUMBER_OF_REPLICAS)
						.put(PROP_MAX_RESULT_WINDOW, MAX_RESULT_WINDOW));

				PuiLanguage lang = new PuiLanguage(index.substring(index.lastIndexOf(STRING_SEPARATOR) + 1));
				if (ESLanguages.getByCode(lang.getIsocode()) == null) {
					lang = null;
				}

				MappingType mapping = buildMapping(dtoClass, lang);
				String mappingJson = gson.toJson(mapping);
				request.mapping(mappingJson, XContentType.JSON);

				CreateIndexResponse response = getClient().indices().create(request, RequestOptions.DEFAULT);

				if (response.isAcknowledged()) {
					indexCache.put(index, mapping);
				} else {
					logger.debug("Could not create Index '" + index + "'");
					throw new PuiElasticSearchCreateIndexException(index);
				}
			} catch (ResourceAlreadyExistsException | IOException e) {
				logger.debug("Could not create Index '" + index + "' because already exists");
			}
		}
	}

	@Override
	public boolean checkIndexMapping(Class<? extends IViewDto> dtoClass) throws PuiElasticSearchNoNodesException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			return true;
		}

		if (indexCache.isEmpty()) {
			refreshIndexesCache();
		}

		List<String> indexes = getIndexesForDto(dtoClass);
		for (String index : indexes) {
			PuiLanguage lang = new PuiLanguage(index.substring(index.lastIndexOf(STRING_SEPARATOR) + 1));
			if (ESLanguages.getByCode(lang.getIsocode()) == null) {
				lang = null;
			}

			MappingType mappingFromDto = buildMapping(dtoClass, lang);
			MappingType mappingFromIndex = indexCache.get(index);

			if (!mappingFromDto.equals(mappingFromIndex)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void deleteIndex(String index)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchDeleteIndexException {
		try {
			DeleteIndexRequest request = new DeleteIndexRequest(index);
			AcknowledgedResponse response = getClient().indices().delete(request, RequestOptions.DEFAULT);

			if (response.isAcknowledged()) {
				indexCache.remove(index);
			} else {
				logger.debug("Could not remove Index '" + index + "'");
				throw new PuiElasticSearchDeleteIndexException(index);
			}
		} catch (IndexNotFoundException | IOException e) {
			logger.debug("Could not remove Index '" + index + "' because doesn't exists");
		}
	}

	@Override
	public void deleteIndex(Class<? extends IViewDto> dtoClass)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchDeleteIndexException {
		deleteIndex(dtoClass, null);
	}

	@Override
	public void deleteIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchDeleteIndexException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			throw new PuiElasticSearchDeleteIndexException(NULL_VIEW);
		}

		List<String> indexes;
		if (language != null) {
			indexes = Collections.singletonList(getIndexForLanguage(dtoClass, language));
		} else {
			indexes = getIndexesForDto(dtoClass);
		}

		for (String index : indexes) {
			try {
				DeleteIndexRequest request = new DeleteIndexRequest(index);
				AcknowledgedResponse response = getClient().indices().delete(request, RequestOptions.DEFAULT);

				if (response.isAcknowledged()) {
					indexCache.remove(index);
				} else {
					logger.debug("Could not remove Index '" + index + "'");
					throw new PuiElasticSearchDeleteIndexException(index);
				}
			} catch (IndexNotFoundException | IOException e) {
				logger.debug("Could not remove Index '" + index + "' because doesn't exists");
			}
		}
	}

	@Override
	public boolean existsIndex(Class<? extends IViewDto> dtoClass)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchExistsIndexException {
		return existsIndex(dtoClass, null);
	}

	@Override
	public boolean existsIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchExistsIndexException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			return false;
		}

		if (indexCache.isEmpty()) {
			refreshIndexesCache();
		}

		List<String> indexes;
		if (language != null) {
			indexes = Collections.singletonList(getIndexForLanguage(dtoClass, language));
		} else {
			indexes = getIndexesForDto(dtoClass);
		}

		int existsNum = 0;
		for (String index : indexes) {
			if (indexCache.containsKey(index)) {
				existsNum++;
			}
		}

		if (existsNum == indexes.size()) {
			return true;
		} else {
			logger.debug("Indexes " + indexes + " doesn't exists");
			if (indexes.size() > 1) {
				StringBuilder sb = new StringBuilder();
				for (Iterator<String> it = indexes.iterator(); it.hasNext();) {
					sb.append(it.next());
					if (it.hasNext()) {
						sb.append(",");
					}
				}
				throw new PuiElasticSearchExistsIndexException(sb.toString());
			}
			return false;
		}
	}

	@Override
	public List<String> getAllIndexes() throws PuiElasticSearchNoNodesException {
		List<String> list = new ArrayList<>();
		if (indexCache.isEmpty()) {
			refreshIndexesCache();
		}
		list.addAll(indexCache.keySet());
		return list;
	}

	@Override
	public long countIndex(Class<? extends IViewDto> dtoClass)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchCountException {
		return countIndex(dtoClass, null);
	}

	@Override
	public long countIndex(Class<? extends IViewDto> dtoClass, PuiLanguage language)
			throws PuiElasticSearchNoNodesException, PuiElasticSearchCountException {
		if (INullView.class.isAssignableFrom(dtoClass)) {
			throw new PuiElasticSearchCountException(NULL_VIEW);
		}

		List<String> indexes;
		if (language != null) {
			indexes = Collections.singletonList(getIndexForLanguage(dtoClass, language));
		} else {
			indexes = getIndexesForDto(dtoClass);
		}

		Map<String, Long> counts = countIndex(indexes);

		long total = -1;
		for (Entry<String, Long> entry : counts.entrySet()) {
			if (total == -1) {
				total = entry.getValue();
			} else if (total != entry.getValue()) {
				logger.debug("Index '" + entry.getKey() + "' with distinct documents in each language");
				throw new PuiElasticSearchCountException(entry.getKey());
			}
		}

		return total;
	}

	@Override
	public Map<String, Long> countIndex(List<String> indexes) throws PuiElasticSearchNoNodesException {
		if (indexes.isEmpty()) {
			return Collections.emptyMap();
		}

		try {
			StringBuilder url = new StringBuilder();
			url.append("/_cat/indices/");
			url.append(String.join(",", indexes));
			url.append("?format=json");
			url.append("&h=index,docs.count");
			Request request = new Request("get", url.toString());
			Response resp = getClient().getLowLevelClient().performRequest(request);
			String jsonStr = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			List<CountObject> counts = GsonSingleton.getSingleton().getGson().fromJson(jsonStr, countObjectType);
			return counts.stream().collect(Collectors.toMap(c -> c.index, c -> c.count));
		} catch (IndexNotFoundException | IOException e) {
			return Collections.emptyMap();
		}
	}

	/**
	 * Build the mapping for an Index represented by the IViewDto class and langugae
	 */
	private MappingType buildMapping(Class<? extends IViewDto> dtoClass, PuiLanguage language) {
		MappingType mapping = new MappingType();

		for (Field field : DtoRegistry.getAllJavaFields(dtoClass)) {
			MappingTypesEnum type = null;
			if (DtoRegistry.getDateTimeFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._date;
			} else if (DtoRegistry.getNumericFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._long;
			} else if (DtoRegistry.getFloatingFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._double;
			} else if (DtoRegistry.getStringFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._text;
			} else if (DtoRegistry.getBooleanFields(dtoClass).contains(field.getName())) {
				type = MappingTypesEnum._boolean;
			}

			ESLanguages lang = null;
			if (language != null) {
				lang = ESLanguages.getByCode(language.getIsocode());
			} else {
				lang = ESLanguages.standard;
			}

			boolean withKeyword = false;
			if (isTextTerm(dtoClass, field.getName())) {
				withKeyword = true;
			}

			mapping.addMappingField(field.getName(), new MappingField(type, withKeyword, lang));
		}

		return mapping;
	}

	/**
	 * Refresh the cache of indexes
	 */
	private void refreshIndexesCache() {
		try {
			GetMappingsRequest request = new GetMappingsRequest();
			request.indices(indexCache.keySet().toArray(new String[0]));
			GetMappingsResponse response = getClient().indices().getMapping(request, RequestOptions.DEFAULT);

			if (CollectionUtils.isEmpty(response.mappings())) {
				return;
			}

			indexCache.clear();

			response.mappings().forEach((index, mappingMetaData) -> {
				if (index.startsWith(getIndexPrefix())) {
					String json = GsonSingleton.getSingleton().getGson().toJson(mappingMetaData.getSourceAsMap());
					MappingType mapping = GsonSingleton.getSingleton().getGson().fromJson(json, MappingType.class);
					indexCache.put(index, mapping);
				}
			});
		} catch (PuiElasticSearchNoNodesException | IOException e) {
			// do nothing
		}
	}

	private class MappingType {
		private Map<String, MappingField> properties;

		public void addMappingField(String field, MappingField definition) {
			if (properties == null) {
				properties = new HashMap<>();
			}
			properties.put(field, definition);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return this.hashCode() == obj.hashCode();
		}

		@Override
		public int hashCode() {
			HashCodeBuilder hcBuilder = new HashCodeBuilder();
			List<String> list = new ArrayList<>(properties.keySet());
			Collections.sort(list);

			for (String key : list) {
				hcBuilder.append(key);
				hcBuilder.append(":");
				hcBuilder.append(properties.get(key));
				hcBuilder.append("; ");
			}

			return hcBuilder.hashCode();
		}

		@Override
		public String toString() {
			return properties.keySet().toString();
		}
	}

	private class MappingField {
		MappingTypesEnum type;
		MappingFieldFields fields;
		ESLanguages analyzer;
		String format;

		public MappingField(MappingTypesEnum type, boolean withKeyword, ESLanguages analyzer) {
			this.type = type;
			if (type.equals(MappingTypesEnum._text) && withKeyword) {
				this.fields = new MappingFieldFields();
				this.analyzer = analyzer;
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return this.hashCode() == obj.hashCode();
		}

		@Override
		public int hashCode() {
			HashCodeBuilder hcBuilder = new HashCodeBuilder();
			hcBuilder.append(type);
			if (fields != null) {
				hcBuilder.append(fields);
			}
			if (analyzer != null) {
				hcBuilder.append(analyzer);
			}
			if (format != null) {
				hcBuilder.append(format);
			}

			return hcBuilder.hashCode();
		}

		@Override
		public String toString() {
			return type.toString() + (fields != null ? ", " + fields.toString() : "")
					+ (analyzer != null ? ", " + analyzer.toString() : "") + (format != null ? ", " + format : "");
		}
	}

	private enum MappingTypesEnum {
		@SerializedName("text")
		_text,

		@SerializedName("long")
		_long,

		@SerializedName("double")
		_double,

		@SerializedName("date")
		_date,

		@SerializedName("boolean")
		_boolean,

		@SerializedName("keyword")
		_keyword;

		@Override
		public String toString() {
			return name();
		}
	}

	private class MappingFieldFields {
		MappingFieldsKeyword keyword = new MappingFieldsKeyword();

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return this.hashCode() == obj.hashCode();
		}

		@Override
		public int hashCode() {
			HashCodeBuilder hcBuilder = new HashCodeBuilder();
			hcBuilder.append(keyword);

			return hcBuilder.hashCode();
		}

		@Override
		public String toString() {
			return keyword.toString();
		}
	}

	private class MappingFieldsKeyword {
		MappingTypesEnum type = MappingTypesEnum._keyword;

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return this.hashCode() == obj.hashCode();
		}

		@Override
		public int hashCode() {
			HashCodeBuilder hcBuilder = new HashCodeBuilder();
			hcBuilder.append(type);

			return hcBuilder.hashCode();
		}

		@Override
		public String toString() {
			return type.toString();
		}
	}

	private class CountObject {
		String index;
		@SerializedName("docs.count")
		Long count;
	}

}