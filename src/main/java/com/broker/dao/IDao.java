package com.broker.dao;

import java.io.IOException;

public interface IDao<T> {
	T parse() throws IOException;
}
