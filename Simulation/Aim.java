package Simulation;

class Aim {
    private static int DIM = 3;
    private static double error = 0.002 * Math.PI;
    private double x, y, z;
    Aim(double fi, double theta) {
        x = Math.sin(theta) * Math.cos(fi) * 1e0;
        y = Math.sin(theta) * Math.sin(fi) * 1e0;
        z = Math.cos(theta) * 1e0;
    }
    double getX() {
        return x;
    }
    double getY() {
        return y;
    }
    double getZ() {
        return z;
    }
    boolean satisf(Sputnik a, Sputnik b, Sputnik c) {
        Vect mul = (b.getY().sub(a.getY())).mulVect(c.getY().sub(a.getY()));
        Vect aim = new Vect(this.x, this.y, this.z);
        return (Math.abs(aim.angBetw(mul)) < error || Math.abs(aim.angBetw(mul.mulA(-1))) < error);
    }
}