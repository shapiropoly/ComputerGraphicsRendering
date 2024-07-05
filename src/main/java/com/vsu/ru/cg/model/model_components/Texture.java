package com.vsu.ru.cg.model.model_components;

import com.vsu.ru.cg.math.vector.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class Texture {
    int[][] pixels;
    Path texturePath;

    Color color = null;
    int width = 0;
    int height = 0;

    public Texture(Path texturePath) {
        this.texturePath = texturePath;
        readPixels();
    }

    public Texture(Color color){
        this.color = color;
        this.texturePath = null;
    }

    private void readPixels(){
        try {
            BufferedImage bufferedImage = ImageIO.read(texturePath.toFile());
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
            pixels = new int[height][width];

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    pixels[Math.abs(row-height+1)][col] = bufferedImage.getRGB(col, row);
                }
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }

    public int getPixel(Vector2f v){
        if(color != null){
            return color.getRGB();
        }
        int width = (int) (this.width * v.x);
        int height = (int) (this.height * v.y);
        if (width >= pixels[0].length || height >= pixels.length){
            return 0;
        }
        return pixels[height][width];
    }


}
