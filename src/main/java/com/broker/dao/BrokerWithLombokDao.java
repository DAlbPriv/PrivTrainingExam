package com.broker.dao;

import static com.broker.utils.DatesOperations.getFormat;
import static com.broker.utils.DatesOperations.getDateParts;
import static com.broker.utils.DatesOperations.lastThursdayInMonth;
import static com.broker.utils.DatesOperations.checkIfLastThursdayInMonth;
import static com.broker.utils.FileHandler.openFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import com.broker.model.Stock;
import lombok.extern.log4j.Log4j;

@Log4j
public class BrokerWithLombokDao implements IDao<List<Stock>>{
	static final Logger logger = Logger.getLogger(BrokerWithLombokDao.class);
	static BigDecimal AMOUNT_INVESTED = BigDecimal.valueOf(50);
	static BigDecimal PERCENTAGE = BigDecimal.valueOf(100);
	static BigDecimal COMISSION = BigDecimal.valueOf(2);
	static BigDecimal investedByMonth = null;
	static BigDecimal totalInvested = BigDecimal.valueOf(0);
	static BigDecimal stocks =  BigDecimal.ZERO;
	static Stock previousStock = new Stock();
	static List<Stock> stockListFromFile = null;
		
	static {
		investedByMonth = calculateRealInvested();
	}
	
	public void parse() throws IOException {
		log.info("Openning CSV File:");
		BufferedReader bufferedReader = openFile();
		log.info("Getting list of stocks:");
		stockListFromFile = bufferedReader.lines().skip(1).map(parseStudent).collect(Collectors.toList());
	}
	
	private static Function<String, Stock> parseStudent = (csvLine) -> {
		Stock stock = new Stock();
		String[] datos = csvLine.split(";");
		try {
			stock.setDate(getFormat().parse(datos[0]));
		} catch (ParseException e) {
			log.error(new StringBuilder("The date: ").append(datos[0]).append("cannot be parsed with: ").append(getFormat().toString()));
		}
		stock.setOut(new BigDecimal(datos[1]));
		stock.setIn(new BigDecimal(datos[2]));
		HashMap<String, Integer> dateParts = getDateParts(stock.getDate());
		
		boolean isLastThursday = checkIfLastThursdayInMonth(lastThursdayInMonth(dateParts), stock.getDate());	
		
		stock.setLastThursday(isLastThursday);
		
		return stock;
	};
	
	private static BigDecimal calculateRealInvested() {
		log.info("initializing the static amount of money to invest:");
		BigDecimal PercentageWithoutCommision = PERCENTAGE.subtract(COMISSION);
		BigDecimal myRealPercentage= PercentageWithoutCommision.divide(PERCENTAGE,10,RoundingMode.HALF_UP);
		BigDecimal toReturn = myRealPercentage.multiply(AMOUNT_INVESTED);
		log.info(toReturn.toString());
		return toReturn;
	}

	public BigDecimal calculateSellingPrice() {
		Collections.sort(stockListFromFile);
		stockListFromFile.forEach(stock -> {
			Stock aux = stock;
			if(previousStock.isLastThursday()){
				stocks = stocks.add(buyStock(aux,investedByMonth));
				log.info(new StringBuilder("").append(stock.getDate()).append(": ").append(stocks.toString()));
			}
			previousStock = aux;
		});
	
		totalInvested = stocks.multiply(stockListFromFile.get(stockListFromFile.size()-1).getOut());
		return totalInvested.setScale(3, RoundingMode.HALF_UP);
	}
	
	
	private static BigDecimal buyStock(Stock aux,BigDecimal investedByMonth){
		return investedByMonth.divide(aux.getIn(),10,RoundingMode.HALF_UP);
	}



	
}
