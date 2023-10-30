package data;

import db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CharacterDB {
    public ArrayList<Character> getCharactersList() throws SQLException {
        var res = new ArrayList<Character>();

        try(Connection conn = PoolingPersistenceManager.getPersistenceManager().getConnection()){
            try (PreparedStatement st = conn.prepareStatement("SELECT * FROM rpgpc.characters ")) {
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    String[] desc = rs.getString("description").split("\n");
                    res.add(new Character(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("rpclass"),
                            rs.getInt("level")));
                }
            }
        }

        return res;
    }
}
