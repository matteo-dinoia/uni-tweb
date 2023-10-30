package tweb.persistence;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Arrays;
import java.util.Enumeration;

public class PoolingPersistenceManager implements PersistenceManager {

    private static PoolingPersistenceManager instance;

    public static PersistenceManager getPersistenceManager() {
        if (instance == null) {
            instance = new PoolingPersistenceManager();
        }
        return instance;
    }

    public static void cleanup() {
        if (instance == null) return;
        try {
            Enumeration<Driver> d = DriverManager.getDrivers();
            while (d.hasMoreElements()) DriverManager.deregisterDriver(d.nextElement());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        instance.dataSource.close();
    }

    private HikariDataSource dataSource;

    private PoolingPersistenceManager() {
        try {
            Class<?> driver = Class.forName("org.postgresql.Driver");
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres?currentSchema=rpgpc");
            config.setUsername("jakarta");
            config.setPassword("jakarta");
            config.addDataSourceProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
            config.addDataSourceProperty("maximumPoolSize", "25");

            dataSource = new HikariDataSource(config);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public String test() {
        StringBuilder builder = new StringBuilder();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM party");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String sn = rs.getString("shortname");
                String name = rs.getString("name");
                builder.append("<p><strong>%s</strong> ha shortname <em>%s</em></p>\n"
                        .formatted(name, sn));
            }
            return builder.toString();
        } catch (SQLException ex) {
            builder.append("ERROR");
            for (StackTraceElement s : ex.getStackTrace()) {
                builder.append(s.toString()).append("<br>");
            }
        }

        return builder.toString();
    }
}
