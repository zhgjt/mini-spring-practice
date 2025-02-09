package org.example.beans;

import org.apache.catalina.authenticator.jaspic.PersistentProviderRegistrations;
import org.apache.tomcat.util.http.ResponseUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanWrapperImpl extends PropertyEditorRegistrySupport {
    Object wrappedObject;//目标对象
    Class<?> clz;
    PropertyValues pvs;//参数值

    public BeanWrapperImpl(Object object) {
        registerDefaultEditors();//加载基本数据类型的参数转换器
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
    }

    public Object getBeanInstance() {
        return wrappedObject;
    }

    //绑定参数值
    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
            System.out.println("=+++=");
            System.out.println(pv.getName());
        }
    }

    //绑定多个参数
    public void setPropertyValue(PropertyValue pv) {
        //拿到参数的处理器
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());
        //找到对该参数类型的editor
        PropertyEditor pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        //设置参数值
        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());
    }

    //一个内部类，用于处理参数，通过getter()和setter()操作属性
    class BeanPropertyHandler {
        Method writeMethod = null;
        Method readMethod = null;
        Class<?> propertyClz = null;

        public Class<?> getPropertyClz() {
            return propertyClz;
        }

        public BeanPropertyHandler(String propertyName) {
            try {
                //获取参数对应的属性及类型
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();
                //获取设置属性的方法，按照约定为setXxx()
                this.writeMethod = clz.getDeclaredMethod("set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                //获取读属性的方法，按照约定为getXxx()
                this.readMethod = clz.getDeclaredMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        //调用getter读属性
        public Object getValue() {
            Object result = null;
            writeMethod.setAccessible(true);
            try {
                result = readMethod.invoke(wrappedObject);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            return result;
        }

        //调用setter设置属性值
        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
