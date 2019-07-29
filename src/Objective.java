/*
    Ordering of feature values is as follows:
    1. Hocket       actualValues[0]
    2. Density      actualValues[1]
    3. Syncopation  actualValues[2]
    4. Balance      actualValues[3]
    5. Downbeat     actualValues[4]
    6. Backbeat     actualValues[5]
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
    private static double firstHalfDensity;
    private static double secondHalfDensity;
    private static int noteCounter;

    // Error variables
    private static double hocketError;
    private static double densityError;
    private static double syncopationError;
    private static double balanceError;
    private static double downbeatError;
    private static double backbeatError;

    public Objective() {
        desiredValues = new double[6];
        actualValues = new double[6];
        resetCounters();
        resetErrors();
    }

    // The actual objective function called on a Member
    public static void assess(Member member) {
        int[][] chrom = member.getDna().getChromosome();
        int rows = chrom.length;
        // Reset all analysis variables and errors to 0
        resetCounters();
        resetErrors();
        // Calculate analysis variables
        for (int i = 0; i < chrom[0].length; i++) {
            // Calculate number of notes per beat
            for (int j = 0; j < rows; j++) {
                if (chrom[j][i] == 1) {
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
        double cost = calcCost();
        // Set Member's cost
        member.setCost(cost);
    }

    // Calculate cost based on error between desired and actual feature values
    private static double calcCost() {
        hocketError = Math.abs(desiredValues[0] - actualValues[0]);
        densityError = Math.abs(desiredValues[1] - actualValues[1]);
        syncopationError = Math.abs(desiredValues[2] - actualValues[2]);
        balanceError = Math.abs(desiredValues[3] - actualValues[3]);
        downbeatError = Math.abs(desiredValues[4] - actualValues[4]);
        backbeatError = Math.abs(desiredValues[5] - actualValues[5]);
        return (hocketError + densityError + syncopationError + balanceError +
                downbeatError + backbeatError);
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
        if (chrom[1][4] == 1 && chrom[1][12] == 1) {
            backbeats = 1;
        }
        if (chrom[1][4] == 1 ^ chrom[1][12] == 1) {
            backbeats = 0.5;
        }
    }

    // Sets all analysis variables to 0
    private static void resetCounters() {
        totalNotes = 0.0;
        hocketNotes = 0.0;
        syncNotes = 0.0;
        firstHalfNotes = 0.0;
        secondHalfNotes = 0.0;
        downbeats = 0.0;
        backbeats = 0.0;
        firstHalfDensity = 0.0;
        secondHalfDensity = 0.0;
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

    // Return desired values to ControlSystem
    public String getDesiredValues() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Hocket: " + desiredValues[0]);
        buffer.append("\nDensity: " + desiredValues[1]);
        buffer.append("\nSyncopation: " + desiredValues[2]);
        buffer.append("\nBalance: " + desiredValues[3]);
        buffer.append("\nDownbeat: " + desiredValues[4]);
        buffer.append("\nBackbeat: " + desiredValues[5]);
        return buffer.toString();
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
}
