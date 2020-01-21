package es.prodevelop.pui9.utils;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class to deal with time datatype. By default, server side will always
 * work with Instant UTC TimeZone. Here you can convert Dates into Instants and
 * vice versa, and parsing or formatting values
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiDateUtil {

	public static final ZoneId utcZone = ZoneId.of("UTC");
	public static final DateTimeFormatter utcFormatter = DateTimeFormatter.ISO_INSTANT.withZone(utcZone);
	public static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private static final String SEP = "{sep}";

	/**
	 * Convert an String into a Instant in UTC zone. Multiple formats are accepted,
	 * see {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value The datetime in string format
	 * @return The parsed Instant
	 */
	public static Instant stringToInstant(String value) {
		return stringToInstant(value, null);
	}

	/**
	 * Convert an String into a LocalDate. Multiple formats are accepted, see
	 * {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value The datetime in string format
	 * @return The parsed LocalDate
	 */
	public static LocalDate stringToLocalDate(String value) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		String dateFormat = getDateFormat(value);
		if (dateFormat == null) {
			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(utcZone);
		try {
			return LocalDate.parse(value, formatter);
		} catch (DateTimeException e) {
			return null;
		}
	}

	/**
	 * Convert an String into LocalDateTime. Multiple formats are accepted, see
	 * {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value The datetime in string format
	 * @return The parsed LocalDateTime
	 */
	public static LocalDateTime stringToLocalDateTime(String value) {
		String dateFormat = getDateFormat(value);
		if (dateFormat == null) {
			return null;
		}

		String timeFormat = getTimeFormat(value, dateFormat);
		if (timeFormat == null) {
			return null;
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat + " " + timeFormat).withZone(utcZone);
		try {
			return LocalDateTime.parse(value, formatter);
		} catch (DateTimeException e) {
			return null;
		}
	}

	/**
	 * Convert an String into a Instant in UTC zone. Multiple formats are accepted,
	 * see {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value     The datetime in string format
	 * @param formatter The formatter to be used
	 * @return The parsed Instant
	 */
	public static Instant stringToInstant(String value, DateTimeFormatter formatter) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		if (formatter == null) {
			// first try to parse using utc default formatter
			try {
				return Instant.from(utcFormatter.parse(value));
			} catch (DateTimeException e) {
				// do nothing
			}

			String dateFormat = getDateFormat(value);
			if (dateFormat == null) {
				return null;
			}

			String timeFormat = getTimeFormat(value, dateFormat);
			if (StringUtils.isEmpty(timeFormat)) {
				formatter = DateTimeFormatter.ofPattern(dateFormat).withZone(utcZone);
				try {
					return LocalDate.parse(value, formatter).atStartOfDay().atZone(utcZone).toInstant();
				} catch (DateTimeException e) {
					return null;
				}
			} else {
				formatter = DateTimeFormatter.ofPattern(dateFormat + " " + timeFormat).withZone(utcZone);
				try {
					return LocalDateTime.parse(value, formatter).atZone(utcZone).toInstant();
				} catch (DateTimeException e) {
					return null;
				}
			}
		} else {
			formatter = formatter.withZone(utcZone);

			try {
				return LocalDateTime.parse(value, formatter).atZone(utcZone).toInstant();
			} catch (DateTimeException e) {
				return null;
			}
		}
	}

	public static boolean stringHasHours(String value) {
		String dateFormat = getDateFormat(value);
		if (dateFormat == null) {
			return false;
		}

		String timeFormat = getTimeFormat(value, dateFormat);
		if (timeFormat == null) {
			return false;
		}

		return TimeFormats.HOUR2.matchFormat(timeFormat) || TimeFormats.HOUR1.matchFormat(timeFormat)
				|| TimeFormats.HOUR2_MINUTE.matchFormat(timeFormat) || TimeFormats.HOUR1_MINUTE.matchFormat(timeFormat)
				|| TimeFormats.HOUR2_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR1_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_TZ.matchFormat(timeFormat);
	}

	public static boolean stringHasMinutes(String value) {
		String dateFormat = getDateFormat(value);
		if (dateFormat == null) {
			return false;
		}

		String timeFormat = getTimeFormat(value, dateFormat);
		if (timeFormat == null) {
			return false;
		}

		return TimeFormats.HOUR2_MINUTE.matchFormat(timeFormat) || TimeFormats.HOUR1_MINUTE.matchFormat(timeFormat)
				|| TimeFormats.HOUR2_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR1_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_TZ.matchFormat(timeFormat);
	}

	public static boolean stringHasSeconds(String value) {
		String dateFormat = getDateFormat(value);
		if (dateFormat == null) {
			return false;
		}

		String timeFormat = getTimeFormat(value, dateFormat);
		if (timeFormat == null) {
			return false;
		}

		return TimeFormats.HOUR2_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR1_MINUTE_SECOND.matchFormat(timeFormat)
				|| TimeFormats.HOUR_MINUTE_SECOND_TZ.matchFormat(timeFormat);
	}

	/**
	 * Convert an String into a Date. Multiple formats are accepted, see
	 * {@link DateFormats} and {@link TimeFormats}. Also multiple separators are
	 * accepted, see {@link DateSeparators}
	 * 
	 * @param value The date in string format
	 * @return The parsed Date
	 */
	public static Date stringToDate(String value) {
		return stringToDate(value, null);
	}

	/**
	 * Convert an String into a Date with the given format
	 * 
	 * @param value The date in string format
	 * @param sdf   The Simple Date Format to be used
	 * @return The parsed Date
	 */
	public static Date stringToDate(String value, SimpleDateFormat sdf) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		if (sdf == null) {
			String dateFormat = getDateFormat(value);
			if (dateFormat == null) {
				return null;
			}

			String timeFormat = getTimeFormat(value, dateFormat);
			if (timeFormat == null) {
				sdf = new SimpleDateFormat(dateFormat);
			} else {
				sdf = new SimpleDateFormat(dateFormat + " " + timeFormat);
			}
		}

		try {
			return sdf.parse(value);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Convert a Date into a String using the default {@link PuiDateUtil#DEFAULT_FORMAT}
	 * format
	 * 
	 * @param date The date to convert
	 * @return The formatted date
	 */
	public static String dateToString(Date date) {
		return dateToString(date, null);
	}

	/**
	 * Convert a Date into a String using the given format
	 * 
	 * @param date The date to convert
	 * @param sdf  The formatter to be used
	 * @return The formatted date
	 */
	public static String dateToString(Date date, SimpleDateFormat sdf) {
		if (date == null) {
			return null;
		}

		if (sdf == null) {
			sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		}

		return sdf.format(date);
	}

	/**
	 * Convert a Date into a Instant
	 * 
	 * @param date The date to be converted
	 * @return The Instant
	 */
	public static Instant dateToInstant(Date date) {
		if (date == null) {
			return null;
		}
		return date.toInstant();
	}

	/**
	 * Convert a Instant into a Date
	 * 
	 * @param instant The Instant to be converted
	 * @return The Date
	 */
	public static Date instantToDate(Instant instant) {
		if (instant == null) {
			return null;
		}
		return Date.from(instant);
	}

	/**
	 * Convert a Temporal Accessor into a String with the
	 * {@link PuiDateUtil#utcFormatter} formatter
	 *
	 * @param temporalAccessor The temporal accessor to be converted
	 * @return The formatted String
	 */
	public static String temporalAccessorToString(TemporalAccessor temporalAccessor) {
		return temporalAccessorToString(temporalAccessor, null);
	}

	/**
	 * Convert a Instant into a String with the given formatter
	 *
	 * @param temporalAccessor The temporal accessor to be converted
	 * @param formatter The formatter to be used
	 * @return The formatted String
	 */
	public static String temporalAccessorToString(TemporalAccessor temporalAccessor, DateTimeFormatter formatter) {
		if (temporalAccessor == null) {
			return null;
		}

		if (formatter == null) {
			if (temporalAccessor instanceof Instant) {
				formatter = utcFormatter;
			} else if (temporalAccessor instanceof ZonedDateTime) {
				formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
			} else if (temporalAccessor instanceof LocalDate) {
				formatter = DateTimeFormatter.ISO_LOCAL_DATE;
			} else if (temporalAccessor instanceof LocalDateTime) {
				formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			} else if (temporalAccessor instanceof OffsetDateTime) {
				formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
			}
		} else {
			if (!(temporalAccessor instanceof ZonedDateTime) && formatter.getZone() == null) {
				formatter = formatter.withZone(utcZone);
			}
		}

		return formatter != null ? formatter.format(temporalAccessor) : null;
	}

	/**
	 * Get the given instant at the given zone id
	 * 
	 * @param instant The instant
	 * @param zoneId  The zone Id
	 * @return The instant at the zone id
	 */
	public static ZonedDateTime getInstantAtZoneId(Instant instant, ZoneId zoneId) {
		if (instant == null) {
			return null;
		}
		return instant.atZone(zoneId);
	}

	private static String getDateFormat(String value) {
		for (DateSeparators sep : DateSeparators.values()) {
			for (DateFormats df : DateFormats.values()) {
				String template = df.template.replace(SEP, sep.sep);
				if (value.matches(template)) {
					return df.format.replace(SEP, sep.sep);
				}
			}
		}

		return null;
	}

	private static String getTimeFormat(String value, String dateFormat) {
		if (dateFormat == null) {
			return null;
		}

		DateFormats df = DateFormats.getByFormat(dateFormat);
		if (df == null) {
			return null;
		}

		DateSeparators ds = DateSeparators.guessSeparator(dateFormat);
		if (ds == null) {
			return null;
		}

		String time = value.replaceAll(df.template.replace(".*", "").replace(SEP, ds.sep), "").trim();
		for (TimeFormats tf : TimeFormats.values()) {
			if (time.matches(tf.template)) {
				return tf.format;
			}
		}

		return null;
	}

	private enum DateSeparators {
		SLASH("/"),

		DASH("-"),

		SPACE(" ");

		private final String sep;

		private DateSeparators(String sep) {
			this.sep = sep;
		}

		public static DateSeparators guessSeparator(String dateFormat) {
			for (DateSeparators ds : values()) {
				if (dateFormat.contains(ds.sep)) {
					return ds;
				}
			}
			return null;
		}
	}

	private enum DateFormats {
		DAY_MONTH_YEAR("\\d{1,2}" + SEP + "\\d{1,2}" + SEP + "\\d{4}.*", "dd" + SEP + "MM" + SEP + "yyyy"),

		YEAR_MONTH("\\d{4}" + SEP + "\\d{1,2}" + SEP + "\\d{1,2}.*", "yyyy" + SEP + "MM" + SEP + "dd");

		private final String template;
		private final String format;

		private DateFormats(String template, String format) {
			this.template = template;
			this.format = format;
		}

		public static DateFormats getByFormat(String format) {
			for (DateSeparators sep : DateSeparators.values()) {
				if (format.contains(sep.sep)) {
					format = format.replace(sep.sep, SEP);
					break;
				}
			}
			for (DateFormats df : values()) {
				if (df.format.equals(format)) {
					return df;
				}
			}

			return null;
		}
	}

	private enum TimeFormats {
		HOUR_MINUTE_SECOND_TZ("[T]\\d{2}:\\d{1,2}:\\d{1,2}[Z]", "'T'HH:mm:ss'Z'"),

		HOUR2_MINUTE_SECOND(".*\\d{2}:\\d{1,2}:\\d{1,2}", "HH:mm:ss"),

		HOUR1_MINUTE_SECOND(".*\\d{1}:\\d{1,2}:\\d{1,2}", "H:mm:ss"),

		HOUR2_MINUTE(".*\\d{2}:\\d{1,2}", "HH:mm"),

		HOUR1_MINUTE(".*\\d{1}:\\d{1,2}", "H:mm"),

		HOUR2(".*\\d{2}", "HH"),

		HOUR1(".*\\d{1}", "H"),

		NO_TIME("", "");

		private final String template;
		private final String format;

		private TimeFormats(String template, String format) {
			this.template = template;
			this.format = format;
		}

		public boolean matchFormat(String format) {
			return this.format.equals(format);
		}
	}

}