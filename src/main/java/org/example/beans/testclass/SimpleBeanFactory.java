package org.example.beans.testclass;

import org.example.beans.BeansException;
import org.example.beans.PropertyValue;
import org.example.beans.PropertyValues;
import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.config.BeanDefinition;
import org.example.beans.factory.config.ConstructorArgumentValue;
import org.example.beans.factory.config.ConstructorArgumentValues;
import org.example.beans.factory.support.BeanDefinitionRegistry;
import org.example.beans.factory.support.DefaultSingletonBeanRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

    private ConcurrentHashMap<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>(256);
    ConcurrentHashMap<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(256);

    @Override
    public Object getBean(String beanName) throws BeansException {
        //从singleton单例容器中获取bean
        Object singleton = getSingleton(beanName);
        if (singleton == null) {
            //获取毛坯实例
            singleton = earlySingletonObjects.get(beanName);

            //如果毛坯实例都不存在，创建实例并注入属性
            if (singleton == null) {
                //获取bean对应的BeanDefinition
                BeanDefinition definition = beanDefinitions.get(beanName);
                //若未找到对应的BeanDefinition则抛出异常
                if (definition == null) throw new BeansException("No beans.");
                //根据BeanDefinition创建bean对象
                singleton = createBean(definition);
                this.registerSingleton(beanName, singleton);
//                try {
//                    //根据找到的BeanDefinition创建bean对象
//                    singleton = Class.forName(definition.getClassName()).newInstance();
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//

            }
        }
        return singleton;
    }

    @Override
    public boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanDefinitions.get(beanName).isPrototype();
    }

    @Override
    public boolean isSingleton(String beanName) {
        return beanDefinitions.get(beanName).isSingleton();
    }

    @Override
    public Class<?> getType(String beanName) {
        return beanDefinitions.get(beanName).getClass();
    }

    //注册BeanDefinition
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitions.put(beanName, beanDefinition);
    }

    //根据BeanDefinition创建bean对象
    public void beanDefinitionInit() {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            //如果不是懒加载，调用get方法，把bean对象注入容器中
            if (!(entry.getValue().isLazyInit())) {
                try {
                    getBean(entry.getKey());
                } catch (BeansException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitions.remove(beanName);
        removeSingleton(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitions.contains(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitions.get(beanName);
    }

    //根据BeanDefinition创建bean
    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clazz = null;
        Object obj = null;

        try {
            clazz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //调用函数创建毛坯实例
        obj = doCreateBean(beanDefinition);
        //将毛坯实例放入earlySingletonObjects
        earlySingletonObjects.put(beanDefinition.getId(), obj);
        //调用handleProperties函数为创建的对象注入属性值
        handleProperties(beanDefinition, clazz, obj);
        return obj;
    }

    //创建毛坯实例
    private Object doCreateBean(BeanDefinition bd) {
        //以下属性分别存储类、bean、构造器对象
        Class<?> clazz = null;
        Object obj = null;
        Constructor<?> con = null;

        try {
            clazz = Class.forName(bd.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //获取构造器参数
        ConstructorArgumentValues cavs = bd.getConstructorArgumentValues();
        if (cavs != null || !cavs.isEmpty()) {
            //存储参数类型
            Class<?>[] paramsClass = new Class<?>[cavs.getArgumentCount()];
            //存储参数值
            Object[] paramsValue = new Object[cavs.getArgumentCount()];
            for (int i = 0; i < cavs.getArgumentCount(); i++) {
                ConstructorArgumentValue constructorArgumentValue = cavs.getIndexedArgumentValue(i);
                String type = constructorArgumentValue.getType();
                Object value = constructorArgumentValue.getValue();

                //判断构造器参数是哪一种基本类型
                if (type.equals("String") || type.equals("java.lang.String")) {
                    paramsClass[i] = String.class;
                    //value的值默认从xml文件中读取的为String类型
                    paramsValue[i] = value;
                } else if (type.equals("int")) {
                    paramsClass[i] = int.class;
                    paramsValue[i] = Integer.valueOf((String) value);
                } else if (type.equals("Integer") || type.equals("java.lang.Integer")) {
                    paramsClass[i] = Integer.class;
                    paramsValue[i] = Integer.valueOf((String) value);
                } else {
                    paramsClass[i] = String.class;
                    paramsValue[i] = value;
                }
            }
            try {
                //根据获取到的参数类型生成构造器
                con = clazz.getConstructor(paramsClass);
                //使用获得的参数向构造器传参,生成对象
                obj = con.newInstance(paramsValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                //如果参数为空，调用无参构造器
                obj = clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }

    //属性注入
    private void handleProperties(BeanDefinition bd, Class<?> clazz, Object obj) {
        //获取属性
        PropertyValues pvs = bd.getPropertyValues();
        if (pvs != null || !pvs.isEmpty()) {
            //分别存储属性的类型和取值
            Class<?> paramClass = null;
            Object paramValue = null;
            List<PropertyValue> paramsList = pvs.getPropertyValueList();

            for (int i = 0; i < pvs.size(); i++) {
                String type = paramsList.get(i).getType();
                Object value = paramsList.get(i).getValue();
                String name = paramsList.get(i).getName();
                boolean isRef = paramsList.get(i).getIsRef();

                //如果value的值不是引用类型
                if (!isRef) {
                    if (type.equals("String") || type.equals("java.lang.String")) {
                        paramClass = String.class;
                        //value的值默认从xml文件中读取的为String类型
                        paramValue = value;
                    } else if (type.equals("int")) {
                        paramClass = int.class;
                        paramValue = Integer.valueOf((String) value);
                    } else if (type.equals("Integer") || type.equals("java.lang.Integer")) {
                        paramClass = Integer.class;
                        paramValue = Integer.valueOf((String) value);
                    } else {
                        paramClass = String.class;
                        paramValue = value;
                    }
                } else {
                    try {
                        //如果value为引用类型
                        paramClass = Class.forName(type);
                        paramValue = getBean((String) value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                //按照setXxxx规范查找setter方法，调用setter方法设置属性
                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                try {
                    //通过反射生成set方法
                    Method method = clazz.getMethod(methodName, paramClass);
                    method.invoke(obj, paramValue);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
