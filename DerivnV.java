import flanagan.integration.DerivnFunction;

class DerivnV implements DerivnFunction {
    private AstronomicalObject[] ao;
    int n, dim;
    static double G = 6.67428 * 1e-11;
    public double[] derivn(double x, double[ ] y){
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
        }
        return dydx;
    }
    public void createUn(AstronomicalObject[] ao, int n, int dim) {
        this.ao = new AstronomicalObject[n];
        this.n = n;
        this.dim = dim;
        System.arraycopy(ao, 0, this.ao, 0, n);
    }


}
