package org.example.service;

import org.example.beans.factory.annotation.Autowired;

public class BaseService {
    @Autowired
    private BaseBaseService basebaseservice;
    private BaseBaseService bbs;


    public BaseService() {
    }

    public BaseBaseService getBbs() {
        return bbs;
    }

    public void setBbs(BaseBaseService bbs) {
        this.bbs = bbs;
    }

    public BaseBaseService getBasebaseservice() {
        return basebaseservice;
    }

    public void setBasebaseservice(BaseBaseService basebaseservice) {
        this.basebaseservice = basebaseservice;
    }
}
