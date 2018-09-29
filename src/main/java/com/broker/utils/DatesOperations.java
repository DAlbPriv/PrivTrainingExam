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

public class DatesOperations {
	
	static Calendar cal = null;
	static DateFormat format = null;

	
	static {
		cal = Calendar.getInstance();
		format = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es", "ES"));
	}
		
	public static LocalDate lastThursdayInMonth(HashMap<String,Integer> abc){
		int year = abc.get("year");
		int month = abc.get("month");
		int day = abc.get("day");
		return LocalDate.of(year, month, day).with(lastInMonth(THURSDAY));
	}

	public static HashMap<String, Integer> getDateParts(Date stockDate){
		cal.setTime(stockDate);
		int realMonth = 0;
		HashMap<String, Integer> dateParts = new HashMap<String, Integer>();
		dateParts.put("year", cal.get(Calendar.YEAR));
		realMonth = cal.get(Calendar.MONTH)+1;
		if (realMonth == 13)
			realMonth = 1;
		dateParts.put("month", realMonth);
		dateParts.put("day",cal.get(Calendar.DAY_OF_MONTH));
		return dateParts;
	}

	public static DateFormat getFormat() {
		return format;
	}
	
	public static boolean checkIfLastThursdayInMonth(LocalDate LastThursdayofMonth, Date fromStock) {
		boolean isLastThursday = false;
		if (LastThursdayofMonth.isEqual(fromStock.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
			return true;
		return isLastThursday;
	}

}
