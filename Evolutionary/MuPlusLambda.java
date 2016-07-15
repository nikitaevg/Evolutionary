package Evolutionary;

import java.util.Arrays;
import java.util.Comparator;

public class MuPlusLambda extends AbstractEvo {
    double THRESHOLD = 0.1;
    void performAlgorithm(Individual[] individuals, int mu, int lambda) {

        for (int i = mu; i < lambda + mu; i++) {
            int t = (int)(Math.random() * mu);
            individuals[i] = performAlgorithmOnInd(individuals[t]);
        }
        for (int i = 0; i < mu + lambda; i++) {
            new Run(individuals[i]).start();
//            individuals[i].getValue();
        }
        Arrays.sort(individuals, new Compare());
    }
    private Individual performAlgorithmOnInd(Individual individual) {
        int n = individual.n;
        Individual copy = individual.copy();
        for (int i = 0; i < 3; i++) {
            if (Math.random() < THRESHOLD) {
                copy.startingPos[i] = createRandomVect(posLower, posUpper);
            }
            if (Math.random() < THRESHOLD) {
                copy.startingAcc[i] = createRandomVect(accLower, accUpper);
            }
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < n - 2; i++) {
                if (Math.random() < THRESHOLD) {
                    copy.accelerations[j][i] = createRandomVect(0, engUpper);
                }
            }
        }
        return copy;
    }
    public class Compare implements Comparator<Individual> {
        public int compare(Individual o1, Individual o2) {
            double a = o1.getValue();
            double b = o2.getValue();
            if (a < b) {
                return 1;
            } else if (a == b){
                return 0;
            } else {
                return -1;
            }
        }
    }
    class Run implements Runnable {
        private Thread t;
        private Individual obj;
        Run (Individual obj) {
            this.obj = obj;
        }
        public void run() {
            obj.getValue();
        }
        public void start() {
            if (t == null) {
                t = new Thread(this);
                t.start();
            }
        }
    }
}
