package com.company;

import java.util.ArrayList;
import java.util.Map;

public class Graph {

    static int[] colors;
    static Dot[] dotsList;
    static Graph constantGraph;
    static int chromaticNumber;

    static void initializeAttributes(int size) {
        colors = new int[size];

        for (int i = 0; i < colors.length; i++) colors[i] = i;


        dotsList = new Dot[size];
        for (int i = 0; i < dotsList.length; i++) dotsList[i] = new Dot();
    }

    static void initializeGraph(int size, int minDegree, int maxDegree) {
        initializeAttributes(size);

        for (int i = 0; i < dotsList.length; i++) {

            int tmpDegree = (int) (Math.random() * (maxDegree - minDegree + 1) + minDegree);

            for (int j = 0; j < tmpDegree; j++) {
                int adjacentDot;
                do {
                    adjacentDot = (int) (Math.random() * size - i) + i;
                } while (dotsList[i].adjacentDots.contains(adjacentDot) || dotsList[adjacentDot].adjacentDots.contains(i));

                dotsList[i].adjacentDots.add(adjacentDot);
                dotsList[adjacentDot].adjacentDots.add(i);

            }

        }


    }
}
