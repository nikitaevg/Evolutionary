package Evolutionary;

abstract class AbstractEvo extends Random implements EvolutionaryAlgorithm{
//    private static int lastLog;
//    static void print(int i, int steps) {
//        if (lastLog < i * 100 / steps) {
//            lastLog = i * 100 / steps;
//            write(lastLog + "% completed of evolutionary completed");
//        }
//    }

    static void write(String s) {
        System.out.println(s);
    }

    public Individual algorithm(int numberOfIterations, int n, int timeEnd,
                                int mu, int lambda) {
        boolean individualLogged = false;
        int time = 0;
        Individual ans;
        double maxVal = -1e10;
        Individual[] currInd = new Individual[mu + lambda];
        for (int i = 0; i < mu; i++) {
            currInd[i] = createRandomIndividual(n, timeEnd);
        }
        ans = currInd[0].copy(true);
        while (time < numberOfIterations) {
            write("#" + time + " of " + numberOfIterations);
//            print(time, numberOfIterations);
            performAlgorithm(currInd, mu, lambda);
            if (currInd[0].getValue().value > maxVal) {
                maxVal = currInd[0].getValue().value;
                write("Found new good answer " + maxVal + " at " + time + " iteration");
                ans = currInd[0].copy(true);
            }
            if (currInd[0].getValue().value < 0) {
                for (int i = 0; i < mu; i++) {
                    currInd[i] = createRandomIndividual(n, timeEnd);
                }
            }
            else if (!individualLogged) {
                individualLogged = true;
                currInd[0].logInd();
            }
            time++;
        }
        return ans;
    }
    abstract void performAlgorithm(Individual[] individual, int mu, int lambda);
}
