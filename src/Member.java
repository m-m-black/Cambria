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

    public void mutate() {

    }
}
