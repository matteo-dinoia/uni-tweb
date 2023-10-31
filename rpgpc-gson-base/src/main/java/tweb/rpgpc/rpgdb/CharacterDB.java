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


    public int saveAsNew(Connection conn) throws SQLException {
        int ret = -1;
        StringBuilder desc = new StringBuilder();
        for (String s : description) desc.append(s).append("\n");
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO characters (name, level, description, rpclass) " +
                "VALUES (?, ?, ?, ?) RETURNING id")) {
            st.setString(1, name);
            st.setInt(2, level);
            st.setString(3, desc.toString());
            st.setString(4, rpgclass);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
                ret = id;
            }
        }
        return ret;
    }

    public boolean saveUpdate(Connection conn) throws SQLException {
        boolean ret = false;
        StringBuilder desc = new StringBuilder();
        for (String s : description) desc.append(s).append("\n");
        try (PreparedStatement st = conn.prepareStatement("UPDATE characters SET (name, level, description, rpclass) " +
                "= (?, ?, ?, ?) WHERE id = ?")) {
            st.setString(1, name);
            st.setInt(2, level);
            st.setString(3, desc.toString());
            st.setString(4, rpgclass);
            st.setInt(5, id);
            int upd = st.executeUpdate();
            if (upd > 0) {
                ret = true;
            }
        }
        return ret;
    }

    public boolean canDelete(Connection conn) throws SQLException {
        boolean can = true;
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM partymembers WHERE character_id = ?")) {
            st.setInt(1, id);
            ResultSet res = st.executeQuery();
            if (res.next()) can = false;
        }
        return can;
    }

    public boolean delete(Connection conn) throws SQLException {
        boolean deleted = false;
        try (PreparedStatement st = conn.prepareStatement("DELETE FROM characters WHERE id = ?")) {
            st.setInt(1, id);
            int n = st.executeUpdate();
            if (n > 0) deleted = true;
        }
        return deleted;
    }

    public static CharacterDB loadCharacter(int id, Connection conn)
            throws SQLException {
        CharacterDB ret = null;
        try (PreparedStatement st = conn.prepareStatement("SELECT * FROM characters " +
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


    public static ArrayList<CharacterDB> loadAllCharacters(Connection conn) throws SQLException {
        ArrayList<CharacterDB> ret = new ArrayList<>();
        try (PreparedStatement stChars = conn.prepareStatement("SELECT * FROM characters")) {
            ResultSet rsChars = stChars.executeQuery();
            while (rsChars.next()) {
                String[] desc = rsChars.getString("description").split("\n");
                CharacterDB c = new CharacterDB(rsChars.getInt("id"),
                        rsChars.getString("name"),
                        rsChars.getInt("level"),
                        rsChars.getString("rpclass"),
                        desc);
                ret.add(c);
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
