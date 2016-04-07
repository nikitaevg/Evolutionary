package Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

class Sandybox extends JPanel {
    private static int R = 6371000, RR = 2;
    private static double NORM = (double)RR / (double)R;
    private AstronomicalObject[] ao;
    private ArrayList<Integer>[] all;
    private int n, dim;
    private static int X = 500, Y = 500;
    Sandybox(AstronomicalObject[] ao, int n, int dim) {
        this.ao = ao;
        all = new ArrayList[dim];
        for (int i = 0; i < dim; i++) {
            all[i] = new ArrayList<>();
        }
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
        for (int i = 0; i < all[0].size(); i++) {
            g2.drawOval((int)(X / 2 + all[0].get(i) * NORM), (int)(Y / 2 - all[1].get(i) * NORM), 1, 1);
            //System.out.println(Y / 2 - all[1].get(i) * NORM);
        }
    }

    void addValues(double[] val) {
        for (int i = 1; i < (val.length) / dim / 2; i++) {
            for (int k = 0; k < dim; k++) {
                //if (Math.abs(val[i * dim * 2 + k + 1][j] - val[i * dim * 2 + k + 1]))
                all[k].add((int) val[i * dim * 2 + k]);
            }
        }
    }

    public static void main() {
        JFrame f = new JFrame();
        f.setSize(X, Y);
        f.setVisible(true);
    }
}
