package com.demon.config.exception;

public class InvalidConfigException extends Exception {
    public static final long serialVersionUID = 5030719115615855909L;

    public InvalidConfigException(String message) {
        super(message);
    }
}
