import java.util.Random;

public class Bot {
    public static int Zug(){
        Random r = new Random();
        return r.nextInt(3);
    }
}
