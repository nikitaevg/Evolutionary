package Evolutionary;

import Simulation.Runge;
import Simulation.Vect;

public class Individual {
    public Vect[] startingPos;
    int n, time;
    public double value = -1e11;
    public Vect[] startingAcc;
    public Vect[][] accelerations;
    Individual(int n, int time) {
        startingAcc = new Vect[3];
        startingPos = new Vect[3];
        accelerations = new Vect[3][n - 2];
        this.n = n;
        this.time = time;
    }
    public Individual copy() {
        Individual ans = new Individual(n, time);
        for (int i = 0; i < 3; i++) {
            ans.startingPos[i] = startingPos[i].copy();
            ans.startingAcc[i] = startingAcc[i].copy();
        }
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < n - 2; i++)
                ans.accelerations[j][i] = accelerations[j][i].copy();
        return ans;
    }
    public double getValue() {
        if (value != -1e11)
            return value;
        return value = new Runge().run(startingPos, startingAcc, accelerations, false, true, time);
    }

}
