package com.library.data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class DateStringConverter {

	//class is used to convert DateTime to String and back
	static protected String dateToString(DateTime date) {
		return date.toString();
	}
	
	static protected DateTime dateStringToDate(String dateString) {
		DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		return fmt.parseDateTime(dateString);
	}
}
