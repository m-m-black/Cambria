public class Objective {

    private double[] desiredValues;
    private double[] actualValues;

    // Variables to analyse the Member
    private static int totalNotes;
    private static int hocketNotes;
    private static int syncNotes;
    private static int firstHalfNotes;
    private static int secondHalfNotes;
    private static int downbeats;
    private static double backbeats;
    private static int noteCounter;

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
                if (chrom[i][j] == 1) {
                    noteCounter++;
                }
            }
            incrementCounters(i, noteCounter, chrom);
            // Reset noteCounter to 0 for next beat
            noteCounter = 0;
        }
        checkEmphasisBeats(chrom);
    }

    public void setDesiredValues(double[] desiredValues) {
        this.desiredValues = desiredValues;
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
    }
}
