package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.GiftBarrier;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private Lance lance;
    private FireBall fireBall;
    private List<Barrier> barriers;
    private List<Remain> remains;
    private List<Hex> hexs = new ArrayList<>();
    private static List<Level> levels = new ArrayList<>();
    private List<SpellBox> spellBoxes;
    private int chances;
    private int score;

    private boolean isHexSpellCollected = false;
    private boolean isExtensionSpellCollected = false;
    private List<Character> spellInventory;
    




    /**
     * This class represents level information in the game.
     * It holds the objects that are present in the level.
     *
     * @param lance     The lance object in the level.
     * @param fireBall  The fireball object in the level.
     * @param barriers  The list of barriers present in the level.
     * 
     * @see Lance
     * @see FireBall
     * @see Barrier
     * @see ExplosiveBarrier
     */
    public Level(Lance lance, FireBall fireBall, List<Barrier> barriers, List<Hex> hexs) {
        // Initialize the lance, fireball, and barriers for the level
        this.lance = lance;
        this.fireBall = fireBall;
        this.barriers = barriers;
        this.score = 0;
        this.chances = 3;
        // Initialize the list of remains for ExplosiveBarriers
        this.remains = new ArrayList<>();
        this.spellBoxes = new ArrayList<>();
        this.spellInventory = new ArrayList<>();


        // Iterate through barriers to extract remains from ExplosiveBarriers
        for (Barrier barrier : barriers) {
            // If the barrier is an ExplosiveBarrier, add its remain to the list
            if (barrier instanceof ExplosiveBarrier) {
                ExplosiveBarrier explosiveBarrier = (ExplosiveBarrier) barrier;
                remains.add(explosiveBarrier.getRemain());
            }
            if (barrier instanceof GiftBarrier) {
                GiftBarrier giftBarrier = (GiftBarrier) barrier;
                spellBoxes.add(giftBarrier.getSpellBox());
            }
        }

        // Add this level instance to the levels list
        levels.add(this);
    }

    public Level(Lance lance, FireBall fireBall) {
        this(lance, fireBall, new ArrayList<>(), new ArrayList<>());
    }

    public Level(){
        // Keeping this just just in case.
//        double xPosLance = LanceOfDestiny.getScreenWidth() / 2. + LanceOfDestiny.getScreenWidth() / 20.;
//        double yPosLance = LanceOfDestiny.getScreenHeight() * 8 / 10.;
//        Lance lance = new Lance(xPosLance, yPosLance);
//        FireBall fireBall = new FireBall(0, 0);
//        fireBall.stickToLance(lance);
        this(
            new Lance(LanceOfDestiny.getScreenWidth() / 2.0 + LanceOfDestiny.getScreenWidth() / 20.0,
                LanceOfDestiny.getScreenHeight() * 0.8),
            new FireBall(0, 0));
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
    }

    public List<Remain> getRemains() {
        return remains;
    }

    public void setRemains(List<Remain> remains) {
        this.remains = remains;
    }

    public static List<Level> getLevels() {
        return levels;
    }

    public static void setLevels(List<Level> levels) {
        Level.levels = levels;
    }

    public List<Hex> getHexs() {
        return hexs;
    }

    public void setHexs(List<Hex> hexs) {
        this.hexs = hexs;
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


    public void collectSpell(char spell){
        if (spell == SpellBox.HEX_SPELL) {
            isHexSpellCollected = true;
        } else if (spell == SpellBox.EXTENSION_SPELL) {
            isExtensionSpellCollected = true;
        }
        spellInventory.add(spell);
    }

    public void removeSpell(char spell) {
        if (!inventoryHasSpell(spell)) return;
        spellInventory.remove(spell);
    }

    public boolean inventoryHasSpell(char spell) {
        return spellInventory.contains(spell);
    }


    public void createHex() {
        double xPosition = lance.getActualHitbox().getBounds2D().getCenterX();
        double yPosition = lance.getActualHitbox().getBounds2D().getCenterY();
        double rotationAngle = lance.getRotationAngle();



        double xOffset = Math.cos(Math.toRadians(rotationAngle)) * lance.getLength()/2;
        double yOffset = Math.sin(Math.toRadians(rotationAngle)) * lance.getLength()/2;
 
        double hex1X = xPosition + xOffset;
        double hex1Y = yPosition + yOffset;

        double hex2X = xPosition - xOffset;
        double hex2Y = yPosition - yOffset;

        Hex newHex1 = new Hex(hex1X, hex1Y, rotationAngle);
        Hex newHex2 = new Hex(hex2X, hex2Y, rotationAngle);

        hexs.add(newHex1);
        hexs.add(newHex2);
    }
}

