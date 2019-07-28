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

    private double[] desiredValues;
    private static double[] actualValues;

    // Variables to analyse the Member
    private static int totalNotes;
    private static int hocketNotes;
    private static int syncNotes;
    private static int firstHalfNotes;
    private static int secondHalfNotes;
    private static int downbeats;
    private static double backbeats;
    private static int noteCounter;
    private static double firstHalfDensity;
    private static double secondHalfDensity;

    public Objective() {
        actualValues = new double[6];
        resetCounters();
    }

    public static void assess(Member member) {
        int[][] chrom = member.getDna().getChromosome();
        int rows = chrom.length;
        // Reset all analysis variables to 0
        resetCounters();
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

    public void setDesiredValues(double[] desiredValues) {
        this.desiredValues = desiredValues;
    }

    private static double calcCost() {
        return 0;
    }

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
        totalNotes = 0;
        hocketNotes = 0;
        syncNotes = 0;
        firstHalfNotes = 0;
        secondHalfNotes = 0;
        downbeats = 0;
        backbeats = 0;
        noteCounter = 0;
        firstHalfDensity = 0;
        secondHalfDensity = 0;
    }
}
