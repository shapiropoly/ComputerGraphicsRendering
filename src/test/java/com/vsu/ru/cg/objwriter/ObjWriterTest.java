package com.vsu.ru.cg.objwriter;

import com.vsu.ru.cg.math.vector.Vector2f;
import com.vsu.ru.cg.math.vector.Vector3f;
import com.vsu.ru.cg.model.Model;
import com.vsu.ru.cg.objreader.ObjReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.vsu.ru.cg.objwriter.ObjWriter.*;
import static org.junit.jupiter.api.Assertions.*;

class ObjWriterTest {

    @Test
    void writeVertex() {
        Vector3f v1 = new Vector3f(1.01f, 2.02f, 3.03f);
        ArrayList<Vector3f> listVertexes = new ArrayList<>(List.of(v1));
        String outputLine = writerVertices(listVertexes);
        assertEquals(TestConst.lineInputWriteVertex, outputLine);
    }


    @Test
    void writeTextureVertex() {
        Vector2f v1 = new Vector2f(4.05f, 6.08f);
        ArrayList<Vector2f> listVertexes = new ArrayList<>(List.of(v1));
        String outputLine = writerTextureVertices(listVertexes);
        assertEquals(TestConst.lineInputWriteTextureVertex, outputLine);
    }


    @Test
    void writeNormalVertex() {
        Vector3f v1 = new Vector3f(2.06f, 3.05f, 6.04f);
        ArrayList<Vector3f> listVertexes = new ArrayList<>(List.of(v1));
        String outputLine = writerNormalVertices(listVertexes);
        assertEquals(TestConst.lineInputWriteNormalVertex, outputLine);
    }


    @Test
    void writePolygon() {
        List<Integer> vertexIndices = new ArrayList<>(Arrays.asList(0, 1, 2));
        List<Integer> textureVertexIndices = new ArrayList<>(Arrays.asList(3, 2, 4));
        List<Integer> normalVertexIndices = new ArrayList<>(Arrays.asList(1, 2, 3));
        String inputPolygon = "";
        inputPolygon = ObjWriter.polygonToString(vertexIndices, textureVertexIndices, normalVertexIndices);
        assertEquals(TestConst.linePolygon, inputPolygon);
    }


    static class TestConst {
        final static String lineInputWriteVertex = "v 1.01 2.02 3.03" + "\n";
        final static String lineInputWriteTextureVertex = "vt 4.05 6.08" + "\n";
        final static String lineInputWriteNormalVertex = "vn 2.06 3.05 6.04" + "\n";
        final static String linePolygon = "f 1/4/2 2/3/3 3/5/4";
    }

}