package Evolutionary;

import Simulation.Vect;

class Individual {
    Vect[] startingPos;
    int n;
    Vect[] startingAcc;
    Vect[][] accelerations;
    Individual(int n) {
        startingAcc = new Vect[3];
        startingPos = new Vect[3];
        accelerations = new Vect[3][n - 2];
        this.n = n;
    }
    public Individual copy() {
        Individual ans = new Individual(n);
        for (int i = 0; i < 3; i++) {
            ans.startingPos[i] = startingPos[i].copy();
            ans.startingAcc[i] = startingAcc[i].copy();
        }
        for (int i = 0; i < n - 2; i++) {
            for (int j = 0; j < 3; j++)
                ans.accelerations[j][i] = accelerations[j][i].copy();
        }
        return ans;
    }
}
