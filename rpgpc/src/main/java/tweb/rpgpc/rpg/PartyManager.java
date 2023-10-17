package tweb.rpgpc.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class PartyManager {
    private static PartyManager manager;
    private HashMap<String, Party> parties = new HashMap<>();
    private HashMap<Integer, Character> characters = new HashMap<>();
    private int charIdCounter = 0;

    private PartyManager() {
    }

    public ArrayList<Character> getPartyCharacterList(String party) {
        ArrayList<Character> list = new ArrayList<>();
        Party p = parties.get(party);
        if (p == null) return list;
        int[] ids = p.getCharacterIds();
        for (int id: ids) {
            Character c = characters.get(id);
            if (c != null) list.add(c);
        }
        return list;
    }

    public Character getCharacter(int id) {
        return characters.get(id);
    }

    public Party getParty(String name) {
        return parties.get(name);
    }

    public int createCharacter(String name, int level, String rpgclass, String[] description) {
        Character added = new Character(++manager.charIdCounter, name, level, rpgclass, description);
        characters.put(added.getId(), added);
        return added.getId();
    }

    public boolean addCharacterToParty(String partyname, int charId) {
        if (characters.get(charId) == null) return false;
        Party p = parties.get(partyname);
        if (p == null) return false;
        p.addCharacter(charId);
        return true;
    }

    public static PartyManager getManager() {
        if (manager == null) {
            manager = new PartyManager();


            // Creiamo degli oggetti di default da inserire nel manager

            Party lotr = new Party("LOTR", "lotr");
            manager.parties.put(lotr.getShortname(), lotr);
            String[] aragornDesc = {"Aragorn II, soprannominato Grampasso o Passolungo in italiano (Strider in originale), " +
                    "salito al trono come Elessar Telcontar, è un personaggio di Arda, l'universo " +
                    "immaginario fantasy creato dallo scrittore inglese J. R. R. Tolkien.",
                    "È uno dei protagonisti de Il Signore degli Anelli e compare anche nel Silmarillion",
                    "Essendo l'ultimo discendente diretto di Elendil e Isildur " +
                            "(precisamente il trentanovesimo),era di diritto l'erede al trono di Gondor. " +
                            "Dopo aver passato i primi anni di vita in esilio, combatte per la guerra " +
                            "dell'Anello e alla fine di questa, nell'anno 3021 T.E. viene incoronato " +
                            "re di Gondor e di Arnor."};
            Character aragorn = new Character(++manager.charIdCounter, "Aragorn", 12, "Ranger", aragornDesc);

            String[] frodoDesc = {"Frodo Baggins è un personaggio di Arda, l'universo immaginario fantasy creato " +
                    "dallo scrittore inglese J. R. R. Tolkien. È il protagonista del famoso romanzo Il Signore degli Anelli.",
                    "È uno Hobbit, nato il 22 settembre 2968 della Terza Era (T.E.) da Drogo Baggins e Primula Brandibuck.",
                    "Bilbo Baggins è suo cugino."};
            Character frodo = new Character(++manager.charIdCounter, "Frodo Baggins", 6, "Rogue", frodoDesc);

            String[] gandalfDesc = {"Gandalf è un personaggio di Arda, l'universo immaginario fantasy creato dallo " +
                    "scrittore inglese J. R. R. Tolkien. Compare con un ruolo da protagonista nei romanzi " +
                    "Lo Hobbit e Il Signore degli Anelli, e appare anche ne Il Silmarillion e nei Racconti incompiuti.",
                    "All'interno del corpus dello scrittore, Gandalf si distingue come uno dei membri del Bianco " +
                            "Consiglio, un'assemblea che ha lo scopo di combattere l'Ombra nella Terra di Mezzo, " +
                            "del quale diventa successivamente capo e guida a seguito del tradimento di Saruman.",
                    "Il ruolo di Gandalf ne Lo Hobbit e Il Signore degli Anelli è quello di guida per i protagonisti, " +
                            "rispettivamente Bilbo e Frodo Baggins. Ne Il Signore degli Anelli, oltre a essere uno dei " +
                            "principali baluardi della resistenza contro l'Oscuro Signore Sauron, Gandalf " +
                            "diviene anche il capo della Compagnia dell'Anello."};
            Character gandalf = new Character(++manager.charIdCounter, "Gandalf", 11, "Gray Wizard", gandalfDesc);
            manager.characters.put(gandalf.getId(), gandalf);
            manager.characters.put(aragorn.getId(), aragorn);
            manager.characters.put(frodo.getId(), frodo);

            lotr.addCharacter(gandalf.getId());
            lotr.addCharacter(aragorn.getId());
            lotr.addCharacter(frodo.getId());

        }
        return manager;
    }

}
