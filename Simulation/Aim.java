package Simulation;

class Aim {
    private int DIM = 3;
    private double error = 0.0005 * Math.PI;
    private double theta, fi;
    Aim(double theta, double fi) {
        this.theta = theta;
        this.fi = fi;
    }
    boolean satisf(Sputnik a, Sputnik b, Sputnik c) {
        Vect x = b.getY().sub(a.getY());
        Vect y = c.getY().sub(a.getY());
        Vect mul = x.mul(y);
        Vect aim = new Vect(Math.sin(theta) * Math.cos(fi) * 1e5, Math.sin(theta) * Math.sin(fi) * 1e5, Math.cos(theta) * 1e5);
        return (Math.abs(aim.angBetw(mul)) < error);
    }
}