package org.example.service;

import org.apache.catalina.realm.UserDatabaseRealm;
import org.example.batis.SqlSession;
import org.example.batis.SqlSessionFactory;
import org.example.beans.factory.annotation.Autowired;
import org.example.entity.User;
import org.example.jdbc.core.JdbcTemplate;
import org.example.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public User getUserInfo1(int userid) {
        final String sql = "select id, name,birthday from users where id=?";
        return (User) jdbcTemplate.query(sql, new Object[]{new Integer(userid)},
                (pstmt) -> {
                    ResultSet rs = pstmt.executeQuery();
                    User rtnUser = null;
                    if (rs.next()) {
                        rtnUser = new User();
                        rtnUser.setId(userid);
                        rtnUser.setName(rs.getString("name"));
                    }
                    return rtnUser;
                }
        );
    }

    public List<User> getUsers(int userid) {
        final String sql = "select id, name,birthday from users where id>?";
        return (List<User>) jdbcTemplate.query(sql, new Object[]{new Integer(userid)}, new RowMapper<User>() {
            public User mapRow(ResultSet rs, int i) throws SQLException {
                User rtnUser = new User();
                rtnUser.setId(rs.getInt("id"));
                rtnUser.setName(rs.getString("name"));
                rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
                return rtnUser;
            }
        });
    }

    public User getUserInfo(int userid) {
        String sqlid = "org.example.entity.User.getUserInfo";
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return (User) sqlSession.selectOne(sqlid, new Object[]{new Integer(userid)}, (pstmt) -> {
            ResultSet rs = pstmt.executeQuery();
            User rtnUser = null;
            if (rs.next()) {
                rtnUser = new User();
                rtnUser.setId(userid);
                rtnUser.setName(rs.getString("name"));
                rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
            } else {
            }
            return rtnUser;
        });
    }

}
