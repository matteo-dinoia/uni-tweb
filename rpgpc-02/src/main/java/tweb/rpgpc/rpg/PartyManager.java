package tweb.rpgpc.rpg;

import java.util.ArrayList;
import java.util.HashMap;

public class PartyManager {
    private static PartyManager manager;
    private final HashMap<String, Party> parties = new HashMap<>();
    private final HashMap<Integer, Character> characters = new HashMap<>();
    private int charIdCounter = 0;

    private PartyManager() {
    }

    public ArrayList<Party> getPartiesList(){
        return new ArrayList<>(parties.values());
    }

    public boolean addParty(String shortName, String name){
        if(name == null || shortName == null || name.isEmpty() || shortName.isEmpty() || getParty(name) != null)
            return false;

        for(int i = 0; i < shortName.length(); i++){
            final char c = shortName.charAt(i);
            if((c < 'a' || c > 'z') && c != '-' && c != '_')
                return false;
        }

        this.recordParty(new Party(name, shortName));
        return true;
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

    public ArrayList<Character> getCharacterNotInPartyList() {
        ArrayList<Character> list = new ArrayList<>(characters.values());

        for (Party p: parties.values()) {
            for (int id: p.getCharacterIds()) {
                Character c = characters.get(id);
                if (c != null) list.remove(c);
            }
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
        Character added = new Character(this.getCharacterId(), name, level, rpgclass, description);
        this.recordCharacter(added);
        return added.getId();
    }

    public boolean addCharacterToParty(String partyname, int charId) {
        if (characters.get(charId) == null) return false;
        Party p = parties.get(partyname);
        if (p == null) return false;
        p.addCharacter(charId);
        return true;
    }

    private synchronized int getCharacterId() {
        return ++manager.charIdCounter;
    }

    private synchronized void recordCharacter(Character c) {
        this.characters.put(c.getId(), c);
    }

    private synchronized void recordParty(Party p) {
        this.parties.put(p.getShortname(), p);
    }
    public static PartyManager getManager() {
        if (manager == null) {
            manager = new PartyManager();


            // Creiamo degli oggetti di default da inserire nel manager

            Party lotr = new Party("LOTR", "lotr");
            manager.recordParty(lotr);
            String[] aragornDesc = {"Aragorn II, soprannominato Grampasso o Passolungo in italiano (Strider in originale), " +
                    "salito al trono come Elessar Telcontar, è un personaggio di Arda, l'universo " +
                    "immaginario fantasy creato dallo scrittore inglese J. R. R. Tolkien.",
                    "È uno dei protagonisti de Il Signore degli Anelli e compare anche nel Silmarillion",
                    "Essendo l'ultimo discendente diretto di Elendil e Isildur " +
                            "(precisamente il trentanovesimo),era di diritto l'erede al trono di Gondor. " +
                            "Dopo aver passato i primi anni di vita in esilio, combatte per la guerra " +
                            "dell'Anello e alla fine di questa, nell'anno 3021 T.E. viene incoronato " +
                            "re di Gondor e di Arnor."};
            Character aragorn = new Character(manager.getCharacterId(), "Aragorn", 12, "Ranger", aragornDesc);

            String[] frodoDesc = {"Frodo Baggins è un personaggio di Arda, l'universo immaginario fantasy creato " +
                    "dallo scrittore inglese J. R. R. Tolkien. È il protagonista del famoso romanzo Il Signore degli Anelli.",
                    "È uno Hobbit, nato il 22 settembre 2968 della Terza Era (T.E.) da Drogo Baggins e Primula Brandibuck.",
                    "Bilbo Baggins è suo cugino."};
            Character frodo = new Character(manager.getCharacterId(), "Frodo Baggins", 6, "Rogue", frodoDesc);

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
            Character gandalf = new Character(manager.getCharacterId(), "Gandalf", 11, "Gray Wizard", gandalfDesc);
            manager.recordCharacter(gandalf);
            manager.recordCharacter(aragorn);
            manager.recordCharacter(frodo);

            lotr.addCharacter(gandalf.getId());
            lotr.addCharacter(aragorn.getId());
            lotr.addCharacter(frodo.getId());

            Party by = new Party("Baba Yaga & Friends", "babayaga");
            manager.recordParty(by);


            String[] descBY = {"Baba Yaga è una creatura leggendaria della mitologia slava."};
            by.addCharacter(manager.createCharacter("Baba Yaga", 102, "Witch", descBY));

            String[] descFB = {"L'uccello di fuoco è una creatura del folklore analoga alla fenice."};
            by.addCharacter(manager.createCharacter("Firebird", 18, "Creature", descFB));

            String[] descIT = {"Ivan Tsarevich (o Zarevich) è uno dei principali eroi del folklore russo."};
            by.addCharacter(manager.createCharacter("Ivan Tsarevich", 12, "Fighter", descIT));

            String[] descVB = {"Vassilissa la Bella è la protagonista di una fiaba popolare slava."};
            by.addCharacter(manager.createCharacter("Vassilissa the Beautiful", 10, "Princess", descVB));

            Party quid = new Party("Quidditch Forever", "quidditch");
            manager.recordParty(quid);

            String[] descAD = {"Albus Dumbledore (o Silente in italiano) è il preside della scuola di magia e stregoneria di Hogwarts."};
            quid.addCharacter(manager.createCharacter("Albus Dumbledore", 99, "Sorcerer", descAD));

            String[] descDM = {"Draco è l'ultimo discendente della nobile casata purosangue dei Malfoy."};
            quid.addCharacter(manager.createCharacter("Draco Malfoy", 3, "Black Wizard", descDM));

            String[] descHP = {"Harry è noto nel mondo magico per aver causato, da neonato, la caduta di Lord Voldemort."};
            quid.addCharacter(manager.createCharacter("Harry Potter", 6, "Chosen One", descHP));

            String[] descHG = {"Hermione è la migliore amica di Harry Potter e Ron Weasley, con cui forma un trio affiatato e indissolubile."};
            quid.addCharacter(manager.createCharacter("Hermione Granger", 7, "Paladin", descHG));

            String[] descRW = {"Ron Weasley, come tutti i suoi parenti non presta attenzione alla purezza di sangue e non discrimina i nati babbani."};
            quid.addCharacter(manager.createCharacter("Ron Weasley", 6, "Cleric", descRW));

            String[] descSB = {"Sirius Black è l'ultimo erede della casata Black, una famiglia di maghi purosangue un tempo famosa."};
            quid.addCharacter(manager.createCharacter("Sirius Black", 25, "Animagus", descSB));

            //EXTRA
            Character extra1 = new Character(manager.getCharacterId(), "Extra1", 122, "Hammerer", new String[]{"Nobody"});
            Character extra2 = new Character(manager.getCharacterId(), "Extra2", 12000, "Ranger", new String[]{"Nobody"});
            manager.recordCharacter(extra1);
            manager.recordCharacter(extra2);
        }
        return manager;
    }

}
