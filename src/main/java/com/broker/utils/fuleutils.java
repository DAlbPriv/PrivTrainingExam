package com.broker.utils;

import static java.time.DayOfWeek.THURSDAY;
import static java.time.temporal.TemporalAdjusters.lastInMonth;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.broker.model.Stock;

public class fuleutils {
	static Properties prop = null;
	static InputStream input = null;
	static List<Stock> stockList = new ArrayList<Stock>();
	static List<Stock> stockInvestedList = new ArrayList<Stock>();
	static Calendar cal = Calendar.getInstance();



	static {
		prop = new Properties();
		try {
			input = FileReader.class.getResourceAsStream("/config.properties");
			prop.load(input);
		}catch(IOException e) {
			
		}
	}

	public static void main(String[] args) throws IOException {
				stockList = csvReader();
				Collections.sort(stockList);
				Stock previousStock = new Stock();
				BigDecimal stockPerDayCalculated = null;
				BigDecimal totalInvested = BigDecimal.valueOf(0);
				BigDecimal investedByMonth = BigDecimal.valueOf(50-(50 * 0.02));
				Stock aux = null;

				BigDecimal stocks =  BigDecimal.ZERO;


		for(int i = 0; i < stockList.size(); i++) {
			aux = stockList.get(i);
			if(previousStock.isLastThursday() && i != stockList.size() - 1)
				stocks = stocks.add(buyStock(aux,investedByMonth));
			if (aux.isLastThursday() && i != stockList.size() - 1)
				previousStock = aux;

/*			if(previousStock.isLastThursday())
				totalInvested = totalInvested.add(investedByMonth);
			if (aux.isLastThursday() && i != stockList.size() - 1)
				previousStock = aux;
			stockPerDayCalculated = CalculateStock(aux);
			if(!totalInvested.equals(BigDecimal.ZERO))
				totalInvested = totalInvested.multiply(stockPerDayCalculated, MathContext.DECIMAL32);
			previousStock = aux;
*/
		}
		System.out.println(stocks.multiply(stockList.get(stockList.size()-1).getOut()));

	}


	private static BigDecimal buyStock(Stock aux,BigDecimal totalInvested){
		return aux.getIn().divide(totalInvested,10,RoundingMode.HALF_UP);
	}

	private static BigDecimal CalculateStock(Stock stock) {
		return stock.getIn().divide(stock.getIn(),10, RoundingMode.HALF_UP);
	}

	private static Function<String, Stock> parseStudent = (csvLine) -> {
		Stock stock = new Stock();
		String[] datos = csvLine.split(";");
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", new Locale("es", "ES"));

		try {
			stock.setDate(format.parse(datos[0]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		stock.setOut(new BigDecimal(datos[1]));
		stock.setIn(new BigDecimal(datos[2]));

		cal.setTime(stock.getDate());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		if (month == 13)
			month = 1;
		int day = cal.get(Calendar.DAY_OF_MONTH);

		LocalDate lastThursday = LocalDate.of(year, month, day).with(lastInMonth(THURSDAY));

		if (lastThursday.isEqual(stock.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
			stock.setLastThursday(true);

		return stock;
	};

	private static List<Stock> csvReader() throws IOException{
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(prop.getProperty("resourcePath")+prop.getProperty("filename")));
			stockList = bufferedReader.lines().skip(1).map(parseStudent).collect(Collectors.toList());
		}catch(IOException e){
			System.out.println("Error");
			throw e;
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
		}
		return stockList;
	}

}
