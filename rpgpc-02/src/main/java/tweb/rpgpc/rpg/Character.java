package tweb.rpgpc.rpg;

public class Character {
    private int id;
    private String name;
    private int level;
    private String rpgclass;
    private String[] description;

    public Character(int id, String name, int level, String rpgclass, String[] description) {
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
}
