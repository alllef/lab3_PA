package com.company;

import java.util.ArrayList;
import java.util.Map;

public class Graph {
    static  int size;
    static int[] colors;
    static Dot[] dotsList;


    static void initializeAttributes(int size) {
        Graph.size=size;
        colors = new int[size];

        for (int i = 0; i < colors.length; i++) colors[i] = i;


        dotsList = new Dot[size];
        for (int i = 0; i < dotsList.length; i++) dotsList[i] = new Dot();
    }

    static void initializeGraph(int size, int minDegree, int maxDegree) {
        initializeAttributes(size);

        for (int i = 0; i < dotsList.length; i++) {

            int tmpDegree = (int) (Math.random() * (maxDegree - minDegree + 1) + minDegree);
            System.out.println("degree is " + tmpDegree);
            for (int j = dotsList[i].adjacentDots.size(); j < tmpDegree; j++) {
                int adjacentDot;

                do {
                    adjacentDot = (int) (Math.random() * size - i) + i;
                } while (dotsList[i].adjacentDots.contains(adjacentDot) || dotsList[adjacentDot].adjacentDots.contains(i)||adjacentDot==i);

                dotsList[i].adjacentDots.add(adjacentDot);
                dotsList[adjacentDot].adjacentDots.add(i);
                System.out.println(i + " " + adjacentDot);
            }

        }


    }
}
