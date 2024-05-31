package tr.edu.ku.comp302.domain.handler;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SoundHandler {
    private static final Map<String, Clip> soundCache = new HashMap<>();
    private static final String PICKUP_GIFT_SOUND = "game_artifact/src/main/resources/sounds/pickupGift.wav";
    private static final String HIT_HURT_SOUND = "game_artifact/src/main/resources/sounds/hitHurt.wav";
    private static final String EVIL_LAUGH_SOUND = "game_artifact/src/main/resources/sounds/evil_laugh.wav";
    private static final String DEFEAT_SOUND = "defeat_sound.wav";

    public static void playSound(String soundFileName) {
        try {
            Clip clip = soundCache.get(soundFileName);
            if (clip == null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundFileName).getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                soundCache.put(soundFileName, clip);
            }
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playGiftSound() {
        playSound(PICKUP_GIFT_SOUND);
    }

    public static void playRemainHitSound() {
        playSound(HIT_HURT_SOUND);
    }

    public static void playLaughSound() {
        playSound(EVIL_LAUGH_SOUND);
    }

    public static void playDefeatSound() {
        playSound(DEFEAT_SOUND);
    }

    
}

    

