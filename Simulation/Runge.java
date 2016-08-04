package Simulation;

import Evolutionary.Report;
import flanagan.integration.RungeKutta;

import javax.swing.*;

public class Runge{
    private static final int DIM = 3, MAXAIMS = 500;
    private static final int SECONDS_IN_DAY = 86400, SECONDS_IN_HOUR = 3600,
            SECONDS_IN_MINUTE = 60, SECONDS_IN_YEAR = SECONDS_IN_DAY * 365;
    private final double SPEC_IMP = 5000, F = 0.1;
    private double ans = 0;
    private final Vect zeroVect = new Vect(0, 0, 0);
    private boolean log;
    private boolean toDraw;
    private Sandybox sb; //Sandybox is class for drawing
    private int[] currTime = new int[3];
    private int approaches = 0, photos = 0;
    private double[] maxDist = new double[]{0, 0, 0};
    private RungeKutta rk;
    private Delaunay del;
    private Universe universe;
    private JFrame f;
    public Report run(int[] startingTime, Vect[] spSpeed,
                      Vect[][] accelerations, boolean draw, boolean toLog, int endTime) {
        toDraw = draw;
        if (toDraw)
            f = new JFrame();
        universe = new Universe();
        log = toLog;
//        print("Starting simulation");
        try {
            universe.loadUniverse();
        } catch (Exception e) {
            e.printStackTrace();
            print(e.getMessage());
            return new Report(-1, maxDist, approaches, photos);
        }
        del = new Delaunay();
        algo(startingTime, spSpeed, accelerations, endTime);
//        print("Simulation finished");
        if (draw) {
            drawAlgo();
        }

        return new Report(ans, maxDist, approaches, photos);
    }

    private Vect getValue(Vect[] val, int ind) {
        return ((ind < 0) ? zeroVect : val[ind]);
    }

    private void algo(int[] startingTime, Vect[] spSpeed, Vect[][] accelerations, int tEnd) {
        if (!Universe.checkFuel(spSpeed, accelerations)) {
            print("Not enough fuel");
            ans = -1e15;
            return;
        }
        double xn = 10, h = 10; // xn -- how much seconds to run Runge, h -- seconds of each step in Runge (so there is only one step for each start, or smth like that)
//        lastLog = 0;
        ans = 0;
        int numOfCoord = 100000, currCoord = 0, steps = (int) (tEnd / xn), lastPhoto = -1;
        double timeLimit = 30;
        double ratio = (double)numOfCoord / (double)steps;
        DerivnV dn = new DerivnV();
        Delaunay.buildConvex(universe.aims);
        dn.createUn(universe.ao, universe.n + universe.ns, DIM);
        sb = new Sandybox(universe.ao, universe.n, DIM);
        double[] yn;
        System.arraycopy(startingTime, 0, currTime, 0, 3);
        double[] y0 = new double[(universe.n + universe.ns) * DIM * 2];
        int[] currDay = new int[3];
        boolean[] enginesStarted = new boolean[3];
        for (int i = 0; i < steps; i++) {
            for (int j = 0; j < 3; j++) {
                currTime[j] += xn;
                currDay[j] = (currTime[j] / SECONDS_IN_DAY);
                if (currTime[j] >= 0 && !enginesStarted[j]) {
                    universe.sp[j].addDy(spSpeed[j]);
                    enginesStarted[j] = true;
                }
            }
            //currDay = (int)(i * xn) / secondsInDay;
            dn.changeAcc(getValue(accelerations[0], currDay[0]),
                    getValue(accelerations[1], currDay[1]),
                    getValue(accelerations[2], currDay[2]), universe.n);
            universe.inputY0(y0);
            universe.changeM(getValue(accelerations[0], currDay[0]),
                    getValue(accelerations[1], currDay[1]),
                    getValue(accelerations[2], currDay[2]), xn);
            rk = initRK(xn, h, y0);
            yn = rk.fourthOrder(dn);
            universe.outputYN(yn);
            report();
            if (toDraw && ((double)i * ratio > currCoord || ratio > 1)) {
                sb.addValues(yn);
                currCoord++;
            }
            if ((i * xn - lastPhoto) >= timeLimit * (double)SECONDS_IN_DAY || lastPhoto == -1) {
                double t = universe.checkSputniks(del);
                if(t > 0) {
                    photos++;
                    ans += t;
                    lastPhoto = (int) (i * xn);

                }
            }
            if (universe.checkCrash()) {
                print("Crash occurred");
                if (toDraw)
                    f.getContentPane().add(sb);
                ans = -1e15;
                return;
            }
//            print(i, steps);
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

    private void report() { // calculates maxDist and number of approaches
        for (int i = 0; i < 3; i++) {
            maxDist[i] = Math.max(maxDist[i], universe.sp[i].getY().length());
            approaches += (universe.sp[i].getY().sub(universe.ao[1].getY()).length() < 6 * 1e6) ? 1 : 0;
        }
    }

//    private int lastLog;
//    private void print(int i, int steps) {
//        int t = 10;
//        if (lastLog < i * t / steps && log) {
//            lastLog++;
//            System.out.println(lastLog * t + "% completed.");
//        }
//    }

}
