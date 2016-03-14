class Aim {
    private int DIM = 3;
    private double error = 0.05;
    double theta, fi;
    public Aim(double theta, double fi) {
        this.theta = theta;
        this.fi = fi;
    }
    public boolean satisf(Sputnik a, Sputnik b, Sputnik c) {
        Vect x = b.getY().sub(a.getY());
        Vect y = c.getY().sub(a.getY());
        Vect mul = x.mul(y);
        Vect aim = new Vect(Math.sin(theta) * Math.cos(fi), Math.sin(theta) * Math.sin(fi), Math.cos(theta));
        return (Math.abs(aim.angBetw(mul)) < error);
    }
}