package tweb.rpgpc.rpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Party {
    private String name;
    private String shortname;
    private ArrayList<Integer> characters = new ArrayList<>();

    public Party(String name, String shortname, ArrayList<Integer> characters) {
        this.name = name;
        this.shortname = shortname;
        this.characters.addAll(characters);
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
}
