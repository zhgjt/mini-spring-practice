package org.example.context;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.beans.factory.config.BeanDefinition;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassPathXmlApplicationContextPractice {
    //存放从xml文件中获取的到beanDefinition
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    //存放单例bean对象
    private HashMap<String, Object> singletons = new HashMap<>();

    public ClassPathXmlApplicationContextPractice(String fileName) throws Exception {
        //读取xml文件的内容，为beansDefinition赋值
        this.readXml(fileName);
        //根据beansDefinition生成beans
        this.instanceBeans();
    }

    private void readXml(String fileName) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        URL resource = this.getClass().getClassLoader().getResource(fileName);
        Document document = saxReader.read(resource);
        Element rootElement = document.getRootElement();

        for(Element element:(List<Element>)(rootElement.elements())){
            String id = element.attributeValue("id");
            String className = element.attributeValue("class");
            beanDefinitions.add(new BeanDefinition(id,className));
        }
    }

    private void instanceBeans() throws Exception {
        for(BeanDefinition beanDefinition:beanDefinitions){
            String id = beanDefinition.getId();
            Object obj = Class.forName(beanDefinition.getClassName()).newInstance();
            singletons.put(id,obj);
        }
    }

    public Object getBean(String id){
        return singletons.get(id);
    }
}
