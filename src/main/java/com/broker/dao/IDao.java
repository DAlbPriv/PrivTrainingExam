package com.broker.dao;

import java.io.IOException;
import java.math.BigDecimal;

public interface IDao<T> {
	/**
	 * Parse the information of a csvFile to an object inside the class
	 * @throws IOException
	 */
	void parse() throws IOException;
	
	/**
	 * 
	 * @return the selling price of the stocks
	 */
	BigDecimal calculateSellingPrice();
}
