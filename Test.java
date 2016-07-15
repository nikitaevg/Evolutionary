import Evolutionary.*;
import Simulation.*;

import java.io.IOException;

public class Test {
    private static int secondsInDay = 86400, secondsInHour = 3600,
            secondsInMinute = 60, secondsInYear = secondsInDay * 365;
    public static void main(String[] args) throws IOException {
        int numOfDays = 365;
        int time = secondsInDay * numOfDays;
        boolean q = false;

        if (!q) {
            EvolutionaryAlgorithm algo = new MuPlusLambda();
            Individual a = algo.algorithm(8, numOfDays + 2, time, 2, 4);
            System.out.println(new Runge().run(a.startingPos, a.startingAcc, a.accelerations, true, true, time));
        } else {
            Individual a = new Random().createRandomIndividual(numOfDays + 2, time);
            a.startingPos = new Vect[]{new Vect(0, -40000000, 0), new Vect(0, 50000000, 0), new Vect(-50000000, 0, 0)};
            a.startingAcc = new Vect[]{new Vect(4000, 0, 0), new Vect(-3000, 0, 0), new Vect(0, 2000, 1200)};
            a.accelerations = new Vect[][]{{},{},{}};
            for (int i = 0; i < 1; i++) {
                Runge c = new Runge();
                System.out.println(c.run(a.startingPos, a.startingAcc, null, true, true, time));
            }
        }
    }
}
