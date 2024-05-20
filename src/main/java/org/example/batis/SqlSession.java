package org.example.batis;

import org.example.jdbc.core.JdbcTemplate;
import org.example.jdbc.core.PreparedStatementCallback;

public interface SqlSession {
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);

    Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback);
}
