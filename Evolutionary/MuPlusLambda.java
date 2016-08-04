package Evolutionary;

import Simulation.Vect;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.*;

public class MuPlusLambda extends AbstractEvo {
    void performAlgorithm(Individual[] individuals, int mu, int lambda) {
        ExecutorService es = Executors.newFixedThreadPool(mu + lambda + 1);
        for (int i = mu; i < lambda + mu; i++) {
            int t = (int)(Math.random() * (double)mu);
            individuals[i] = performAlgorithmOnInd(individuals[t]);
        }
        for (int i = 0; i < mu + lambda; i++) {
            es.execute(new Run(individuals[i]));
//            individuals[i].getValue();
//            new Run(individuals[i]).start();
        }
        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.DAYS); // wait 1 day for threads to terminate
        } catch (InterruptedException e) {
            write("Fuck up");
            e.printStackTrace();
        }
        for (int i = 0; i < 2; i++)
            write(((i == 1) ? "New" : "Old") + " individual:\n\tvalue = " + individuals[i].getValue().value +  "\n\tapproaches = " +
                individuals[i].getValue().approaches + "\n\tmax distances to Earth = " +
                individuals[i].getValue().maxDist[0] + ", " + individuals[i].getValue().maxDist[1] + ", " +
                    individuals[i].getValue().maxDist[2] + "\n\tphotos taken = " + individuals[i].getValue().photos);
        Arrays.sort(individuals, new Compare());
    }

    private static String func(int i) {
        return " of the " + (i + 1) + " sputnik was changed";
    }
    private static String func(int i, int j) {
        return " of the " + (i + 1) + " sputnik for the " + j + " day was changed";
    }

    private static String changes(int a, int b) {
        return a + " to " + b;
    }
    private static String changes(Vect a, Vect b) {
        return a.angleToString() + " to " + b.angleToString();
    }


    private Individual performAlgorithmOnInd(Individual individual) {
        int n = individual.n;
        Individual copy = individual.copy(false);
        double THRESHOLD = 1. / ((individual.accelerations[0].length) * 3), spTHRESHOLD = 1. / 12;
        for (int i = 0; i < 3; i++)
            if (Math.random() < spTHRESHOLD) {
                copy.startTime[i] = createRandomInt(0, secondsInMonth);
                write("Starting time" + func(i));
                write(changes(Math.abs(individual.startTime[i]), Math.abs(copy.startTime[i])));
            }
        for (int i = 0; i < 3; i++) {
            if (changeRandomAngle(ACC_LOWER, accUpper, copy.startingAcc[i], spTHRESHOLD)) {
                write("Starting acceleration vector" + func(i));
                write(changes(individual.startingAcc[i], copy.startingAcc[i]));
            }
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < n - 2; i++) {
                if (changeRandomAngle(0, ENG_UPPER, copy.accelerations[j][i], THRESHOLD)) {
                    write("Low thrust acceleration vector" + func(j, i));
                    write(changes(individual.accelerations[j][i], copy.accelerations[j][i]));
                }
            }
        }
        return copy;
    }
    private class Compare implements Comparator<Individual> {
        public int compare(Individual o1, Individual o2) {
            double a = o1.getValue().value;
            double b = o2.getValue().value;
            if (a < b) {
                return 1;
            } else if (a == b){
                return 0;
            } else {
                return -1;
            }
        }
    }
    private class Run implements Runnable {
        private Individual obj;
        Run (Individual obj) {
            this.obj = obj;
        }
        public void run() {
            obj.getValue();
        }
    }
}
