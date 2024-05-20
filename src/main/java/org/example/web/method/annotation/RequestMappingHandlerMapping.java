package org.example.web.method.annotation;

import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.example.beans.BeansException;
import org.example.web.bind.annotation.RequestMapping;
import org.example.web.context.WebApplicationContext;
import org.example.web.method.HandlerMethod;
import org.example.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RequestMappingHandlerMapping implements HandlerMapping {
    private WebApplicationContext was;
    //MappingRegistry对象用于存储url到controller以及方法的映射
    private final MappingRegistry mappingRegistry = new MappingRegistry();

    public RequestMappingHandlerMapping(WebApplicationContext was) {
        this.was = was;
        initMapping();
    }

    protected void initMapping() {
        Method[] methods = null;
        Class<?> clazz = null;
        Object obj = null;
        //获取controller容器中所有的BeanDefinition
        String[] controllerNames = was.getBeanDefinitionNames();

        for (String controllerName : controllerNames) {
            try {
                //从WebApplicationContext上下文环境中获取指定bean对象
                obj = this.was.getBean(controllerName);
            } catch (BeansException e) {
                throw new RuntimeException(e);
            }
            clazz = obj.getClass();
            methods = clazz.getDeclaredMethods();
            if (methods == null) continue;

            //遍历controller中所有的方法
            for (Method method : methods) {
                //如果方法被RequestMapping标记
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    //获取RequestMapping注解的value值
                    String value = method.getAnnotation(RequestMapping.class).value();
                    this.mappingRegistry.getUrlMappingNames().add(value);
                    this.mappingRegistry.getMappingObjs().put(value, obj);
                    this.mappingRegistry.getMappingMethods().put(value, method);
                }
            }
        }
    }

    //根据URL查找对应的调用方法
    @Override
    public HandlerMethod getHandler(HttpServletRequest request) throws Exception {
        String url = request.getServletPath();
        System.out.println("请求路径：" + url);
        if (!(this.mappingRegistry.getMappingObjs().containsKey(url))) {
            return null;
        }
        Method method = this.mappingRegistry.getMappingMethods().get(url);
        Object obj = this.mappingRegistry.getMappingObjs().get(url);
        //根据获取到的方法和bean构造HandlerMethod对象
        HandlerMethod handlerMethod = new HandlerMethod(obj, method);
        return handlerMethod;
    }
}
