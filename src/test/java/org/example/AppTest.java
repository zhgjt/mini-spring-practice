package org.example;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.example.context.ApplicationContext;
import org.example.context.ClassPathXmlApplicationContext;
import org.example.service.AServiceImpl;
import org.example.service.BaseService;
import org.example.web.servlet.DispatcherServlet;
import org.junit.jupiter.api.Test;

import java.io.File;

class AppTest {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Tomcat tomcat = new Tomcat();
        String webappDirLocation = "/mini-spring-practice/src/main/resources/web";
//        String webappDirLocation = "/src/main/WebContent";
        StandardContext context = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());

//        String path1 = "/mini-spring-practice/target/classes";
//        File additionWebInfClasses = new File(path1);
//        WebResourceRoot resources = new StandardRoot(context);
//        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
//                additionWebInfClasses.getAbsolutePath(), "/"));
//        context.setResources(resources);
//        System.out.println("%%%");
        Connector connector = new Connector();
        connector.setPort(8888);
        tomcat.setConnector(connector);
        try {
            tomcat.start();
        } catch (LifecycleException e) {
            throw new RuntimeException(e);
        }
        tomcat.getServer().await();
    }

    @Test
    public void t1() {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("test.xml", true);
            AServiceImpl aServiceImpl = (AServiceImpl) context.getBean("aService");
            aServiceImpl.sayhello();
            String property1 = aServiceImpl.getProperty1();
            String property2 = aServiceImpl.getProperty2();
            Object obj = aServiceImpl.getRef1();
            System.out.println("property1:" + property1);
            System.out.println("property2:" + property2);
            System.out.println(obj);
            System.out.println("*************************************");
            BaseService baseService = (BaseService) context.getBean("baseservice");
            System.out.println(baseService.getBasebaseservice());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void t2() {
        try {
            int i = int.class.newInstance();
            System.out.println("i:" + i);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}