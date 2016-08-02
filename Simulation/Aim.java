package Simulation;

class Aim {
//    private static int DIM = 3;
//    private static double error = 0.002 * Math.PI;
    private double x, y, z;
    Aim(double fi, double theta) {
        x = Math.sin(theta) * Math.cos(fi);
        y = Math.sin(theta) * Math.sin(fi);
        z = Math.cos(theta);
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
//    boolean satisf(Sputnik a, Sputnik b, Sputnik c) {
//        Vect mul = (b.getY().sub(a.getY())).mulVect(c.getY().sub(a.getY()));
//        Vect aim = new Vect(this.x, this.y, this.z);
//        return Math.abs(aim.angBetweenLines(mul)) < error;
//    }
}