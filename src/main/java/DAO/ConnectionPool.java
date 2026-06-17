package DAO;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class ConnectionPool implements DataSource {
    private static final int MAX_POOL_SIZE = 10;
    private final BlockingQueue<Connection> pool;
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public ConnectionPool(String jdbcUrl, String username, String password) throws SQLException {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.pool = new ArrayBlockingQueue<>(MAX_POOL_SIZE);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
        for (int i = 0; i < 3; i++) {
            pool.offer(createRawConnection());
        }
    }

    private Connection createRawConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    private Connection createPooledConnection(Connection rawConnection) {
        InvocationHandler handler = (proxy, method, args) -> {
            if ("close".equals(method.getName())) {
                releaseConnection(rawConnection);
                return null;
            }
            try {
                return method.invoke(rawConnection, args);
            } catch (Exception e) {
                throw new SQLException(e);
            }
        };
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                handler);
    }

    private void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        try {
            if (connection.isClosed()) {
                return;
            }
            if (!pool.offer(connection)) {
                connection.close();
            }
        } catch (SQLException e) {
            // ignore
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection rawConnection = pool.poll();
        if (rawConnection == null) {
            rawConnection = createRawConnection();
        }
        return createPooledConnection(rawConnection);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        // not supported
    }

    @Override
    public void setLoginTimeout(int seconds) {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isInstance(this);
    }
}
