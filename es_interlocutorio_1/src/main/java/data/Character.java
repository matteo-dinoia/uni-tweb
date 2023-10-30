package data;

public class Character {
    int id;
    String name;
    String classe;
    int level;

    public Character(int id, String name, String classe, int level) {
        this.id = id;
        this.name = name;
        this.classe = classe;
        this.level = level;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", classe='" + classe + '\'' +
                ", level=" + level +
                '}';
    }
}
