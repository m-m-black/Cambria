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

    public void evolve(int genNum) {
        for (int i = 0; i < genNum; i++) {
            // Assess fitness of parent population
            assess(parentPop);
            // Iterate over population to perform selection and crossover
            for (int j = 0; j < parentPop.length; j++) {
                if (j < crossPoint) {
                    // Perform crossover of parents
                    Member[] parents = select();
                    Member child = crossover(parents);
                    child.mutate(mutRate, random);
                    childPop[j] = child;
                } else {
                    // Perform tournament and add winner to population
                    childPop[j] = tourney();
                }
            }
            // Assess fitness of child population
            assess(childPop);
            // Set parent population to child population
            parentPop = childPop;
        }
//        for (Member m: parentPop) {
//            System.out.println(m.getCost());
//        }
    }

    private void assess(Member[] pop) {
        // Assess fitness of each Member
        for (Member m: pop) {
            Objective.assess(m);
        }
    }

    private Member tourney() {
        // Return winner of a tournament
        int a = random.nextInt(parentPop.length);
        int b = random.nextInt(parentPop.length);
        if (parentPop[a].getCost() > parentPop[b].getCost()) {
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

    public Member bestMember() {
        double bestCost = Double.MAX_VALUE;
        Member bestMember = null;
        for (Member m: parentPop) {
            if (m.getCost() < bestCost) {
                bestCost = m.getCost();
                bestMember = m;
            }
        }
        Objective.assess(bestMember);
        return bestMember;
    }

    public void printMember(Member member) {
        int[][] chrom = member.getDna().getChromosome();
        for (int i = 0; i < chrom.length; i++) {
            for (int j = 0; j < chrom[i].length; j++) {
                System.out.print(chrom[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Cost: " + member.getCost());
    }
}
