package com.broker.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Stock implements Comparable<Stock> {
	private Date date;
	private BigDecimal in;
	private BigDecimal out;
	private boolean firstDayAfterLastThursday;
	
	public Stock() {
		firstDayAfterLastThursday=false;
	}
	
	@Override
	public int compareTo(Stock o) {
	    int a = this.getDate().compareTo(o.getDate());
	    return a;
	}
	
	
}



