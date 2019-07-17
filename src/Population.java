public class Population {

    private Member[] population;

    public Population(int popSize, int chromLength) {
        population = new Member[popSize];
        initPop(popSize, chromLength);
    }

    // For debugging
    public void printPop() {
        for (Member m: population) {
            int[][] chrom = m.getDna().getChromosome();
            for (int i = 0; i < chrom.length; i++) {
                for (int j = 0; j < chrom[i].length; j++) {
                    System.out.print(chrom[i][j] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    private void initPop(int popSize, int chromLength) {
        for (int i = 0; i < popSize; i++) {
            population[i] = new Member(chromLength);
        }
    }

    public void evolve() {
        // Assess fitness
        assess();
        // Perform selection, including crossover
        select();
    }

    private void assess() {}

    private void select() {}

    public Member sendMember() {
        // Send best Member to Conductor, via ControlSystem,
        // or cycle through best Members each time this is called
        return null;
    }
}
