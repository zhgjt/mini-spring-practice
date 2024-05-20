package org.example.web.servlet;

import org.example.beans.BeansException;
import org.example.beans.factory.annotation.Autowired;
import org.example.web.bind.annotation.RequestMapping;
import org.example.web.context.WebApplicationContext;
import org.example.web.context.support.AnnotationConfigWebApplicationContext;
import org.example.web.context.support.XmlScanComponentHelper;
import org.example.web.context.support.XmlWebApplicationContext;
import org.example.web.method.HandlerMethod;
import org.example.web.method.annotation.RequestMappingHandlerAdapter;
import org.example.web.method.annotation.RequestMappingHandlerMapping;
import org.example.web.servlet.view.InternalResourceViewResolver;

import javax.naming.NamingEnumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 174351454328L;
    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";

    //IOC容器
    private WebApplicationContext parentApplicationContext;
    //存放controller的容器
    private WebApplicationContext webApplicationContext;
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    public DispatcherServlet() {
        super();
    }

    //初始化方法
    public void init(ServletConfig config) throws ServletException {
        //TODO 注释
        super.init(config);
        //拿到listener阶段生成的IOC容器，并赋值给dispatcherServlet的对应属性
        this.parentApplicationContext = (WebApplicationContext) (this.getServletContext().getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE));
        //通过config获取web.xml中配置的文件地址
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(contextConfigLocation, this.parentApplicationContext);
        refresh();
    }

    //初始化属性
    protected void refresh() {
        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.webApplicationContext);
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        this.requestMappingHandlerMapping = new RequestMappingHandlerMapping(wac);
    }

    protected void initHandlerAdapters(WebApplicationContext wac) {
        try {
            //从IOC容器中获取bean对象handlerAdapter
            this.requestMappingHandlerAdapter = (RequestMappingHandlerAdapter) this.webApplicationContext.getBean(HANDLER_ADAPTER_BEAN_NAME);
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
    }


    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);
        try {
            doDispatch(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = this.requestMappingHandlerMapping.getHandler(request);
        if (handlerMethod == null) return;
        ModelAndView mv = this.requestMappingHandlerAdapter.handle(request, response, handlerMethod);
        if (mv != null) {
            //把数据按照一定格式显示并输出到前端界面上
            render(mv, request, response);
        }
    }

    protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) {
        //拿到目标方法返回对象中的viewName
        String sTarget = mv.getViewName();
        //拿到目标方法返回对象中的数据
        Map<String, Object> modelMap = mv.getModel();
        //生成ViewResolver来找到目标页面
        ViewResolver viewResolver = null;
        try {
            viewResolver = (InternalResourceViewResolver) this.webApplicationContext.getBean("viewResolver");
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
        try {
            //生成View渲染页面
            View view = viewResolver.resolveViewName(sTarget);
            view.render(modelMap, request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
