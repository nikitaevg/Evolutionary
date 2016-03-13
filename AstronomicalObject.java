class AstronomicalObject {
    private double y[];
    private double dy[];
    private double m;
    public AstronomicalObject() {

    }
    public AstronomicalObject(double[] y, double[] dy, double m) {
        this.y = y;
        this.dy = dy;
        this.m = m;
    }
    public double[] getY() {
        return y;
    }
    public double[] getDy() {
        return dy;
    }
    public double getM() {
        return m;
    }
    public void setY(double[] y) {
        this.y = y;
    }
    public void setDy(double[] dy) {
        this.dy = dy;
    }
}

