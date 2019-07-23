public class Objective {

    private double[] desiredValues;

    public Objective(double[] desiredValues) {
        this.desiredValues = desiredValues;
    }

    public static void assess(Member member) {
        // This is our fitness function
    }

    public void setDesiredValues(double[] desiredValues) {
        this.desiredValues = desiredValues;
    }
}
