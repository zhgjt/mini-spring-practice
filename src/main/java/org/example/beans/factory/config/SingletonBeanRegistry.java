package org.example.beans.factory.config;

public interface SingletonBeanRegistry {
    void registerSingleton(String beanName,Object objSingleton);
    Object getSingleton(String beanName);
    boolean containsSingleton(String nameName);
    String[] getSingletonNames();
}
