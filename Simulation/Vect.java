package Simulation;

public class Vect {
    private double x, y, z;
    private double xd, yd, zd;
    public Vect(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        updateXD();
    }
    public Vect(double[] a) {
        this.x = a[0];
        this.y = a[1];
        this.z = a[2];
        updateXD();
    }
    public Vect(){

    }

    private void updateXD() {
        double l = length();
        xd = x / l;
        yd = y / l;
        zd = z / l;
    }

//    void fromArr(double[] a) {
//        x = a[0];
//        y = a[1];
//        z = a[2];
//        updateXD();
//    }

    public void fromArgs(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        updateXD();
    }

    Vect mulVect(Vect b) {
        return new Vect(y * b.z - z * b.y, z * b.x - x * b.z, x * b.y - y * b.x);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    Vect add(Vect a) {
        return new Vect(x + a.x, y + a.y, z + a.z);
    }

    Vect sub(Vect a) {
        return new Vect(x - a.x, y - a.y, z - a.z);
    }

    private double sqLength() {
        return x * x + y * y + z * z;
    }

    void setNull() {
        x = 0;
        y = 0;
        z = 0;
        updateXD();
    }

    Vect mulA(double a) {
        x *= a;
        y *= a;
        z *= a;
        return this;
    }

    void toArray(double[] a) {
        a[0] = x;
        a[1] = y;
        a[2] = z;
    }

    double angBetween(Vect a) {
//        return Math.acos((x * a.x + y * a.y + z * a.z) / Math.sqrt(sqLength() * a.sqLength()));
        return Math.acos(xd * a.xd + yd * a.yd + zd * a.zd);
    }


    double angBetweenLines(Vect a) {
        double t = Math.acos(xd * a.xd + yd * a.yd + zd * a.zd);
        return Math.min(Math.abs(t), Math.abs(t - Math.PI));
    }

    public Vect copy() {
        return new Vect(x, y, z);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String toString() {
        return "x = " + x + ", y = " + y + ", z = " + z;
    }

    public String angleToString() {
        return "fi = " + x + ", theta = " + y + ", r = " + z;
    }
}