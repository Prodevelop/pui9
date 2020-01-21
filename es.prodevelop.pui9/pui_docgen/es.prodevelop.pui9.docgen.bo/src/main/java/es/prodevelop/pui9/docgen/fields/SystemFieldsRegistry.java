package es.prodevelop.pui9.docgen.fields;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Registry of System Fields to manage them
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class SystemFieldsRegistry {

	private static SystemFieldsRegistry singleton;

	public static SystemFieldsRegistry getSingleton() {
		if (singleton == null) {
			singleton = new SystemFieldsRegistry();
		}
		return singleton;
	}

	private Map<String, ISystemField> map;

	private SystemFieldsRegistry() {
		map = new HashMap<>();
	}

	public void registerSystemField(ISystemField systemField) {
		if (map.containsKey(systemField.getName())) {
			throw new IllegalArgumentException(
					"There exists two or more System Field with the same name '" + systemField.getName() + "'");
		}
		map.put(systemField.getName(), systemField);
	}

	public ISystemField getSystemField(String systemFieldName) {
		return map.get(systemFieldName);
	}

	public List<String> getAllSystemFieldNames() {
		List<String> list = new ArrayList<>();
		list.addAll(map.keySet());
		Collections.sort(list);

		return list;
	}

}
