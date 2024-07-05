package com.vsu.ru.cg.gui;

import com.vsu.ru.cg.model.Model;
import com.vsu.ru.cg.model.model_components.Texture;

import com.vsu.ru.cg.render_engine.Camera;
import com.vsu.ru.cg.render_engine.RenderEngine;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashMap;

public class Scene {

    private String curModel;
    private String curCamera;
    private HashMap<String, Model> loadedModels = new HashMap<>();
    private HashMap<String, Camera> cameras = new HashMap<>();
    private HashMap<String, Texture> textures = new HashMap<>();


    public Integer getCameraCount() {
        return cameras.keySet().size();
    }
    public void setCurrentCamera(String currentCamera) {
        this.curCamera = currentCamera;
    }
    public void addCamera(String name, Camera camera) {
        curCamera = name;
        cameras.put(name, camera);
    }
    public Camera getCurrentCamera() {
        return cameras.get(curCamera);
    }
    public void deleteCamera(String name){
        cameras.remove(name);
    }


    public Model getCurrentModelObject() {
        return loadedModels.get(curModel);
    }
    public String getCurrentModel() {
        return curModel;
    }
    public void setCurrentModel(String currentModel) {
        this.curModel = currentModel;
    }
    public String getModel() {
        return curModel;
    }
    public void setModel(String model) {
        this.curModel = model;
    }
    public void addModel(Model model, String name) {
        curModel = name;
        loadedModels.put(name, model);
        //this.setAffineTransformation(name, new AffineTransformation());
    }
    public void deleteAllModels() {
        loadedModels.clear();
    }
    public void deleteModel(String name) {
        loadedModels.remove(name);
    }


    public void addTexture(String name, Texture texture) {
        textures.put(name, texture);
    }
    public void deleteTexture(String name) {
        textures.remove(name);
    }


    public void render(GraphicsContext graphicsContext, Camera camera, int width, int height) {
        for (String key : loadedModels.keySet()) {
            Texture texture = textures.get(key);
            Model model = loadedModels.get(key);

            RenderEngine.render(graphicsContext, camera, loadedModels.get(key), width, height);
        }
    }
}
