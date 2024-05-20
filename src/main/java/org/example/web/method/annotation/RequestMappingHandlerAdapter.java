package org.example.web.method.annotation;

import org.apache.catalina.User;
import org.example.http.converter.HttpMessageConverter;
import org.example.web.bind.WebDataBinder;
import org.example.web.bind.annotation.RequestMapping;
import org.example.web.bind.annotation.ResponseBody;
import org.example.web.bind.support.WebDataBinderFactory;
import org.example.web.context.WebApplicationContext;
import org.example.web.method.HandlerMethod;
import org.example.web.servlet.HandlerAdapter;
import org.example.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter implements HandlerAdapter {
    private WebApplicationContext was;

    private HttpMessageConverter messageConverter = null;

    public RequestMappingHandlerAdapter() {
    }

    public RequestMappingHandlerAdapter(WebApplicationContext was) {
        this.was = was;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler == null) response.getWriter().append("result.toString()!");
        ModelAndView mv = invokeHandlerMethod(request, response, (HandlerMethod) handler);
        return mv;
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        ModelAndView mv = null;

        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();
        Object[] methodParamObjs = new Object[methodParameters.length];
        int i = 0;
        //对调用方法里的每一个参数，处理绑定
        for (Parameter methodParameter : methodParameters) {
            Object methodParamObj = methodParameter.getType().newInstance();
            //给这个参数创建WebDataBinder
            WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
            wdb.bind(request);
            methodParamObjs[i] = methodParamObj;
            i++;
        }
        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);
        //如果是ResponseBody注解，仅仅返回值，则转换数据格式后直接写到response
        if (invocableMethod.isAnnotationPresent(ResponseBody.class)) {
            this.messageConverter.write(returnObj, response);
        } else {//返回的是前端页面
            if (returnObj instanceof ModelAndView) mv = (ModelAndView) returnObj;
            else if (returnObj instanceof String) mv = new ModelAndView((String) returnObj);
        }
        return mv;
    }

    public WebApplicationContext getWas() {
        return was;
    }

    public void setWas(WebApplicationContext was) {
        this.was = was;
    }

    public HttpMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(HttpMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }
}
