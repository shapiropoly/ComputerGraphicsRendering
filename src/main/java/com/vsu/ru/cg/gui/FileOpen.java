package com.vsu.ru.cg.gui;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.canvas.Canvas;

public class FileOpen {
    public class FileInfo {
        private final String content;
        private final String name;
        private final String path;

        public FileInfo(String content, String name, String path) {
            this.content = content;
            this.name = name;
            this.path = path;
        }

        public String getContent() {
            return content;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }
    }

    public class Directory {
        private final Path path;

        public Directory(Path path) {
            this.path = path;
        }

        public Path getPath() {
            return path;
        }
    }

    public FileInfo fileOpenModel(Canvas canvas) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        return fileInfo(canvas, fileChooser);
    }

    public Path fileOpenTexture(Canvas canvas) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Texture");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return null;
        }

        return Path.of(file.getAbsolutePath());
    }

    public int[] fileOpenDeleteVertices(Canvas canvas) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File (*.txt)", "*.txt"));
        fileChooser.setTitle("Delete Vertices");

        FileInfo fileInfo = fileInfo(canvas, fileChooser);
        if (fileInfo == null) {
            return null;
        }

        if (fileInfo.getContent() == null) {
            AlertInfo.info("Файл пустой", "Файл, который пытаетесь открыть - пустой", "Выберите новый файл");
        }

        String[] numberStrings = fileInfo.getContent().split("\n");

        int[] intArray = new int[numberStrings.length];

        for (int i = 0; i < numberStrings.length; i++) {
            intArray[i] = Integer.parseInt(numberStrings[i]);
        }

        return intArray;
    }

    private FileInfo fileInfo(Canvas canvas, FileChooser fileChooser) {
        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return null;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            return new FileInfo(Files.readString(fileName), file.getName(), file.getAbsolutePath());
            // todo: обработка ошибок
        } catch (IOException exception){
            AlertInfo.error(exception, getClass().getSimpleName());
            return null;
        }
    }

    public Directory directoryOpen(Canvas canvas) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Load Directory");

        File directory = directoryChooser.showDialog((Stage) canvas.getScene().getWindow());
        if (directory == null) {
            return null;
        }
        return new Directory(Paths.get(directory.getAbsolutePath()));
    }

}
