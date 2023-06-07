package com.demon.object.manipulator.path.impl;

import com.demon.object.manipulator.path.IPathResult;
import lombok.Builder;

import java.util.Collection;

@Builder
public class CollectionPathResult<O>  implements IPathResult<Collection<O>> {
    private String path;
    private String subPath;
    private Collection<O> data;
    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public String getSubPath() {
        return this.subPath;
    }

    @Override
    public Collection<O> getPathResult() {
        return this.data;
    }

    @Override
    public boolean isCollectionResult() {
        return true;
    }

    public void addData(O dataToAdd){
        this.data.add(dataToAdd);
    }

    @Override
    public String toString() {
        return "CollectionPathResult{" +
                "path='" + path + '\'' +
                ", subPath='" + subPath + '\'' +
                ", data=" + data +
                '}';
    }
}
