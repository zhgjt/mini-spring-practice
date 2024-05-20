package org.example.beans.factory.config;

import org.example.beans.BeansException;
import org.example.beans.factory.BeanFactory;

public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object obj, String beanName) throws BeansException;

    Object postProcessAfterInitialization(Object obj, String beanName) throws BeansException;

    BeanFactory getBeanFactory();

    void setBeanFactory(BeanFactory beanFactory);
}
