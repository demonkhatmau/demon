package com.demon.config.exception;

public class ConfigNotFoundException extends Exception {
    public static final long serialVersionUID = 5807063290983290141L;

    public ConfigNotFoundException(String message) {
        super(message);
    }
}
