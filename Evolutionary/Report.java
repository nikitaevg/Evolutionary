package Evolutionary;

public class Report {
    double value;
    int photos;
    double[] maxDist;
    int approaches; // approaches to the Earth
    public Report (double value, double[] maxDist, int approaches, int photos) {
        this.photos = photos;
        this.value = value;
        this.maxDist = new double[3];
        System.arraycopy(maxDist, 0, this.maxDist, 0, maxDist.length);
        this.approaches = approaches;
    }
}
