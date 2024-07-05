package com.vsu.ru.cg.render_engine;

import java.util.ArrayList;

import com.vsu.ru.cg.math.vector.Vector3f;
import com.vsu.ru.cg.model.utils.ModelUtils;
import com.vsu.ru.cg.triangulation.Triangulation;
import javafx.scene.canvas.GraphicsContext;
import javax.vecmath.Point2f;
import com.vsu.ru.cg.math.matrix.*;
import com.vsu.ru.cg.model.Model;
import static com.vsu.ru.cg.render_engine.GraphicConveyor.*;

public class RenderEngine {
    public static void render(
            final GraphicsContext graphicsContext,
            final Camera camera,
            final Model mesh,
            final int width,
            final int height) {
        Matrix4f modelMatrix = rotateScaleTranslate();
        Matrix4f viewMatrix = camera.getViewMatrix();
        Matrix4f projectionMatrix = camera.getProjectionMatrix();

        Matrix4f modelViewProjectionMatrix = new Matrix4f(modelMatrix);
        modelViewProjectionMatrix.mul(viewMatrix);
//        System.out.println(Arrays.toString(modelViewProjectionMatrix.val));
//        System.out.println(Arrays.toString(projectionMatrix.val));
        modelViewProjectionMatrix.mul(projectionMatrix);


        //TODO
        Triangulation.triangulationModel(mesh);

        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                Vector3f vertex = mesh.vertices.get(mesh.polygons.get(polygonInd).getVertexIndices().get(vertexInPolygonInd));

                Vector3f vertexVecmath = new Vector3f(vertex.x, vertex.y, vertex.z);

                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, vertexVecmath), width, height);
                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).x,
                        resultPoints.get(vertexInPolygonInd - 1).y,
                        resultPoints.get(vertexInPolygonInd).x,
                        resultPoints.get(vertexInPolygonInd).y);
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).x,
                        resultPoints.get(nVerticesInPolygon - 1).y,
                        resultPoints.get(0).x,
                        resultPoints.get(0).y);
        }
    }
}