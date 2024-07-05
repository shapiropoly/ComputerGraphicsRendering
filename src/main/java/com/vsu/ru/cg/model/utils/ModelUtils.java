package com.vsu.ru.cg.model.utils;

import com.vsu.ru.cg.model.Model;
import com.vsu.ru.cg.model.model_components.Polygon;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {

    public static void enableTextureMode(Model mesh) {
        mesh.setTextureMode(true);
    }
    public static void disableTextureMode(Model mesh) {
        mesh.setTextureMode(false);
    }
//    public static void triangulate(Model model) {
//        // Создаем новый список треугольников
//        List<Polygon> triangulatedFaces = new ArrayList<>();
//
//        for (Polygon polygon : model.getPolygons()) {
//            List<Integer> indices = polygon.getVertexIndices();
//
//            // Проходим по каждой тройке вершин в полигоне
//            for (int i = 0; i < indices.size() - 2; i++) {
//                int v1 = indices.get(0);
//                int v2 = indices.get(i + 1);
//                int v3 = indices.get(i + 2);
//
//                // Создаем новый треугольник
//                List<Integer> triangleIndices = new ArrayList<>();
//                triangleIndices.add(v1);
//                triangleIndices.add(v2);
//                triangleIndices.add(v3);
//
//                Polygon trianglePolygon = new Polygon();
//                trianglePolygon.setVertexIndices(triangleIndices);
//                triangulatedFaces.add(trianglePolygon);
//
//            }
//        }
//
//        // Заменяем текущие полигоны на новые треугольники
//        model.setPolygons(triangulatedFaces);
//    }
}
