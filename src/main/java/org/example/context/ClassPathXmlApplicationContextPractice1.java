package org.example.context;

import org.example.beans.BeansException;
import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.example.beans.factory.config.AbstractAutowireCapableBeanFactory;
import org.example.beans.factory.support.DefaultListableBeanFactory;
import org.example.beans.factory.xml.XmlBeanDefinitionReader;
import org.example.core.ClassPathXmlResource;
import org.example.core.Resource;

public class ClassPathXmlApplicationContextPractice1 implements BeanFactory {
    private AbstractAutowireCapableBeanFactory beanFactory;

    public ClassPathXmlApplicationContextPractice1(String fileName) {
        //根据fileName文件名解析xml文件
        Resource resource = new ClassPathXmlResource(fileName);
//        AbstractAutowireCapableBeanFactory beanFactory = new AbstractAutowireCapableBeanFactory();
        AbstractAutowireCapableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        //使用xmlBeanDefinitionReader对象为bean容器加载BeanDefinition(bean的基本信息)
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory;
    }

    //参数isReFresh表示是否加载所有的bean对象到bean容器中
    public ClassPathXmlApplicationContextPractice1(String fileName, boolean isReFresh) {
        this(fileName);
        if (isReFresh) {
            refresh();
        }
    }

    public void refresh() {
        //先向bean容器中添加处理器，在调用refresh方法
        registryBeanPostProcessor(this.beanFactory);
        onRefresh();
    }

    //向bean容器中添加处理器
    private void registryBeanPostProcessor(AbstractAutowireCapableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    private void onRefresh() {
        this.beanFactory.refresh();
    }


    @Override
    public Object getBean(String beanName) throws BeansException {
        return beanFactory.getBean(beanName);
    }

    @Override
    public boolean containsBean(String beanName) {
        return beanFactory.containsBean(beanName);
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanFactory.isPrototype(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return beanFactory.isSingleton(beanName);
    }

    @Override
    public Class<?> getType(String beanName) {
        return beanFactory.getType(beanName);
    }

}
