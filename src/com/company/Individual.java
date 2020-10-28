package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class Individual {

    static Individual[] population;
    static int bestChromaticNumber = Integer.MAX_VALUE;

    ArrayList<Integer> usedColors = new ArrayList<>();
    ArrayList<Integer> colorsVector;
    int chromaticNumber = 0;

    Individual() {

        colorsVector = new ArrayList<>(Graph.size);
        for (int i = 0; i < Graph.size; i++) {
            colorsVector.add(-5);
        }

    }

    static void makePopulation(int populationSize) throws Exception {
        population = new Individual[populationSize];

        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual();
            individual.randomGreedyColoring();
            if (individual.chromaticNumber < Individual.bestChromaticNumber)
                Individual.bestChromaticNumber = individual.chromaticNumber;
            population[i] = individual;
        }

    }

    void randomGreedyColoring() throws Exception {

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
        System.out.print("number when initializing is" + chromaticNumber + " ");
        for (Integer tmp : colorsVector) {
            if (tmp == -5) throw new Exception();
            System.out.print(tmp + " ");
        }
        System.out.println();
    }


    private boolean canBeColored(int chosenDot, int color) {
        for (Integer adjacentDot : Graph.dotsList[chosenDot].adjacentDots) {
            if (colorsVector.get(adjacentDot) == color) return false;
        }
        return true;
    }

    static Individual[] chooseParents() throws Exception {

        Individual[] parents = new Individual[2];

        int bestParent = 0;

        for (Individual tmp : population) {
            if (tmp.chromaticNumber < population[bestParent].chromaticNumber) bestParent = tmp.chromaticNumber;
        }

        ArrayList<Integer> bestParentsList = new ArrayList<>();

        for (int j = 0; j < population.length; j++) {
            if (Individual.population[j].chromaticNumber == Individual.population[bestParent].chromaticNumber)
                bestParentsList.add(j);
        }

        bestParent = bestParentsList.get((int) (Math.random() * bestParentsList.size()));
        parents[0] = population[bestParent];

        int randomIndividual;

        do {
            randomIndividual = (int) (Math.random() * population.length);
        } while (randomIndividual == bestParent);

        parents[1] = population[randomIndividual];
        boolean test = true;

        for (int i = 0; i < parents[0].colorsVector.size(); i++) {
            int first = parents[0].colorsVector.get(i);
            int second = parents[1].colorsVector.get(i);
            if (first != second) {
                test = false;
                break;
            }
        }

        if (test) {

            chooseParents();
        }




        return parents;
    }

    static Individual interBreed(Individual[] parents, int dotsNumber) {


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
        children[0] = new Individual();
        children[1] = new Individual();


        for (int i = 0; i < dotsNumber; i++) {

            for (int j = splitterDots.get(i); j < splitterDots.get(i + 1); j++) {
                //   System.out.println(parents[i % 2].colorsVector.get(j));
                children[i % 2].colorsVector.set(j, parents[i % 2].colorsVector.get(j));
            }

        }

        for (Individual tmp : children) {
            tmp.recalculateChromaticNumber();
        }

        int[] currPositions = new int[2];


        dotsloop:
        for (int j = 0; j < children.length; j++) {

            int a = j % 2;
            int b = (j + 1) % 2;

            for (int i = 0; i < parents[0].colorsVector.size(); i++) {


                if (children[a].colorsVector.get(i) < 0) {

                    while (currPositions[b] < parents[b].colorsVector.size() && !children[a].canBeColored(i, parents[b].colorsVector.get(currPositions[b]))) {
                        currPositions[b]++;
                    }


                    if (currPositions[b] < parents[b].colorsVector.size()) {

                        Integer colorToSet = parents[b].colorsVector.get(currPositions[b]);
                        children[a].colorsVector.set(i, colorToSet);

                        if (!children[a].usedColors.contains(colorToSet)) {
                            children[a].usedColors.add(colorToSet);
                            children[a].chromaticNumber++;
                        }

                    } else {

                        outerloop:
                        for (int k = i; k < children[a].colorsVector.size(); k++) {

                            for (Integer usedColor : children[a].usedColors) {

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
                            } while (children[a].usedColors.contains(notUsedColor));

                            children[a].usedColors.add(notUsedColor);
                            children[a].chromaticNumber++;
                            children[a].colorsVector.set(k, notUsedColor);
                        }
                        continue dotsloop;
                    }

                }
            }
        }
        // System.out.println("somethingkkk " + children[0].usedColors.size());
        if (children[0].chromaticNumber < children[1].chromaticNumber) return children[0];
        else if (children[0].chromaticNumber > children[1].chromaticNumber) return children[1];
        else return children[(int) (Math.random() * children.length)];

    }


    int recalculateChromaticNumber() {
        usedColors.clear();

        for (Integer tmp : colorsVector) {
            if (!usedColors.contains(tmp) && tmp != -5) usedColors.add(tmp);
        }


        chromaticNumber = usedColors.size();
        return chromaticNumber;
    }

    void makeLeastImprovement() {

        HashMap<Integer, Integer> colorFrequencies = new HashMap<>();

        for (Integer color : usedColors) {
            colorFrequencies.put(color, 0);
        }

        for (Integer color : colorsVector) {
            Integer tmp = colorFrequencies.get(color);
            if (tmp == null) {
                System.out.println("color is" + color);
                for (Integer tmpColor : usedColors) {
                    System.out.println(tmpColor);
                }
            }
            colorFrequencies.put(color, ++tmp);
        }

        int minFrequencyColor = usedColors.get(0);
        System.out.println(colorFrequencies.get(minFrequencyColor));

        for (Integer tmp : colorFrequencies.keySet()) {
            if (colorFrequencies.get(tmp) < colorFrequencies.get(minFrequencyColor)) minFrequencyColor = tmp;
        }


        for (Integer color : colorsVector) {

            if (color == minFrequencyColor) {
                for (Integer usedColor : usedColors) {

                    if (usedColor != minFrequencyColor && canBeColored(color, usedColor)) {
                        System.out.print("whyyyyy ");
                        System.out.println(colorFrequencies.get(minFrequencyColor));
                        colorsVector.set(color, usedColor);
                        recalculateChromaticNumber();
                        return;
                    }

                }

                return;
            }

        }
    }

    void makeDegreeImprovement() {
        int bestDot = 0;

        for (int i = 0; i < Graph.dotsList.length; i++) {
            if (Graph.dotsList[i].adjacentDots.size() > Graph.dotsList[bestDot].adjacentDots.size()) bestDot = i;
        }

        for (Integer color : usedColors) {
            if (canBeColored(bestDot, color)) {
                colorsVector.set(bestDot, color);
                recalculateChromaticNumber();
                break;
            }
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
                recalculateChromaticNumber();
                return true;
            }

        }
        return false;
    }

    boolean mutateByColorSwap() {
        //  System.out.println("somethingkkk " + usedColors.size());
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

}


