package org.example.context;

import org.example.beans.BeansException;
import org.example.beans.factory.ListableBeanFactory;
import org.example.beans.factory.config.BeanFactoryPostProcessor;
import org.example.beans.factory.config.ConfigurableBeanFactory;
import org.example.beans.factory.config.ConfigurableListableBeanFactory;
import org.example.core.env.Environment;
import org.example.core.env.EnvironmentCapable;

public interface  ApplicationContext extends EnvironmentCapable, ApplicationEventPublisher, ConfigurableBeanFactory, ListableBeanFactory {
    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();
}
