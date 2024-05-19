package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.GiftBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.HollowBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.BuildHandler.BarrierType;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.services.threads.PausableThread;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;
import tr.edu.ku.comp302.ui.view.View;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;


public class LevelHandler {
    private static final View fireBallView = View.of(View.FIREBALL);
    private static final View lanceView = View.of(View.LANCE);
    private BarrierRenderer barrierRenderer = new BarrierRenderer();
    private static final View remainView = View.of(View.REMAIN);
    private static final View hexView = View.of(View.HEX);
    private static final View spellBoxView = View.of(View.SPELL_BOX);
    private final Logger logger = LogManager.getLogger(LevelHandler.class);
    private Level level;
    private ScheduledExecutorService scheduler;
    private SpellHandler spellHandler;
    private List<PausableThread> pausableThreads= new ArrayList<>();
    private LevelPanel levelPanel;

    /**
     * Handling the render of the views of the objects of the level instance.
     * (Use this with your own risk!!!)
     * @param level
     */
    public LevelHandler(Level level) {
        this.level = level;
        spellHandler = new SpellHandler();
    }


    
    public void resizeHexImage() {
    }

    public void resizeSpellBoxImage(){
        if (!getSpellBoxes().isEmpty()) {
            spellBoxView.resizeImage(level.getSpellBoxes().getFirst().getSize(), level.getSpellBoxes().getFirst().getSize());
        }
    }

    public void resizeFireBallImage() {
        FireBall fireBall = getFireBall();
        fireBallView.resizeImage(fireBall.getSize(), fireBall.getSize());
    }

    public void resizeBarrierImages() {
        barrierRenderer.resizeBarrierImages(getBarriers());
    }

    public void resizeRemainImage() {
        if (!getRemains().isEmpty()) {
            remainView.resizeImage(level.getRemains().getFirst().getSize(), level.getRemains().getFirst().getSize());
        }
    }

    public void resizeLanceImage(){
        Lance lance = level.getLance();
        
        lanceView.resizeImage((int) lance.getLength(), (int) lance.getThickness());
    }

    public void renderLance(Graphics g) {
        Lance lance = level.getLance();
        Graphics2D g2d = (Graphics2D) g;
        double rotationAngle = Math.toRadians(lance.getRotationAngle());
        AffineTransform oldTransform = g2d.getTransform();
        g2d.rotate(rotationAngle, lance.getXPosition() + lance.getLength() / 2.0, lance.getYPosition() + lance.getThickness() / 2.0);
        g2d.drawImage(lanceView.getImage(), (int) lance.getXPosition(), (int) lance.getYPosition(), null);
        g2d.setTransform(oldTransform);    // Reset transformation to prevent unintended rotations.

        // uncomment the below two lines to see Lance Hit Box and Lance Bounding Box
        // g2d.drawPolygon(lance.getActualHitbox());
        // ((Graphics2D) g).draw(lance.getLanceBounds());
    }

    public void renderFireBall(Graphics g) {
        FireBall fireBall = level.getFireBall();
        g.drawImage(fireBallView.getImage(), (int) fireBall.getXPosition(), (int) fireBall.getYPosition(), null);
        // uncomment the below line to see FireBall hit box
        // g.drawRect((int) fireBall.getXPosition(), (int) fireBall.getYPosition(), fireBall.getSize(), fireBall.getSize());
    }


    public void renderBarriers(Graphics g) {
        barrierRenderer.renderBarriers(g, getBarriers());
    }

    public void renderSpellBox(Graphics g){
        List<SpellBox> spellBoxes = level.getSpellBoxes();
        
        // Output the number of remains for debugging purposes
        //System.out.println(remains.size());
        
        // Iterate through the remains and render those that are marked as dropped
        for (SpellBox spellBox : spellBoxes.stream().filter(SpellBox::isDropped).toList()) {
            renderSpellBoxView(g, spellBox);
        }
    }

