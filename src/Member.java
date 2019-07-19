import java.util.Random;

public class Member {

    private DNA dna;
    private double fitness;

    // Constructor for making blank child during crossover
    public Member() {
        dna = null;
        fitness = 0;
    }

    public Member(int chromLength) {
        dna = new DNA(chromLength);
        fitness = 0;
    }

    public DNA getDna() {
        return dna;
    }

    public void setDna(DNA dna) {
        this.dna = dna;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void mutate(double mutRate, Random random) {
        // Iterate over chromosome, flipping bits if we exceed the mutation threshold
        for (int i = 0; i < dna.getChromosome().length; i++) {
            for (int j = 0; i < dna.getChromosome()[i].length; j++) {
                if (random.nextDouble() < mutRate) {
                    if (dna.getChromosome()[i][j] == 0) {
                        dna.getChromosome()[i][j] = 1;
                    } else {
                        dna.getChromosome()[i][j] = 0;
                    }
                }
            }
        }
    }
}
