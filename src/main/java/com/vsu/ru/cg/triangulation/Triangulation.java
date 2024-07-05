package com.vsu.ru.cg.triangulation;

import com.vsu.ru.cg.model.Model;
import com.vsu.ru.cg.model.model_components.Polygon;

import java.util.ArrayList;
import java.util.List;

public class Triangulation {

    public static void triangulationModel(Model model) {
        model.setPolygons(triangulationPolygons(model.getPolygons()));
    }


    protected static ArrayList<Polygon> triangulationPolygons(List<Polygon> polygons) {
        ArrayList<Polygon> triangulatedPolygons = new ArrayList<>();

        for (Polygon p: polygons) {
            triangulatedPolygons.addAll(triangulation(p));
        }

        return triangulatedPolygons;
    }
    protected static List<Polygon> triangulation(Polygon polygon) {

        List<Polygon> triangulatedPolygons = new ArrayList<>();

        List<Integer> vertexIndices = polygon.getVertexIndices();

        List<Integer> textureVertexIndices = polygon.getVertexIndices();
        checkForCorrectListSize(textureVertexIndices, vertexIndices.size(), "texture vertices");

        List<Integer> normalIndices = polygon.getNormalIndices();
        checkForCorrectListSize(normalIndices, vertexIndices.size(), "normals");

        for (int i = 1; i < vertexIndices.size() - 1; i++) {
            Polygon triangulatedPolygon = new Polygon();

            triangulatedPolygon.setVertexIndices(getTriangulatedIndices(vertexIndices, i));
            triangulatedPolygon.setTextureVertexIndices(getTriangulatedIndices(textureVertexIndices, i));
            triangulatedPolygon.setNormalIndices(getTriangulatedIndices(normalIndices, i));

            triangulatedPolygons.add(triangulatedPolygon);
        }

        return triangulatedPolygons;
    }


    private static void checkForCorrectListSize(List<Integer> list, int expectedSize, String listName) {
        if (list.size() != 0 && list.size() != expectedSize) {
            throw new IllegalArgumentException("Incorrect number of " + listName + "  in the polygon");
        }
    }

    protected static List<Integer> getTriangulatedIndices(List<Integer> vertexes, int indexVertex) {
        List<Integer> triangulatedIndices = new ArrayList<>();

        if (!vertexes.isEmpty()) {
            triangulatedIndices.add(vertexes.get(0));
            triangulatedIndices.add(vertexes.get(indexVertex));
            triangulatedIndices.add(vertexes.get(indexVertex + 1));
        }

        return triangulatedIndices;
    }
}
