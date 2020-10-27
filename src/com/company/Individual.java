package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Individual {

    static Individual[] population;
    static int bestChromaticNumber = 0;

    ArrayList<Integer> usedColors = new ArrayList<>();
    ArrayList<Integer> colorsVector;
    int chromaticNumber = 0;

    Individual() {

        colorsVector = new ArrayList<>(Graph.size);
        for (int i = 0; i < Graph.size; i++) {
            colorsVector.add(-5);
        }

    }

    static void makePopulation(int populationSize) {
        population = new Individual[populationSize];

        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual();
            individual.randomGreedyColoring();
            if (individual.chromaticNumber < Individual.bestChromaticNumber)
                Individual.bestChromaticNumber = individual.chromaticNumber;
            population[i] = individual;
        }

    }

    void randomGreedyColoring() {

        ArrayList<Integer> paintedDots = new ArrayList<>();

        outerloop:
        for (int i = 0; i < Graph.size; i++) {

            int chosenDot;
            do {
                chosenDot = (int) (Math.random() * Graph.size);
            } while (paintedDots.contains(chosenDot));
            paintedDots.add(chosenDot);
            for (Integer usedColor : usedColors) {

                if (canBeColored(chosenDot, usedColor)) {

                    colorsVector.set(chosenDot, usedColor);
                    continue outerloop;
                }
            }

            usedColors.add(Graph.colors[usedColors.size()]);
            colorsVector.set(chosenDot, usedColors.get(usedColors.size() - 1));

        }
        chromaticNumber = usedColors.size();

    }


    private boolean canBeColored(int chosenDot, int color) {
        for (Integer adjacentDot : Graph.dotsList[chosenDot].adjacentDots) {
            if (colorsVector.get(adjacentDot) == color) return false;
        }
        return true;
    }

    static Individual[] chooseParents() {
        Individual[] parents = new Individual[2];
        Individual bestParent = population[0];

        for (Individual tmp : population) {
            if (tmp.chromaticNumber < bestParent.chromaticNumber) bestParent = tmp;
        }

        parents[0] = bestParent;

        int randomIndividual;
        do {
            randomIndividual = (int) (Math.random() * population.length);

        } while (population[randomIndividual] == parents[0]);
        parents[1] = population[randomIndividual];

        return parents;
    }

    void interBreed(Individual[] parents, int dotsNumber) {


        ArrayList<Integer> splitterDots = new ArrayList<>(dotsNumber);

        for (int i = 0; i < dotsNumber; i++) {
            int dot;

            do {
                dot = (int) (Math.random() * dotsNumber);
            } while (splitterDots.contains(dot));
            splitterDots.add(dot);
        }

        splitterDots.add(parents[0].colorsVector.size());
        Collections.sort(splitterDots);


        Individual[] children = new Individual[2];

        for (int i = 0; i < dotsNumber; i++) {


            for (int j = splitterDots.get(i); j < splitterDots.get(i + 1); j++) {

                children[i % 2].colorsVector.set(splitterDots.get(i), parents[i % 2].colorsVector.get(j));
            }

        }

        int firstParentCurrPos = 0;
        int secondParentCurrPos = 0;

        int[] currPositions = new int[2];

        for (int i = 0; i < parents[0].colorsVector.size(); i++) {
            for (int j = 0; j < children.length; j++) {

                int a = j % 2;
                int b = (j + 1) % 2;

                if (children[a].colorsVector.get(i) < 0) {
                    while (!children[a].canBeColored(i, parents[b].colorsVector.get(currPositions[b]))) {
                        currPositions[b]++;
                    }

                    if (currPositions[b] == parents[b].colorsVector.size()) {
                        children[a].recalculateChromaticNumber();

                        outerloop:
                        for (int k = i; k < children[a].colorsVector.size(); k++) {
                            for (Integer usedColor : children[b].usedColors) {
                                if (children[a].canBeColored(k, usedColor)) {
                                    children[a].colorsVector.set(k, usedColor);
                                    continue outerloop;
                                }
                            }
                            int notUsedColor;
                            int counter = 0;

                            do {
                                notUsedColor = Graph.colors[counter];
                                counter++;
                            } while (children[a].colorsVector.contains(notUsedColor));
                            children[a].colorsVector.add(notUsedColor);
                            children[a].chromaticNumber++;
                        }

                    }

                }
            }
        }
    }


  /*      for (int i = 0; i < secondParent.colorsVector.size(); i++) {

            if (firstChild.colorsVector.get(i) < 0) {

                while (!firstChild.canBeColored(i, secondParent.colorsVector.get(secondParentCurrPos))/*&&secondParentCurrPos!=secondParent.colorsVector.size()) {
                    secondParentCurrPos++;
                }

                if (secondParentCurrPos == secondParent.colorsVector.size()) {
                    firstChild.recalculateChromaticNumber();

                    outerloop:
                    for (int j = i; j < firstChild.colorsVector.size(); j++) {
                        for (Integer usedColor : usedColors) {

                            if (canBeColored(j, usedColor)) {
                                colorsVector.set(j, usedColor);
                                continue outerloop;
                            }
                        }

                        int notUsedColor;
                        int counter = 0;

                        do {
                            notUsedColor = Graph.colors[counter];
                        } while (firstChild.colorsVector.contains(notUsedColor));
                        firstChild.colorsVector.add(notUsedColor);
                        firstChild.chromaticNumber++;
                    }

                }

            }

            if (secondChild.colorsVector.get(i) < 0) {

                while (!secondChild.canBeColored(i, firstParent.colorsVector.get(firstParentCurrPos))) {
                    firstParentCurrPos++;
                }

            }

        }*/
}

    int recalculateChromaticNumber() {
        usedColors.clear();

        for (Integer tmp : colorsVector) {
            if (!usedColors.contains(tmp)) usedColors.add(tmp);
        }

        usedColors.remove(-5);
        chromaticNumber = usedColors.size();
        return chromaticNumber;
    }

    void makeLocalImprovement() {

        for (Integer dot : colorsVector) {

            if (dot == (int) usedColors.get(usedColors.size() - 1)) {
                for (Integer usedColor : usedColors) {

                    if (canBeColored(dot, usedColor)) {

                        colorsVector.set(dot, usedColor);
                        return;
                    }
                }
                return;
            }

        }
    }

    void makeOtherLocalImprovement() {
        // int bestDo
        for (Dot dot : Graph.dotsList) {

        }
    }

    boolean mutateByChangingColor() {

        double probability = 0.2;
        if (Math.random() <= probability) {

            int randomDot = (int) (Math.random() * colorsVector.size());
            int randomColor;

            do {
                randomColor = (int) (Math.random() * usedColors.size());
            } while (randomColor == colorsVector.get(randomDot));

            if (canBeColored(randomDot, randomColor)) {
                colorsVector.set(randomDot, randomColor);
                return true;
            }

        }
        return false;
    }

    boolean mutateByColorSwap() {
        double probability = 0.3;

        if (Math.random() <= probability) {

            int firstSwapDot = (int) (Math.random() * colorsVector.size());
            int secondSwapDot;

            do {
                secondSwapDot = (int) (Math.random() * colorsVector.size());
            } while (secondSwapDot == firstSwapDot);


            if (canBeColored(firstSwapDot, colorsVector.get(secondSwapDot)) && canBeColored(secondSwapDot, colorsVector.get(firstSwapDot))) {
                int tmpColor = colorsVector.get(firstSwapDot);
                colorsVector.set(firstSwapDot, colorsVector.get(secondSwapDot));
                colorsVector.set(secondSwapDot, tmpColor);
                return true;
            }


        }
        return false;
    }

    public static void main(String[] args) {

        ArrayList<Integer> testSubList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            testSubList.add(i);
        }
        System.out.println(testSubList.get(0));
        ArrayList<Integer> subList = new ArrayList<>();
        subList.add(testSubList.get(0));
        subList.set(0, 27);
        System.out.println(subList.get(0) + " " + testSubList.get(0));
    }
}


