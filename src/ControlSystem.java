import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ControlSystem extends Thread {

    // User input variables
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String line;
    boolean quit = false;

    // Global population variables
    int popSize;
    int genNum;
    int chromLength;
    double crossRate;
    double mutRate;

    Conductor conductor = new Conductor();
    Population population = new Population(popSize, chromLength);

    @Override
    public void run() {
        System.out.println("Welcome to Cambria");
        try {
            while (!quit && (line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(" ");
                String command = tokens[0];

                switch (command.toUpperCase()) {
                    case "TEMPO":
                        if (tokens.length == 1) {
                            System.out.println(conductor.getTempo());
                        } else if (tokens.length > 1) {
                            conductor.setTempo(Integer.parseInt(tokens[1]));
                        }
                        break;
                    case "START":
                        conductor.start();
                        break;
                    case "STOP":
                        conductor.stop();
                        break;
                    case "MEMBER":
                        // Print currMember from conductor
                        break;
                    case "SET":
                        // Set currMember in conductor
                        break;
                    case "EVOLVE":
                        population.evolve();
                        break;
                    case "FEATURES":
                        // Prints current values for all features
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
