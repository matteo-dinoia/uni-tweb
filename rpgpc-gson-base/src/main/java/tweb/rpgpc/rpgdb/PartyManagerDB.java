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

    /* public ArrayList<CharacterDB> getPartyCharacterList(String party) {
        ArrayList<CharacterDB> list = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            list = CharacterDB.loadCharactersInParty(party, conn);
        } catch (SQLException ex) {}
        return list;
    } */

    /* public ArrayList<CharacterDB> getPotentialPartyCharacters(String partyName) {
        ArrayList<CharacterDB> list = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            list = CharacterDB.loadCharactersNotInParty(partyName, conn);
        } catch (SQLException ex) {}
        return list;
    } */


    public ArrayList<CharacterDB> getAllCharacters() {
        ArrayList<CharacterDB> ret = new ArrayList<>();
        try (Connection conn = persistence.getConnection()) {
            ret = CharacterDB.loadAllCharacters(conn);
        } catch (SQLException ex) {

        }
        return ret;

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
        if(name == null) return null;
        PartyDB p = null;
        try (Connection conn = persistence.getConnection()) {
            p = PartyDB.loadParty(name, conn);
        } catch (SQLException ex) {
        }
        return p;
    }

    public ArrayList<PartyDB> getAllParties() {
        ArrayList<PartyDB> list = null;
        try (Connection conn = persistence.getConnection()) {
            list = PartyDB.loadAllParties(conn);
        } catch (SQLException ex) {}

        return list;
    }

    /* public ArrayList<PartyDB> getParties() {
        ArrayList<PartyDB> list = null;
        try (Connection conn = persistence.getConnection()) {
            list = PartyDB.loadAllParties(conn);
        } catch (SQLException ex) {
        }
        return list;
    } */

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

    public boolean deleteCharacter(int charId) {
        boolean deleted = false;
        try (Connection conn = persistence.getConnection()) {
            CharacterDB c = CharacterDB.loadCharacter(charId, conn);
            if (c != null && c.canDelete(conn)) {
                deleted = c.delete(conn);
            }
        } catch (SQLException ex) {
        }
        return deleted;
    }


    public boolean updateCharacter(CharacterDB c) {
        boolean updated = false;
        try (Connection conn = persistence.getConnection()) {
                updated = c.saveUpdate(conn);
        } catch (SQLException ex) {
        }
        return updated;
    }

    /* public boolean addCharacterToParty(String partyname, int charId) {
        boolean added = false;
        try (Connection conn = persistence.getConnection()) {
             added = PartyDB.saveMemberInParty(partyname, charId, conn);
        } catch (SQLException ex) {
        }
        return added;
    } */

    /* public boolean createParty(String shortname, String name) {
        boolean ret = false;
        PartyDB p = new PartyDB(name, shortname);
        try (Connection conn = persistence.getConnection()) {
            ret = PartyDB.saveParty(p, conn);
        } catch (SQLException ex) {
        }
        return ret;
    } */

    public static PartyManagerDB getManager() {
        if (manager == null) {
            manager = new PartyManagerDB();
        }
        return manager;
    }

    public int addNewCharacter(CharacterDB c) {
        int ret = 0;
        try (Connection conn = persistence.getConnection()) {
            ret = c.saveAsNew(conn);
        } catch (SQLException ex) {
        }
        return ret;
    }

    public String addNewParty(PartyDB p) {
        String ret = "";
        try (Connection conn = persistence.getConnection()) {
            ret = p.saveAsNew(conn);
        } catch (SQLException ex) {}

        return ret;
    }

    public boolean deleteParty(String shortName) {
        boolean deleted = false;
        try (Connection conn = persistence.getConnection()) {
            PartyDB p = PartyDB.loadParty(shortName, conn);
            if (p == null)
                return false;
            try{
                conn.setAutoCommit(false);
                ArrayList<CharacterDB> characters = CharacterDB.loadCharactersInParty(shortName, conn);
                for(int i = 0; i < characters.size(); i++){
                    p.removeFromParty(characters.get(i).getId(),conn);
                }
                deleted = p.delete(conn);
                conn.commit();
            }catch (SQLException ex) {
                try{
                    conn.rollback();
                }catch (SQLException ex2){
                    ex2.printStackTrace();
                }
            }
        }catch (SQLException ex) {}

        return deleted;
    }

    public boolean changePartyName(PartyDB p, String newName){
        if (p == null) return false;
        boolean res = false;

        try (Connection conn = persistence.getConnection()) {
                res = p.changeFullName(newName, conn);
        }catch (SQLException ex) {}

        return res;
    }

    public boolean removePartyMember(PartyDB p, int id){
        if (p == null) return false;
        boolean res = false;

        try (Connection conn = persistence.getConnection()) {
            res = p.removeFromParty(id, conn);
        }catch (SQLException ex) {}

        return res;
    }

    public boolean addPartyMember(PartyDB p, int id){
        if (p == null) return false;
        boolean res = false;

        try (Connection conn = persistence.getConnection()) {
            res = p.addToParty(id, conn);
        }catch (SQLException ex) {}

        return res;
    }
}
