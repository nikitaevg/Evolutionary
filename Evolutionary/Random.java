package Evolutionary;

import Simulation.Vect;

public class Random {
    double R = 6371000, GEO = 35786000;
    double posUpper = R + GEO * 3, posLower = R + GEO * 1.5, accUpper = 1300, accLower = 900, engUpper = 0.1;
    public Individual createRandomIndividual(int n, int time) {
        Individual ans = new Individual(n, time);
        for (int i = 0; i < 3; i++) {
            ans.startingAcc[i] = createRandomVect(accLower, accUpper);
            ans.startingPos[i] = createRandomVect(posLower, posUpper);
        }
        for (int i = 0; i < n - 2; i++) {
            for (int j = 0; j < 3; j++) {
                ans.accelerations[j][i] = createRandomVect(0, engUpper);
            }
        }
        return ans;
    }
    Vect createRandomVect(double lowerBound, double upperBound) {
        return new Vect(createRandomDouble(upperBound, lowerBound),
                createRandomDouble(upperBound, lowerBound),
                createRandomDouble(upperBound, lowerBound));
    }
    double createRandomDouble(double upperBound, double lowerBound) {
        double a = Math.random() * (upperBound - lowerBound) + lowerBound;
        if (Math.random() > 0.5)
            a = -a;
        return a;
    }
}
