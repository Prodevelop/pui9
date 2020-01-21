package es.prodevelop.pui9.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.lang.LanguageThreadLocal;
import es.prodevelop.pui9.login.PuiUserSession;
import es.prodevelop.pui9.model.dto.DtoRegistry;
import es.prodevelop.pui9.model.dto.interfaces.IDto;

/**
 * Utility class to operate with the Languages. It offers some utility methods
 * like: obtain the language of a DTO, get the language of the current session,
 * check if a language is available for the application...
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiLanguageUtils {

	private static List<PuiLanguage> languages = new ArrayList<>();

	/**
	 * Get the language for the current session
	 * 
	 * @return The language used in the request of the user
	 */
	public static PuiLanguage getSessionLanguage() {
		return PuiUserSession.getCurrentSession() != null ? PuiUserSession.getCurrentSession().getLanguage()
				: LanguageThreadLocal.getSingleton().getData() != null ? LanguageThreadLocal.getSingleton().getData()
						: getDefaultLanguage();
	}

	/**
	 * Check if the given DTO has language support or not
	 * 
	 * @param dto The dto to check
	 * @return true if supports language; false if not
	 */
	public static <T extends IDto> boolean hasLanguageSupport(T dto) {
		if (dto == null) {
			return false;
		}

		Field field = DtoRegistry.getJavaFieldFromAllFields(dto.getClass(), IDto.LANG_FIELD_NAME);
		return field != null;
	}

	/**
	 * Get the language of the DTO object (depending on if the DTO object contains
	 * the 'lang' attribute)
	 * 
	 * @param dto The {@link IDto} to retrieve the language
	 * @return The language of the DTO, or null if not applies
	 */
	public static <T extends IDto> PuiLanguage getLanguage(T dto) {
		if (dto == null) {
			return null;
		}

		Field field = DtoRegistry.getJavaFieldFromAllFields(dto.getClass(), IDto.LANG_FIELD_NAME);
		if (field == null) {
			return null;
		}

		try {
			String val = (String) FieldUtils.readField(field, dto, true);
			if (StringUtils.isEmpty(val)) {
				return null;
			} else {
				return new PuiLanguage(val);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Set the language of the DTO object (depending on if the DTO object contains
	 * the 'lang' attribute)
	 * 
	 * @param dto      The {@link IDto} to set the language
	 * @param language The language to be set
	 */
	public static <TPK extends IDto> void setLanguage(TPK dto, PuiLanguage language) {
		if (dto == null || language == null) {
			return;
		}

		Field field = DtoRegistry.getJavaFieldFromAllFields(dto.getClass(), IDto.LANG_FIELD_NAME);
		if (field == null) {
			return;
		}

		try {
			FieldUtils.writeField(field, dto, language.getIsocode(), true);
		} catch (Exception e) {
			// do nothing
		}
	}

	/**
	 * Clear all the languages in the cache
	 */
	public static void clearLanguages() {
		languages.clear();
	}

	/**
	 * Add a language to the cache
	 * 
	 * @param isocode The iso code of the language
	 */
	public static void addLanguage(String isocode) {
		addLanguage(new PuiLanguage(isocode));
	}

	/**
	 * Add a language to the cache
	 * 
	 * @param language The language
	 */
	public static void addLanguage(PuiLanguage language) {
		if (language == null || StringUtils.isEmpty(language.getIsocode())) {
			return;
		}

		languages.add(language);
	}

	/**
	 * Remove a language from the cache
	 * 
	 * @param language The language
	 */
	public static void removeLanguage(PuiLanguage language) {
		if (language == null) {
			return;
		}

		removeLanguage(language.getIsocode());
	}

	/**
	 * Remove a language from the cache
	 * 
	 * @param isocode The iso code of the language
	 */
	public static void removeLanguage(String isocode) {
		if (StringUtils.isEmpty(isocode)) {
			return;
		}

		for (Iterator<PuiLanguage> it = languages.iterator(); it.hasNext();) {
			if (it.next().getIsocode().equals(isocode)) {
				it.remove();
			}
		}
	}

	/**
	 * Check if the cache has languages or not
	 * 
	 * @return true if the cache has languages; false if not
	 */
	public static boolean hasLanguages() {
		return !languages.isEmpty();
	}

	/**
	 * Check if the given language exists for the application
	 * 
	 * @param language The language
	 * @return true if exists; false if not
	 */
	public static boolean existLanguage(PuiLanguage language) {
		if (language == null) {
			return false;
		}

		return existLanguage(language.getIsocode());
	}

	/**
	 * Check if the given Locale language exists for the application
	 * 
	 * @param locale The locale
	 * @return true if exists; false if not
	 */
	public static boolean existLanguage(Locale locale) {
		if (locale == null) {
			return false;
		}

		return existLanguage(locale.getLanguage());
	}

	/**
	 * Check if the given iso code language exists for the application
	 * 
	 * @param isocode The iso code
	 * @return true if exists; false if not
	 */
	public static boolean existLanguage(String isocode) {
		if (StringUtils.isEmpty(isocode)) {
			return false;
		}

		for (Iterator<PuiLanguage> it = languages.iterator(); it.hasNext();) {
			if (it.next().getIsocode().equals(isocode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get an interator of the languages
	 * 
	 * @return An iterator for the available languages
	 */
	public static Iterator<PuiLanguage> getLanguagesIterator() {
		List<PuiLanguage> list = new ArrayList<>();
		list.addAll(languages);
		return list.iterator();
	}

	/**
	 * Get the number of supported languages
	 * 
	 * @return The number of supported languages
	 */
	public static Integer getLanguageCount() {
		return languages.size();
	}

	/**
	 * Get the default language of the application
	 * 
	 * @return The default language
	 */
	public static PuiLanguage getDefaultLanguage() {
		for (PuiLanguage lang : languages) {
			if (lang.getIsdefault().equals(PuiConstants.TRUE_INT)) {
				return lang;
			}
		}

		return null;
	}

}
