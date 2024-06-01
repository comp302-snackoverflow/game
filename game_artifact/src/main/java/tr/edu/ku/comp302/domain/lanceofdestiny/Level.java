package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.listeners.SaveListener;
import tr.edu.ku.comp302.domain.services.save.SaveService;
import tr.edu.ku.comp302.domain.entity.barrier.GiftBarrier;

import java.util.ArrayList;
import java.util.List;

public class Level implements SaveListener {
    private static List<Level> levels = new ArrayList<>();
    private static final SaveService saveService = SaveService.getInstance();
    private Lance lance;
    private FireBall fireBall;
    private List<Barrier> barriers;
    private List<Remain> remains;
    private List<Hex> hexes;
    private List<SpellBox> spellBoxes;
    private List<Character> spellInventory;
    private int chances;
    private int score;
    private long nextTimeMs;
    private int secondsPassed;

    public Level(Lance lance, FireBall fireBall, List<Barrier> barriers, List<Remain> remains, List<SpellBox> spellBoxes, List<Hex> hexes, List<Character> spellInventory, int score, int secondsPassed, long nextTimeMs, int chances) {
        this.lance = lance;
        this.fireBall = fireBall;
        this.barriers = barriers;
        this.remains = remains;
        this.hexes = hexes;
        this.spellBoxes = spellBoxes;
        this.spellInventory = spellInventory;
        this.nextTimeMs = nextTimeMs;
        this.secondsPassed = secondsPassed;
        this.score = score;
        this.chances = chances;

        

        levels.add(this);
    }

    public Level(Lance lance, FireBall fireBall, List<Barrier> barriers) {
        this(lance, fireBall, barriers, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, 3);

        for (Barrier barrier : barriers) {
            if (barrier instanceof ExplosiveBarrier b) {
                remains.add(b.getRemain());
            } else if (barrier instanceof GiftBarrier b) {
                spellBoxes.add(b.getSpellBox());
            }
        }

        

        levels.add(this);
    }

    public Level(Lance lance, FireBall fireBall) {
        this(lance, fireBall, new ArrayList<>());
    }

    public Level() {
        // Keeping this just just in case.
//        double xPosLance = LanceOfDestiny.getScreenWidth() / 2. + LanceOfDestiny.getScreenWidth() / 20.;
//        double yPosLance = LanceOfDestiny.getScreenHeight() * 8 / 10.;
//        Lance lance = new Lance(xPosLance, yPosLance);
//        FireBall fireBall = new FireBall(0, 0);
//        fireBall.stickToLance(lance);
        this(new Lance(LanceOfDestiny.getScreenWidth() / 2.0 - LanceOfDestiny.getScreenWidth() / 20.0, LanceOfDestiny.getScreenHeight() * 0.8), new FireBall(0, 0));
    }

    public static List<Level> getLevels() {
        return levels;
    }

    public static void setLevels(List<Level> levels) {
        Level.levels = levels;
    }

    public Lance getLance() {
        return lance;
    }

    public void setLance(Lance lance) {
        this.lance = lance;
    }

    public FireBall getFireBall() {
        return fireBall;
    }

    public void setFireBall(FireBall fireBall) {
        this.fireBall = fireBall;
    }

    public List<Barrier> getBarriers() {
        return barriers;
    }

    public void setBarriers(List<Barrier> barriers) {
        this.barriers = barriers;
        remains.clear();
        spellBoxes.clear();
        for (Barrier barrier : barriers) {
            if (barrier instanceof ExplosiveBarrier b) {
                remains.add(b.getRemain());
            } else if (barrier instanceof GiftBarrier b) {
                spellBoxes.add(b.getSpellBox());
            }
        }
    }

    public List<Remain> getRemains() {
        return remains;
    }

    public void setRemains(List<Remain> remains) {
        this.remains = remains;
    }

    @Override
    public boolean save() {
        return saveService.saveGame(fireBall, lance, barriers, remains, score);
    }

    public List<Hex> getHexes() {
        return hexes;
    }

    public void setHexes(List<Hex> hexes) {
        this.hexes = hexes;
    }

    public List<SpellBox> getSpellBoxes() {
        // TODO Auto-generated method stub
        return this.spellBoxes;
    }

    public void decreaseChances() {
        this.chances--;
    }
    public void increaseChances() {this.chances++;}

    public int getChances() {
        return chances;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public long getNextTimeMs() {
        return nextTimeMs;
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }

    public void updateTimeInSeconds(long currentTimeInMillis) {
        if (currentTimeInMillis >= nextTimeMs) {
            nextTimeMs = currentTimeInMillis + 1000;
            secondsPassed++;
        }
    }

    public void collectSpell(char spell){
        spellInventory.add(spell);
    }

    public void removeSpell(char spell) {
        if (!inventoryHasSpell(spell)) return;
        spellInventory.remove((Character)spell);
    }

    public boolean inventoryHasSpell(char spell) {
        return spellInventory.contains(spell);
    }


    public void createHex() {
        double xPosition = lance.getBoundingBox().getCenterX();
        double yPosition = lance.getBoundingBox().getCenterY();
        double rotationAngle = lance.getRotationAngle();

        double xOffset = Math.cos(Math.toRadians(rotationAngle)) * lance.getLength()/2;
        double yOffset = Math.sin(Math.toRadians(rotationAngle)) * lance.getLength()/2;

        double hex1X = xPosition + xOffset;
        double hex1Y = yPosition + yOffset;

        double hex2X = xPosition - xOffset;
        double hex2Y = yPosition - yOffset;

        Hex newHex1 = new Hex(hex1X, hex1Y, rotationAngle);
        Hex newHex2 = new Hex(hex2X, hex2Y, rotationAngle);

        hexes.add(newHex1);
        hexes.add(newHex2);
    }
}

