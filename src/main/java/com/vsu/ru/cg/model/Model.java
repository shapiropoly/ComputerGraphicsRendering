package com.vsu.ru.cg.model;
import com.vsu.ru.cg.math.vector.Vector2f;
import com.vsu.ru.cg.math.vector.Vector3f;
import com.vsu.ru.cg.model.model_components.Polygon;

import java.util.*;

public class Model {
    public boolean isMeshMode = true;
    public boolean isTextureMode = false;
    public boolean isLightedMode = false;
    public List<Vector3f> vertices;
    public List<Vector2f> textureVertices;
    public List<Vector3f> normals;
    public List<Polygon> polygons;

    public Model() {
    }

    public Model(List<Vector3f> vertices, List<Polygon> polygons) {
        this.vertices = vertices;
        this.polygons = polygons;
    }

    public Model(List<Vector3f> vertices, List<Vector2f> textureVertices, List<Vector3f> normals, List<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
    }

    public List<Vector3f> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vector3f> vertices) {
        this.vertices = vertices;
    }

    public List<Vector2f> getTextureVertices() {
        return textureVertices;
    }

    public void setTextureVertices(List<Vector2f> textureVertices) {
        this.textureVertices = textureVertices;
    }

    public List<Vector3f> getNormals() {
        return normals;
    }

    public void setNormals(List<Vector3f> normals) {
        this.normals = normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    public boolean isMeshMode() {
        return isMeshMode;
    }

    public void setMeshMode(boolean meshMode) {
        isMeshMode = meshMode;
    }

    public boolean isTextureMode() {
        return isTextureMode;
    }

    public void setTextureMode(boolean textureMode) {
        isTextureMode = textureMode;
    }

    public boolean isLightedMode() {
        return isLightedMode;
    }

    public void setLightedMode(boolean lightedMode) {
        isLightedMode = lightedMode;
    }
}
