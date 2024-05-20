package org.example.beans.factory.xml;

import org.dom4j.Element;
import org.example.beans.PropertyValue;
import org.example.beans.PropertyValues;
import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.config.*;
import org.example.beans.factory.support.AbstractBeanFactory;
import org.example.beans.testclass.SimpleBeanFactory;
import org.example.core.Resource;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    private AbstractBeanFactory beanFactory;

    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            //创建BeanDefinition对象存储bean信息
            Element element = (Element) resource.next();
            String beanName = element.attributeValue("id");
            String className = element.attributeValue("class");
            String initMethod = element.attributeValue("init-method");

            BeanDefinition bd = new BeanDefinition(beanName, className);
            bd.setInitMethodName(initMethod);

            //PVS对象存储bean的属性
            PropertyValues PVS = new PropertyValues();
            //refs存储bean的依赖
            List<String> refs = new ArrayList<>();

            for (Element e : element.elements("property")) {
                //bean属性是否为引用
                boolean isRef = false;
                String type = e.attributeValue("type");
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");
                String ref = e.attributeValue("ref");
                String val = "";
                //判断ref是否有值
                if (ref != null && !(ref.equals(""))) {
                    isRef = true;
                    val = ref;
                    refs.add(ref);
                } else if (value != null && !(value.equals(""))) {
                    val = value;
                }
                PropertyValue pv = new PropertyValue(type, name, val, isRef);
                PVS.addPropertyValue(pv);
            }
            String[] dependsOn = refs.toArray(new String[0]);
            bd.setPropertyValues(PVS);
            bd.setDependsOn(dependsOn);


            //创建CAVS对象存储bean的构造器参数
            ConstructorArgumentValues CAVS = new ConstructorArgumentValues();
            for (Element e : element.elements("constructor-arg")) {
                String type = e.attributeValue("type");
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");
                ConstructorArgumentValue cav = new ConstructorArgumentValue(value, type, name);
                CAVS.addArgumentValue(cav);
            }
            bd.setConstructorArgumentValues(CAVS);

            //注册BeanDefinition先把BeanDefinition全部加载进来
            beanFactory.registerBeanDefinition(beanName, bd);
        }
        //如果不是懒加载模式，根据beanDefinition生成bean对象
        beanFactory.beanDefinitionInit();
    }
}
