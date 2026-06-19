package control;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public final class DataSourceFactory {
    private static DataSource DATA_SOURCE;

    static {
        try {
            // Effettua il JNDI lookup per ottenere il DataSource configurato in Tomcat
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DATA_SOURCE = (DataSource) envCtx.lookup("jdbc/dividdi");
        } catch (NamingException e) {
            System.err.println("Errore critico nel Lookup del DataSource: " + e.getMessage());
        }
    }

    private DataSourceFactory() {
        // Costruttore privato per evitare l'istanziazione
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }
}