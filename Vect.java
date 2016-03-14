class Vect {
    public double x, y, z;
    public Vect(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public Vect(){

    }

    void fromArr(double[] a) {
        x = a[0];
        y = a[1];
        z = a[2];
    }

    Vect mul(Vect b) {
        return new Vect(y * b.z - z * b.y, z * b.x - x * b.z, x * b.y - y * b.x);
    }

    double size() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    Vect add(Vect a) {
        return new Vect(x + a.x, y + a.y, z + a.z);
    }

    Vect sub(Vect a) {
        return new Vect(x - a.x, y - a.y, z - a.z);
    }

    double[] toArray() {
        return new double[]{x, y, z};
    }

    double angBetw(Vect a) {
        return Math.acos((x * a.x + y * a.y + z * a.z) / (size() * a.size()));
    }
}