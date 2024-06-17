import javax.swing.*;

public class Main {
    static int  spunkte = 0;
    static int bpunkte = 0;
    static final Object lock = new Object();
    static boolean methodeAufgerufen = false;
    static boolean spielende = false;

    public static void main(String[] args) {
        GUI.erstelleFenster();
        startegame();
    }
    public static void startegame(){

        boolean wieder = false;

        while (!wieder) {

            Soundplayer.playBackgroundMusic();
            int punkteZumGewinnen = GUI.punktabfrage();
            bpunkte = 0;
            spunkte = 0;
            spielende = false;
            GUI.zeigeBilder();


            while (bpunkte != punkteZumGewinnen && spunkte != punkteZumGewinnen) {
                synchronized (lock) {
                    while (!methodeAufgerufen) {
                        try {
                            lock.wait(); // Warte, bis die Methode aufgerufen wird
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            JOptionPane.showMessageDialog(null,"Spielfehler: Warten wurde unterbrochen");
                        }
                    }
                    // Hier wird fortgesetzt, wenn die Methode aufgerufen wurde
                    int botzug = GUI.botz();
                    int spielerzug = GUI.spielerz();
                    if (spielerzug != -1){
                        Regeln.gewinncheck(spielerzug, botzug);
                    } else {
                        bpunkte++;
                    }
                }
                methodeAufgerufen = false;
                if (spunkte == punkteZumGewinnen) {
                    spielende = true;
                    Soundplayer.stopBackgroundMusic(Soundplayer.backgroundClip);
                    Soundplayer.playWinMusic();
                    GUI.showTimedMessage("Du hast gewonnen!","Sieg", JOptionPane.INFORMATION_MESSAGE,4000);

                } else if (bpunkte == punkteZumGewinnen) {
                    spielende = true;
                    Soundplayer.stopBackgroundMusic(Soundplayer.backgroundClip);
                    Soundplayer.playLoseMusic();
                    GUI.showTimedMessage("Du hast verloren!","Niederlage", JOptionPane.INFORMATION_MESSAGE,4000);
                }
            }
            wieder = !GUI.neustartAbfrage();
        }
        GUI.schließeFenster();
    }
}