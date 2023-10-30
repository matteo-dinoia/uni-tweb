package tweb.rpgpc.rpg;

import tweb.rpgpc.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PartyManager {
    private static PartyManager manager;
    private final PoolingPersistenceManager persistence;

    private PartyManager() {
        persistence = PoolingPersistenceManager.getPersistenceManager();
    }

    public ArrayList<Character> getPartyCharacterList(String party) {
        ArrayList<Character> list = new ArrayList<>();
        try (Connection conn = persistence.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM rpgpc.partymembers JOIN rpgpc.characters " +
                     "ON partymembers.character_id = characters.id " +
                     "WHERE partymembers.party_name = ?")) {
            st.setString(1, party);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String[] desc = rs.getString("description").split("\n");
                list.add(new Character(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getString("rpclass"),
                        desc));
            }
        } catch (SQLException ex) {
        }
        return list;
    }

    public Character getCharacter(int id) {
        Character ret = null;
        try (Connection conn = persistence.getConnection();
             PreparedStatement st = conn.prepareStatement("SELECT * FROM rpgpc.characters " +
                     "WHERE characters.id = ?")) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String[] desc = rs.getString("description").split("\n");
                ret = new Character(rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("level"),
                        rs.getString("rpclass"),
                        desc);
            }
        } catch (SQLException ex) {
        }
        return ret;
    }

    public Party getParty(String name) {
        Party p = null;
        try (Connection conn = persistence.getConnection()) {
            try (PreparedStatement st = conn.prepareStatement("SELECT * FROM rpgpc.party " +
                    "WHERE party.shortname = ?")) {
                st.setString(1, name);
                ResultSet rs = st.executeQuery();
                ArrayList<Integer> characters = new ArrayList<>();
                try (PreparedStatement st2 = conn.prepareStatement("SELECT * FROM rpgpc.partymembers " +
                        "WHERE party_name = ?")) {
                    st2.setString(1, name);
                    ResultSet rs2 = st2.executeQuery();
                    while (rs2.next()) {
                        characters.add(rs2.getInt("character_id"));
                    }
                }
                if (rs.next()) {
                    p = new Party(rs.getString("name"),
                            rs.getString("shortname"),
                            characters);

                }
            }
        } catch (SQLException ex) {
        }
        return p;
    }

    public int createCharacter(String name, int level, String rpgclass, String[] description) {
        StringBuilder desc = new StringBuilder();
        int ret = -1;
        for (String s: description) desc.append(s).append("\n");
        try (Connection conn = persistence.getConnection();
             PreparedStatement st = conn.prepareStatement("INSERT INTO characters (name, level, description, rpclass) " +
                     "VALUES (?, ?, ?, ?) RETURNING id")) {
            st.setString(1, name);
            st.setInt(2, level);
            st.setString(3, desc.toString());
            st.setString(4, rpgclass);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("id");
            }
        } catch (SQLException ex) {
        }
        return ret;
    }

    public boolean addCharacterToParty(String partyname, int charId) {
        boolean added = false;
        try (Connection conn = persistence.getConnection();
             PreparedStatement st = conn.prepareStatement("INSERT INTO partymembers (party_name, character_id) " +
                     "VALUES (?, ?)")) {
            st.setString(1, partyname);
            st.setInt(2, charId);
            int a = st.executeUpdate();
            if (a > 0) added = true;
        } catch (SQLException ex) {
        }
        return added;
    }



    public static PartyManager getManager() {
        if (manager == null) {
            manager = new PartyManager();
        }
        return manager;
    }
}
