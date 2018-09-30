package com.broker.dao;

import static com.broker.utils.DatesOperations.getFormat;
import static com.broker.utils.DatesOperations.getDateParts;
import static com.broker.utils.DatesOperations.lastThursdayInMonth;
import static com.broker.utils.DatesOperations.checkIfDayAfterLastThursdayInMonth;
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
/**
 * This class parses and do operations on an array list of 
 * Stocks.
 * 
 * @author daniel.albendin
 *
 */
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
	static boolean firstDayAfterLastThursday = false;
	static int month = 0;
		
	static {
		investedByMonth = calculateRealInvested();
	}
	
	
	/**
	 * This method reads the file and assign to the stockListFromFile variable, the result
	 * of parsing that information with the parseStudent Method.
	 */
	public void parse() throws IOException {
		log.info("Openning CSV File:");
		BufferedReader bufferedReader = openFile();
		log.info("Getting list of stocks:");
		stockListFromFile = bufferedReader.lines().skip(1).map(parseStudent).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @parameters csvLine. A plain line from a CSV with the information of an investment.
	 * @return The function returns a Stock object created after parsing the CSV file.
	 */
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
		
		return stock;
	};
	
	/**
	 * 
	 * @return The value of the real amount invested after substracting the comission percentage
	 */
	private static BigDecimal calculateRealInvested() {
		log.info("initializing the static amount of money to invest:");
		BigDecimal PercentageWithoutCommision = PERCENTAGE.subtract(COMISSION);
		BigDecimal myRealPercentage= PercentageWithoutCommision.divide(PERCENTAGE,10,RoundingMode.HALF_UP);
		BigDecimal toReturn = myRealPercentage.multiply(AMOUNT_INVESTED);
		log.info(toReturn.toString());
		return toReturn;
	}

	/**
	 * This method calculates the amount of money that has been achieved with the investment.
	 * This method sorts the array and then foreach element it checks if it is the last day after the 
	 * last Thursday of each month and add stocks to the current amount. selling it the last day in the file.
	 * 
	 * @return Money after investing
	 */
	public BigDecimal calculateSellingPrice() {
		Collections.sort(stockListFromFile);
		stockListFromFile.forEach(stock -> {
				Stock aux = stock;
				if(previousStock.getDate()!=null) {
					HashMap<String, Integer> dateParts = getDateParts(previousStock.getDate());
					lastThursdayInMonth(dateParts);
					firstDayAfterLastThursday = checkIfDayAfterLastThursdayInMonth(lastThursdayInMonth(dateParts), stock.getDate(),previousStock.getDate());	
					if(firstDayAfterLastThursday){
						stocks = stocks.add(buyStock(aux,investedByMonth));
						log.info(new StringBuilder("").append(stock.getDate()).append(": ").append(stocks.toString()));
						firstDayAfterLastThursday = false;
					}
				}
			previousStock = aux;
			
			
		});
	
		totalInvested = stocks.multiply(stockListFromFile.get(stockListFromFile.size()-1).getOut());
		return totalInvested.setScale(3, RoundingMode.HALF_UP);
	}
	
	/**
	 * 
	 * @param currentDaySTock The day with the price of in and out of the stocks
	 * @param investedByMonth The amount of money to be invested
	 * @return The amount of stocks bought 
	 */
	private static BigDecimal buyStock(Stock currentDaySTock,BigDecimal investedByMonth){
		return investedByMonth.divide(currentDaySTock.getIn(),10,RoundingMode.HALF_UP);
	}



	
}
