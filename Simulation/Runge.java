package Simulation;

import flanagan.integration.RungeKutta;

import javax.swing.*;

public class Runge{
    private static int DIM = 3, MAXAIMS = 500;
    private static int secondsInDay = 86400, secondsInHour = 3600, secondsInMinute = 60, secondsInYear = secondsInDay * 365;
    private static int ans = 0;
    private static boolean[] used = new boolean[MAXAIMS];
    private static RungeKutta rk;
    private static JFrame f;
    public static int run(Vect[] SpPos, Vect[] SpAcc,
                          Vect[][] accelerations) {
        f = new JFrame();
        print("Starting simulation");
        try {
            Universe.loadUniverse(SpPos, SpAcc);
        } catch (Exception e) {
            print("Couldn't load Simulation.Universe");
            return -1;
        }
        algo(accelerations);
        print("Simulation finished");
        drawAlgo();

        return ans;
    }

    private static void algo(Vect[][] accelerations) {
        double xn = 100, h = 40;
        lastLog = 0;
        int numOfCoord = 1000000, currCoord = 0, tEnd = secondsInDay * 5, steps = (int) (tEnd / xn), lastPhoto = -1;
        int timeLimit = 15;
        double ratio = (double)numOfCoord / (double)steps;
        DerivnV dn = new DerivnV();
        dn.createUn(Universe.ao, Universe.n + Universe.ns, DIM);
        Sandybox sb = new Sandybox(Universe.ao, Universe.n, DIM);
        double[] yn;
        double[] y0 = new double[(Universe.n + Universe.ns) * DIM * 2]; // maybe it must be written +2 strings
        for (int i = 0; i < steps; i++) {

            dn.changeAcc(accelerations[0][(int)(i * xn) / secondsInDay],
                    accelerations[1][(int)(i * xn) / secondsInDay],
                    accelerations[2][(int)(i * xn) / secondsInDay], Universe.n);
            Universe.inputY0(y0);
            rk = initRK(xn, h, y0);
            yn = rk.fourthOrder(dn);
            Universe.outputYN(yn);
            if ((double)i * ratio > currCoord || ratio > 1) {
                sb.addValues(yn);
                currCoord++;
            }
            /*if ((i * xn - lastPhoto) / secondsInDay >= timeLimit || lastPhoto == -1) {
                if(Simulation.Universe.checkSputniks()) {
                    lastPhoto = (int) (i * xn);
                }
            }*/
            if (Universe.checkCrash()) {
                print("Crash occurred");
                ans = (int)-1e9;
                return;
            }
            print(i, steps);
        }
        f.getContentPane().add(sb);
    }

    private static void drawAlgo() {
        f.setSize(500, 500);
        f.setVisible(true);
    }

    private static void print(String message) {
        System.out.println(message);
    }

    private static RungeKutta initRK(double xn, double h, double[] y0) {
        RungeKutta rk = new RungeKutta();
        rk.setInitialValueOfX(0);
        rk.setFinalValueOfX(xn);
        rk.setInitialValuesOfY(y0);
        rk.setStepSize(h);
        return rk;
    }

    private static int lastLog;
    private static void print(int i, int steps) {
        if (lastLog < i * 10 / steps) {
            lastLog++;
            System.out.println(lastLog + "0% completed. The flight is OK");
        }
    }

}
