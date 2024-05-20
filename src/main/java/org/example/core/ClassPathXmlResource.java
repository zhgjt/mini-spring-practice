package org.example.core;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ClassPathXmlResource implements Resource {
    private Iterator<Element> iterable;

    public ClassPathXmlResource(String fileName) {
        SAXReader saxReader = new SAXReader();
        URL resource = this.getClass().getClassLoader().getResource(fileName);
        Document document = null;
        try {
            document = saxReader.read(resource);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        Element rootElement = document.getRootElement();
        iterable = rootElement.elementIterator();
    }

    public boolean hasNext() {
        return this.iterable.hasNext();
    }

    public Object next() {
        return this.iterable.next();
    }

}
