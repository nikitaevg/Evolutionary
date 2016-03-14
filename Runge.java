import flanagan.integration.RungeKutta;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Runge{
    static int DIM = 3, MAXN = 20, R = 6371000, GEO = 35786000;
    static int n, ns = 0, nAims = 0;
    static int secondsInDay = 86400, secondsInHour = 3600, secondsInMinute = 60, secondsInYear = secondsInDay * 365;
    static AstronomicalObject[] ao;
    static Sputnik[] sp;
    static Aim[] aims;
    static RungeKutta rk;
    static JFrame f;
    public static void main(String[ ] arg) throws IOException {
        f = new JFrame();
        loadUniverse();
        algo();
        drawAlgo();
    }
    private static void loadUniverse() throws FileNotFoundException {
        Scanner reader = new Scanner(new File("objects.txt"));
        n = reader.nextInt();
        ao = new AstronomicalObject[MAXN];
        sp = new Sputnik[10];
        double m;
        Vect y, dy;
        for (int i = 0; i < n; i++) {
            m = reader.nextDouble();
            y = new Vect(reader.nextDouble(), reader.nextDouble(), reader.nextDouble());
            dy = new Vect(reader.nextDouble(), reader.nextDouble(), reader.nextDouble());
            ao[i] = new AstronomicalObject(y, dy, m);
        }
        reader = new Scanner(new File("aims.txt"));
        nAims = reader.nextInt();
        for (int i = 0; i < nAims; i++) {
            aims[i] = new Aim(reader.nextDouble(), reader.nextDouble());
        }
        addSputnik(new Vect(0, R + GEO, 0),
                new Vect(4000, 0, 100),
                20);
        addSputnik(new Vect(R + GEO * 5.5, 0, 0),
                new Vect(0, 1640, 10),
                30);
        addSputnik(new Vect(R + GEO * 0.5, 0, 0),
                new Vect(0, 5665, 0),
                30);

    }
    private static void algo() {
        double xn = 100, h = 40;
        int numOfCoord = 1000000, currCoord = 0, tEnd = secondsInYear, steps = (int) (tEnd / xn);
        double ratio = (double)numOfCoord / (double)steps;
        DerivnV dn = new DerivnV();
        dn.createUn(ao, n + ns, DIM);
        Sandybox sb = new Sandybox(ao, n, DIM);
        double[] yn;
        for (int i = 0; i < steps; i++) {
            rk = new RungeKutta();
            double[] y0 = new double[(n + ns) * DIM * 2];
            inputY0(y0);
            rk.setInitialValueOfX(0);
            rk.setFinalValueOfX(xn);
            rk.setInitialValuesOfY(y0);
            rk.setStepSize(h);
            yn = rk.fourthOrder(dn);
            outputYN(yn);
            if ((double)i * ratio > currCoord || ratio > 1) {
                sb.addValues(yn);
                currCoord++;
            }
            for (int j = 0; j < nAims; j++) {
                if (aims[j].satisf(sp[0], sp[1], sp[2])) {
                    //do smths
                }
            }
        }
        f.getContentPane().add(sb);
    }
    private static void inputY0(double[] y0) {
        for (int j = 0; j < n + ns; j++) {
            double[] y = ao[j].getY().toArray();
            double[] dy = ao[j].getDy().toArray();
            for (int k = 0; k < DIM; k++) {
                y0[j * DIM * 2 + k] = y[k];
                y0[j * DIM * 2 + DIM + k] = dy[k];
            }
        }
    }
    private static void outputYN(double[] yn) {
        for (int j = 0; j < n + ns; j++) {
            double[] y = new double[DIM];
            double[] dy = new double[DIM];
            for (int k = 0; k < DIM; k++) {
                y[k] = yn[j * DIM * 2 + k];
                dy[k] = yn[j * DIM * 2 + DIM + k];
            }
            ao[j].setY(y);
            ao[j].setDy(dy);
        }
    }
    private static void drawAlgo() {
        f.setSize(500, 500);
        f.setVisible(true);
    }
    private static void addSputnik(Vect y, Vect dy, double m) {
        sp[ns] = new Sputnik(y, dy, 20);
        ao[n + ns] = sp[ns];
        ns++;
    }
}
