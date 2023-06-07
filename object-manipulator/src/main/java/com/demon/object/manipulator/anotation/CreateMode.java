package com.demon.object.manipulator.anotation;

public @interface CreateMode {

    Mode mode() default Mode.Singleton;


    static enum Mode {
        Singleton,
        Prototype
    }
}
