public class Member {

    private DNA dna;
    private double fitness;

    public Member(int chromLength) {
        dna = new DNA(chromLength);
        fitness = 0;
    }
}
