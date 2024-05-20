package org.example.base_package;

import org.example.beans.factory.annotation.Autowired;
import org.example.entity.User;
import org.example.service.AServiceImpl;
import org.example.service.UserService;
import org.example.web.bind.annotation.RequestMapping;
import org.example.web.bind.annotation.ResponseBody;
import org.example.web.method.HandlerMethod;
import org.example.web.servlet.DispatcherServlet;

public class HelloWordBean {
    @Autowired
    private AServiceImpl aService;
    private String str;
    @Autowired
    private UserService userService;

    public HelloWordBean() {
    }

    @RequestMapping("/test")
    public String get() {
        System.out.println("/test");
        System.out.println(aService.getProperty1());
        System.out.println(aService.getProperty2());
        System.out.println(aService.getRef1());
        return "hello world for doGet!";
    }

    @RequestMapping("/test1")
    public String get1(User user) {
        if (user != null) {
            System.out.println("name:" + user.getName());
            System.out.println("age:" + user.getId());
        }
        return "test1";
    }

    @RequestMapping("/test2")
    @ResponseBody
    public User get2(User user) {
        //user.setAge(23);
        user.setName("zhangjintao");
        return user;
    }

    @RequestMapping("/test3")
    public String get3(User user) {
        //user.setAge(23);
        user.setName("zhangjintao");
        return "index";
    }

    @RequestMapping("/test4")
    @ResponseBody
    public User get4(User user) {
        //DispatcherServlet
        return userService.getUserInfo(user.getId());
    }
}
