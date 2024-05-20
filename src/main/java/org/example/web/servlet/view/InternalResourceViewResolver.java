package org.example.web.servlet.view;

import org.example.web.servlet.View;
import org.example.web.servlet.ViewResolver;

public class InternalResourceViewResolver implements ViewResolver {
    private Class viewClass = null;
    private String viewClassName = "";
    private String prefix = "";
    private String suffix = "";
    private String contentType;

    public InternalResourceViewResolver() {
        if (getViewClass() == null) {
            setViewClass(JstlView.class);
        }
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
        Class clz = null;
        try {
            clz = Class.forName(viewClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setViewClass(clz);
    }

    protected String getViewClassName() {
        return this.viewClassName;
    }

    public void setViewClass(Class viewClass) {
        this.viewClass = viewClass;
    }

    protected Class getViewClass() {
        return this.viewClass;
    }

    public void setPrefix(String prefix) {
        this.prefix = (prefix != null ? prefix : "");
    }

    protected String getPrefix() {
        return this.prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = (suffix != null ? suffix : "");
    }

    protected String getSuffix() {
        return this.suffix;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    protected String getContentType() {
        return this.contentType;
    }

    //根据viewName生成View
    @Override
        public View resolveViewName(String viewName) throws Exception {
        return buildView(viewName);
    }

    protected View buildView(String viewName) throws Exception {
        //通过反射创建View对象
        Class viewClass = getViewClass();
        View view = (View) viewClass.newInstance();
        //设置View的处理文件路径、类型
        view.setUrl(getPrefix() + viewName + getSuffix());
        String contentType = getContentType();
        view.setContentType(contentType);
        return view;
    }
}
