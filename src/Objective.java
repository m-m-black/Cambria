/*
    Ordering of feature values is as follows:
    1. Hocket       actualValues[0]
    2. Density      actualValues[1]
    3. Syncopation  actualValues[2]
    4. Balance      actualValues[3]
    5. Downbeat     actualValues[4]
    6. Backbeat     actualValues[5]
    6. Spread       actualValues[6]
 */

public class Objective {

    // Variables for feature values
    private static double[] desiredValues;
    private static double[] actualValues;

    // Variables to analyse the Member
    private static double totalNotes;
    private static double hocketNotes;
    private static double syncNotes;
    private static double firstHalfNotes;
    private static double secondHalfNotes;
    private static double downbeats;
    private static double backbeats;
    private static double backbeatCount;
    private static double backbeatScore;
    private static double firstHalfDensity;
    private static double secondHalfDensity;
    private static double spread;
    private static double maxPart;
    private static double subMaxPart;
    private static double minPart;
    private static double subMinPart;
    private static double[] partCounts;
    private static boolean[] usedParts;
    private static int noteCounter;

    // Error variables
    private static double[] errors;
    private static double hocketError;
    private static double densityError;
    private static double syncopationError;
    private static double balanceError;
    private static double downbeatError;
    private static double backbeatError;
    private static double spreadError;

    // Weight variables
    private static double[] weights;
    private static double hocketWeight;
    private static double densityWeight;
    private static double syncopationWeight;
    private static double balanceWeight;
    private static double downbeatWeight;
    private static double backbeatWeight;
    private static double spreadWeight;

    public Objective() {
        desiredValues = new double[7];
        actualValues = new double[7];
        weights = new double[7];
        errors = new double[7];
        // Part count array length set to number of rows in chromosome
        partCounts = new double[4];
        usedParts = new boolean[4];
        resetCounters();
        resetErrors();
        resetWeights();
    }

    // The actual objective function called on a Member
    public static void assess(Member member, boolean print, boolean setErrors) {
        int[][] chrom = member.getDna().getChromosome();
        int rows = chrom.length;
        // Reset all analysis variables and errors to 0
        resetCounters();
        resetErrors();
        // Calculate analysis variables
        for (int i = 0; i < chrom[0].length; i++) {
            // Calculate number of notes per part and per beat
            for (int j = 0; j < rows; j++) {
                if (chrom[j][i] == 1) {
                    // Increment part counter
                    partCounts[j] += 1.0;
                    // Increment beat counter
                    noteCounter++;
                }
            }
            incrementCounters(i, noteCounter, chrom);
            // Reset noteCounter to 0 for next beat
            noteCounter = 0;
        }
        checkEmphasisBeats(chrom);
        // Calculate actual values
        calcActual(chrom[0].length);
        // Calculate error between desired and actual values
        double cost = calcCost(print, setErrors);
        // Set Member's cost
        member.setCost(cost);
    }

    // Calculate cost based on error between desired and actual feature values
    private static double calcCost(boolean print, boolean setErrors) {
        hocketWeight = weights[0];
        densityWeight = weights[1];
        syncopationWeight = weights[2];
        balanceWeight = weights[3];
        downbeatWeight = weights[4];
        backbeatWeight = weights[5];
        spreadWeight = weights[6];
        hocketError = Math.abs(desiredValues[0] - actualValues[0]);
        densityError = Math.abs(desiredValues[1] - actualValues[1]);
        syncopationError = Math.abs(desiredValues[2] - actualValues[2]);
        balanceError = Math.abs(desiredValues[3] - actualValues[3]);
        downbeatError = Math.abs(desiredValues[4] - actualValues[4]);
        backbeatError = Math.abs(desiredValues[5] - actualValues[5]);
        spreadError = Math.abs(desiredValues[6] - actualValues[6]);
        if (print) {
            System.out.println("Hocket error: " + hocketError);
            System.out.println("Density error: " + densityError);
            System.out.println("Syncopation error: " + syncopationError);
            System.out.println("Balance error: " + balanceError);
            System.out.println("Downbeat error: " + downbeatError);
            System.out.println("Backbeat error: " + backbeatError);
            System.out.println("Spread error: " + spreadError);
        }
        if (setErrors) {
            errors[0] = hocketError;
            errors[1] = densityError;
            errors[2] = syncopationError;
            errors[3] = balanceError;
            errors[4] = downbeatError;
            errors[5] = backbeatError;
            errors[6] = spreadError;
        }
        return ((hocketWeight * hocketError) + (densityWeight * densityError) +
                (syncopationWeight * syncopationError) +
                (balanceWeight * balanceError) + (downbeatWeight * downbeatError) +
                (backbeatWeight * backbeatError) + (spreadWeight * spreadError));
    }

