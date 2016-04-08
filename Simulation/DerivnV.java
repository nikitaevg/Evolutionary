package Simulation;

import flanagan.integration.DerivnFunction;

class DerivnV implements DerivnFunction {
    private AstronomicalObject[] ao;
    private int n, dim;
    static double G = 6.67428 * 1e-11;
    private Vect[] accel;
    public double[] derivn(double x, double[] y){
        double[] dydx = new double [y.length];
        double[] vector;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < dim; j++) {
                dydx[j + i * dim * 2] = y[i * dim * 2 + j + dim];
            }
            double dr;
            double[] aoY;
            for (AstronomicalObject anAo : ao) {
                if (anAo == ao[i])
                    continue;
                aoY = anAo.getY().toArray();
                dr = 0;
                for (int j = 0; j < dim; j++) {
                    dr += Math.pow((y[i * 2 * dim + j] - aoY[j]), 2);
                }
                vector = new double[3];
                for (int j = 0; j < dim; j++) {
                    vector[j] = (y[i * 2 * dim + j] - aoY[j]) / Math.sqrt(dr);
                }
                for (int j = 0; j < dim; j++) {
                    dydx[i * 2 * dim + dim + j] -= G * anAo.getM() / dr * vector[j];
                }
            }
            dydx[i * 2 * dim + dim] += accel[i].x / ao[i].getM();
            dydx[i * 2 * dim + dim + 1] += accel[i].y / ao[i].getM();
            dydx[i * 2 * dim + dim + 2] += accel[i].z / ao[i].getM();
        }
        return dydx;
    }
    void createUn(AstronomicalObject[] ao, int n, int dim) {
        this.ao = new AstronomicalObject[n];
        this.n = n;
        this.dim = dim;
        accel = new Vect[n];
        for (int i = 0; i < n; i++)
            accel[i] = new Vect(0, 0, 0);
        System.arraycopy(ao, 0, this.ao, 0, n);
    }

    void changeAcc(Vect a, Vect b, Vect c, int objects) {
        for (int i = 0; i < objects; i++)
            accel[i].setNull();
        accel[objects] = a;
        accel[objects + 1] = b;
        accel[objects + 2] = c;
    }

}
