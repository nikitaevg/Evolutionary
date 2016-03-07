import flanagan.integration.RungeKutta;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Runge{
    static int DIM = 2, MAXN = 20, R = 6371000, GEO = 35786000;
    static int n, ns = 0;
    static int secondsInDay = 86400, secondsInHour = 3600, secondsInMinute = 60, secondsInYear = secondsInDay * 365;
    static AstronomicalObject[] ao;
    static Sputnik[] sp;
    static RungeKutta rk;
    static JFrame f;
    public static void main(String[ ] arg) throws IOException {
        f = new JFrame();
        readUniverse();
        algo();
        drawAlgo();
    }
    private static void readUniverse() throws FileNotFoundException {
        Scanner reader = new Scanner(new File("objects.txt"));
        n = reader.nextInt();
        ao = new AstronomicalObject[MAXN];
        sp = new Sputnik[10];
        double m;
        double[] y, dy;
        for (int i = 0; i < n; i++) {
            y = new double[DIM];
            dy = new double[DIM];
            m = reader.nextDouble();
            for (int j = 0; j < DIM; j++) {
                y[j] = reader.nextDouble();
            }
            for (int j = 0; j < DIM; j++) {
                dy[j] = reader.nextDouble();
            }
            ao[i] = new AstronomicalObject(y, dy, m);
        }
        addSputnik(new double[]{0, R + GEO},
                new double[]{4000, 0},
                20);
        addSputnik(new double[]{R + GEO * 5.5, 0},
                new double[]{0, 1640},
                30);
        addSputnik(new double[]{R + GEO * 0.5, 0},
                new double[]{0, 5665},
                30);

    }
    private static void algo() {
        double xn = 100, h = 10;
        int numOfCoord = 100000, currCoord = 0, tEnd = secondsInYear * 2, steps = (int) (tEnd / xn);
        double ratio = (double)numOfCoord / (double)steps;

        DerivnV dn = new DerivnV();
        dn.createUn(ao, n + ns, DIM);
        Sandybox sb = new Sandybox(ao, n, DIM);
        double[] y, dy, yn;
        for (int i = 0; i < steps; i++) {
            rk = new RungeKutta();
            double[] y0 = new double[(n + ns) * DIM * 2];
            for (int j = 0; j < n + ns; j++) {
                y = ao[j].getY();
                dy = ao[j].getDy();
                for (int k = 0; k < DIM; k++) {
                    y0[j * DIM * 2 + k] = y[k];
                    y0[j * DIM * 2 + DIM + k] = dy[k];
                }
            }
            rk.setInitialValueOfX(0);
            rk.setFinalValueOfX(xn);
            rk.setInitialValuesOfY(y0);
            rk.setStepSize(h);
            yn = rk.fourthOrder(dn);
            for (int j = 0; j < n + ns; j++) {
                y = new double[DIM];
                dy = new double[DIM];
                for (int k = 0; k < DIM; k++) {
                    y[k] = yn[j * DIM * 2 + k];
                    dy[k] = yn[j * DIM * 2 + DIM + k];
                }
                ao[j].setY(y);
                ao[j].setDy(dy);
            }
            if ((double)i * ratio > currCoord || ratio > 1) {
                sb.addValues(yn);
                currCoord++;
            }

        }
        f.getContentPane().add(sb);
    }
    private static void drawAlgo() {
        f.setSize(500, 500);
        f.setVisible(true);
    }
    private static void addSputnik(double[] y, double[] dy, double m) {
        sp[ns] = new Sputnik(y, dy, 20);
        ao[n + ns] = sp[ns];
        ns++;
    }
}
