package Simulation;

class AstronomicalObject {
    private Vect y, dy; // y -- coordinates of AstronomicalObjects (x, y, z), dy -- speed of AO.
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
    void setM(double m) {
        this.m = m;
    }
    double getR() {
        return r;
    }
    void setY(Vect y) {
        this.y = y;
    }
    void setY(double x, double y, double z) {
        if (this.y == null) {
            this.y = new Vect(x, y, z);
        }
        this.y.fromArgs(x, y, z);
    }
    void setDy(double x, double y, double z) {
        if (this.dy == null) {
            this.dy = new Vect(x, y, z);
        }
        this.dy.fromArgs(x, y, z);
    }
    void addDy(Vect x) {
        this.dy = this.dy.add(x);
    }
    void setDy(Vect dy) {
        this.dy = dy;
    }
    void setY(double[] y) {
        this.y.fromArgs(y[0], y[1], y[2]);
    }
    void setDy(double[] dy) {
        this.dy.fromArgs(dy[0], dy[1], dy[2]);
    }
    boolean clash(Vect sp) {
        double dist = sp.sub(y).length();
        return (dist - r) < 0;
    }
}

