package org.example.context;

import org.example.beans.BeansException;
import org.example.beans.factory.config.BeanFactoryPostProcessor;
import org.example.beans.factory.config.BeanPostProcessor;
import org.example.beans.factory.config.ConfigurableListableBeanFactory;
import org.example.core.env.Environment;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractApplicationContext implements ApplicationContext {
    private Environment environment;
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();
    private long startupDate;
    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicBoolean closed = new AtomicBoolean();
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Object getBean(String beanName) throws BeansException {
        return getBeanFactory().getBean(beanName);
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    public void refresh() throws BeansException, IllegalStateException {
        postProcessBeanFactory(getBeanFactory());
        registerBeanPostProcessors(getBeanFactory());
        initApplicationEventPublisher();
        onRefresh();
        registerListeners();
        finishRefresh();
    }

    public abstract void registerListeners();

    public abstract void initApplicationEventPublisher();

    public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);

    public abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory);

    public abstract void onRefresh();

    public abstract void finishRefresh();

    @Override
    public String getApplicationName() {
        return "";
    }

    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public long getStartupDate() {
        return startupDate;
    }

    public void setStartupDate(long startupDate) {
        this.startupDate = startupDate;
    }

    public AtomicBoolean getActive() {
        return active;
    }

    public AtomicBoolean getClosed() {
        return closed;
    }

    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return getBeanFactory().isPrototype(name);
    }

    @Override
    public Class<?> getType(String name) {
        return getBeanFactory().getType(name);
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        getBeanFactory().registerSingleton(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return getBeanFactory().getSingleton(beanName);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return getBeanFactory().containsSingleton(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return getBeanFactory().getSingletonNames();
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        getBeanFactory().addBeanPostProcessor(beanPostProcessor);

    }

    @Override
    public int getBeanPostProcessorCount() {
        return getBeanFactory().getBeanPostProcessorCount();
    }

    @Override
    public void registerDependentBean(String beanName, String dependentBeanName) {
        getBeanFactory().registerDependentBean(beanName, dependentBeanName);
    }

    @Override
    public String[] getDependentBeans(String beanName) {
        return getBeanFactory().getDependentBeans(beanName);
    }

    @Override
    public String[] getDependenciesForBean(String beanName) {
        return getBeanFactory().getDependenciesForBean(beanName);
    }
}
