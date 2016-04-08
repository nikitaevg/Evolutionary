package Simulation;

import flanagan.integration.RungeKutta;

import javax.swing.*;

public class Runge{
    private static int DIM = 3, MAXAIMS = 500;
    private static int secondsInDay = 86400, secondsInHour = 3600, secondsInMinute = 60, secondsInYear = secondsInDay * 365;
    private int ans = 0;
    boolean[] used;
    private boolean log;
    private boolean toDraw;
    private Sandybox sb;
    private RungeKutta rk;
    private Universe universe;
    private JFrame f;
    public int run(Vect[] SpPos, Vect[] SpAcc,
                          Vect[][] accelerations, boolean draw, boolean toLog, int endTime) {
        toDraw = draw;
        if (toDraw)
            f = new JFrame();
        universe = new Universe();
        log = toLog;
        print("Starting simulation");
        try {
            universe.loadUniverse(SpPos, SpAcc);
        } catch (Exception e) {
            print("Couldn't load Universe");
            return -1;
        }
        algo(accelerations, endTime);
        print("Simulation finished");
        if (draw) {
            drawAlgo();
        }

        return ans;
    }

    private void algo(Vect[][] accelerations, int tEnd) {
        double xn = 10, h = 10;
        used = new boolean[MAXAIMS];
        lastLog = 0;
        ans = 0;
        int numOfCoord = 100000, currCoord = 0, steps = (int) (tEnd / xn), lastPhoto = -1;
        double timeLimit = 0.5;
        double ratio = (double)numOfCoord / (double)steps;
        DerivnV dn = new DerivnV();
        dn.createUn(universe.ao, universe.n + universe.ns, DIM);
        if (toDraw) {
            sb = new Sandybox(universe.ao, universe.n, DIM, numOfCoord);
        }
        double[] yn;
        double[] y0 = new double[(universe.n + universe.ns) * DIM * 2];
        for (int i = 0; i < steps; i++) {

            dn.changeAcc(accelerations[0][(int)(i * xn) / secondsInDay],
                    accelerations[1][(int)(i * xn) / secondsInDay],
                    accelerations[2][(int)(i * xn) / secondsInDay], universe.n);
            universe.inputY0(y0);
            rk = initRK(xn, h, y0);
            yn = rk.fourthOrder(dn);
            universe.outputYN(yn);
            if (toDraw && ((double)i * ratio > currCoord || ratio > 1)) {
                sb.addValues(yn);
                currCoord++;
            }
            if ((i * xn - lastPhoto) >= timeLimit * (double)secondsInDay || lastPhoto == -1) {
                int t = universe.checkSputniks();
                if(t != -1) {
                    ans++;
                    lastPhoto = (int) (i * xn);

                }
            }
            if (universe.checkCrash()) {
                print("Crash occurred");
                ans = (int)-1e9;
                return;
            }
            print(i, steps);
            //System.gc();
        }
        if (toDraw)
            f.getContentPane().add(sb);
    }

    private void drawAlgo() {
        f.setSize(500, 500);
        f.setVisible(true);
    }

    private void print(String message) {
        if (log)
            System.out.println(message);
    }

    private RungeKutta initRK(double xn, double h, double[] y0) {
        RungeKutta rk = new RungeKutta();
        rk.setInitialValueOfX(0);
        rk.setFinalValueOfX(xn);
        rk.setInitialValuesOfY(y0);
        rk.setStepSize(h);
        return rk;
    }

    private int lastLog;
    private void print(int i, int steps) {
        if (lastLog < i * 100 / steps && log) {
            lastLog++;
            System.out.println(lastLog + "% completed.");
        }
    }

}
