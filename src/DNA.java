import java.util.Random;

public class DNA {

    private int[][] chromosome;

    public DNA(int chromLength) {
        chromosome = initChrom(chromLength);
    }

    public DNA(int[][] chromosome) {
        this.chromosome = chromosome;
    }

    private int[][] initChrom(int chromLength) {
        // Randomly set 0s and 1s in 2D bit array
        int[][] chromosome = new int[4][chromLength];
        Random random = new Random();
        for (int i = 0; i < chromosome.length; i++) {
            for (int j = 0; j < chromLength; j++) {
                Double d = Math.floor(random.nextDouble()*2);
                chromosome[i][j] = d.intValue();
            }
        }
        return chromosome;
    }

    public int[][] getChromosome() {
        return chromosome;
    }
}
