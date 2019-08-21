import java.util.Random;

public class Utility {

    private static Random random = new Random();

    public static int[][] initChrom(int chromLength) {
        // Randomly set 0s and 1s in 2D bit array
        int[][] chromosome = new int[4][chromLength];
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromLength; j++) {
                Double d = Math.floor(random.nextDouble()*2);
                chromosome[i][j] = d.intValue();
            }
        }
        return chromosome;
    }

    public static int[] initRow(int chromLength) {
        // Randomly set 0s and 1s in 1D bit array
        int[] row = new int[chromLength];
        for (int i = 0; i < chromLength; i++) {
            Double d = Math.floor(random.nextDouble()*2);
            row[i] = d.intValue();
        }
        return row;
    }

    // Map, or scale, a number from one range to another range
    public static double map(double n, double oldMin, double oldMax, double newMin, double newMax) {
        return (newMax - newMin) * (n - oldMin) / (oldMax - oldMin) + newMin;
    }
}
