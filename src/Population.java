import java.util.ArrayList;
import java.util.Random;

public class Population {

    private Member[] parentPop;
    private Member[] childPop;
    private Random random;
    private int chromLength;
    private double crossRate;
    private int crossPoint;
    private double mutRate;
    private Member bestMember;
    private double[] errors;
    private double worstError;
    private int worstErrorIndex;
    private int popSize;
    private double[][] errorsPerGen;

    // Output file for logging experiment data
    private static final String OUTPUT_FILENAME = "/Users/mblack/IdeaProjects/Cambria/output";
    private static final String LOG_FILENAME = "/Users/mblack/IdeaProjects/Cambria/log";


    public Population(int popSize, int chromLength, double crossRate, double mutRate) {
        parentPop = new Member[popSize];
        childPop = new Member[popSize];
        random = new Random();
        this.popSize = popSize;
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

    /*
    This method evolves until the worst error of the best member is below
    a certain threshold, OR until a certain number of generations are reached,
    whichever happens first. This method is for experimenting with ideas
    such as boosting mutation at certain generations, or resetting the population
    each time the evolve method is called.
     */
    public void evolve() {
        mutRate = 0.005;
        // Try initialising the whole population each time evolve is called
        initPop(popSize, chromLength);
        int genNum = 0;
        assess(parentPop);
        double worstError = getWorstError();
        while (worstError > 0.05) {
            // Boost mutation every n generations
            if (genNum % 1000 == 0 && worstError > 0.2) {
                System.out.println("Boosting mutation");
                mutRate = 0.5;
            } else {
                mutRate = 0.005;
            }
            for (int i = 0; i < parentPop.length; i++) {
                if (i < crossPoint) {
                    Member[] parents = select();
                    Member child = crossover(parents);
                    //Member child = crossoverParts(parents);
                    child.mutate(mutRate, random);
                    childPop[i] = child;
                } else {
                    childPop[i] = tourney();
                }
            }
            assess(childPop);
            parentPop = childPop;
            bestMember = bestMember();
            errors = getMemberErrors(bestMember);
            worstError = 0.0;
            worstErrorIndex = Integer.MAX_VALUE;
            for (int j = 0; j < errors.length; j++) {
                if (errors[j] > worstError) {
                    worstError = errors[j];
                    worstErrorIndex = j;
                }
            }
            Objective.resetWeights();
            if (worstErrorIndex < errors.length) {
                Objective.setWeight(worstErrorIndex);
            }
            genNum++;
            if (genNum > 4000) {
                System.out.println("Max gens reached");
                break;
            }
        }
        bestMember = bestMember();
    }

    /*
    This method evolves until genNum generations have been reached.
    This was the original evolve method I was using. I'm now experimenting
    with the evolve method above which currently takes no arguments.
     */
    public void evolve(int genNum) {
        initPop(popSize, chromLength);
        errorsPerGen = new double[7][genNum];
        // Assess fitness of parent population
        assess(parentPop);
        // Iterate over population to perform selection and crossover
        for (int i = 0; i < genNum; i++) {
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
            // Find best Member in population
            bestMember = bestMember();
            // Get error values of the best Member
            errors = getMemberErrors(bestMember);
            // Find the worst error
            worstError = 0.0;
            worstErrorIndex = Integer.MAX_VALUE;
            for (int j = 0; j < errors.length; j++) {
                if (errors[j] > worstError) {
                    worstError = errors[j];
                    worstErrorIndex = j;
                }
            }
            // Update errors in Objective class
            Objective.resetWeights();
            if (worstErrorIndex < errors.length) {
                Objective.setWeight(worstErrorIndex);
            }
            // Add errors for each feature to errorsPerGen
            for (int j = 0; j < errors.length; j++) {
                errorsPerGen[j][i] = errors[j];
            }
        }
        // Set population's best Member
        bestMember = bestMember();
        // Write data to file
        // Need to decide what data I want to write
        String desiredValues = Objective.getDesiredValues();
        String errors = Objective.getErrorsAsString();
        FileHandler.writeDataToFile(desiredValues, errors, OUTPUT_FILENAME);
        FileHandler.writeLogToFile(errorsPerGen, LOG_FILENAME);
    }

    private double getWorstError() {
        errors = getMemberErrors(bestMember());
        worstError = 0.0;
        worstErrorIndex = Integer.MAX_VALUE;
        for (int i = 0; i < errors.length; i++) {
            if (errors[i] > worstError) {
                worstError = errors[i];
                worstErrorIndex = i;
            }
        }
        return worstError;
    }

    private void assess(Member[] pop) {
        // Assess fitness of each Member
        for (Member m: pop) {
            Objective.assess(m, false, false);
        }
    }

    private Member tourney() {
        // Return winner of a tournament
        int a = random.nextInt(parentPop.length);
        int b = random.nextInt(parentPop.length);
        if (parentPop[a].getCost() < parentPop[b].getCost()) {
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

    /*
    This is a standard crossover method that takes the LHS
    from one parent and the RHS from the other.
    I think it would be worth trying a different crossover method,
    that splices based on parts (rows), for example, taking 2 rows from
    one parent and 2 rows from the other. This would be more musically meaningful
     */
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

    /*
    This method will perform a domain-specific crossover,
    whereby parts (rows) will remain intact, and the combination of parts
    will form the child
     */
    private Member crossoverParts(Member[] parents) {
        Member child = new Member();
        int[][] chromA = parents[0].getDna().getChromosome();
        int[][] chromB = parents[1].getDna().getChromosome();
        int[][] childChrom = new int[4][chromLength];
        // Set rows in child from parents, need to figure out midpoint
        for (int i = 0; i < childChrom.length; i++) {
            int midpoint = random.nextInt(4);
            if (i < midpoint) {
                childChrom[i] = chromA[i];
            } else {
                childChrom[i] = chromB[i];
            }
        }
        child.setDna(new DNA(childChrom));
        return child;
    }

    // Returns the Member with the lowest cost
    public Member bestMember() {
        double bestCost = Double.MAX_VALUE;
        Member bestMember = null;
        for (Member m: parentPop) {
            Double cost = m.getCost();
            if (cost < bestCost) {
                bestCost = cost;
                bestMember = m;
            }
        }
        return bestMember;
    }

    // Return the error values for all features of a particular Member
    public double[] getMemberErrors(Member member) {
        Objective.assess(member, false, true);
        return Objective.getErrors();
    }

    public void printMember(Member member) {
        // Print error values of Member
        Objective.assess(member, true, false);
        // Print chromosome of Member
        int[][] chrom = member.getDna().getChromosome();
        for (int i = 0; i < chrom.length; i++) {
            for (int j = 0; j < chrom[i].length; j++) {
                System.out.print(chrom[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Cost: " + member.getCost());
    }

    public Member breakMember(Member member) {
        int[][] chrom = member.getDna().getChromosome();
        // Choose random row to replace
        int rowToBreak = random.nextInt(member.getDna().getChromosome().length);
        int[] brokenRow = Utility.initRow(chromLength);
        // Replace row
        chrom[rowToBreak] = brokenRow;
        member.setDna(new DNA(chrom));
        return member;
    }

    public Member getBestMember() {
        return bestMember;
    }
}
