package es.prodevelop.pui9.messages;

import java.util.ListResourceBundle;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A abstract class to internationalize the messages of the application. It
 * extends from {@link ListResourceBundle} that is a way to define this messages
 * with Java classes. Using this approach, we ensure that the text is using
 * UTF-8 encoding<br>
 * <br>
 * Each language should define its own Java file with its translations. The Java
 * class name should end with the code of the language: _es, _en, _ca,
 * _fr...<br>
 * <br>
 * Have a look at <a href=
 * "http://www.baeldung.com/java-resourcebundle">http://www.baeldung.com/java-resourcebundle</a>
 * to see the differences between using Java files and property files
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public abstract class AbstractPuiListResourceBundle extends ListResourceBundle {

	@Override
	protected final Object[][] getContents() {
		Map<Object, String> messages = getMessages();
		Object[][] asArray = asArray(messages);
		return asArray;
	}

	/**
	 * Convert the Map of key-value text into a
	 * 
	 * @param messages
	 *            The messages as map, where the Key is the code of the text, and
	 *            the Value is the translated text
	 * @return A corresponding matrix Object type of the Map parameter
	 */
	private Object[][] asArray(Map<Object, String> messages) {
		Object[][] values = new Object[messages.keySet().size()][2];
		AtomicInteger index = new AtomicInteger(0);
		messages.forEach((key, value) -> {
			values[index.get()][0] = key.toString();
			values[index.get()][1] = value;
			index.incrementAndGet();
		});
		return values;
	}

	/**
	 * Get the list of translated messages
	 * 
	 * @return The messages as map, where the Key is the code of the text, and the
	 *         Value is the translated text
	 */
	protected abstract Map<Object, String> getMessages();

}
