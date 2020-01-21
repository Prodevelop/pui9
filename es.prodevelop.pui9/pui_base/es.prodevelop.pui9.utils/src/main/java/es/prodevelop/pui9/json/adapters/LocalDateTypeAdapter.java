package es.prodevelop.pui9.json.adapters;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import es.prodevelop.pui9.utils.PuiDateUtil;

/**
 * Type adapter for {@link Instant} types. The datetime read from a JSON will be
 * always parsed to the local time of the server, and stored in the database
 * with the local time of the server
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

	@Override
	public void write(JsonWriter out, LocalDate instant) throws IOException {
		String value = null;
		if (instant != null) {
			value = PuiDateUtil.temporalAccessorToString(instant);
		}
		out.value(value);
	}

	@Override
	public LocalDate read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}

		String value = in.nextString();
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		LocalDate localDate = PuiDateUtil.stringToLocalDate(value);
		if (localDate == null) {
			throw new DateTimeParseException("Date format not supported. Only UTC ISO format is supported", value, 0);
		}

		return localDate;
	}

}
