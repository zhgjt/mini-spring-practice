package org.example.beans.factory.annotation;

import org.example.beans.BeansException;
import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.config.AbstractAutowireCapableBeanFactory;
import org.example.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
    //beanFactory表示Autowired注解处理器操作的是哪一个bean容器
    private BeanFactory beanFactory;

    //初始化函数之前的处理逻辑
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object obj = bean;
        Class<?> clazz = bean.getClass();
        //拿到bean对象的属性
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                //表示属性是否被Autowired注解修饰
                boolean isAutoWired = field.isAnnotationPresent(Autowired.class);
                if (!isAutoWired) continue;

                //获取属性的name
                String name = field.getName();
                //根据属性名向bean容器中获取bean对象
                Object value = this.getBeanFactory().getBean(name);
                //通过反射修改把获取到的bean对象注入属性中
                field.setAccessible(true);
                try {
                    field.set(obj, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }

    //初始化函数之后的处理逻辑
    @Override
    public Object postProcessAfterInitialization(Object obj, String beanName) throws BeansException {
        return null;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
