package org.example.web.context.support;

import org.example.beans.BeansException;
import org.example.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.example.beans.factory.config.BeanDefinition;
import org.example.beans.factory.config.ConfigurableListableBeanFactory;
import org.example.beans.factory.support.BeanDefinitionRegistry;
import org.example.beans.factory.support.DefaultListableBeanFactory;
import org.example.context.*;
import org.example.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnnotationConfigWebApplicationContext extends AbstractApplicationContext implements WebApplicationContext {
    //存储servlet的上下文，（用来连接bean容器和ServlentContext
    private ServletContext servletContext;
    //父容器的引用
    private WebApplicationContext parentWAC;
    private DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(String packagePath) {
        this(packagePath, null);
    }

    public AnnotationConfigWebApplicationContext(String packagePath, WebApplicationContext parentWAC) {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        this.beanFactory = bf;

        if (parentWAC != null) {
            this.parentWAC = parentWAC;
            this.servletContext = parentWAC.getServletContext();
            //设置当前容器的父容器
            this.beanFactory.setParentBeanFactory(this.parentWAC.getBeanFactory());
        }

        URL url = this.getClass().getClassLoader().getResource(packagePath);
        //向url指定的文件中获取包名
        List<String> packageNames = XmlScanComponentHelper.getNodeValue(url);
        //从指定包中获取类的全类名
        List<String> controllerNames = scanPackage(packageNames);
        //根据controller的全类名加载对应的BeanDefinition
        loadBeanDefinitions(controllerNames);

        try {
            //加载所有bean到beanFactory容器中
            refresh();
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }

    //遍历获取到的包名，拿到每一个包名下的类对象
    private List<String> scanPackage(List<String> packages) {
        List<String> result = new ArrayList<>();
        for (String pg : packages) {
            result.addAll(scanPackage(pg));
        }
        return result;
    }

    //获取当前包名下的所有类（递归
    private List<String> scanPackage(String packageName) {
        List<String> result = new ArrayList<>();
        //把全类名中的"."转换为“\”
        URI uri = null;
        try {
            String name = packageName.replace(".", "/");
            System.out.println("name:" + name);
            uri = this.getClass().getClassLoader().getResource(name).toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        File dir = new File(uri);
        //遍历dir包下的子类或目录
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                //如果是文件，去掉后缀名添加到result中
                result.add(packageName + "." + file.getName().replace(".class", ""));
            } else {
                //如果是目录，递归调用目录
                String subPackageName = packageName + "." + file.getName();
                result.addAll(scanPackage(subPackageName));
            }
        }
        return result;
    }

    private void loadBeanDefinitions(List<String> controllerNames) {
        for (String controllerName : controllerNames) {
            //创建的BeanDefinition的id和className都为controllerName
            BeanDefinition bd = new BeanDefinition(controllerName, controllerName);
            this.beanFactory.registerBeanDefinition(controllerName, bd);
        }
        //先把BeanDefinition全部加载进来，然后创建bean对象
        this.beanFactory.beanDefinitionInit();
    }

    //设置父容器
    public void setParent(WebApplicationContext parentApplicationContext) {
        this.parentWAC = parentApplicationContext;
        this.beanFactory.setParentBeanFactory(this.parentWAC.getBeanFactory());
    }

    public ServletContext getServletContext() {
        return this.servletContext;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    public void registerListeners() {
        this.addApplicationListener(new ApplicationListener());
    }

    //初始化事件发布器
    public void initApplicationEventPublisher() {
        setApplicationEventPublisher(new SimpleApplicationEventPublisher());
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
    }

    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    public void onRefresh() {
        this.beanFactory.refresh();
    }

    public void finishRefresh() {
    }

    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }
}
