package com.vsu.ru.cg.model.model_components;

import com.vsu.ru.cg.math.vector.Vector2f;

public class TextureVertex {
    private Vector2f coordinates;

    public TextureVertex(Vector2f coordinates) {
        this.coordinates = coordinates;
    }

    public Vector2f getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Vector2f coordinates) {
        this.coordinates = coordinates;
    }
}
