package com.vsu.ru.cg;

import com.vsu.ru.cg.gui.AlertInfo;
import com.vsu.ru.cg.gui.FileOpen;
import com.vsu.ru.cg.gui.Scene;
import com.vsu.ru.cg.gui.TreeViewController;
import com.vsu.ru.cg.model.Model;
import com.vsu.ru.cg.model.model_components.Texture;
import com.vsu.ru.cg.model.utils.DeleteVertices;
import com.vsu.ru.cg.model.utils.ModelUtils;
import com.vsu.ru.cg.objreader.ObjReader;
import com.vsu.ru.cg.objwriter.ObjWriter;
import com.vsu.ru.cg.render_engine.Camera;
import com.vsu.ru.cg.math.vector.*;

import com.vsu.ru.cg.triangulation.Triangulation;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;


public class GuiController {
    final private float TRANSLATION = 0.5F;
    final private float fovDelta = 0.05f;
    final private float maxFov = 3f;
    public static final float EPS = 1e-6f;

    @FXML
    public TextField cameraX;
    @FXML
    public TextField cameraY;
    @FXML
    public TextField cameraZ;
    public Button createCameraButton;
    @FXML
    private TextField moveZ;
    @FXML
    private TextField moveY;
    @FXML
    private TextField moveX;

    @FXML
    private ScrollPane topScrollPane;

    @FXML
    private TextField textureColorCode;

    @FXML
    private Canvas canvas;

    @FXML
    private AnchorPane anchorPaneCanvas;

    private Model mesh = null;

    private final TreeView<TreeViewController.ItemWrap> treeView = new TreeView<>();

    private final TreeViewController treeViewController = new TreeViewController(treeView);

    private double mouseX, mouseY;

    private final Scene scene = new Scene();

    private boolean darkTheme = true;

    private final Map<KeyCode, Runnable> keyActions = new HashMap<>();

    private boolean rotateButtonRotationFlag = false;
    private AnimationTimer rotationTimer;
    private float rotationAngle = 0.0f;
    private float rotationSpeed = 0.02f;
    private boolean isRotationTimerRunning = false;


