import Evolutionary.*;
import Simulation.*;

import java.io.IOException;

public class Test { // starting class
    private static final int SECONDS_IN_DAY = 86400, SECONDS_IN_HOUR = 3600,
            SECONDS_IN_MINUTE = 60, SECONDS_IN_YEAR = SECONDS_IN_DAY * 365;
    public static void main(String[] args) throws IOException {
        int numOfDays = 365 * 3; // number of days to fly
        int time = SECONDS_IN_DAY * numOfDays;
        boolean q = false;

        if (!q) {
            EvolutionaryAlgorithm algo = new MuPlusLambda();
            Individual a = algo.algorithm(100, numOfDays + 2, time, 1, 1);
            System.out.println(new Runge().run(a.startTime, Individual.sphToDek2(a.startingAcc)
                    , Individual.sphToDek3(a.accelerations), true, true, time));
        } else {
            Individual a = new Random().createRandomIndividual(numOfDays + 2, time);
            a.startingAcc = new Vect[]{new Vect(4000, 0, 0), new Vect(-3000, 0, 0), new Vect(0, 2000, 1200)};
            a.accelerations = new Vect[][]{{},{},{}};
            for (int i = 0; i < 1; i++) {
                Runge c = new Runge();
                System.out.println(c.run(a.startTime, a.startingAcc, null, true, true, time));
            }
        }
    }
}
