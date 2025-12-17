
import java.util.Scanner;

public class Utils {
    private static final Scanner sc = new Scanner(System.in);

    public static String readLineFromKeyboard() {
        if (sc.hasNextLine()) {
            return sc.nextLine();
        }
        return null;
    }

    public static int readCharFromKeyboard() {
        String s = readLineFromKeyboard();
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return -1;
        }
    }
}