    // Calculate actual feature values
    private static void calcActual(int length) {
        // Hocket
        actualValues[0] = hocketNotes / totalNotes;
        // Density
        actualValues[1] = totalNotes / length;
        // Syncopation
        actualValues[2] = syncNotes / totalNotes;
        // Balance
        firstHalfDensity = firstHalfNotes / (length / 2);
        secondHalfDensity = secondHalfNotes / (length / 2);
        if (firstHalfDensity < secondHalfDensity) {
            actualValues[3] = firstHalfDensity / secondHalfDensity;
        } else if (firstHalfDensity > secondHalfDensity) {
            actualValues[3] = secondHalfDensity / firstHalfDensity;
        } else {
            actualValues[3] = 1;
        }
        // Downbeat
        actualValues[4] = downbeats;
        // Backbeat
        actualValues[5] = backbeats;
        // Spread
        spread = calcSpread();
        actualValues[6] = spread;
    }

    // Increment counter variables used to calculate actual feature values
    private static void incrementCounters(int i, int noteCounter, int[][] chrom) {
        // Calculate total beats with at least 1 note
        if (noteCounter > 0) {
            totalNotes++;
        }
        // Calculate hocket notes (beats with exactly 1 note)
        if (noteCounter == 1) {
            hocketNotes++;
        }
        // Calculate syncopated notes (off-beats with notes)
        if (i % 2 != 0 && noteCounter > 0) {
            syncNotes++;
        }
        // Calculate total notes in first half
        if (i < (chrom[0].length / 2) && noteCounter > 0) {
            firstHalfNotes++;
        }
        // Calculate total notes in second half
        if (i >= (chrom[0].length / 2) && noteCounter > 0) {
            secondHalfNotes++;
        }
    }

    // Check if downbeats and backbeats are active
    private static void checkEmphasisBeats(int[][] chrom) {
        // Calculate downbeats
        if (chrom[0][0] == 1) {
            downbeats = 1;
        }
        // Calculate backbeats
        for (int i = 0; i < chrom[1].length; i++) {
            if (chrom[1][i] == 1) {
                // Count total number of snare hits
                backbeatCount++;
            }
        }
        // If both backbeats are active
        if (chrom[1][4] == 1 && chrom[1][12] == 1) {
            // Set highest possible score
            backbeatScore = (double) chrom[1].length;
            // Decrement by total number of other snare hits
            backbeatScore -= (backbeatCount - 2);
        }
        // If only 1 backbeat is active
        if (chrom[1][4] == 1 ^ chrom[1][12] ==1) {
            // Set middle score
            backbeatScore = (double) (chrom[1].length) / 2.0;
        }
        // If no backbeats are active
        if (chrom[1][4] == 0 && chrom[1][12] == 0) {
            // Set lowest possible score
            backbeatScore = 0.0;
        }
        // Scale backbeat score to range of 0.0 - 1.0
        backbeats = Utility.map(backbeatScore, 0.0, chrom[1].length, 0.0, 1.0);
    }