    @FXML
    private void initialize() {
        treeViewController.initialize();

        Camera camera1 = new Camera(
                new Vector3f(0, 0, 100),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100);
        scene.addCamera("Камера 1", camera1);

        treeViewController.addItem(new TreeViewController.ItemWrap("Камера 1", TreeViewController.ItemType.CAMERA));

        anchorPaneCanvas.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPaneCanvas.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(120), event -> {

            double width = anchorPaneCanvas.getWidth();
            double height = anchorPaneCanvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            scene.getCurrentCamera().setAspectRatio((float) (width / height));
            if (scene.getCurrentModel() != null) {
                scene.render(canvas.getGraphicsContext2D(), scene.getCurrentCamera(), (int) width, (int) height);
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();

        topScrollPane.setContent(treeView);
        treeView.prefHeightProperty().bind(topScrollPane.heightProperty());
        treeView.prefWidthProperty().bind(topScrollPane.widthProperty());

        keyActions.put(KeyCode.UP, () -> scene.getCurrentCamera().movePosition(new Vector3f(0, TRANSLATION, 0)));
        keyActions.put(KeyCode.DOWN, () -> scene.getCurrentCamera().movePosition(new Vector3f(0, -TRANSLATION, 0)));
        keyActions.put(KeyCode.RIGHT, () -> scene.getCurrentCamera().movePosition(new Vector3f(-TRANSLATION, 0, 0)));
        keyActions.put(KeyCode.LEFT, () -> scene.getCurrentCamera().movePosition(new Vector3f(TRANSLATION, 0, 0)));
        keyActions.put(KeyCode.W, () -> scene.getCurrentCamera().movePosition(new Vector3f(0, 0, -TRANSLATION)));
        keyActions.put(KeyCode.S, () -> scene.getCurrentCamera().movePosition(new Vector3f(0, 0, TRANSLATION)));

        canvas.setOnKeyPressed(e -> {
            Runnable action = keyActions.get(e.getCode());
            if (action != null) {
                action.run();
            }
        });

        canvas.setOnMousePressed(this::handleMousePressed);
        handleWheelScroll();

        canvas.setFocusTraversable(true);
        canvas.setOnMouseClicked(event -> canvas.requestFocus());

        treeView.setOnKeyPressed(event -> {
            TreeViewController.ItemWrap itemWrap = treeViewController.getSelectedItem().getValue();
            if (event.getCode() == KeyCode.DELETE) {
                if (itemWrap.getItemType() == TreeViewController.ItemType.CAMERA) {
                    scene.deleteCamera(itemWrap.getName());
                } else {
                    scene.deleteModel(itemWrap.getName());
                }
                treeViewController.deleteSelectedItem();
            } else if (event.getCode() == KeyCode.ENTER) {
                if (itemWrap.getItemType() == TreeViewController.ItemType.CAMERA) {
                    scene.setCurrentCamera(itemWrap.getName());
                } else {
                    scene.setCurrentModel(itemWrap.getName());
                }
            }
        });

        treeView.setFocusTraversable(false);
        treeView.setOnMouseClicked(mouseEvent -> treeView.requestFocus());
    }
    private void handleWheelScroll() {
        anchorPaneCanvas.setOnScroll(scrollEvent -> {
            float newFov = scene.getCurrentCamera().getFov();

            if (scrollEvent.getDeltaY() < 0){
                if (newFov <= maxFov) {
                    newFov += fovDelta;
                }
            } else if (scrollEvent.getDeltaY() > 0) {
                if (newFov >= fovDelta) {
                    newFov -= fovDelta;
                }
            }
            scene.getCurrentCamera().setFov(newFov);
        });
    }

    private void handleMousePressed(javafx.scene.input.MouseEvent event) {
        var ref = new Object() {
            float prevX = (float) event.getX();
            float prevY = (float) event.getY();
        };
        canvas.setOnMouseDragged(mouseEvent -> {
            final float actualX = (float) mouseEvent.getX();
            final float actualY = (float) mouseEvent.getY();
            float dx = ref.prevX - actualX;
            final float dy = actualY - ref.prevY;
            final float dxy = Math.abs(dx) - Math.abs(dy);
            float dz = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

            if (dxy >= EPS && (scene.getCurrentCamera().getPosition().x <= EPS && dx < 0 ||
                    scene.getCurrentCamera().getPosition().x > EPS && dx > 0)) {
                dz *= -1;
            } else if (dxy < EPS) { //если больше перемещаем по y, то по z не перемещаем
                dz = 0;
            }
            if (scene.getCurrentCamera().getPosition().z <= EPS) {
                dx *= -1;
            }

            ref.prevX = actualX;
            ref.prevY = actualY;
            scene.getCurrentCamera().movePosition(new Vector3f(new float[]{dx * 0.5f, dy * 0.5f, dz * 0.5f}));
        });
    }
    @FXML
    public void handleUploadModel() {
        FileOpen.FileInfo file = new FileOpen().fileOpenModel(canvas);

        Model mesh = ObjReader.read(file.getContent());
        Triangulation.triangulationModel(mesh);

        scene.deleteAllModels();
        scene.addModel(mesh, file.getName());

        treeViewController.deleteAllObjects();
        treeViewController.addItem(new TreeViewController.ItemWrap(file.getName(), TreeViewController.ItemType.OBJECT));
    }

    @FXML
    public void handleAddModel() {
        FileOpen.FileInfo file = new FileOpen().fileOpenModel(canvas);
        Model mesh = ObjReader.read(file.getContent());
        Triangulation.triangulationModel(mesh);

        scene.addModel(mesh, file.getName());
        treeViewController.addItem(new TreeViewController.ItemWrap(file.getName(), TreeViewController.ItemType.OBJECT));
    }

    @FXML
    public void handleUploadTexture() {
        Path path = new FileOpen().fileOpenTexture(canvas);
        Texture texture = new Texture(path);
        scene.addTexture(scene.getCurrentModel(), texture);
        ModelUtils.enableTextureMode(scene.getCurrentModelObject());
    }

    @FXML
    public void handleCameraCreate(ActionEvent actionEvent) {
        float x = Float.parseFloat(cameraX.getText());
        float y = Float.parseFloat(cameraY.getText());
        float z = Float.parseFloat(cameraZ.getText());

        Camera camera =  new Camera(
                new Vector3f(x, y, z),
                new Vector3f(0, 0, 0),
                1.0F, 1, 0.01F, 100);

        int countCameras = scene.getCameraCount() + 1;
        String name = "Камера " + countCameras;
        scene.addCamera(name, camera);
        treeViewController.addItem(new TreeViewController.ItemWrap(name, TreeViewController.ItemType.CAMERA));
    }

    public void handleModelMove(ActionEvent actionEvent) {
        float x = Float.parseFloat(moveX.getText());
        float y = Float.parseFloat(moveY.getText());
        float z = Float.parseFloat(moveZ.getText());
    }

    public void handleModelRotate(ActionEvent actionEvent) {
    }

    public void handleModelScale(ActionEvent actionEvent) {
    }

    @FXML
    public void handleColorTexture(ActionEvent actionEvent) {
        Texture texture = new Texture(Color.decode(textureColorCode.getText()));
        scene.addTexture(scene.getCurrentModel(), texture);
    }

    @FXML
    public void handleDeleteTexture() {
        scene.deleteTexture(scene.getCurrentModel());
        ModelUtils.disableTextureMode(scene.getCurrentModelObject());
    }

    @FXML
    public void handleExportModel() throws IOException {
        Model mesh = scene.getCurrentModelObject();
        String name = scene.getCurrentModel();

        FileOpen.Directory dir = new FileOpen().directoryOpen(canvas);
        Path sourcePath = dir.getPath().resolve(name);

        Path tempFilePath = Files.createTempFile(null, ".obj"); // временный файл

        ObjWriter.write(tempFilePath.toString(), mesh);

        Files.move(tempFilePath, sourcePath, StandardCopyOption.REPLACE_EXISTING); // замена исходника временным
    }

    @FXML
    public void handleDeleteVertices() {
        int[] vertices = new FileOpen().fileOpenDeleteVertices(canvas);
        DeleteVertices.removeVertices(scene.getCurrentModelObject(), vertices);
    }

    public void handleShowPanelTexture(ActionEvent actionEvent) {
    }

    public void handleShowPanelMesh(ActionEvent actionEvent) {
    }

    public void handleShowPanelLight(ActionEvent actionEvent) {
    }

    public void handleUseLight(ActionEvent actionEvent) {
    }

    public void handleShowMesh(ActionEvent actionEvent) {
    }

    @FXML
    public void handleInterfaceMode() {
        darkTheme = !darkTheme;
        if (!darkTheme) {
            canvas.getScene().getStylesheets().add(getClass().getResource("darkDesign.css").toExternalForm());
        } else {
            canvas.getScene().getStylesheets().remove(getClass().getResource("darkDesign.css").toExternalForm());
        }
    }

    @FXML
    public void handleInfo() {
        AlertInfo.info("Справка", "Обратная связь",
                "Ваши предложения по улучшению и информацию об ошибках можете отправить в Telegram: @shapiropoly");
    }

    @FXML
    private void handleRotateObject(ActionEvent actionEvent) {
        rotateButtonRotationFlag = !rotateButtonRotationFlag;

        if (rotateButtonRotationFlag) {
            // создание таймера
            if (!isRotationTimerRunning) {
                rotationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        rotationAngle += rotationSpeed;
                        scene.getCurrentCamera().movePosition(new Vector3f(new Vector3f(TRANSLATION, 0, 0)));
                    }
                };
                rotationTimer.start();
                isRotationTimerRunning = true;
            }
        } else {
            rotationAngle = 0.0f;

            // остановка таймера
            if (isRotationTimerRunning) {
                rotationTimer.stop();
                isRotationTimerRunning = false;
            }
        }
    }
}



