import java.util.Random;

public class Population {

    private Member[] parentPop;
    private Member[] childPop;
    private Random random;
    private int chromLength;
    private double crossRate;
    private int crossPoint;
    private double mutRate;

    public Population(int popSize, int chromLength, double crossRate, double mutRate) {
        parentPop = new Member[popSize];
        childPop = new Member[popSize];
        random = new Random();
        this.chromLength = chromLength;
        this.crossRate = crossRate;
        crossPoint = (int) (parentPop.length * crossRate);
        this.mutRate = mutRate;
        initPop(popSize, chromLength);
    }

    // For debugging
    public void printPop() {
        for (Member m: parentPop) {
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
            parentPop[i] = new Member(chromLength);
        }
    }

    public void evolve() {
        // Assess fitness
        assess();
        // Iterate over population to perform selection and crossover
        for (int i = 0; i < parentPop.length; i++) {
            if (i < crossPoint) {
                // Perform crossover of parents
                Member[] parents = select();
                Member child = crossover(parents);
                child.mutate(mutRate);
                childPop[i] = child;
            } else {
                // Perform tournament and add winner to population
                childPop[i] = tourney();
            }
            // Set parent population to child population
            parentPop = childPop;
        }
    }

    private void assess() {
        // Assess fitness of each Member
        for (Member m: parentPop) {
            Fitness.assess(m);
        }
    }

    private Member tourney() {
        // Return winner of a tournament
        int a = random.nextInt(parentPop.length);
        int b = random.nextInt(parentPop.length);
        if (parentPop[a].getFitness() > parentPop[b].getFitness()) {
            return parentPop[a];
        } else {
            return parentPop[b];
        }
    }

    private Member[] select() {
        // Perform tournament selection and return 2 parents
        Member[] parents = new Member[2];
        for (int i = 0; i < 2; i++) {
            parents[i] = tourney();
        }
        return parents;
    }

    private Member crossover(Member[] parents) {
        // Perform crossover and return child
        Member child = new Member();
        int[][] chromA = parents[0].getDna().getChromosome();
        int[][] chromB = parents[1].getDna().getChromosome();
        int[][] childChrom = new int[4][chromLength];
        // Set bits in child from parents, split at random midpoint
        for (int i = 0; i < childChrom.length; i++) {
            int midpoint = random.nextInt(chromLength);
            for (int j = 0; j < childChrom[i].length; j++) {
                if (j < midpoint) {
                    childChrom[i][j] = chromA[i][j];
                } else {
                    childChrom[i][j] = chromB[i][j];
                }
            }
        }
        // Assign new chromosome to child
        child.setDna(new DNA(childChrom));
        return child;
    }

    public Member sendMember() {
        // Send best Member to Conductor, via ControlSystem,
        // or cycle through best Members each time this is called
        return null;
    }
}
