package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class Graph implements Serializable {

    static int size;
    static int[] colors;
    static Dot[] dotsList;


    static void initializeAttributes(int size) {
        Graph.size = size;
        colors = new int[size];

        for (int i = 0; i < colors.length; i++) colors[i] = i;


        dotsList = new Dot[size];
        for (int i = 0; i < dotsList.length; i++) dotsList[i] = new Dot();
    }

    static void serialize(String filename) {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(filename + ".ser"));

            stream.writeObject(size);
            stream.writeObject(colors);
            stream.writeObject(dotsList);

            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void deserialize(String filename) {

        try {
            ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filename + ".ser"));
            try {
                size = (int) stream.readObject();
                colors = (int[]) stream.readObject();
                dotsList = (Dot[]) stream.readObject();
                stream.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void initializeGraph() {
        String name = "Graph";
        File file = new File(name+".ser");
        if (file.exists()) {
            deserialize(name);
        }
        else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            generateGraph(300, 2, 30);
            serialize(name);
        }
    }

    static void generateGraph(int size, int minDegree, int maxDegree) {
        initializeAttributes(size);

        for (int i = 0; i < dotsList.length; i++) {

            int tmpDegree = (int) (Math.random() * (maxDegree - minDegree + 1) + minDegree);
            System.out.println("degree is " + tmpDegree);
            for (int j = dotsList[i].adjacentDots.size(); j < tmpDegree; j++) {
                int adjacentDot;

                do {
                    adjacentDot = (int) (Math.random() * size - i) + i;
                } while (dotsList[i].adjacentDots.contains(adjacentDot) || dotsList[adjacentDot].adjacentDots.contains(i) || adjacentDot == i);

                dotsList[i].adjacentDots.add(adjacentDot);
                dotsList[adjacentDot].adjacentDots.add(i);
                System.out.println(i + " " + adjacentDot);
            }

        }


    }
}
