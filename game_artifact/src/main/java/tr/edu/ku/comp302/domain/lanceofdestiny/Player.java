package tr.edu.ku.comp302.domain.lanceofdestiny;

public class Player {
    private final int ID;
    private final String name;

    public Player(int ID, String name) {
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "Player{" + "ID=" + ID + ", name='" + name + '\'' + '}';
    }
}
