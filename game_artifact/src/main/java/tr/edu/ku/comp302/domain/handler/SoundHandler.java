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
            System.err.println("Error playing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playGiftSound() {
        playSound("game_artifact/src/main/resources/sounds/pickupGift.wav");
    }

    public static void playRemainHitSound() {
        playSound("game_artifact/src/main/resources/sounds/hitHurt.wav");
    }

    public static void playLaughSound() {
        playSound("game_artifact/src/main/resources/sounds/evil_laugh.wav");
    }

    public static void playDefeatSound() {
        playSound("defeat_sound.wav");
    }

    
}

    

