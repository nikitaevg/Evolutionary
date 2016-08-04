package Simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


class Universe {
    int n = 0, ns = 0;
    private static final int DIM = 3, MAXN = 6, R = 6371000, GEO = 35786000, MAXAIMS = 450;
    private static int nAims;
    private static final double MIN_DIST = 6578140, MAX_DIST = 1e9, ACCELERATION = 7700, MIN_FUEL = 1890;
    AstronomicalObject[] ao; // ao -- array of AOs (Earth, Moon), and also contains sputniks, that are from array sp
    private static double IMP = 5000;
    Sputnik[] sp;
    Aim[] aims;
    private Vect[] startingSpeed = new Vect[]{new Vect(0, ACCELERATION, 0), new Vect(-ACCELERATION, 0, 0), new Vect(0, -ACCELERATION, 0)};
    private Vect[] startingPos = new Vect[]{new Vect(R + 400000, 0, 0), new Vect(0, -R - 400000, 0), new Vect(-R - 400000, 0, 0)};
    void loadUniverse() throws FileNotFoundException { // inputs objects and aims and creates sputniks
        Scanner reader = new Scanner(new File("objects.txt"));
        n = reader.nextInt();
        ns = 0;
        ao = new AstronomicalObject[MAXN];
        sp = new Sputnik[3];
        double m, r;
        Vect y, dy;
        for (int i = 0; i < n; i++) {
            m = reader.nextDouble();
            r = reader.nextDouble();
            y = new Vect(reader.nextDouble(), reader.nextDouble(), reader.nextDouble());
            dy = new Vect(reader.nextDouble(), reader.nextDouble(), reader.nextDouble());
            ao[i] = new AstronomicalObject(y, dy, m, r);
        }
        reader.close();
        reader = new Scanner(new File("aims.txt"));
        nAims = reader.nextInt();
        aims = new Aim[nAims];
        for (int i = 0; i < nAims; i++) {
            reader.nextInt();
            aims[i] = new Aim(Math.toRadians(reader.nextDouble()), Math.PI / 2 - Math.toRadians(reader.nextDouble()));
        }
        reader.close();
        for (int i = 0; i < 3; i++) {
            addSputnik(startingPos[i], startingSpeed[i], 4000);
        }
    }
    private void addSputnik(Vect y, Vect dy, double m) {
        sp[ns] = new Sputnik(y, dy, m, 5);
        ao[n + ns] = sp[ns];
        ns++;
    }
    double checkSputniks(Delaunay del) {
//        int ans = -1;
//        for (int j = 0; j < nAims; j++) {
//            if (aims[j].satisf(sp[0], sp[1], sp[2])) {
//                ans = j;
//                break;
//            }
//        }
//        Pair<Integer, Vect> a = del.nearestFace(sp[0], sp[1], sp[2]);
//        Vect mul = null;
//        if (a.getKey() != -1) {
//            mul = null;
//            ok++;
//        }
//        if ((a.getKey() == -1 && ans != -1) || (a.getKey() != -1 && ans == -1)) {
//            System.out.println("Problem");
//            mul = (sp[1].getY().sub(sp[0].getY())).mulVect(sp[2].getY().sub(sp[0].getY()));
//            ang = mul.angBetweenLines(a.getValue());
//        }
        return del.nearestFace(sp[0], sp[1], sp[2]);
//        return a.getKey();
    }

    boolean checkCrash() { // checks whether sputnik is too far away or it crashed
        for (Sputnik s : sp) {
            if (s.getY().length() > MAX_DIST || s.getY().length() < MIN_DIST)
                return true;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < ns; j++) {
                if (ao[i].clash(sp[j].getY()))
                    return true;
            }
        }
        return false;
    }

    private double[] y = new double[3];
    private double[] dy = new double[3];

    void inputY0(double[] y0) { //inserts data for starting Runge
        for (int j = 0; j < n + ns; j++) {
            ao[j].getY().toArray(y);
            ao[j].getDy().toArray(dy);
            for (int k = 0; k < DIM; k++) {
                y0[j * DIM * 2 + k] = y[k];
                y0[j * DIM * 2 + DIM + k] = dy[k];
            }
        }
    }
    void outputYN(double[] yn) { // loads data from yn to Universe after Runge finished
        for (int j = 0; j < n + ns; j++) {
            ao[j].setY(yn[j * DIM * 2], yn[j * DIM * 2 + 1], yn[j * DIM * 2 + 2]);
            ao[j].setDy(yn[j * DIM * 2 + DIM], yn[j * DIM * 2 + 1 + DIM], yn[j * DIM * 2 + 2 + DIM]);
        }
    }

    private void decreaseM(Vect a, Sputnik s, double seconds) {
        s.setM(s.getM() - (a.length() / IMP) * seconds);
    }

    void changeM(Vect a, Vect b, Vect c, double seconds) { // calculates fuel consumption
        decreaseM(a, sp[0], seconds);
        decreaseM(b, sp[1], seconds);
        decreaseM(c, sp[2], seconds);
    }

    static boolean checkFuel(Vect[] spSpeed, Vect[][] acceleration) { // checks whether it is enough fuel to fly
        double g = 9.81, I = 450;
        int secondsInDay = 86400;
        double[] mass = new double[]{4000, 4000, 4000};
        for (int i = 0; i < 3; i++) {
            mass[i] *= Math.exp(-spSpeed[i].length() / (g * I));
        }
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < acceleration[0].length; j++) {
                mass[i] -= acceleration[i][j].length() / IMP * secondsInDay;
        }
        for (double a : mass) {
            if (a < MIN_FUEL)
                return false;
        }
        return true;
    }
}
