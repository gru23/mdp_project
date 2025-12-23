package org.unibl.etf.utils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConnectionFactoryUtil {

	public static Connection createConnection() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(Config.get("connection.factory.host"));
		factory.setUsername(Config.get("connection.factory.username"));
		factory.setPassword(Config.get("connection.factory.password"));
		return factory.newConnection();
	}
}
