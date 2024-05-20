package org.example.context;

import org.example.beans.BeansException;
import org.example.beans.factory.ListableBeanFactory;
import org.example.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.example.beans.factory.config.AbstractAutowireCapableBeanFactory;
import org.example.beans.factory.config.ConfigurableListableBeanFactory;
import org.example.beans.factory.support.DefaultListableBeanFactory;
import org.example.beans.factory.support.DefaultSingletonBeanRegistry;
import org.example.beans.factory.xml.XmlBeanDefinitionReader;
import org.example.core.ClassPathXmlResource;
import org.example.core.Resource;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {
    //bean容器
    private DefaultListableBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        //根据fileName文件名解析xml文件
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        //使用xmlBeanDefinitionReader对象为bean容器加载BeanDefinition(bean的基本信息)
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
        if (isRefresh) {
            try {
                refresh();
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * AbstractApplicationContext抽象类中的的refresh方法，
     * 方法中的函数全部为abstract类型，需要在当前类中实现
     * <p>
     * <p>
     * public void refresh() throws BeansException, IllegalStateException {
     * postProcessBeanFactory(getBeanFactory());
     * registerBeanPostProcessors(getBeanFactory());
     * initApplicationEventPublisher();
     * onRefresh();
     * registerListeners();
     * finishRefresh();
     * }
     */

    //注册监听者
    @Override
    public void registerListeners() {
        this.addApplicationListener(new ApplicationListener());
    }

    //初始化事件发布器
    @Override
    public void initApplicationEventPublisher() {
        setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    //注册bean处理器
    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    //刷新上下文
    @Override
    public void onRefresh() {
        beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        getApplicationEventPublisher().publishEvent(event);
    }

    //向publisher中添加listener，publisher在AbstractApplicationContext中
    @Override
    public void addApplicationListener(ApplicationListener listener) {
        getApplicationEventPublisher().addApplicationListener(listener);
    }
}
