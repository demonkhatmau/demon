package com.demon.object.manipulator.exception;

import lombok.Getter;

public class InvalidPathFromObject extends Exception {
    @Getter
    private String invalidPathMessage;

    public InvalidPathFromObject(String path, String subPath) {
        super();
        invalidPathMessage = "Invalid path: " + path + ", at sub path: " + subPath;
    }

    public InvalidPathFromObject(String errorMessage) {
        super(errorMessage);
        invalidPathMessage = errorMessage;
    }
}
