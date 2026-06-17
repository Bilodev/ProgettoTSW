package DAO;

import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class DataSourceFactory {
    private static final DataSource DATA_SOURCE;

    static {
        DataSource dataSource = null;
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/dividdi");
        } catch (NamingException ignored) {
            // Fallback to environment variables when JNDI is not configured
        }

        if (dataSource == null) {
            try {
                dataSource = new ConnectionPool("jdbc:mysql://localhost:3306/dividdi", "anto", "anto");
            } catch (SQLException e) {
                throw new ExceptionInInitializerError("Unable to create connection pool: " + e.getMessage());
            }
        }

        DATA_SOURCE = dataSource;
    }

    private DataSourceFactory() {
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }
}
