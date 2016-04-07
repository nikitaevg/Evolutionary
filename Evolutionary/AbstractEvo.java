package Evolutionary;

public abstract class AbstractEvo extends Random implements EvolutionaryAlgorithm{
    public Individual[] algorithm(int numberOfIterations, int n, int k) {
        int time = 0;

        Individual[] ans = new Individual[0];
        double maxVal = 0;
        Individual[] currInd = new Individual[k];
        for (int i = 0; i < k; i++) {
            currInd[i] = createRandomIndividual(n);
        }
        while (time < numberOfIterations) {
            double t = performAlgorithm(currInd);
            if (t > maxVal) {
                maxVal = t;
                ans = currInd;
            }
            time++;
        }
        return ans;
    }
    abstract double performAlgorithm(Individual[] individual);
}
