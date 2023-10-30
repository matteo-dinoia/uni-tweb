package tweb.rpgpc.rpgdb;

import tweb.rpgpc.db.PoolingPersistenceManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class PartyManagerDB {
    private static PartyManagerDB manager;
    private final PoolingPersistenceManager persistence;

    private PartyManagerDB() {
        persistence = PoolingPersistenceManager.getPersistenceManager();
    }

    public ArrayList<CharacterDB> getPartyCharacterList(String party) {
        ArrayList<CharacterDB> list = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            list = CharacterDB.loadCharactersInParty(party, conn);
        } catch (SQLException ex) {}
        return list;
    }

    public ArrayList<CharacterDB> getPotentialPartyCharacters(String partyName) {
        ArrayList<CharacterDB> list = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            list = CharacterDB.loadCharactersNotInParty(partyName, conn);
        } catch (SQLException ex) {}
        return list;
    }

    public CharacterDB getCharacter(int id) {
        CharacterDB ret = null;
        try (Connection conn = persistence.getConnection()) {
            ret = CharacterDB.loadCharacter(id, conn);
        } catch (SQLException ex) {
        }
        return ret;
    }

    public PartyDB getParty(String name) {
        PartyDB p = null;
        try (Connection conn = persistence.getConnection()) {
            p = PartyDB.loadParty(name, conn);
        } catch (SQLException ex) {
        }
        return p;
    }

    public ArrayList<PartyDB> getParties() {
        ArrayList<PartyDB> list = null;
        try (Connection conn = persistence.getConnection()) {
            list = PartyDB.loadAllParties(conn);
        } catch (SQLException ex) {
        }
        return list;
    }

    public int createCharacter(String name, int level, String rpgclass, String[] description) {
        int ret = -1;
        CharacterDB c = new CharacterDB(-1, name, level, rpgclass, description);
        try (Connection conn = persistence.getConnection()) {
             CharacterDB.saveCharacter(c, conn);
             ret = c.getId();
        } catch (SQLException ex) {
        }
        return ret;
    }

    public boolean addCharacterToParty(String partyname, int charId) {
        boolean added = false;
        try (Connection conn = persistence.getConnection()) {
             added = PartyDB.saveMemberInParty(partyname, charId, conn);
        } catch (SQLException ex) {
        }
        return added;
    }

    public boolean createParty(String shortname, String name) {
        boolean ret = false;
        PartyDB p = new PartyDB(name, shortname);
        try (Connection conn = persistence.getConnection()) {
            ret = PartyDB.saveParty(p, conn);
        } catch (SQLException ex) {
        }
        return ret;
    }

    public static PartyManagerDB getManager() {
        if (manager == null) {
            manager = new PartyManagerDB();
        }
        return manager;
    }
}
