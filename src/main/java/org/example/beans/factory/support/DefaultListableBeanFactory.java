package org.example.beans.factory.support;

import org.example.beans.BeansException;
import org.example.beans.factory.config.AbstractAutowireCapableBeanFactory;
import org.example.beans.factory.config.BeanDefinition;
import org.example.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.*;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory {
    //AbstractBeanFactory中beanDefinitions权限为proteced，在该类中可以访问到


    private ConfigurableListableBeanFactory parentBeanFactory;

    //返回beanDefinition中的beaNames
    @Override
    public String[] getBeanDefinitionNames() {
        Set<String> beanNames = beanDefinitions.keySet();
        return beanNames.toArray(new String[beanNames.size()]);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            String className = entry.getValue().getClassName();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (type.isAssignableFrom(clazz)) beanNames.add(entry.getKey());
        }
        return beanNames.toArray(new String[beanNames.size()]);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        HashMap<String, T> beans = new HashMap<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            String className = entry.getValue().getClassName();
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (type.isAssignableFrom(clazz)) beans.put(entry.getKey(), (T) (entry.getValue()));
        }
        return beans;
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return new String[0];
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        BeanDefinition bd = getBeanDefinition(beanName);
        if (bd == null) return new String[0];
        return bd.getDependsOn();
    }

    public void setParentBeanFactory(ConfigurableListableBeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    public Object getBean(String beanName) throws BeansException {
        //先调用继承与父类AbstractBeanFactory的getBean方法
        Object bean = super.getBean(beanName);
        //如果当前bean容器中不存在指定的bean,在父容器中查找
        if (bean == null && parentBeanFactory != null) {
            bean = parentBeanFactory.getBean(beanName);
        }
        return bean;
    }
}
