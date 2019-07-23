import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ControlSystem extends Thread {

    // User input variables
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String line;
    boolean quit = false;
    double[] desiredValues = new double[6]; // For the 6 musical features

    // Global population variables
    int popSize = 10;
    int genNum;
    int chromLength = 16;
    double crossRate = 0.8;
    double mutRate = 0.01;

    Conductor conductor = new Conductor();
    Population population = new Population(popSize, chromLength, crossRate, mutRate);
    Objective objective = new Objective();

    @Override
    public void run() {
        //population.printPop();
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
                        conductor.setCurrMember(population.sendMember());
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
