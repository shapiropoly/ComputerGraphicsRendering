package com.vsu.ru.cg.model.utils;

import com.vsu.ru.cg.math.vector.Vector3f;
import com.vsu.ru.cg.model.Model;
import com.vsu.ru.cg.model.model_components.Polygon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class DeleteVertices {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new File("vertex.txt"));
            while (scanner.hasNextLine()) {
                String[] numberStrings = scanner.nextLine().split("\\s+");
                int[] intArray = new int[numberStrings.length];

                for (int i = 0; i < numberStrings.length; i++) {
                    intArray[i] = Integer.parseInt(numberStrings[i]);
                }

                for (int i = 0; i < intArray.length; i++) {
                    System.out.println(intArray[i]);
                }
            }
    }
    public static void removeVertices(Model model, int[] vertexIndicesToRemove) {
        // Удаление выбранных вершин
        ArrayList<Vector3f> newVertices = new ArrayList<>(model.getVertices());
        for (int index : vertexIndicesToRemove) {
            newVertices.remove(index);
        }
        model.setVertices(newVertices);
        // Удаление связанных полигонов
        Iterator<Polygon> iterator = model.getPolygons().iterator();
        while (iterator.hasNext()) {
            Polygon polygon = iterator.next();
            List<Integer> vertexIndices = polygon.getVertexIndices();
            vertexIndices.removeIf(index -> containsIndex(vertexIndicesToRemove, index));

            if (vertexIndices.size() < 3) {
                iterator.remove(); // Удаляем полигоны с менее чем тремя вершинами
            }
        }
        // Переставляем индексы для корректной привязки вершин к полигонам
        for (Polygon polygon : model.getPolygons()) {
            List<Integer> vertexIndices = polygon.getVertexIndices();
            ArrayList<Integer> newVertexIndices = new ArrayList<>();
            for (int x : vertexIndices) {
                for(int y : vertexIndicesToRemove) {
                    if (x >= y) {
                        x = x - 1;
                    }
                }
                newVertexIndices.add(x);
            }

            polygon.setVertexIndices(newVertexIndices);
            System.out.println(polygon.getVertexIndices());
        }
    }

    private static boolean containsIndex(int[] indices, int targetIndex) {
        for (int index : indices) {
            if (index == targetIndex) {
                return true;
            }
        }
        return false;
    }
}