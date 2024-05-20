package org.example.beans.factory;

import org.example.beans.BeansException;
import org.example.beans.factory.config.BeanDefinition;

import java.util.concurrent.ConcurrentHashMap;

public interface BeanFactory {
    //earlySingletonObjects用来存储创建后但未注入属性的的实例（毛坯实例)

    Object getBean(String beanName) throws BeansException;

    boolean containsBean(String beanName);

    boolean isPrototype(String beanName);

    boolean isSingleton(String beanName);

    Class<?> getType(String beanName);
}
