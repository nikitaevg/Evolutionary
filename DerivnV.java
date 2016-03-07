import flanagan.integration.DerivnFunction;

class DerivnV implements DerivnFunction {
    private AstronomicalObject[] ao;
    int n, dim;
    static double G = 6.67428 * 1e-11;
    public double[] derivn(double x, double[ ] y){
        double[ ] dydx = new double [y.length];
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < dim; j++) {
                dydx[j + i * dim * 2] = y[i * dim * 2 + j + dim];
            }
            double dr, alpha;
            double[] aoY;
            for (AstronomicalObject anAo : ao) {
                if (anAo == ao[i])
                    continue;
                aoY = anAo.getY();
                dr = 0;
                alpha = Math.atan2(y[i * 2 * dim + 1] - aoY[1], y[i * 2 * dim] - aoY[0]);
                for (int j = 0; j < dim; j++) {
                    dr += Math.pow((y[i * 2 * dim + j] - aoY[j]), 2);
                }
                dydx[i * 2 * dim + dim] -= G * anAo.getM() / dr * Math.cos(alpha);
                dydx[i * 2 * dim + dim + 1] -= G * anAo.getM() / dr * Math.sin(alpha);
            }
        }
        if (Math.abs(x - 3000) < 1) {
            dydx[2] += 4000;
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