    /**
     * Renders the remains of destroyed ExplosiveBarriers on the game screen.
     * Retrieves the remains from the current level and renders them if they are marked as dropped.
     * 
     * @param g The Graphics object used for rendering.
     * 
     * @see Level
     * @see Remain
     * @see Barrier
     * @see ExplosiveBarrier
     */
    public void renderRemains(Graphics g) {
        // Retrieve the remains from the current level
        List<Remain> remains = level.getRemains();
        
        // Output the number of remains for debugging purposes
        //System.out.println(remains.size());
        
        // Iterate through the remains and render those that are marked as dropped
        for (Remain remain : remains.stream().filter(Remain::isDropped).toList()) {
            renderRemainView(g, remain);
        }
    }


    public void renderHexs(Graphics g) {
        // Retrieve the hexs from the current level
        List<Hex> hexs = level.getHexs();
        
        
        
        if (hexs != null){
            for (Hex hex : hexs) {
                g.drawImage(hexView.getImage(), (int) hex.getXPosition(), (int) hex.getYPosition(), null);
            }
        }
        
    }

    private void renderRemainView(Graphics g, Remain remain) {
        g.drawImage(remainView.getImage(), (int) remain.getXPosition(), (int) remain.getYPosition(), null);
    }

    private void renderSpellBoxView(Graphics g, SpellBox spellBox) {
        g.drawImage(spellBoxView.getImage(), (int) spellBox.getXPosition(), (int) spellBox.getYPosition(), null);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Barrier> getBarriers() {
        return level.getBarriers();
    }

    public List<Remain> getRemains() {
        return level.getRemains();
    }

    public FireBall getFireBall() {
        return level.getFireBall();
    }

    public Lance getLance() {
        return level.getLance();
    }
    public List<SpellBox> getSpellBoxes(){
        return level.getSpellBoxes();
    }


    /**
     * Creates a new hex at a position relative to the lance.
     */
    
    

    public void startCreatingHex() {

        PausableThread pausableThread = new PausableThread(level::createHex, 10000,1000);
        pausableThreads.add(pausableThread);
    }

    public List<Hex> getHexs(){
        return level.getHexs();
    }


    public ArrayList<Barrier> eightRandomBarriers() {
        ArrayList<Barrier> chosen = new ArrayList<>();
        List<Barrier> allBarriers = Collections.synchronizedList((ArrayList) getBarriers());
        int barrierCount = allBarriers.size();
            //less than 8 barriers remain
        if (barrierCount < 8) {
            for(Barrier b : allBarriers){
                b.setFrozen(true);
            }
            return (ArrayList)allBarriers;
        }
        
        Random random = new Random();
        while (chosen.size() < 8) {
            int index = random.nextInt(barrierCount);
            Barrier barrier = allBarriers.get(index);
            //no duplicates
            if (!chosen.contains(barrier)) {  
                chosen.add(barrier);
            }
        }
        for (Barrier b : chosen){
            b.setFrozen(true);
        }
        return chosen;

    }


    public void extendLance() {
        
        
        spellHandler.extendLance(getLance());
        resizeLanceImage();

        Runnable shrinkLanceTask = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                
                spellHandler.shrinkLance(getLance());
                resizeLanceImage();
            }
        };

