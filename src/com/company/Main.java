package com.company;

public class Main {

    public static void main(String[] args) {
        Graph.initializeGraph(5,2,5);

        Individual individual = new Individual();

        individual.randomGreedyColoring();

        for(int i=0; i<individual.colorsVector.size();i++){
            System.out.println("dot "+i+ "color"+individual.colorsVector.get(i));
        }
    }
}