//    @FXML
//    private void handleRotateObject(ActionEvent actionEvent) {
//        // TRANSLATION — rotationSpeed
//
//        float curX = scene.getCurrentCamera().getPosition().x;
//        float curY = scene.getCurrentCamera().getPosition().y;
//
//        float dx =
//        float dy =
//        final float dxy = Math.abs(dx) - Math.abs(dy);
//        float dz = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
//
//        if (dxy >= EPS && (scene.getCurrentCamera().getPosition().x <= EPS && dx < 0 ||
//                    scene.getCurrentCamera().getPosition().x > EPS && dx > 0)) {
//                dz *= -1;
//            } else if (dxy < EPS) { //если больше перемещаем по y, то по z не перемещаем
//                dz = 0;
//            }
//            if (scene.getCurrentCamera().getPosition().z <= EPS) {
//                dx *= -1;
//            }
//
//        if (Math.abs(dx) > Math.abs(dy)) {
//            float rotationAngle = dx * TRANSLATION;
//
//
//            scene.getCurrentCamera().movePosition(new Vector3f(new float[]{dx * 0.5f, dy * 0.5f, dz * 0.5f}));
////            scene.getCurrentCamera().movePosition(new Vector3f(TRANSLATION, 0, 0));
//        }
//
//    }


//        var ref = new Object() {
//            float prevX = (float) event.getX();
//            float prevY = (float) event.getY();
//        };
//
//        canvas.setOnMouseDragged(mouseEvent -> {
//            final float actualX = (float) mouseEvent.getX();
//            final float actualY = (float) mouseEvent.getY();
//            float dx = ref.prevX - actualX;
//            final float dy = actualY - ref.prevY;
//            final float dxy = Math.abs(dx) - Math.abs(dy);
//            float dz = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
//
//            if (dxy >= EPS && (scene.getCurrentCamera().getPosition().x <= EPS && dx < 0 ||
//                    scene.getCurrentCamera().getPosition().x > EPS && dx > 0)) {
//                dz *= -1;
//            } else if (dxy < EPS) { //если больше перемещаем по y, то по z не перемещаем
//                dz = 0;
//            }
//            if (scene.getCurrentCamera().getPosition().z <= EPS) {
//                dx *= -1;
//            }
//
//            ref.prevX = actualX;
//            ref.prevY = actualY;
//
//            // поворот вокруг оси
//            float rotationSpeed = 0.5f; // Adjust the rotation speed as needed
//            if (Math.abs(dx) > Math.abs(dy)) {
//                float rotationAngle = dx * rotationSpeed;
//                //Vector3f viewDirection = scene.getCurrentCamera().getViewDirection();
//                //scene.getCurrentCamera().setViewDirection(rotateVector(viewDirection, new Vector3f(1, 0, 0), rotationAngle));
//            }
//
//            // поворот камеры
//            scene.getCurrentCamera().movePosition(new Vector3f(new float[]{dx * 0.5f, dy * 0.5f, dz * 0.5f}));
//        });
//    }





