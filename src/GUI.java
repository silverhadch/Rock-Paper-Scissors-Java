import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI {

    private static JFrame frame;
    private static JPanel bilderPanel;
    private static JPanel ausgewähltesPanel;
    private static JLabel ausgewähltesLabel;
    private static JLabel anderesLabel;
    private static int spielZustand = 0;
    private static int spielerzug = -1; // Initialwert für den Spielerzug
    private static int zugicon = -1;
    private static Timer timer;
    private static int ausgewähltesBildIndex = -1;
    private static JLabel spielerPunkteLabel;
    private static JLabel botPunkteLabel;
    private static JLabel bothand;
    private static JLabel ausgewhand;
    private static JLabel deinhand;
    public static String andicon = "res/bilder/0.png";
    public static String sek3lefticon = "res/bilder/3.png";
    public static String sek2lefticon = "res/bilder/2.png";
    public static String sek1lefticon = "res/bilder/1.png";
    public static String scheicon = "res/bilder/Schere.png";
    public static String steicon = "res/bilder/Stein.png";
    public static String papieicon = "res/bilder/Papier.png";
    static boolean neustartAbfrage() {
        int result = JOptionPane.showConfirmDialog(null, "Das Spiel ist beendet! Möchten Sie nochmal spielen?", "Neustart", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    public static void erstelleFenster() {
        frame = new JFrame("Schere, Stein, Papier Spiel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBackground(Color.cyan);
        frame.setSize(1000, 850); // Größe des Fensters setzen (3x so groß)
        frame.setLocationRelativeTo(null);



        frame.setVisible(true);
    }

    public static void erstellepcounter(){
        // Erstelle Punktestand Labels
        spielerPunkteLabel = new JLabel("Spieler: 0");
        spielerPunkteLabel.setForeground(Color.BLUE); // Blaue Schrift für Spielerpunkte
        botPunkteLabel = new JLabel("Bot: 0");
        botPunkteLabel.setForeground(Color.RED); // Rote Schrift für Botpunkte

        // Platziere die Punktestand Labels in den obersten Ecken
        JPanel punktestandPanel = new JPanel(new BorderLayout());
        punktestandPanel.add(spielerPunkteLabel, BorderLayout.WEST);
        punktestandPanel.add(botPunkteLabel, BorderLayout.EAST);
        punktestandPanel.setBackground(Color.gray);
        frame.getContentPane().add(punktestandPanel, BorderLayout.NORTH);
    }

    static void showTimedMessage(String message, String title, int messageType, int duration) {
        JOptionPane optionPane = new JOptionPane(message, messageType);
        JDialog dialog = optionPane.createDialog(title);


        Timer timer = new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose(); // Schließe das Dialogfeld nach Ablauf der Zeit
            }
        });

        timer.setRepeats(false); // Der Timer wird nur einmal ausgeführt
        timer.start();

        dialog.setVisible(true); // Zeige das Dialogfeld

        // Optional: Halte den Timer an, wenn das Dialogfeld vom Benutzer geschlossen wird
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                timer.stop();
            }
        });
    }

    private static void zeigeAusgewähltesIcon(int auswahl) {
        ImageIcon ausgewähltIcon = switch (auswahl) {
            case 0 -> new ImageIcon(scheicon);
            case 1 -> new ImageIcon(steicon);
            case 2 -> new ImageIcon(papieicon);
            default -> null;
        };

        if (ausgewähltIcon != null) {
            if (ausgewähltesLabel != null) {
                ausgewähltesPanel.remove(ausgewähltesLabel);
            }

            ausgewähltesLabel = new JLabel(ausgewähltIcon);
            ausgewähltesLabel.setOpaque(false);


            ausgewähltesPanel.add(ausgewähltesLabel, BorderLayout.CENTER); // In der Mitte positionieren
            ausgewähltesPanel.revalidate();
            ausgewähltesPanel.repaint();
        }
    }

    private static int auswahl = -1; // Initialwert für die Auswahl

    // Methode zum Anzeigen des Punktestands für eine Sekunde
    public static void aktualisierePunktestandAnzeige(int spunkte, int bpunkte) {
        spielerPunkteLabel.setText("Spieler: " + spunkte); // Aktualisiere Spielerpunkte
        botPunkteLabel.setText("Bot: " + bpunkte); // Aktualisiere Botpunkte
    }
    public static void zuggrafik() {
        if (Main.spielende){
            aktualisierePunktestandAnzeige(Main.spunkte,Main.bpunkte);
        } else {
            aktualisierePunktestandAnzeige(Main.spunkte,Main.bpunkte);
            timer = new Timer(1000, new ActionListener() {
                int sekunden = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    sekunden++;

                    if (sekunden == 1) {
                        anderesLabel.setIcon(new ImageIcon(sek3lefticon));
                    } else if (sekunden == 2) {
                        anderesLabel.setIcon(new ImageIcon(sek2lefticon));
                    } else if (sekunden == 3) {
                        anderesLabel.setIcon(new ImageIcon(sek1lefticon));

                        spielZustand = 1;
                    } else if (sekunden == 4) {
                        synchronized (Main.lock) {
                            zugicon = Bot.Zug();

                            if (zugicon == 0) {
                                andicon = scheicon;
                            } else if (zugicon == 1) {
                                andicon = steicon;
                            } else if (zugicon == 2){
                                andicon = papieicon;
                            }

                            spielerzug = getAuswahl();
                            zeigeAusgewähltesIcon(spielerzug);
                            Main.methodeAufgerufen = true;
                            Main.lock.notify(); // Benachrichtige die wartende main-Methode
                        }
                        aktualisierePunktestandAnzeige(Main.spunkte,Main.bpunkte);
                        ((Timer) e.getSource()).stop();// Timer stoppen, um keine weiteren Änderungen durchzuführen
                        spielZustand = 0;
                        zeigeBilder();
                    }
                }
            });

            timer.start(); // Timer starten

        }
    }

    public static int spielerz() {
        return spielerzug;
    }
    public static int botz(){
        return zugicon;
    }

    private static ImageIcon verkleinereIconUmFaktor(ImageIcon icon, double faktor) {
        int neueBreite = (int) (icon.getIconWidth() * faktor);
        int neueHoehe = (int) (icon.getIconHeight() * faktor);
        Image image = icon.getImage().getScaledInstance(neueBreite, neueHoehe, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    private static ImageIcon verkleinereIconUmProzent(ImageIcon icon, double prozent) {
        double faktor = 1 - (prozent / 100);
        return verkleinereIconUmFaktor(icon, faktor);
    }


    public static void tempauswahl(JPanel bilderPanel, int index) {
        // Setze die Hintergrundfarbe aller Icons auf transparent
        for (Component component : bilderPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel iconLabel = (JLabel) component;
                iconLabel.setOpaque(false);
                iconLabel.repaint();
            }
        }

        if (index >= 0 && index < bilderPanel.getComponents().length) {
            // Setze die Hintergrundfarbe des ausgewählten Icons auf orange
            JLabel selectedLabel = (JLabel) bilderPanel.getComponent(index);
            selectedLabel.setOpaque(true);
            selectedLabel.setBackground(Color.ORANGE);
            selectedLabel.repaint();
        }
    }

    public static void zeigeBilder() {
        erstellepcounter();
        // Laden der Bilder
        ImageIcon schereIcon = new ImageIcon(scheicon);
        schereIcon = verkleinereIconUmProzent(schereIcon,15);
        ImageIcon steinIcon = new ImageIcon(steicon);
        steinIcon = verkleinereIconUmProzent(steinIcon,15);
        ImageIcon papierIcon = new ImageIcon(papieicon);
        papierIcon = verkleinereIconUmProzent(papierIcon,15);
        ImageIcon anderesIcon = new ImageIcon(andicon); // Anderes Icon oben anzeigen

        // Erzeugen von Labels für die Bilder
        JLabel schereLabel = new JLabel(schereIcon);
        JLabel steinLabel = new JLabel(steinIcon);
        JLabel papierLabel = new JLabel(papierIcon);
        anderesLabel = new JLabel(anderesIcon); // Label für das andere Icon
        if (spielZustand == 0){
            zuggrafik();
            schereLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    auswahl = 0; // Schere auswählen
                    ausgewähltesBildIndex = 0;
                    tempauswahl(bilderPanel, ausgewähltesBildIndex);
                }
            });

            steinLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    auswahl = 1; // Stein auswählen
                    ausgewähltesBildIndex = 1;
                    tempauswahl(bilderPanel, ausgewähltesBildIndex);
                }
            });

            papierLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    auswahl = 2; // Papier auswählen
                    ausgewähltesBildIndex = 2;
                    tempauswahl(bilderPanel, ausgewähltesBildIndex);
                }
            });

        }

        JPanel panel = new JPanel(new BorderLayout());
        /*bothand = new JLabel("Bothand:");
        bothand.setForeground(Color.GRAY);
        ausgewhand = new JLabel("Gesetzte Hand");
        ausgewhand.setForeground(Color.GRAY);
        deinhand = new JLabel("Deine Hand:");
        deinhand.setForeground(Color.GRAY);*/

        // Panel für die Bilder im Süden des BorderLayouts hinzufügen
        bilderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bilderPanel.setBackground(Color.GRAY);
        bilderPanel.add(schereLabel);
        bilderPanel.add(steinLabel);
        bilderPanel.add(papierLabel);
        //bilderPanel.add(deinhand,BorderLayout.WEST);
        panel.add(bilderPanel, BorderLayout.SOUTH);

        // Panel für das ausgewählte Icon erstellen und in der Mitte positionieren
        ausgewähltesPanel = new JPanel(new BorderLayout());
        ausgewähltesPanel.setBackground(Color.BLUE); // Hintergrundfarbe wie bei den anderen Icons
        //ausgewähltesPanel.add(ausgewhand,BorderLayout.WEST);
        panel.add(ausgewähltesPanel, BorderLayout.CENTER);

        // Anderes Icon oben anzeigen
        anderesLabel.setOpaque(true); // Label ist undurchsichtig
        anderesLabel.setBackground(Color.RED);
        //anderesLabel.add(bothand,BorderLayout.WEST);
        panel.add(anderesLabel, BorderLayout.NORTH);


        // Initial ausgewähltes Icon anzeigen
        zeigeAusgewähltesIcon(auswahl);

        // Inhalte zum JFrame hinzufügen
        frame.getContentPane().add(panel);

        // JFrame sichtbar machen
        frame.setVisible(true);
    }

    public static int getAuswahl() {
        return auswahl;
    }

    public static int punktabfrage() {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, "Bei wie vielen Punkten soll das Spiel enden?");
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ungültige Eingabe. Bitte geben Sie eine ganze Zahl ein.");
            }
        }
    }


    public static void schließeFenster() {
        if (frame != null) {
            frame.dispose();
        }
    }
}