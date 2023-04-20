public class OccasionalCharacteristics {
    private final double M;
    private final double Mx2;
    private final double D;
    private final double deviation;

    public OccasionalCharacteristics(double Mx, double Mx2) {
        this.M = Mx;
        this.Mx2 = Mx2;
        this.D = Mx2 - Math.pow(Mx, 2);
        this.deviation = Math.sqrt(D);
    }

    public double getMathematicalExpectation() {
        return M;
    }

    public double getMx2() {
        return Mx2;
    }

    public double getVariance() {
        return D;
    }

    public double getDeviation() {
        return deviation;
    }

    @Override
    public String toString() {
        return "M = " + M + "\n" + "D = " + D + "\n" + "V = " + deviation + "\n";
    }
}
