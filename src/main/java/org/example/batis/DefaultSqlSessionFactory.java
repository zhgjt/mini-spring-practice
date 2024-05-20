package org.example.batis;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.beans.factory.annotation.Autowired;
import org.example.jdbc.core.JdbcTemplate;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultSqlSessionFactory implements SqlSessionFactory {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Map<String, MapperNode> mapperNodeMap = new HashMap<>();
    //mapper目录的路径
    private String mapperLocations;

    public DefaultSqlSessionFactory() {
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public Map<String, MapperNode> getMapperNodeMap() {
        return mapperNodeMap;
    }

    @Override
    public SqlSession openSession() {
        SqlSession newSqlSession = new DefaultSqlSession();
        newSqlSession.setJdbcTemplate(jdbcTemplate);
        newSqlSession.setSqlSessionFactory(this);

        return newSqlSession;
    }

    @Override
    public MapperNode getMapperNode(String name) {
        return this.mapperNodeMap.get(name);
    }

    public void init() {
        scanLocation(this.mapperLocations);
    }

    private void scanLocation(String location) {
        //获取mapper目录的路径
        String sLocationPath = this.getClass().getClassLoader().getResource(location).getPath();
        File dir = new File(sLocationPath);
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanLocation(location + "/" + file.getName());
            } else {
                buildMapperNodes(location + "/" + file.getName());
            }
        }
    }

    private Map<String, MapperNode> buildMapperNodes(String filePath) {
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(filePath);
        Document document = null;
        try {
            document = saxReader.read(xmlPath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Element rootElement = document.getRootElement();
        String namespace = rootElement.attributeValue("namespace");

        Iterator<Element> nodes = rootElement.elementIterator();
        while (nodes.hasNext()) {
            Element node = nodes.next();
            String id = node.attributeValue("id");
            String parameterType = node.attributeValue("parameterType");
            String resultType = node.attributeValue("resultType");
            String sql = node.getText();

            MapperNode selectnode = new MapperNode();
            selectnode.setNamespace(namespace);
            selectnode.setId(id);
            selectnode.setParameterType(parameterType);
            selectnode.setResultType(resultType);
            selectnode.setSql(sql);
            selectnode.setParameter("");
            this.mapperNodeMap.put(namespace + "." + id, selectnode);
        }
        return this.mapperNodeMap;
    }
}
