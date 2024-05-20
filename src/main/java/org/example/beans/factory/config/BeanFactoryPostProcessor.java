package org.example.beans.factory.config;

import org.example.beans.BeansException;
import org.example.beans.factory.BeanFactory;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
