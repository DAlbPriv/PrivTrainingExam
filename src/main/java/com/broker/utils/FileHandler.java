package com.broker.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.broker.dao.BrokerWithLombokDao;


public class FileHandler {
	static Properties prop = null;
	static InputStream input = null;

	static {
		prop = new Properties();
		try {
			input = BrokerWithLombokDao.class.getResourceAsStream("/config.properties");
			prop.load(input);
		}catch(IOException e) {
			
		}
	}

	public static BufferedReader openFile() throws IOException{
		String fileName = prop.getProperty("filename");
		String resourcePath = prop.getProperty("resourcePath");
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(resourcePath+fileName));
		}catch(IOException e){
			System.out.println("Error");
			throw e;
		}		

		return bufferedReader;
	}
	

	
	

}
