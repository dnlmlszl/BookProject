package org.example.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    static {
        try {
            Properties properties = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            properties.load(fis);

            boolean isTestEnvironment = isTestExecution();

            if (isTestEnvironment) {
                URL = properties.getProperty("test.db.url");
                USER = properties.getProperty("test.db.user");
                PASSWORD = properties.getProperty("test.db.password");
            } else {
                URL = properties.getProperty("db.url");
                USER = properties.getProperty("db.user");
                PASSWORD = properties.getProperty("db.password");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database configuration!");
        }
    }

    private static boolean isTestExecution() {
        try {
            Class.forName("org.junit.jupiter.api.Test");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String getUrl() {
        return URL;
    }

    public static String getUser() {
        return USER;
    }

    public static String getPassword() {
        return PASSWORD;
    }
}
