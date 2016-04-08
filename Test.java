import Evolutionary.*;
import Simulation.*;

import java.io.IOException;

public class Test {
    private static int secondsInDay = 86400, secondsInHour = 3600, secondsInMinute = 60, secondsInYear = secondsInDay * 365;
    public static void main(String[] args) throws IOException {
        int numOfDays = 115;
        int time = secondsInDay * numOfDays;
        boolean q = true;

        if (q) {
            EvolutionaryAlgorithm algo = new MuPlusLambda();
            Individual a = algo.algorithm(2, numOfDays + 2, time, 3, 6);
            System.out.println(new Runge().run(a.startingPos, a.startingAcc, a.accelerations, true, true, time));
        } else {
            Individual a = new Random().createRandomIndividual(numOfDays + 2, time);
            for (int i = 0; i < 1; i++) {
                Runge c = new Runge();
                c.run(a.startingPos, a.startingAcc, a.accelerations, true, true, time);
            }
        }
    }
}
