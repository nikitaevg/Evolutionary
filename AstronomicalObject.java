class AstronomicalObject {
    private Vect y, dy;
    private double m;
    public AstronomicalObject() {

    }
    public AstronomicalObject(Vect y, Vect dy, double m) {
        this.y = y;
        this.dy = dy;
        this.m = m;
    }
    public Vect getY() {
        return y;
    }
    public Vect getDy() {
        return dy;
    }
    public double getM() {
        return m;
    }
    public void setY(Vect y) {
        this.y = y;
    }
    public void setDy(Vect dy) {
        this.dy = dy;
    }
    public void setY(double[] y) {
        this.y.x = y[0];
        this.y.y = y[1];
        this.y.z = y[2];
    }
    public void setDy(double[] dy) {
        this.dy.x = dy[0];
        this.dy.y = dy[1];
        this.dy.z = dy[2];
    }
}

