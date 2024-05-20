package org.example.http.converter;

import org.example.util.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DefaultHttpMessageConverter implements HttpMessageConverter {
    String defaultContentType = "text/json;charset=UTF-8";
    String defaultCharacterEncoding = "UTF-8";
    //这里的objectMapper对象没有被初始化，但不会出现空指针异常，是由于在加载IOC容器时把该bean对象的属性设置好了
    ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void write(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType(defaultContentType);
        response.setCharacterEncoding(defaultCharacterEncoding);
        writeInternal(obj, response);
        //将写入response中的内容立即返回
        response.flushBuffer();
    }

    private void writeInternal(Object obj, HttpServletResponse response) throws IOException {
        //把对象转换为json字符串
        String sJsonStr = this.objectMapper.writeValuesAsString(obj);
        //把转换得到的json字符串写入response中
        PrintWriter pw = response.getWriter();
        pw.append(sJsonStr);
    }
}
