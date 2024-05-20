package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.ui.view.View;

import java.awt.*;
import java.util.List;

public class BarrierRenderer {
    protected static final View simpleBarrierView = View.of(View.SIMPLE_BARRIER);
    protected static final View firmBarrierView = View.of(View.FIRM_BARRIER);
    protected static final View explosiveBarrierView = View.of(View.EXPLOSIVE_BARRIER);
    private static final Logger logger = LogManager.getLogger(BarrierRenderer.class);

    public void resizeBarrierImages(List<Barrier> barriers) {
        if (!barriers.isEmpty()) {
            Barrier b = barriers.getFirst();
            int length = (int) b.getLength();
            int thickness = (int) b.getThickness();
            simpleBarrierView.resizeImage(length, thickness);
            firmBarrierView.resizeImage(length, thickness);
            explosiveBarrierView.resizeImage(length, thickness);
        }
    }

    public void renderBarriers(Graphics g, List<Barrier> barriers) {
        for (Barrier barrier : barriers) {
            renderBarrier(g, barrier);
        }
    }

    private void renderBarrier(Graphics g, Barrier barrier) {
        var image = switch (barrier) {
            case SimpleBarrier ignored -> simpleBarrierView.getImage();
            case FirmBarrier ignored -> {
                renderFirmBarrier(g, barrier);
                yield null;
            }
            case ExplosiveBarrier ignored -> explosiveBarrierView.getImage();
            case Barrier ignored -> {
                logger.warn("renderBarrier: Unknown barrier type");
                yield View.of(View.MISSING_TEXTURE) // I hope this works
                        .resizeImage((int) barrier.getLength(), (int) barrier.getThickness());
            }
        };

        if (image != null) {
            g.drawImage(image, (int) barrier.getXPosition(), (int) barrier.getYPosition(), null);
        }
        // This can be used to draw the invisible ellipse hit boxes around the explosive barriers
//        if (barrier instanceof ExplosiveBarrier b) {
//            var strategy = b.getMovementStrategy();
//            g.drawOval((int) (b.getXPosition() - strategy.getXPadding()),
//                       (int) (b.getYPosition() - strategy.getYPadding()),
//                       (int) (b.getLength() + 2 * strategy.getXPadding()),
//                       (int) (b.getThickness() + 2 * b.getMovementStrategy().getYPadding()));
//        }
    }

    private void renderFirmBarrier(Graphics g, Barrier barrier) {
        g.drawImage(firmBarrierView.getImage(), (int) barrier.getXPosition(), (int) barrier.getYPosition(), null);
        int health = barrier.getHealth();
        int textX = (int) barrier.getXPosition() + firmBarrierView.getImage().getWidth(null) / 2;
        int textY = (int) barrier.getYPosition() - 3; // 3 pixels above the barrier
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 8));
        g.drawString(String.valueOf(health), textX, textY);
    }
}
