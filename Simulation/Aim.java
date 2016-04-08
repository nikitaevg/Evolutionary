package Simulation;

class Aim {
    private int DIM = 3;
    private double error = 0.002 * Math.PI;
    private double x, y, z;
    Aim(double theta, double fi) {
        x = Math.sin(theta) * Math.cos(fi) * 1e2;
        y = Math.sin(theta) * Math.sin(fi) * 1e2;
        z = Math.cos(theta) * 1e2;
    }
    boolean satisf(Sputnik a, Sputnik b, Sputnik c) {
        Vect mul = (b.getY().sub(a.getY())).mul(c.getY().sub(a.getY()));
        Vect aim = new Vect(this.x, this.y, this.z);
        return (Math.abs(aim.angBetw(mul)) < error || Math.abs(aim.angBetw(mul.mulA(-1))) < error);
    }
}