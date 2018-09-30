package com.broker.dao;

import java.io.IOException;
import java.math.BigDecimal;

public interface IDao<T> {
	void parse() throws IOException;
	BigDecimal calculateSellingPrice();
}
