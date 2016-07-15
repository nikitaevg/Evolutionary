package Simulation;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


class Universe {
    int n = 0, ns = 0;
    private static int DIM = 3, MAXN = 6, R = 6371000, GEO = 35786000, nAims = 0, MAXAIMS = 450;
    AstronomicalObject[] ao;
    private int lastStar;
    Sputnik[] sp;
    Aim[] aims;
    void loadUniverse(Vect[] SpPos, Vect[] SpAcc) throws FileNotFoundException {
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
        addSputnik(SpPos[0],
                SpAcc[0],
                2000);
        addSputnik(SpPos[1],
                SpAcc[1],
                2000);
        addSputnik(SpPos[2],
                SpAcc[2],
                2000);
    }
    private void addSputnik(Vect y, Vect dy, double m) {
        sp[ns] = new Sputnik(y, dy, m, 5);
        ao[n + ns] = sp[ns];
        ns++;
    }
    int checkSputniks(Delaunay del) {
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
//        }
//        if ((a.getKey() == -1 && ans != -1) || (a.getKey() != -1 && ans == -1)) {
//            System.out.println("Problem");
//            mul = (sp[1].getY().sub(sp[0].getY())).mulVect(sp[2].getY().sub(sp[0].getY()));
//        }
        return del.nearestFace(sp[0], sp[1], sp[2]);
//        return a.getKey();
    }

    boolean checkCrash() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < ns; j++) {
                if (ao[i].clash(sp[j].getY()))
                    return true;
            }
        }
        return false;
    }

    void inputY0(double[] y0) {
        for (int j = 0; j < n + ns; j++) {
            double[] y = ao[j].getY().toArray();
            double[] dy = ao[j].getDy().toArray();
            for (int k = 0; k < DIM; k++) {
                y0[j * DIM * 2 + k] = y[k];
                y0[j * DIM * 2 + DIM + k] = dy[k];
            }
        }
    }
    void outputYN(double[] yn) {
        for (int j = 0; j < n + ns; j++) {
            ao[j].setY(new Vect(yn[j * DIM * 2], yn[j * DIM * 2 + 1], yn[j * DIM * 2 + 2]));
            ao[j].setDy(new Vect(yn[j * DIM * 2 + DIM], yn[j * DIM * 2 + 1 + DIM], yn[j * DIM * 2 + 2 + DIM]));
        }
    }
}
