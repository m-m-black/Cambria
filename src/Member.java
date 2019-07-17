public class Member {

    private DNA dna;
    private double fitness;

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
}
