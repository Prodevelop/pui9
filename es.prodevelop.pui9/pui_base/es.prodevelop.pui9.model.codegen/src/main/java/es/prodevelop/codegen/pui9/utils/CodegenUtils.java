package es.prodevelop.codegen.pui9.utils;

import java.text.Normalizer;

import org.apache.commons.lang3.StringUtils;

public class CodegenUtils {

	public static String convertEntityNameToJavaName(String dbName) {
		if (StringUtils.isEmpty(dbName)) {
			return dbName;
		}

		// Normalize string: remove accents
		String str = Normalizer.normalize(dbName, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+",
				"");

		// remove special characters
		str = dbName.replaceAll("[a-zA-Z0-9]+", "");

		// calculate all the delimiters
		StringBuilder sbSplits = new StringBuilder();
		for (int i = 0; i < str.toCharArray().length; i++) {
			char c = str.charAt(i);
			sbSplits.append("\\" + c + "");
			if (i != str.toCharArray().length - 1) {
				sbSplits.append("|");
			}
		}

		// split the string using the delimiters and create an upper case after
		// every split
		StringBuilder result = new StringBuilder();
		if (sbSplits.length() != 0) {
			String[] splits = dbName.split(sbSplits.toString());
			for (String split : splits) {
				result.append(StringUtils.capitalize(split.toLowerCase()));
			}
		} else {
			result.append(StringUtils.capitalize(dbName.toLowerCase()));
		}

		String javaName = result.toString();
		javaName = javaName.replaceAll("^[0-9]+", "");
		javaName = StringUtils.capitalize(javaName);

		return javaName;
	}

	public static String convertColumnNameToJavaName(String dbName) {
		if (StringUtils.isEmpty(dbName)) {
			return dbName;
		}

		String javaName = new String(dbName);
		// Normalize the string: remove accents
		javaName = javaName.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		// remove special characters
		javaName = javaName.replaceAll("[^a-zA-Z0-9]+", "");
		// to lower case
		javaName = javaName.toLowerCase();

		return javaName;
	}

}
