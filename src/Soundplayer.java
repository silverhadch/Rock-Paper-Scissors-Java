
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Soundplayer extends GUI{

    private static final String SOUND_FILE_PATH = "res/Sound/Background.wav";
    private static final String SOUND_FILE_PATH2 = "res/Sound/Winning Sound Effect.wav";
    private static final String SOUND_FILE_PATH3 = "res/Sound/Sad Trombone - Sound Effect (HD).wav";
    static Clip backgroundClip;

    static void playBackgroundMusic() {
        try {
            File soundFile = new File(SOUND_FILE_PATH);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioIn);
            backgroundClip.start();
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);

            // Stoppe die Hintergrundmusik, wenn eine andere Musik abgespielt wird
            stopBackgroundMusic(backgroundClip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void stopBackgroundMusic(Clip backgroundClip) {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    static void playLoseMusic() {
        try {
            File soundFile = new File(SOUND_FILE_PATH3);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            // Stoppe die Hintergrundmusik, wenn eine andere Musik abgespielt wird
            stopBackgroundMusic(clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void playWinMusic() {
        try {
            File soundFile = new File(SOUND_FILE_PATH2);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            // Stoppe die Hintergrundmusik, wenn eine andere Musik abgespielt wird
            stopBackgroundMusic(clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


