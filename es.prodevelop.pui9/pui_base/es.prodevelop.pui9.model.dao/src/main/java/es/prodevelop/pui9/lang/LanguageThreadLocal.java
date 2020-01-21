package es.prodevelop.pui9.lang;

import java.util.List;
import java.util.Locale;
import java.util.Locale.LanguageRange;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import es.prodevelop.pui9.utils.PuiLanguage;

public class LanguageThreadLocal {

	private static LanguageThreadLocal singleton;

	public static LanguageThreadLocal getSingleton() {
		if (singleton == null) {
			singleton = new LanguageThreadLocal();
		}
		return singleton;
	}

	private ThreadLocal<PuiLanguage> threadLocal;

	private LanguageThreadLocal() {
		threadLocal = new ThreadLocal<>();
	}

	public void setData(String langs) {
		if (StringUtils.isEmpty(langs)) {
			return;
		}

		List<LanguageRange> allLangs = Locale.LanguageRange.parse(langs);
		if (CollectionUtils.isEmpty(allLangs)) {
			return;
		}

		threadLocal.set(new PuiLanguage(Locale.forLanguageTag(allLangs.get(0).getRange())));
	}

	public PuiLanguage getData() {
		return threadLocal.get();
	}

}
