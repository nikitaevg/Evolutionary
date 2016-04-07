package Evolutionary;

import Simulation.Runge;

public class OnePlusOne extends AbstractEvo{
    double THRESHOLD = 0.1;
    double performAlgorithm(Individual[] curr) {
        int n = curr.length;
        Individual[] copy = new Individual[n];
        copy[0] = curr[0].copy();
        for (int i = 0; i < 3; i++) {
            if (Math.random() < THRESHOLD) {
                copy[0].startingPos[i] = createRandomVect(posLower, posUpper);
            }
            if (Math.random() < THRESHOLD) {
                copy[0].startingAcc[i] = createRandomVect(accLower, accUpper);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                if (Math.random() < THRESHOLD) {
                    copy[0].accelerations[j][i] = createRandomVect(0, engUpper);
                }
            }
        }
        double a = Runge.run(curr[0].startingPos, curr[0].startingAcc, curr[0].accelerations);
        double b = Runge.run(copy[0].startingPos, copy[0].startingAcc, copy[0].accelerations);
        if (a > b) {
            return a;
        } else {
            curr = copy;
            return b;
        }
    }
}
