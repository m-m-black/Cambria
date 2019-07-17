public class Population {

    private Member[] population;

    public Population() {}

    public void evolve() {
        // Assess fitness
        assess();
        // Perform selection, including crossover
        select();
    }

    private void assess() {}

    private void select() {}
}
