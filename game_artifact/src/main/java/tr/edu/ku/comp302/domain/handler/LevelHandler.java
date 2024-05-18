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
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.view.View;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;


public class LevelHandler {
    private static final View fireBallView = View.of(View.FIREBALL);
    private static final View lanceView = View.of(View.LANCE);
    private BarrierRenderer barrierRenderer = new BarrierRenderer();
    private static final View remainView = View.of(View.REMAIN);
    private static final View hexView = View.of(View.HEX);
    private static final View spellBoxView = View.of(View.SPELL_BOX);
    private final Logger logger = LogManager.getLogger(LevelHandler.class);
    private Level level;

    /**
     * Handling the render of the views of the objects of the level instance.
     * (Use this with your own risk!!!)
     * @param level
     */
    public LevelHandler(Level level) {
        this.level = level;
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
    public void createHex() {
        Lance lance = level.getLance();
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

        level.getHexs().add(newHex1);
        level.getHexs().add(newHex2);
    }


    public List<Hex> getHexs(){
        return level.getHexs();
    }

}
