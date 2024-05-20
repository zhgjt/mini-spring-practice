package org.example.service;

public class AServiceImpl implements AService {
    //name、level作为构造器参数，使用构造器注入
    private String name;
    private int level;
    //property1、property2作为属性，使用getter、setter注入
    private String property1;
    private String property2;
    private BaseService ref1;

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public AServiceImpl() {
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public BaseService getRef1() {
        return ref1;
    }

    public void setRef1(BaseService ref1) {
        this.ref1 = ref1;
    }

    @Override
    public void sayhello() {
        System.out.println("AServiecImple:hello!");
    }
}
