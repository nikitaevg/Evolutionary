package Evolutionary;

import Simulation.Vect;

public class Random {
    private double R = 6371000, GEO = 35786000;
    double accUpper = 3000, accLower = 250, engUpper = 0.1;
    int secondsInMonth = 2678400;
    public Individual createRandomIndividual(int n, int time) {
        Individual ans = new Individual(n, time);
        for (int i = 0; i < 3; i++) {
            ans.startingAcc[i] = new Vect();
            changeRandomAngle(accLower, accUpper, ans.startingAcc[i], 1);
        }
        for (int i = 0; i < 3; i++)
            ans.startTime[i] = createRandomInt(0, secondsInMonth);
        for (int i = 0; i < n - 2; i++) {
            for (int j = 0; j < 3; j++) {
                ans.accelerations[j][i] = new Vect();
                changeRandomAngle(0, engUpper, ans.accelerations[j][i], 1);
            }
        }
        return ans;
    }
//    void createRandomVect(double lowerBound, double upperBound, Vect buff) {
//        buff.fromArgs(createRandomDouble(upperBound, lowerBound),
//                createRandomDouble(upperBound, lowerBound),
//                createRandomDouble(upperBound, lowerBound));
//    }

    int createRandomInt(int lowerBound, int upperBound) {
        return -(int)(Math.random() * (upperBound - lowerBound) + lowerBound);
    }
    private double createRandomDouble(double lowerBound, double upperBound) {
        return Math.random() * (upperBound - lowerBound) + lowerBound;
    }
    boolean changeRandomAngle(double lowerAcc, double upperAcc, Vect buff, double THRESHOLD) { // changes each coordinate of vector with probability THRESHOLD and returns whether it was changed
        double x = (THRESHOLD < Math.random()) ? buff.getX() : (createRandomDouble(0, Math.PI * 2));
        double y = (THRESHOLD < Math.random()) ? buff.getY() : createRandomDouble(0, Math.PI * 2);
        double z = (THRESHOLD < Math.random()) ? buff.getZ() : createRandomDouble(lowerAcc, upperAcc);
        boolean changed = (x != buff.getX()) || (y != buff.getY()) || (z != buff.getZ());
        buff.fromArgs(x, y, z);
        return changed;
    }

}
