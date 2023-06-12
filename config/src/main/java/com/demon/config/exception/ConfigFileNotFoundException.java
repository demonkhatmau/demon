package com.demon.config.exception;

public class ConfigFileNotFoundException extends Exception {
    public static final long serialVersionUID = 7066322351249579256L;

    public ConfigFileNotFoundException(String message) {
        super(message);
    }
}
