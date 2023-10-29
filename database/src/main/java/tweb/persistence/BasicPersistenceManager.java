package tweb.persistence;

import java.sql.*;

public class BasicPersistenceManager implements PersistenceManager {
    private  String url;
    private  String username;
    private  String password;

    private static BasicPersistenceManager instance;
    public static PersistenceManager getPersistenceManager() {
        if (instance == null) {
            instance = new BasicPersistenceManager();
            instance.url = "jdbc:postgresql://localhost:5432/postgres?currentSchema=rpgpc";
            instance.username = "jakarta";
            instance.password = "jakarta";
        }
        return instance;
    }


    @Override
    public String test() {
        StringBuilder builder = new StringBuilder();
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                try(PreparedStatement ps = conn.prepareStatement("DELETE FROM rpgpc.characters WHERE id=?")){
                    int count = 0;
                    for(int i = 11; i < 13; i++) {
                        ps.setInt(1, i);
                        count += ps.executeUpdate();
                    }

                    return "Deleted:" + count;
                }
            } catch (SQLException ex) {
                builder.append("ERROR SQLException<p>%s</p><p>".formatted(ex.getMessage()));
                for (StackTraceElement s: ex.getStackTrace()) {
                    builder.append("%s<br>".formatted(s));
                }
                builder.append("</p>");
            }
        } catch (ClassNotFoundException ex) {
            builder.append("ERROR ClassNotFoundException<p>%s</p><p>".formatted(ex.getMessage()));
            for (StackTraceElement s: ex.getStackTrace()) {
                builder.append("%s<br>".formatted(s));
            }
            builder.append("</p>");
        }
        return builder.toString();
    }
}
