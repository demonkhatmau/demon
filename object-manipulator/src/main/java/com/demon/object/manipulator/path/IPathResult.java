package com.demon.object.manipulator.path;

public interface IPathResult <O> {
    String getPath();
    String getSubPath();
    O getPathResult();
    boolean isCollectionResult();
}
