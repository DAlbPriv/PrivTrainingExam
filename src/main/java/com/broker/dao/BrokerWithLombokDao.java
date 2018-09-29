package com.broker.dao;

import static com.broker.utils.DatesOperations.getFormat;
import static com.broker.utils.DatesOperations.getDateParts;
import static com.broker.utils.DatesOperations.lastThursdayInMonth;
import static com.broker.utils.DatesOperations.checkIfLastThursdayInMonth;
import static com.broker.utils.FileHandler.openFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.broker.model.Stock;


public class BrokerWithLombokDao implements IDao<List<Stock>>{

	static final Logger logger = Logger.getLogger(BrokerWithLombokDao.class);
		
	
	
	public List<Stock> parse() throws IOException {
		BufferedReader bufferedReader = openFile();
		return bufferedReader.lines().skip(1).map(parseStudent).collect(Collectors.toList());

	}
	
	private static Function<String, Stock> parseStudent = (csvLine) -> {
		Stock stock = new Stock();
		String[] datos = csvLine.split(";");
		try {
			stock.setDate(getFormat().parse(datos[0]));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		stock.setOut(new BigDecimal(datos[1]));
		stock.setIn(new BigDecimal(datos[2]));
		HashMap<String, Integer> dateParts = getDateParts(stock.getDate());
		
		boolean isLastThursday = checkIfLastThursdayInMonth(lastThursdayInMonth(dateParts), stock.getDate());	
		
		stock.setLastThursday(isLastThursday);
		
		return stock;
	};
	


	
}
