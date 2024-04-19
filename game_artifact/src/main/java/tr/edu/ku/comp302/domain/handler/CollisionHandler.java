package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;

import java.awt.*;

public class CollisionHandler {
    public static void checkCollisions(FireBallView fireBallView, LanceView lanceView) {
        FireBall fireBall = fireBallView.getFireBall();
        Lance lance = lanceView.getLance();
        Rectangle fireBallBounds = new Rectangle((int) fireBall.getXPosition(), (int) fireBall.getYPosition(), fireBall.getSize(), fireBall.getSize());
        Rectangle lanceBounds = new Rectangle((int) lance.getXPosition(), (int) lance.getYPosition(), (int) lance.getLength(), (int) lance.getThickness());

        if (fireBallBounds.intersects(lanceBounds)) {
            fireBall.bounceOffHorizontalSurface();
            // TODO: Make a proper fireball method so that this bouncing actually works !

            // TODO: implement all the possible collisions between the lance and the fireball. For some reason
            // the vertical reflection is working, but the rest is not.
        }
    }
}


