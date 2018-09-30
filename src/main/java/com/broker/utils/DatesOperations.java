package com.broker.utils;

import static java.time.DayOfWeek.THURSDAY;
import static java.time.temporal.TemporalAdjusters.lastInMonth;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import com.broker.dao.BrokerWithLombokDao;

import lombok.Getter;
import lombok.Setter;


/**
 * This class defines some operations that can be done on dates.
 * @author daniel.albendin
 * @methods
 *
 */
public class DatesOperations {
	
	static Calendar calendar = null;
	static DateFormat format = null;

	
	static {
		calendar = Calendar.getInstance();
		format = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es", "ES"));
	}
		

	/**
	 * 
	 * @param mappingDateValues a map with the values of a date
	 * @return  Return the last Thursday of each month for a specified date.
	 */
	public static LocalDate lastThursdayInMonth(HashMap<String,Integer> mappingDateValues){
		int year = mappingDateValues.get("year");
		int month = mappingDateValues.get("month");
		int day = mappingDateValues.get("day");
		return LocalDate.of(year, month, day).with(lastInMonth(THURSDAY));
	}

	/**
	 * 
	 * @param stockDate
	 * @return a Hashmap with the value of the fields year, month and day date parsed with a calendar.
	 */
	public static HashMap<String, Integer> getDateParts(Date stockDate){
		calendar.setTime(stockDate);
		int realMonth = 0;
		HashMap<String, Integer> dateParts = new HashMap<String, Integer>();
		dateParts.put("year", calendar.get(Calendar.YEAR));
		realMonth = calendar.get(Calendar.MONTH)+1;
		if (realMonth == 13)
			realMonth = 1;
		dateParts.put("month", realMonth);
		dateParts.put("day",calendar.get(Calendar.DAY_OF_MONTH));
		return dateParts;
	}

	/**
	 * 
	 * @return format date instantiated in this class
	 */
	public static DateFormat getFormat() {
		return format;
	}
	
	
	/**
	 * 
	 * @param lastThursdayofMonth
	 * @param dateFromActualStock
	 * @param dateFromPreviousStock
	 * @return true if the previous stock date is before or a last Thursday of that month and the actual stock is bigger.
	 */
	public static boolean checkIfDayAfterLastThursdayInMonth(LocalDate lastThursdayofMonth, Date dateFromActualStock,Date dateFromPreviousStock) {
		LocalDate actualStockDate = dateFromActualStock.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate previousStockDate = dateFromPreviousStock.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		if((previousStockDate.isBefore(lastThursdayofMonth)||previousStockDate.isEqual(lastThursdayofMonth)) && actualStockDate.isAfter(lastThursdayofMonth))
			return true;
		return false;

	}

}
