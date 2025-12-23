package org.unibl.etf.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Properties props = new Properties();

    static {
        try (InputStream in =
             Config.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (in == null) {
                throw new RuntimeException("config.properties not found");
            }
            props.load(in);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
