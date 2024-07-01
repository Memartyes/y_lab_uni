package ru.domain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Ютилити класс для загрузки конфигурации application.properties.
 */
public class ConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);
    private static Properties properties = new Properties();

    // Статический блок для ранней статической инициализации считывания файла application.properties.
    static {
        try (InputStream inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream == null) {
                throw new IOException("Unable to find application.properties");
            }
            properties.load(inputStream);
        } catch (Exception e) {
//            e.printStackTrace();
//            throw new ExceptionInInitializerError(e);
            logger.error("Failed to load configuration properties.", e);
        }
    }

    /**
     * Берем значение property ключа из параметра
     *
     * @param key the property key
     * @return the property value
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}