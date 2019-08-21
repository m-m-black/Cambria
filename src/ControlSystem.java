import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ControlSystem extends Thread {

    // User input variables
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    String line;
    boolean quit = false;

    // Global population variables
    int popSize = 1000;
    int genNum = 200;
    int chromLength = 32;
    double crossRate = 0.9;
    double mutRate = 0.005;

    Conductor conductor = new Conductor();
    Population population = new Population(popSize, chromLength, crossRate, mutRate);
    Objective objective = new Objective();

    private static final String DOUBLE_ERROR_MSG = "Must be a number from 0.0 to 1.0";

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
                            if (isInteger(tokens[1])) {
                                conductor.setTempo(Integer.parseInt(tokens[1]));
                            } else {
                                System.out.println("Not a valid number");
                            }
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
                        conductor.setCurrMember(population.getBestMember());
                        break;
                    case "EVOLVE":
                        population.evolve(genNum);
                        System.out.println("Done");
                        break;
                    case "POP":
                        population.printPop();
                        break;
                    case "BEST":
                        population.printMember(population.getBestMember());
                        break;
                    case "BREAK":
                        population.printMember(population.breakMember(population.getBestMember()));
                    case "HOCK":
                        if (tokens.length == 1) {
                            System.out.println(objective.getHocket());
                        } else if (tokens.length > 1) {
                            if (isValidDouble(tokens[1])) {
                                objective.setHocket(Double.valueOf(tokens[1]));
                            } else {
                                System.out.println(DOUBLE_ERROR_MSG);
                            }
                        }
                        break;
                    case "DENS":
                        if (tokens.length == 1) {
                            System.out.println(objective.getDensity());
                        } else if (tokens.length > 1) {
                            if (isValidDouble(tokens[1])) {
                                objective.setDensity(Double.valueOf(tokens[1]));
                            } else {
                                System.out.println(DOUBLE_ERROR_MSG);
                            }
                        }
                        break;
                    case "SYNC":
                        if (tokens.length == 1) {
                            System.out.println(objective.getSyncopation());
                        } else if (tokens.length > 1) {
                            if (isValidDouble(tokens[1])) {
                                objective.setSyncopation(Double.valueOf(tokens[1]));
                            } else {
                                System.out.println(DOUBLE_ERROR_MSG);
                            }
                        }
                        break;
                    case "BAL":
                        if (tokens.length == 1) {
                            System.out.println(objective.getBalance());
                        } else if (tokens.length > 1) {
                            if (isValidDouble(tokens[1])) {
                                objective.setBalance(Double.valueOf(tokens[1]));
                            } else {
                                System.out.println(DOUBLE_ERROR_MSG);
                            }
                        }
                        break;
                    case "DOWN":
                        if (tokens.length == 1) {
                            System.out.println(objective.getDownbeat());
                        } else if (tokens.length > 1) {
                            if (isValidDouble(tokens[1])) {
                                objective.setDownbeat(Double.valueOf(tokens[1]));
                            } else {
                                System.out.println(DOUBLE_ERROR_MSG);
                            }
                        }
                        break;
                    case "BACK":
                        if (tokens.length == 1) {
                            System.out.println(objective.getBackbeat());
                        } else if (tokens.length > 1) {
                            if (isValidDouble(tokens[1])) {
                                objective.setBackbeat(Double.valueOf(tokens[1]));
                            } else {
                                System.out.println(DOUBLE_ERROR_MSG);
                            }
                        }
                        break;
                    case "SPR":
                        if (tokens.length == 1) {
                            System.out.println(objective.getSpread());
                        } else if (tokens.length > 1) {
                            if (isValidDouble(tokens[1])) {
                                objective.setSpread(Double.valueOf(tokens[1]));
                            } else {
                                System.out.println(DOUBLE_ERROR_MSG);
                            }
                        }
                        break;
                    case "FEATURES":
                        // Prints current values for all features
                        System.out.println(objective.getDesiredValues());
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

    // Check if a string can be parsed as a double
    private static boolean isValidDouble(String str) {
        try {
            double d = Double.parseDouble(str);
            if (d < 0.0 || d > 1.0) {
                return false;
            } else {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Check if a string can be parsed as an integer
    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
