public class OccasionalCharacteristics {
    private final double M;
    private final double Mx2;

    public OccasionalCharacteristics(double Mx, double Mx2) {
        this.M = Mx;
        this.Mx2 = Mx2;
    }

    public double getMathematicalExpectation() {
        return M;
    }

    public double getMx2() {
        return Mx2;
    }

    public double getVariance() {
        return Mx2 - Math.pow(M, 2);
    }

    public double getDeviation() {
        return Math.sqrt(this.getVariance());
    }

    @Override
    public String toString() {
        return "M = " + M + "\n" + "D = " + this.getVariance() + "\n" + "V = " + this.getDeviation() + "\n";
    }
}
