package ru.otus.andrk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHandler {
    public static Properties getConfig(String configFile) {
        try (InputStream input = ConfigHandler.class.getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new IOException("config file " + configFile + " not found");
            }
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
