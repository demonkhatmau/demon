package com.demon.object.manipulator.path;


import com.demon.object.manipulator.exception.InvalidPathFromObject;

public interface ISubPathProcessor <I> {
    IPathResult processSubPath(I input,String path, String subPath) throws NoSuchFieldException, IllegalAccessException, InvalidPathFromObject;
}
