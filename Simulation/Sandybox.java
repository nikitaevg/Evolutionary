package Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;

class Sandybox extends JPanel {
    private static int R = 6371000, RR = 2;
    private static double NORM = (double)RR / (double)R;
    private AstronomicalObject[] ao;
    private double[][] all;
    private int n, dim, curr = 0;
    private static int X = 500, Y = 500;
    Sandybox(AstronomicalObject[] ao, int n, int dim, int coord) {
        this.ao = ao;
        all = new double[3][coord * 4];
        this.n = n;
        this.dim = dim;
    }
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        double[] aoY;
        for (int i = 0; i < 1; i++) {
            aoY = ao[i].getY().toArray();
            g2.drawOval((int)(X / 2 + aoY[0] * NORM - RR), (int)(Y / 2 - aoY[1] * NORM - RR), RR * 2, RR * 2);
        }
        for (int i = 0; i < all[0].length; i++) {
            g2.drawOval((int)(X / 2 + all[0][i] * NORM), (int)(Y / 2 - all[1][i] * NORM), 1, 1);
            //System.out.println(Y / 2 - all[1].get(i) * NORM);
        }
    }

    void addValues(double[] val) {
        for (int i = 1; i < (val.length) / dim / 2; i++) {
            for (int k = 0; k < dim; k++) {
                //if (Math.abs(val[i * dim * 2 + k + 1][j] - val[i * dim * 2 + k + 1]))
                all[k][curr] = val[i * dim * 2 + k];
            }
            curr++;
        }
    }

    public static void main() {
        JFrame f = new JFrame();
        f.setSize(X, Y);
        f.setVisible(true);
    }
}
