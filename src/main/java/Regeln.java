public class Regeln {

    public static void gewinncheck(int szug,int bzug){
        if (szug == bzug) {
            int irgendwasmusshierstehenlol;
        } else if ((szug == 0 && bzug == 1) || (szug == 1 && bzug == 2) || (szug == 2 && bzug == 0)) {
            //Counter in Grafik
            Main.bpunkte++;
        } else {
            //Counter in Grafik
            Main.spunkte++;
        }
    }
}
