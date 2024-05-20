package org.example.beans.factory.support;

import org.example.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    void removeBeanDefinition(String beanName);
    boolean containsBeanDefinition(String beanName);
    BeanDefinition getBeanDefinition(String beanName);
}
