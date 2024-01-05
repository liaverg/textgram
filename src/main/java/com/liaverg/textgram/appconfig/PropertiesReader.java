package com.liaverg.textgram.appconfig;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertiesReader {
    private final Properties properties;
    private static final String APPLICATION_PROPERTIES_FILE = "application.properties";

    public PropertiesReader() {
        this.properties = new Properties();
        readProperties();
    }

    private void readProperties(){
        URL url = ClassLoader.getSystemResource(APPLICATION_PROPERTIES_FILE);
        try {
            properties.load(url.openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJdbcUrl() {
        return properties.getProperty("jdbcUrl");
    }

    public String getUser() {
        return properties.getProperty("user");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public int getLeakDetectionThreshold() {
        return Integer.parseInt(properties.getProperty("leakDetectionThreshold"));
    }
}