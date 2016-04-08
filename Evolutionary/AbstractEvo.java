package Evolutionary;

public abstract class AbstractEvo extends Random implements EvolutionaryAlgorithm{
    private static int lastLog;
    static void print(int i, int steps) {
        if (lastLog < i * 100 / steps) {
            lastLog = i * 100 / steps;
            System.out.println(lastLog + "% completed. The flight is OK");
        }
    }

    public Individual algorithm(int numberOfIterations, int n, int timeEnd,
                                int mu, int lambda) {
        int time = 0;

        Individual ans;
        double maxVal = -1e10;
        Individual[] currInd = new Individual[mu + lambda];
        for (int i = 0; i < mu; i++) {
            currInd[i] = createRandomIndividual(n, timeEnd);
        }
        ans = currInd[0];
        while (time < numberOfIterations) {
            print(time, numberOfIterations);
            performAlgorithm(currInd, mu, lambda);
            if (currInd[0].getValue() > maxVal) {
                maxVal = currInd[0].getValue();
                ans = currInd[0].copy();
            }
            System.gc();
            time++;
        }
        return ans;
    }
    abstract void performAlgorithm(Individual[] individual, int mu, int lambda);
}
