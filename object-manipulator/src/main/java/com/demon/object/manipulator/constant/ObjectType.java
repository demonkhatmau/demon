package com.demon.object.manipulator.constant;

public enum ObjectType {
    OBJECT("OBJECT"),
    ARRAY("ARRAY"),
    COLLECTION("COLLECTION"),
    MAP("MAP");
    private String type;
    private ObjectType(String type) {
        this.type = type;
    }
}
