package org.example.beans.factory;

import org.example.beans.BeansException;

import java.beans.Beans;
import java.util.Map;

//将bean容器作为一个集合来对待
public interface ListableBeanFactory extends BeanFactory{
    boolean containsBeanDefinition(String beanName);
    int getBeanDefinitionCount();
    String[] getBeanDefinitionNames();
    String[] getBeanNamesForType(Class<?> type);
    <T> Map<String,T> getBeansOfType(Class<T> type) throws BeansException;

}
