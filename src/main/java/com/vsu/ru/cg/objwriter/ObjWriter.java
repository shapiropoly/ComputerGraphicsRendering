package com.vsu.ru.cg.objwriter;

import com.vsu.ru.cg.math.vector.Vector2f;
import com.vsu.ru.cg.math.vector.Vector3f;
import com.vsu.ru.cg.model.Model;
import com.vsu.ru.cg.model.model_components.*;
import java.io.*;
import java.util.*;

public class ObjWriter {
    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    public static void write(String fileName, Model model)  {
        if (model == null || model.isEmpty()) {
            throw new IllegalArgumentException("Invalid model for writing");
        }

        File file = new File(fileName);

        try {
            if (file.createNewFile()) {
                System.out.println("File created");
            }
        } catch (IOException e) {
            throw new ObjWriterException("Error creating the file: " + fileName + " " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writerVertices(writer, model.vertices);
            writerTextureVertices(writer, model.textureVertices);
            writerNormalVertices(writer, model.normals);
            writerPolygons(writer, model.polygons);
        } catch (IOException e) {
            throw new ObjWriterException(e.getMessage());
        }
    }

    protected static String writerVertices(List<Vector3f> vertices) {
        StringBuilder builder = new StringBuilder();
        for (Vector3f v : vertices) {
            builder.append(OBJ_VERTEX_TOKEN + " " + v.x + " " + v.y + " " + v.z).append("\n");
        }
        return builder.toString();
    }

    protected static void writerVertices(PrintWriter writer, List<Vector3f> vertices) {
        for (Vector3f v : vertices) {
            writer.write(OBJ_VERTEX_TOKEN + " " + v.x + " " + v.y + " " + v.z);
            writer.write("\n");
        }
    }

    protected static String writerTextureVertices(List<Vector2f> textureTextureVertices) {
        StringBuilder builder = new StringBuilder();
        for (Vector2f v : textureTextureVertices) {
            builder.append(OBJ_TEXTURE_TOKEN + " " + v.x + " " + v.y).append("\n");
        }
        return builder.toString();
    }

    protected static void writerTextureVertices(PrintWriter writer, List<Vector2f> textureVertices) {
        for (Vector2f v : textureVertices) {
            writer.write(OBJ_TEXTURE_TOKEN + " " + v.x + " " + v.y);
            writer.write("\n");
        }
    }

    protected static void writerNormalVertices(PrintWriter writer, List<Vector3f> textureNormalVertices) {
        for (Vector3f v : textureNormalVertices) {
            writer.write(OBJ_NORMAL_TOKEN + " " + v.x + " " + v.y + " " + v.z);
            writer.write("\n");
        }
    }

    protected static String writerNormalVertices(List<Vector3f> vertices) {
        StringBuilder builder = new StringBuilder();
        for (Vector3f v : vertices) {
            builder.append(OBJ_NORMAL_TOKEN + " " + v.x + " " + v.y + " " + v.z).append("\n");
        }
        return builder.toString();
    }

    protected static void writerPolygons(PrintWriter writer, List<Polygon> polygons) throws IOException {
        for (Polygon polygon : polygons) {
            List<Integer> vertexIndices = polygon.getVertexIndices();
            List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
            List<Integer> normalVertexIndices = polygon.getNormalIndices();

            writer.print(polygonToString(vertexIndices, textureVertexIndices, normalVertexIndices));
            writer.println();
        }
    }

    protected static String polygonToString(List<Integer> vertexIndices, List<Integer> textureVertexIndices, List<Integer> normalVertexIndices) {
        StringBuilder builder = new StringBuilder(OBJ_FACE_TOKEN + " ");

        for (int i = 0; i < vertexIndices.size(); i++) {
            if (!textureVertexIndices.isEmpty()) {
                builder.append(vertexIndices.get(i) + 1).append("/").append(textureVertexIndices.get(i) + 1);
            } else {
                builder.append(vertexIndices.get(i) + 1);
            }

            if (!normalVertexIndices.isEmpty()) {
                if (textureVertexIndices.isEmpty()) {
                    builder.append("/");
                }
                builder.append("/").append(normalVertexIndices.get(i) + 1);
            }
            builder.append(" ");
        }
        return builder.toString().trim();
    }
}
