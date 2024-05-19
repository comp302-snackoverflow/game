package tr.edu.ku.comp302.domain.entity;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpellBox extends Entity {
    private double speed = 1.5;     // TODO: Change speed
    private int size = 50;
    private boolean isDropped = false;
    //TODO Add spell attribute 
    private char spell = 0;

    private static int[] spellCounts = new int[4];

    // List to track assigned spells
    private static List<Character> unassignedSpells = new ArrayList<>();

    static {
        for (int i = 0; i < spellCounts.length; i++) {
            spellCounts[i] = 0;
            unassignedSpells.add((char) i);
        }
    }

    public final static char EXTENSION_SPELL = 0;
    public final static char OVERWHELMING_SPELL = 1;
    public final static char HEX_SPELL = 2;
    public final static char FELIX_FELICIS_SPELL = 3;

    public SpellBox(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        if (!unassignedSpells.isEmpty()) {
            spell = unassignedSpells.remove(0);
        } else {
            spell = (char) ((new Random()).nextInt(4));
        }
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void move() {
        yPosition += speed;
        boundingBox.setRect(xPosition, yPosition, size, size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isDropped() {
        return isDropped;
    }

    public void drop() {
        isDropped = true;
    }

    public char getSpell() {
        return spell;
    }

    public static int[] getSpellCounts() {
        return spellCounts;
    }

    public static void resetSpellCounts() {
        for (int i = 0; i < spellCounts.length; i++) {
            spellCounts[i] = 0;
        }
    }

    public static void incrementSpellCount(char spell) {
        spellCounts[spell]++;
    }

    public static boolean decrementSpellCount(char spell) {
        if (spellCounts[spell] > 0) {
            spellCounts[spell]--;
            return true;
        } else {
            return false;
        }
    }
}

