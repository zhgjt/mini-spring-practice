package org.example.beans.factory.config;

import org.example.beans.BeansException;
import org.example.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.example.beans.factory.support.AbstractBeanFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
    private final List<BeanPostProcessor> processors = new ArrayList<>();

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        //保证处理器唯一
        processors.remove(beanPostProcessor);
        processors.add(beanPostProcessor);
    }

    public int getBeanPostProcessorCount() {
        return processors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return processors;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object obj = bean;
        for (BeanPostProcessor processor : processors) {
            if (obj == null) return bean;
            processor.setBeanFactory(this);
            obj = processor.postProcessBeforeInitialization(obj, beanName);
        }
        return obj;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeansException {
        Object obj = bean;
        for (BeanPostProcessor processor : processors) {
            if (obj == null) return obj;
            obj = processor.postProcessAfterInitialization(obj, beanName);
        }
        return obj;
    }

}
