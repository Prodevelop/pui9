package es.prodevelop.pui9.utils;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

/**
 * A representation of a Language available in the application
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiLanguage implements Comparable<PuiLanguage>, Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The default language for the application is Spanish (es)
	 */
	public static final PuiLanguage DEFAULT_LANG = new PuiLanguage("es");

	private String isocode;
	private String country;
	private String name;
	private Integer isdefault = PuiConstants.FALSE_INT;

	/**
	 * Create an empty LanguageDto. Typically, after create the object using this
	 * constructor, you should set the properties by hand
	 */
	public PuiLanguage() {
	}

	/**
	 * Create a new LanguageDto from the given Locale. The language is set as no
	 * default and has no name
	 * 
	 * @param locale The locale of the language to be created
	 */
	public PuiLanguage(Locale locale) {
		this(locale.getLanguage());
		this.country = StringUtils.isEmpty(locale.getCountry()) ? "" : locale.getCountry();
	}

	/**
	 * Create a new LanguageDto from the given isocode. The language is set as no
	 * default and has no name
	 * 
	 * @param isocode The iso code of the language to be created
	 */
	public PuiLanguage(String isocode) {
		this.isocode = isocode;
		this.country = "";
	}

	public String getIsocode() {
		return isocode;
	}

	public String getCountry() {
		return country;
	}

	public String getName() {
		return name;
	}

	public Integer getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		return this.isocode.equalsIgnoreCase(((PuiLanguage) obj).isocode);
	}

	@Override
	public int compareTo(PuiLanguage o) {
		return isocode.compareTo(o.getIsocode());
	}

	@Override
	public String toString() {
		return isocode;
	}
}
