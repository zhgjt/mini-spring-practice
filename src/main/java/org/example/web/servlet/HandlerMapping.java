package org.example.web.servlet;

import org.example.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

public interface HandlerMapping {
    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}
