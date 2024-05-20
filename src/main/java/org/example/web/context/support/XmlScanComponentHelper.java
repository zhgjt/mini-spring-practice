package org.example.web.context.support;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlScanComponentHelper {
    public static List<String> getNodeValue(URL xmlPath) {
        //packages存储xml文件中配置的包名
        List<String> packages = new ArrayList<>();

        SAXReader saxReader = new SAXReader();
        Document document = null;
        System.out.println("xmlPath:"+xmlPath);
        try {
            document = saxReader.read(xmlPath);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootElement = document.getRootElement();
        Iterator<Element> elementIterator = rootElement.elementIterator();
        while (elementIterator.hasNext()) {
            String s = elementIterator.next().attributeValue("base-package");
            packages.add(s);
        }
        return packages;
    }
}
