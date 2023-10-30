package tweb.persistence;

import java.sql.*;
import java.util.Arrays;
import java.util.Enumeration;

public class BasicPersistenceManager implements PersistenceManager {

    private static BasicPersistenceManager instance;

    public static PersistenceManager getPersistenceManager() {
        if (instance == null) {
            instance = new BasicPersistenceManager();
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
    }

    private final String url;
    private final String username;
    private final String password;


    private BasicPersistenceManager() {
        url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=rpgpc";
        username = "jakarta";
        password = "jakarta";
    }

    @Override
    public String test() {
        StringBuilder builder = new StringBuilder();
        try {
            Class<?> driver = Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = conn.prepareStatement("SELECT * FROM party");
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String sn = rs.getString("shortname");
                    String name = rs.getString("name");
                    builder.append("<p><strong>%s</strong> ha shortname <em>%s</em></p>\n"
                            .formatted(name, sn));
                }
            } catch (SQLException ex) {
                builder.append("ERROR");
                for (StackTraceElement s: ex.getStackTrace()) {
                    builder.append(s.toString()).append("<br>");
                }
            }
        } catch (ClassNotFoundException ex) {
            builder.append("ERROR");
            for (StackTraceElement s: ex.getStackTrace()) {
                builder.append(s.toString()).append("<br>");
            }}

        return builder.toString();
    }
}