    // Calculate ratio of lowest to highest part count
    private static double calcSpread() {
        int minIndex = 0;
        int maxIndex = 0;
        // Find min and max part counts
        for (int i = 0; i < partCounts.length; i++) {
            if (partCounts[i] > maxPart) {
                maxPart = partCounts[i];
                maxIndex = i;
            }
            if (partCounts[i] < minPart) {
                minPart = partCounts[i];
                minIndex = i;
            }
        }
        // Set min and max part counts to "used"
        usedParts[maxIndex] = true;
        usedParts[minIndex] = true;
        // Find new min and max parts
        for (int i = 0; i < partCounts.length; i++) {
            if (partCounts[i] <= maxPart && partCounts[i] > subMaxPart && !usedParts[i]) {
                subMaxPart = partCounts[i];
            }
            if (partCounts[i] >= minPart && partCounts[i] < subMinPart && !usedParts[i]) {
                subMinPart = partCounts[i];
            }
        }
        return (minPart + subMinPart) / (maxPart + subMaxPart);
    }

    // Sets all analysis variables to 0.0
    private static void resetCounters() {
        totalNotes = 0.0;
        hocketNotes = 0.0;
        syncNotes = 0.0;
        firstHalfNotes = 0.0;
        secondHalfNotes = 0.0;
        downbeats = 0.0;
        backbeats = 0.0;
        backbeatCount = 0.0;
        backbeatScore = 0.0;
        firstHalfDensity = 0.0;
        secondHalfDensity = 0.0;
        spread = 0.0;
        maxPart = 0.0;
        subMaxPart = 0.0;
        minPart = Double.MAX_VALUE;
        subMinPart = Double.MAX_VALUE;
        for (int i = 0; i < partCounts.length; i++) {
            partCounts[i] = 0.0;
        }
        for (int i = 0; i < usedParts.length; i++) {
            usedParts[i] = false;
        }
        noteCounter = 0;
    }

    // Sets all error values to 0.0
    private static void resetErrors() {
        hocketError = 0.0;
        densityError = 0.0;
        syncopationError = 0.0;
        balanceError = 0.0;
        downbeatError = 0.0;
        backbeatError = 0.0;
    }

    // Sets all weight values to 1.0
    public static void resetWeights() {
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1.0;
        }
    }

    // Set specified weight value to 2.0
    public static void setWeight(int i) {
        weights[i] = 2.0;
    }

    // Return desired values to ControlSystem
    public String getDesiredValues() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Hocket: " + desiredValues[0]);
        buffer.append("\nDensity: " + desiredValues[1]);
        buffer.append("\nSyncopation: " + desiredValues[2]);
        buffer.append("\nBalance: " + desiredValues[3]);
        buffer.append("\nDownbeat: " + desiredValues[4]);
        buffer.append("\nBackbeat: " + desiredValues[5]);
        buffer.append("\nSpread: " + desiredValues[6]);
        return buffer.toString();
    }

    // Return list of error values
    public static double[] getErrors() {
        return errors;
    }

    public double getHocket() {
        return desiredValues[0];
    }

    public void setHocket(double v) {
        desiredValues[0] = v;
    }

    public double getDensity() {
        return desiredValues[1];
    }

    public void setDensity(double v) {
        desiredValues[1] = v;
    }

    public double getSyncopation() {
        return desiredValues[2];
    }

    public void setSyncopation(double v) {
        desiredValues[2] = v;
    }

    public double getBalance() {
        return desiredValues[3];
    }

    public void setBalance(double v) {
        desiredValues[3] = v;
    }

    public double getDownbeat() {
        return desiredValues[4];
    }

    public void setDownbeat(double v) {
        desiredValues[4] = v;
    }

    public double getBackbeat() {
        return desiredValues[5];
    }

    public void setBackbeat(double v) {
        desiredValues[5] = v;
    }

    public double getSpread() {
        return desiredValues[6];
    }

    public void setSpread(double v) {
        desiredValues[6] = v;
    }
}
