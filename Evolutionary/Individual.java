package Evolutionary;

import Simulation.Runge;
import static Evolutionary.AbstractEvo.write;
import Simulation.Vect;

public class Individual {
    int n;
    private int time;
    private Report value = new Report(-1e11, new double[]{0, 0, 0}, 0, 0);
    public int[] startTime;
    public Vect[] startingAcc;
    public Vect[][] accelerations;
    Individual(int n, int time) {
        startingAcc = new Vect[3];
        accelerations = new Vect[3][n - 2];
        startTime = new int[3];
        this.n = n;
        this.time = time;
    }
    Individual copy(boolean copyValue) {
        Individual ans = new Individual(n, time);
        for (int i = 0; i < 3; i++) {
            ans.startingAcc[i] = startingAcc[i].copy();
            ans.startTime[i] = startTime[i];
        }
        for (int j = 0; j < 3; j++)
            for (int i = 0; i < n - 2; i++)
                ans.accelerations[j][i] = accelerations[j][i].copy();
        if (copyValue)
            ans.value = value;
        return ans;
    }
    Report getValue() { // First time it gets the value by simulating and writes it to not to simulate it twice
        if (value.value != -1e11)
            return value;
        value = new Runge().run(startTime, sphToDek2(startingAcc), sphToDek3(accelerations), false, true, time);
        dekToSph3(accelerations);
        dekToSph2(startingAcc);
        return value;
    }

    void logInd() {
        StringBuilder sb;
        for (int i = 0; i < 3; i++) {
            sb = new StringBuilder();
            write((i + 1) + " sputnik:");
            write("\tStarting time:");
            write("\t\t" + Math.abs(startTime[i]));
            write("\tStarting acceleration");
            write("\t\t" + startingAcc[i].angleToString());
            write("\tLow thrust accelerations:");
            for (int j = 0; j < accelerations[i].length; j++) {
                sb.append("{").append(j + 1).append(":").append(accelerations[i][j].angleToString()).append("}");
            }
            write(sb.toString());
        }
    }

    // these functions convert from spherical to Cartesian and back. It has to be rewritten

    private static void sphToDek1(Vect a) {
        a.fromArgs(Math.sin(a.getX()) * Math.cos(a.getY()) * a.getZ(),
                Math.sin(a.getX()) * Math.sin(a.getY()) * a.getZ(), Math.cos(a.getX()) * a.getZ());
    }

    public static Vect[] sphToDek2(Vect[] a) {
        for (Vect anA : a) {
            sphToDek1(anA);
        }
        return a;
    }

    public static Vect[][] sphToDek3(Vect[][] a) {
        for (Vect[] anA : a) {
            sphToDek2(anA);
        }
        return a;
    }

    private static void dekToSph1(Vect a) {
        a.fromArgs(Math.acos(a.getZ() / a.length()), Math.atan2(a.getY(), a.getX()), a.length());
    }

    private static Vect[] dekToSph2(Vect[] a) {
        for (Vect anA : a) {
            dekToSph1(anA);
        }
        return a;
    }

    private static Vect[][] dekToSph3(Vect[][] a) {
        for (Vect[] anA : a) {
            dekToSph2(anA);
        }
        return a;
    }

}