        PausableThread pausableThread = new PausableThread(() -> {}, shrinkLanceTask, 10000);
        pausableThreads.add(pausableThread);


    }

    /**
     * Applies the Overwhelming Spell. This spell creates a massive fireball that
     * travels across the screen, damaging and destroying barriers in its path.
     */
    public void applyOverwhelmingSpell() {
        // Create a thread that will run the code to apply the Overwhelming Spell
        Runnable overwhelmingSpellTask = new Runnable() {
            @Override
            public void run() {
                // Call the method in the SpellHandler to apply the Overwhelming Spell
                spellHandler.overwhelmingSpell(level);
            }
        };

        // Create a thread that will run the code to normalize the fireball after the
        // Overwhelming Spell has finished
        Runnable normalizeFireball = new Runnable() {
            @Override
            public void run() {
                // Call the method in the SpellHandler to normalize the fireball
                spellHandler.endOverwhelmingBall(level);
            }
        };

        // Create a PausableThread that will run the two tasks in sequence
        PausableThread pausableThread = new PausableThread(overwhelmingSpellTask, normalizeFireball, 10000);
        // Add the thread to the list of pausable threads
        pausableThreads.add(pausableThread);
    }
    


    
    public void collectSpell(char spell){
        SpellBox.incrementSpellCount(spell);
        switch(spell) {
            case(SpellBox.EXTENSION_SPELL):
                level.collectSpell(spell);
                break;
            case(SpellBox.OVERWHELMING_SPELL):
                applyOverwhelmingSpell();
                break; //TODO: add the overwhelming spell once it is done.
            case(SpellBox.HEX_SPELL):
                level.collectSpell(spell);
                break;
            case(SpellBox.FELIX_FELICIS_SPELL):
                spellHandler.felixFelicis(level);
                break;
            default:
                return;
        }
        if (levelPanel != null) {
            levelPanel.updateSpellCounts();
        }
    }
    public void useSpell(char spell) {
        if (!level.inventoryHasSpell(spell)) return;
        
        if (SpellBox.decrementSpellCount(spell)) {
            switch (spell) {
                case SpellBox.EXTENSION_SPELL:
                    extendLance();
                    //level.removeSpell(spell);
                    break;
                case SpellBox.HEX_SPELL:
                    startCreatingHex();
                    break;
                case SpellBox.OVERWHELMING_SPELL:
                    applyOverwhelmingSpell();
                    break;
                default:
                    return;
            }
            // Update the spell counts in the LevelPanel
            if (levelPanel != null) {
                levelPanel.updateSpellCounts();
            }
        } else {
            // Handle the case where there are no more spells left
            System.out.println("No more spells left of type: " + spell);
        }
    }


    public void generateHollowBarriers(){
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0; i<8; i++){
            List<Barrier> barriers = getBarriers();
            double width = barriers.get(0).getLength();
            generateRandomBarrier(width, BarrierType.HOLLOW_BARRIER, secureRandom);
        }
    }

    //temporarily replicated in this class
    public void generateRandomBarrier(double barrierWidth, BarrierType barrierType, SecureRandom secureRandom){
        int buildSectionWidth = LanceOfDestiny.getScreenWidth()/2;
        int buildSectionHeight = LanceOfDestiny.getScreenHeight()/2; // TODO: Change ratio later to a constant value
        boolean collided;
        int x, y;
        Barrier randomBarrier;
        do{
            x = (int)barrierWidth / 2 + secureRandom.nextInt(buildSectionWidth - (int) barrierWidth - (int) barrierWidth / 2);
            y = 20 + secureRandom.nextInt(buildSectionHeight - 40);  // -20 barrier -20 padding
            randomBarrier = createBarrier(barrierType, x, y, barrierWidth);
            collided = checkBarrierCollisionWithBarriers(randomBarrier);
        }while(collided);
        getBarriers().add(randomBarrier);
    }

    private Barrier createBarrier(BarrierType barrierType, int x, int y, double barrierWidth){
        Barrier barrier;
        switch (barrierType) {
            case SIMPLE_BARRIER -> barrier = new SimpleBarrier(x, y);
            case FIRM_BARRIER -> barrier = new FirmBarrier(x, y);
            case EXPLOSIVE_BARRIER -> barrier = new ExplosiveBarrier(x, y);
            case GIFT_BARRIER -> barrier = new GiftBarrier(x, y);
            case HOLLOW_BARRIER -> barrier = new HollowBarrier(x, y);
            default -> barrier = new SimpleBarrier(x, y);
        }
        barrier.setLength(barrierWidth);
        return barrier;
    }

    public boolean checkBarrierCollisionWithBarriers(Barrier barrier){
        for (Barrier b : getBarriers()) {
            if (b != barrier && checkBarrierCollision(b, barrier)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkBarrierCollision(Barrier b1, Barrier b2){
        return b1.getBoundingBox().intersects(b2.getBoundingBox());
    }




    public List<PausableThread> getPausableThreads() {
        return pausableThreads;
    }

    public void setLevelPanel(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
    }


    
}
