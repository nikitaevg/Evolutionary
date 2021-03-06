package Evolutionary;

public interface EvolutionaryAlgorithm {

    Individual algorithm(int numberOfIterations, int n, int timeEnd,
                         int mu, int lambda);
}
