package com.company;

import java.util.ArrayList;

public class Algorithm {
    static void makeAlgorithm(int populationSize) throws Exception {

        Graph.initializeGraph();
        Individual.makePopulation(populationSize);

        for (int i = 0; i < 1000; i++) {
            System.out.println("chromatic number is " + Individual.bestChromaticNumber);
            Individual child = Individual.interBreed(Individual.chooseParents(), 50);
            System.out.println("mutation is " + child.mutateByChangingColor());
            child.makeLeastImprovement();

            for (Integer tmp : child.colorsVector) {
                if (tmp == -5) {

                    throw new Exception();
                }
                System.out.print(tmp + " ");
            }


            int worstIndividual = 0;

            for (int j = 0; j < populationSize; j++) {
                if (Individual.population[j].chromaticNumber > Individual.population[worstIndividual].chromaticNumber)
                    worstIndividual = j;
            }

            ArrayList<Integer> worstIndividualsList = new ArrayList<>();
            for (int j = 0; j < populationSize; j++) {
                if (Individual.population[j].chromaticNumber == Individual.population[worstIndividual].chromaticNumber)
                    worstIndividualsList.add(j);
            }

            worstIndividual = worstIndividualsList.get((int) (Math.random() * worstIndividualsList.size()));
            Individual.population[worstIndividual] = child;

            if (child.chromaticNumber < Individual.bestChromaticNumber)
                Individual.bestChromaticNumber = child.chromaticNumber;
        }
    }

    public static void main(String[] args) throws Exception {
        Algorithm.makeAlgorithm(100);
    }
}
