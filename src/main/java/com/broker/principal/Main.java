package com.broker.principal;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import com.broker.dao.BrokerWithLombokDao;
import com.broker.dao.IDao;
import com.broker.model.Stock;

public class Main {
	static BigDecimal AMOUNT_INVESTED = BigDecimal.valueOf(50);
	static BigDecimal PERCENTAGE = BigDecimal.valueOf(100);
	static BigDecimal COMISSION = BigDecimal.valueOf(2);
	static BigDecimal investedByMonth = null;

	public static void main(String[] args) {
		investedByMonth = calculateRealInvested();
		// TODO Auto-generated method stub
		List<Stock> stockListFromFile = null;
		IDao stock = new BrokerWithLombokDao();
		try {
			stockListFromFile = (List<Stock>) stock.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(stockListFromFile != null) {
			Collections.sort(stockListFromFile);
			System.out.println(calculateSellingPrice(stockListFromFile));
		}
		
	}
	
	
	private static BigDecimal calculateSellingPrice(List<Stock> stockListFromFile) {
		Stock previousStock = new Stock();
		//BigDecimal stockPerDayCalculated = null;
		BigDecimal totalInvested = BigDecimal.valueOf(0);
		Stock aux = null;
		BigDecimal stocks =  BigDecimal.ZERO;
		
		for(int i = 0; i < stockListFromFile.size(); i++) {
			aux = stockListFromFile.get(i);
			if(previousStock.isLastThursday() && i != stockListFromFile.size() - 1)
				stocks = stocks.add(buyStock(aux,investedByMonth));
			if (aux.isLastThursday() && i != stockListFromFile.size() - 1)
				previousStock = aux;
		}

			totalInvested = stocks.multiply(stockListFromFile.get(stockListFromFile.size()-1).getOut());
/*			if(previousStock.isLastThursday())
				totalInvested = totalInvested.add(investedByMonth);
			if (aux.isLastThursday() && i != stockList.size() - 1)
				previousStock = aux;
			stockPerDayCalculated = CalculateStock(aux);
			if(!totalInvested.equals(BigDecimal.ZERO))
				totalInvested = totalInvested.multiply(stockPerDayCalculated, MathContext.DECIMAL32);
			previousStock = aux;
*/
		

		return totalInvested.setScale(3, RoundingMode.HALF_UP);

	}
	
	private static BigDecimal calculateRealInvested() {
		BigDecimal PercentageWithoutCommision = PERCENTAGE.subtract(COMISSION);
		BigDecimal myRealPercentage= PercentageWithoutCommision.divide(PERCENTAGE,10,RoundingMode.HALF_UP);
		return myRealPercentage.multiply(AMOUNT_INVESTED);
	}
	
	private static BigDecimal buyStock(Stock aux,BigDecimal investedByMonth){
		return aux.getIn().divide(investedByMonth,10,RoundingMode.HALF_UP);
	}

}
