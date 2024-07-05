package com.vsu.ru.cg.triangulation;

import com.vsu.ru.cg.model.model_components.Polygon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TriangulationTest {

    public static boolean comparisonPolygon(Polygon p1, Polygon p2) {

        List<Integer> firstVertexIndices = p1.getVertexIndices();
        List<Integer> firstTextureVertexIndices = p1.getTextureVertexIndices();
        List<Integer> firstNormalIndices = p1.getNormalIndices();


        List<Integer> secondVertexIndices = p2.getVertexIndices();
        List<Integer> secondTextureVertexIndices = p2.getTextureVertexIndices();
        List<Integer> secondNormalIndices = p2.getNormalIndices();

        return firstVertexIndices.equals(secondVertexIndices) &&
                firstTextureVertexIndices.equals(secondTextureVertexIndices) &&
                firstNormalIndices.equals(secondNormalIndices);
    }

    @Test
    public void triangulationTest() {
        ArrayList<Integer> originalPolygon = new ArrayList<>(Arrays.asList(1, 3, 4, 2));

        Polygon polygon = new Polygon();
        polygon.setVertexIndices(originalPolygon);
        polygon.setTextureVertexIndices(originalPolygon);
        polygon.setNormalIndices(originalPolygon);


        Polygon polygon1 = new Polygon();
        polygon1.setVertexIndices(TestConst.firstPolygon);
        polygon1.setTextureVertexIndices(TestConst.firstPolygon);
        polygon1.setNormalIndices(TestConst.firstPolygon);


        Polygon polygon2 = new Polygon();
        polygon2.setVertexIndices(TestConst.secondPolygon);
        polygon2.setTextureVertexIndices(TestConst.secondPolygon);
        polygon2.setNormalIndices(TestConst.secondPolygon);


        List<Polygon> result = Triangulation.triangulation(polygon);
        Assertions.assertTrue(comparisonPolygon(polygon1, result.get(0)));
        Assertions.assertTrue(comparisonPolygon(polygon1, result.get(0)));
    }

    @Test
    public void getTriangulatedIndicesTest() {

        ArrayList<Integer> originalPolygon1 = new ArrayList<>(Arrays.asList(1, 3, 4, 2));
        List<Integer> modifiedIndices = Triangulation.getTriangulatedIndices(originalPolygon1, 1);
        Assertions.assertArrayEquals(modifiedIndices.toArray(), TestConst.listTestTriangulatedIndices.toArray());

    }
    static class TestConst {
        final static ArrayList<Integer> firstPolygon = new ArrayList<>(Arrays.asList(1, 3, 4));
        final static ArrayList<Integer> secondPolygon = new ArrayList<>(Arrays.asList(1, 4, 2));
        final static ArrayList<Integer> listTestTriangulatedIndices = new ArrayList<>(Arrays.asList(1, 3, 4));
    }
}