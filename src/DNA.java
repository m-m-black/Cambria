public class DNA {

    private int[][] chromosome;

    public DNA(int chromLength) {
        chromosome = Utility.initChrom(chromLength);
    }

    public DNA(int[][] chromosome) {
        this.chromosome = chromosome;
    }

    public int[][] getChromosome() {
        return chromosome;
    }
}
