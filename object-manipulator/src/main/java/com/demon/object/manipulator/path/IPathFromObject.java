package com.demon.object.manipulator.path;

import com.demon.object.manipulator.exception.InvalidPathFromObject;

public interface IPathFromObject <SOURCE> {

    String getPath() throws InvalidPathFromObject;

    IPathResult getValueFromPath(SOURCE input) throws InvalidPathFromObject, NoSuchFieldException, IllegalAccessException;
}
