package org.example.beans.factory.config;

import org.example.beans.BeansException;
import org.example.beans.factory.BeanFactory;

public interface AutowireCapableBeanFactory extends BeanFactory {
    int AUTOWIRE_NO = 0;
    int AUTOWIRE_BY_NAME = 1;
    int AUTOWIRE_BY_TYPE = 2;

    Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeansException;

    Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeansException;
}
