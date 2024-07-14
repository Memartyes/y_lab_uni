package ru.domain;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ru.domain.config.AppConfig;
import ru.domain.config.WebConfig;

/**
 * Application класс для инициализации приложения.
 */
public class Application extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Возвращает корневые конфигурационные классы, которые создают корневой контекст приложения.
     *
     * @return массив классов корневой конфигурации
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
                AppConfig.class
        };
    }

    /**
     * Возвращает конфигурационные классы для сервлетов, которые создают контекст диспетчера сервлетов.
     *
     * @return массив классов конфигурации сервлетов
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
                WebConfig.class
        };
    }

    /**
     * Возвращает маппинг сервлетов, который определяет, какие URL-адреса будут обрабатываться диспетчером сервлетов.
     *
     * @return массив строк маппинга сервлетов
     */
    @Override
    protected String[] getServletMappings() {
        return new String[] {
                "/"
        };
    }
}
