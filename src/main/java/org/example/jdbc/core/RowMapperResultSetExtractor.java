package org.example.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {
    private final RowMapper rowMapper;

    public RowMapperResultSetExtractor(RowMapper rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public List extractData(ResultSet rs) throws SQLException {
        List results = new ArrayList<>();
        int rowNum = 0;
        //对结果集，循环调用mapRow进行数据记录映射
        while (rs.next()) {
            results.add(this.rowMapper.mapRow(rs, rowNum++));
        }
        return results;
    }
}
