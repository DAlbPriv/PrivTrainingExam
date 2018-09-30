package com.broker.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.broker.dao.BrokerWithLombokDao;

import lombok.extern.log4j.Log4j;

@Log4j
public class FileHandler {
	static Properties prop = null;
	static InputStream input = null;

	static {
		prop = new Properties();
		try {
			input = BrokerWithLombokDao.class.getResourceAsStream("/config.properties");
			prop.load(input);
		}catch(IOException e) {
			log.error(new StringBuilder("The FileHandler class cannot be instantiated because of this error: ").append(e.getMessage()));
		}
	}

	public static BufferedReader openFile() throws IOException{
		String fileName = prop.getProperty("filename");
		String resourcePath = prop.getProperty("resourcePath");
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(resourcePath+fileName));
		}catch(IOException e){
			log.error(new StringBuilder("The CSV File cannot be oppened because: ").append(e.getMessage()));
			throw e;
		}		

		return bufferedReader;
	}
	

	
	

}
