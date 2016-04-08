package Simulation;

class AstronomicalObject {
    Vect y, dy;
    private double r;
    private double m;
    public AstronomicalObject() {

    }
    AstronomicalObject(Vect y, Vect dy, double m, double r) {
        this.y = y.copy();
        this.dy = dy.copy();
        this.m = m;
        this.r = r;
    }
    Vect getY() {
        return y;
    }
    Vect getDy() {
        return dy;
    }
    double getM() {
        return m;
    }
    public double getR() {
        return r;
    }
    public void setY(Vect y) {
        this.y = y;
    }
    public void setY(double x, double y, double z) {
        if (this.y == null) {
            this.y = new Vect(x, y, z);
        }
        this.y.x = x;
        this.y.y = y;
        this.y.z = z;
    }
    public void setDy(double x, double y, double z) {
        if (this.dy == null) {
            this.dy = new Vect(x, y, z);
        }
        this.dy.x = x;
        this.dy.y = y;
        this.dy.z = z;
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
    public boolean clash(Vect sp) {
        double dist = sp.sub(y).length();
        return (dist - r) < 0;
    }
}

