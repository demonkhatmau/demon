package com.demon.object.manipulator.path.impl;

import com.demon.object.manipulator.path.IPathResult;
import lombok.Builder;

@Builder
public class SinglePathResult<O> implements IPathResult<O> {
    private String path;
    private String subPath;
    private O data;

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getSubPath() {
        return this.subPath;
    }

    @Override
    public O getPathResult() {
        return this.data;
    }

    @Override
    public boolean isCollectionResult() {
        return false;
    }

    @Override
    public String toString() {
        return "SinglePathResult{" +
                "path='" + path + '\'' +
                ", subPath='" + subPath + '\'' +
                ", data=" + data +
                '}';
    }
}
