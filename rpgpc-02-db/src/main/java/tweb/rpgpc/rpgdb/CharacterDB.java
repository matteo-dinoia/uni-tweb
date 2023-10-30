package tweb.rpgpc.rpgdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CharacterDB {
    private int id;
    private String name;
    private int level;
    private String rpgclass;
    private String[] description;

    public CharacterDB(int id, String name, int level, String rpgclass, String[] description) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.rpgclass = rpgclass;
        this.description = description;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getRpgclass() {
        return rpgclass;
    }

    public String[] getDescription() {
        return description;
    }

    public static CharacterDB loadCharacter(int id, Connection conn)
            throws SQLException {
        CharacterDB ret = null;
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM rpgpc.characters " +
                "WHERE characters.id = ?")) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String[] desc = rs.getString("description").split("\n");
                ret = new CharacterDB(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getString("rpclass"),
                        desc);
            }
        }
        return ret;
    }

    public static ArrayList<CharacterDB> loadCharactersInParty(String partyname, Connection conn)
            throws SQLException {
        ArrayList<CharacterDB> list = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM rpgpc.partymembers JOIN rpgpc.characters " +
                "ON partymembers.character_id = characters.id " +
                "WHERE partymembers.party_name = ?")) {
            st.setString(1, partyname);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String[] desc = rs.getString("description").split("\n");
                list.add(new CharacterDB(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getString("rpclass"),
                        desc));
            }
        }
        return list;
    }

    public static ArrayList<CharacterDB> loadCharactersNotInParty(String partyname, Connection conn)
            throws SQLException {
        ArrayList<CharacterDB> list = new ArrayList<>();
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM characters " +
                "WHERE id NOT IN (" +
                    "SELECT DISTINCT character_id FROM rpgpc.partymembers WHERE party_name = ?" +
                ")")) {
            st.setString(1, partyname);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String[] desc = rs.getString("description").split("\n");
                list.add(new CharacterDB(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getString("rpclass"),
                        desc));
            }
        }
        return list;
    }


    public static boolean saveCharacter(CharacterDB c, Connection conn) throws SQLException {
        boolean ret = false;
        StringBuilder desc = new StringBuilder();
        for (String s : c.description) desc.append(s).append("\n");
        if (c.id < 0) {
            try (PreparedStatement st = conn.prepareStatement("INSERT INTO characters (name, level, description, rpclass) " +
                    "VALUES (?, ?, ?, ?) RETURNING id")) {
                st.setString(1, c.name);
                st.setInt(2, c.level);
                st.setString(3, desc.toString());
                st.setString(4, c.rpgclass);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    c.id = rs.getInt("id");
                    ret = true;
                }
            }
        } else {
            try (PreparedStatement st = conn.prepareStatement("UPDATE characters SET " +
                    "(name, level, description, rpclass) = (?, ?, ?, ?) WHERE id = ?")) {
                st.setString(2, c.name);
                st.setInt(2, c.level);
                st.setString(3, desc.toString());
                st.setString(4, c.rpgclass);
                st.setInt(5, c.id);
                int n = st.executeUpdate();
                if (n > 0) {
                    ret = true;
                }
            }
        }
        return ret;
    }
}
