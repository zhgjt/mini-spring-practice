package org.example.jdbc.pool;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PooledDataSource implements DataSource {
    private List<PooledConnection> connections = null;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private int initialSize = 2;
    private Properties connectionProperties;
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    private synchronized void initPool() {
        this.connections = new CopyOnWriteArrayList<>();
        for (int i = 0; i < initialSize; i++) {
            Connection connect = null;
            try {
                connect = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            PooledConnection pooledConnection = new PooledConnection(connect, false);
            this.connections.add(pooledConnection);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        PooledConnection pooledConnection = getAvailableConnection();
        while (pooledConnection == null) {
            pooledConnection = getAvailableConnection();
            if (pooledConnection == null) {
                try {
                    TimeUnit.MICROSECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return pooledConnection;
    }

    private PooledConnection getAvailableConnection() throws SQLException {
        //如果数据库连接池为null，则初始化
        if (this.connections == null) {
            synchronized (this.lock1) {
                if (this.connections == null) {
                    initPool();
                }
            }
        }

        for (PooledConnection pooledConnection : this.connections) {
            synchronized (pooledConnection) {
                if (!pooledConnection.isActive()) {
                    pooledConnection.setActive(true);
                    return pooledConnection;
                }
            }
        }
        return null;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    //设置driver class name的方法，要加载driver类
    public void setDriverClassName(String dirverClassName) {
        this.driverClassName = dirverClassName;
        try {
            Class.forName(this.driverClassName);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not load JDBC driver class [" + driverClassName + "]", e);
        }
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }
}
