public class Population {

    private Member[] population;

    public Population(int popSize, int chromLength) {
        population = new Member[popSize];
        initPop(popSize, chromLength);
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
}
