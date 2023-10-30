package tweb.rpgpc.rpgdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PartyDB {
    private String name;
    private String shortname;
    private ArrayList<Integer> characters = new ArrayList<>();

    public PartyDB(String name, String shortname) {
        this.name = name;
        this.shortname = shortname;
    }

    public static boolean saveMemberInParty(String partyname, int charId, Connection conn)
            throws SQLException {
        boolean saved = false;
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO " +
                "partymembers (party_name, character_id) " +
                "VALUES (?, ?) " +
                "on conflict (party_name, character_id) do nothing")) {
            st.setString(1, partyname);
            st.setInt(2, charId);
            int a = st.executeUpdate();
            if (a > 0) saved = true;
        }
        return saved;
    }

    public static boolean saveParty(PartyDB p, Connection conn)
            throws SQLException {
        boolean ret = false;
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO party (name, shortname, owner) " +
                "VALUES (?, ?, ?)")) {
            st.setString(1, p.name);
            st.setString(2, p.shortname);
            st.setString(3, "spock"); // temporaneo! deve prendere l'utente corrente
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                ret = true;
            }
        }
        return ret;
    }

    public int[] getCharacterIds() {
        int[] ret = new int[characters.size()];
        for (int i = 0; i < characters.size(); i++) {
            ret[i] = characters.get(i);
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    public String getShortname() {
        return shortname;
    }

    public boolean hasCharacter(int id) {
        return characters.stream().anyMatch(val -> val == id);
    }

    public static PartyDB loadParty(String shortname, Connection conn)
            throws SQLException {
        PartyDB p = null;
        try (PreparedStatement stParty = conn.prepareStatement("SELECT * FROM rpgpc.party " +
                "WHERE party.shortname = ?")) {
            stParty.setString(1, shortname);
            ResultSet rsParty = stParty.executeQuery();
            if (rsParty.next()) {
                p = new PartyDB(rsParty.getString("name"),
                        rsParty.getString("shortname"));
                try (PreparedStatement stMembers = conn.prepareStatement("SELECT * FROM rpgpc.partymembers " +
                        "WHERE party_name = ?")) {
                    stMembers.setString(1, shortname);
                    ResultSet rsMembers = stMembers.executeQuery();
                    while (rsMembers.next()) {
                        p.characters.add(rsMembers.getInt("character_id"));
                    }
                }

            }
        }
        return p;
    }

    public static ArrayList<PartyDB> loadAllParties(Connection conn)
            throws SQLException {
        ArrayList<PartyDB> ret = new ArrayList<>();
        try (PreparedStatement stParties = conn.prepareStatement("SELECT * FROM rpgpc.party")) {
            ResultSet rsParties = stParties.executeQuery();
            while (rsParties.next()) {
                PartyDB p = new PartyDB(rsParties.getString("name"),
                        rsParties.getString("shortname"));
                try (PreparedStatement stMembers = conn.prepareStatement("SELECT * FROM rpgpc.partymembers " +
                        "WHERE party_name = ?")) {
                    stMembers.setString(1, p.shortname);
                    ResultSet rsMembers = stMembers.executeQuery();
                    while (rsMembers.next()) {
                        p.characters.add(rsMembers.getInt("character_id"));
                    }
                }
            }
        }
        return ret;
    }
}
