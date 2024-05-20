package org.example.web.context;

import org.example.web.context.support.AnnotationConfigWebApplicationContext;
import org.example.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoaderListener implements ServletContextListener {
    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
    private WebApplicationContext webAapplicationContext;

    public ContextLoaderListener() {
    }

    public ContextLoaderListener(WebApplicationContext was) {
        this.webAapplicationContext = was;
    }

    //servlet容器上下文初始化
    public void contextInitialized(ServletContextEvent sce) {
        initWebApplicationContext(sce.getServletContext());
    }

    //创建IOC容器，放到servlet上下文中
    private void initWebApplicationContext(ServletContext servletContext) {
        //获取变量contextConfigLocation指定的xml文件名
        String fileName = servletContext.getInitParameter(this.CONFIG_LOCATION_PARAM);
        //创建bean容器
        WebApplicationContext was = new XmlWebApplicationContext(fileName);
        was.setServletContext(servletContext);
        this.webAapplicationContext = was;
        //将IOC容器和其对应的键放入到servlet上下文中
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webAapplicationContext);
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
