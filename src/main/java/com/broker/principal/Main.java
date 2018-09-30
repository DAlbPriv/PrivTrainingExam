package com.broker.principal;


import java.io.IOException;


import com.broker.dao.BrokerWithLombokDao;

import com.broker.dao.IDao;

import lombok.extern.log4j.Log4j;

@Log4j
public class Main {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IDao stock = new BrokerWithLombokDao();
		try {
			stock.parse();
		} catch (IOException e) {
			log.error(new StringBuilder("The file content cannot be parsed to a list of Stock").append(e.getMessage()));
			e.printStackTrace();
		}
		log.info(new StringBuilder("\t Stock evolution:"));
		log.info(new StringBuilder("\nMoney after investment: ").append(stock.calculateSellingPrice()));
		
	}
}
