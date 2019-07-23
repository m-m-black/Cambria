public class Objective {

    private double[] desiredValues;
    private double[] actualValues;

    public Objective() {
        actualValues = new double[6];
    }

    public static void assess(Member member) {
        // This is our fitness function
    }

    public void setDesiredValues(double[] desiredValues) {
        this.desiredValues = desiredValues;
    }
}
