import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ControlSystem extends Thread {

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String line;
    boolean quit = false;

    @Override
    public void run() {
        System.out.println("Welcome to Cambria");
        try {
            while (!quit && (line = bufferedReader.readLine()) != null) {
                switch (line.toUpperCase()) {
                    case "TEST":
                        System.out.println("System stable");
                        break;
                    case "QUIT":
                        quit = true;
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
