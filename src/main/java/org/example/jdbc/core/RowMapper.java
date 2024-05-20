package org.example.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 把jdbc返回的ResultSet里的某一行数据映射成一个对象
 *
 * @param <T>
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
