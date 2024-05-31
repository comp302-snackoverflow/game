package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.HorizontalMovement;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class GiftBarrier extends Barrier {
    private final SpellBox spellBox;

    public GiftBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        health = 1;
        this.movementStrategy = new HorizontalMovement();
        spellBox = new SpellBox(0, 0);
    }

    ;

    public void dropSpellBox() {
        spellBox.setXPosition(getXPosition() + getLength() / 2);
        spellBox.setYPosition(getYPosition() + getThickness() / 2);
        spellBox.drop();
    }

    public SpellBox getSpellBox() {
        return spellBox;
    }

}

