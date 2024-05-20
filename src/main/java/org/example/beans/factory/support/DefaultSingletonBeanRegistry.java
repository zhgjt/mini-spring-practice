package org.example.beans.factory.support;

import org.example.beans.factory.config.SingletonBeanRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    //对bean容器中的对象有更新和读取操作，使用ConcurrentHashMap避免多线程下的不安全
    protected ConcurrentHashMap<String, Object> singletons = new ConcurrentHashMap<>(256);
    protected List<String> beanNames = new ArrayList<>();

    @Override
    public void registerSingleton(String beanName, Object objSingleton) {
        //加入同步监视器，把两个操作绑定到一块，避免多线程下的不安全
        synchronized (this.singletons) {
            singletons.put(beanName, objSingleton);
            beanNames.add(beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletons.get(beanName);
    }

    @Override
    public boolean containsSingleton(String nameName) {
        return singletons.containsKey(beanNames);
    }

    @Override
    public String[] getSingletonNames() {
        return beanNames.toArray(new String[0]);
    }

    protected Object removeSingleton(String beanName) {
        Object obj = null;
        synchronized (this.singletons) {
            beanNames.remove(beanName);
            obj = singletons.remove(beanName);
        }
        return obj;
    }

}
