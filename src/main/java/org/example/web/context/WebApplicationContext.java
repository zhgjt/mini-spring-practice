package org.example.web.context;

import org.example.context.ApplicationContext;

import javax.servlet.ServletContext;

public interface WebApplicationContext extends ApplicationContext {
    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    void setServletContext(ServletContext servletContext);

    ServletContext getServletContext();
}
