package org.example.batis;

public interface SqlSessionFactory {
    SqlSession openSession();
    MapperNode getMapperNode(String name);
}
