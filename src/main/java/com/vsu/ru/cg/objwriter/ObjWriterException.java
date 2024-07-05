package com.vsu.ru.cg.objwriter;

public class ObjWriterException extends RuntimeException {
    public ObjWriterException(String errorMessage) {
        super("Error writing OBJ file:" + errorMessage);
    }
}