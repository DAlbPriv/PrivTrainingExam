package com.broker.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.broker.model.Stock;

public class StockOperations {

	
	
	
	private static BigDecimal buyStock(Stock aux,BigDecimal totalInvested){
		return aux.getIn().divide(totalInvested,10,RoundingMode.HALF_UP);
	}

	private static BigDecimal buyStock(Stock stock) {
		return stock.getIn().divide(stock.getIn(),10, RoundingMode.HALF_UP);
	}


}
