package es.prodevelop.pui9.elasticsearch.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.common.enums.PuiVariableValues;
import es.prodevelop.pui9.common.service.interfaces.IPuiVariableService;
import es.prodevelop.pui9.elasticsearch.PuiElasticSearchManager;
import es.prodevelop.pui9.elasticsearch.exceptions.PuiElasticSearchNoNodesException;
import es.prodevelop.pui9.elasticsearch.interfaces.IPuiElasticSearchEnablement;
import es.prodevelop.pui9.model.dao.registry.DaoRegistry;
import es.prodevelop.pui9.model.dto.DtoFactory;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;
import es.prodevelop.pui9.model.dto.interfaces.IViewDto;
import es.prodevelop.pui9.utils.PuiLanguage;
import es.prodevelop.pui9.utils.PuiLanguageUtils;

/**
 * Provides common methods that can be used in the concrete services of
 * ElasticSearch
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiElasticSearchService {

	protected static final String STRING_SEPARATOR = "_";

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IPuiVariableService variableService;

	@Autowired
	protected DaoRegistry daoRegistry;

	@Autowired
	private PuiElasticSearchManager puiElasticSearchManager;

	@Autowired
	protected IPuiElasticSearchEnablement puiElasticSearchEnablement;

	private Map<Class<? extends IViewDto>, List<String>> cacheDtoIndexes = new HashMap<>();
	private Map<String, PuiLanguage> cacheIndexLang = new HashMap<>();

	/**
	 * Get the ElasticSearch transport client object
	 * 
	 * @return The ElasticSerch transport client object
	 * @throws PuiElasticSearchNoNodesException If Elastic Search is not connected
	 *                                          to any Node
	 */
	protected RestHighLevelClient getClient() throws PuiElasticSearchNoNodesException {
		return puiElasticSearchManager.getClient();
	}

	/**
	 * Return the list of indexes for the given Dto.<br>
	 * <ul>
	 * <li>If the Dto has language support:</li>
	 * <ul>
	 * <li>If no language is specified: returns all the indexes for each existing
	 * language</li>
	 * <li>If a language is specified: it returns only this index</li>
	 * </ul>
	 * <li>If Dto has no language support: it returns the only existing index for
	 * it</li>
	 * </ul>
	 * 
	 * @param dtoClass The {@link IViewDto} class that represents the index
	 * @return The list of indexes of the given DTO class
	 */
	@SuppressWarnings("unchecked")
	protected List<String> getIndexesForDto(Class<? extends IViewDto> dtoClass) {
		dtoClass = (Class<? extends IViewDto>) DtoFactory.getClassFromInterface(dtoClass);

		if (!cacheDtoIndexes.containsKey(dtoClass)) {
			String prefix = getIndexPrefix();
			String view = daoRegistry.getEntityName(daoRegistry.getDaoFromDto(dtoClass));
			String indexName = StringUtils.isEmpty(prefix) ? view : prefix + STRING_SEPARATOR + view;
			List<String> indexes = new ArrayList<>();

			if (DtoRegistry.getAllFields(dtoClass).contains(IDto.LANG_FIELD_NAME)) {
				for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
					indexes.add(indexName + STRING_SEPARATOR + it.next().getIsocode());
				}
			} else {
				indexes.add(indexName);
			}

			cacheDtoIndexes.put(dtoClass, indexes);
		}

		return cacheDtoIndexes.get(dtoClass);
	}

	/**
	 * Get the index for the given {@link IViewDto} class and language
	 * 
	 * @param dtoClass The {@link IViewDto} class that represents the index
	 * @param language The desired language
	 * @return The index for given DTO class and language
	 */
	protected String getIndexForLanguage(Class<? extends IViewDto> dtoClass, PuiLanguage language) {
		List<String> indexes = getIndexesForDto(dtoClass);
		if (indexes.size() == 1) {
			return indexes.get(0);
		} else {
			if (language == null) {
				return indexes.get(0);
			} else {
				for (String index : indexes) {
					if (index.endsWith(STRING_SEPARATOR + language.getIsocode())) {
						return index;
					}
				}
			}

			return null;
		}
	}

	/**
	 * Get the index prefix for the indexes of ElasticSearch for this application
	 * 
	 * @return The prefix for the indexes
	 */
	protected String getIndexPrefix() {
		return variableService.getVariable(PuiVariableValues.ELASTICSEARCH_INDEX_PREFIX.name());
	}

	/**
	 * Get the language for the given Index
	 * 
	 * @param index The index name
	 * @return The language of the index
	 */
	protected PuiLanguage getLanguageFromIndex(String index) {
		if (!cacheIndexLang.containsKey(index)) {
			for (Iterator<PuiLanguage> it = PuiLanguageUtils.getLanguagesIterator(); it.hasNext();) {
				PuiLanguage next = it.next();
				if (index.endsWith(STRING_SEPARATOR + next.getIsocode())) {
					cacheIndexLang.put(index, next);
					break;
				}
			}
		}

		return cacheIndexLang.get(index);
	}

	/**
	 * Check if the given field is considered as Term for the given DTO class
	 * 
	 * @param dtoClass  The DTO class of the field
	 * @param fieldName The field to check
	 * @return true if it's a text term; false if not
	 */
	protected boolean isTextTerm(Class<? extends IViewDto> dtoClass, String fieldName) {
		Integer length = DtoRegistry.getFieldMaxLength(dtoClass, fieldName);
		return length == null || (length > 0 && length < (32 * 1024));
	}

}
