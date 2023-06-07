package com.demon.object.manipulator.path.impl;


import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.path.IPathResult;
import com.demon.object.manipulator.path.ISubPathProcessor;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ObjectSubPathProcessor<I> implements ISubPathProcessor<I> {
    @Override
    public IPathResult processSubPath(I input, String path, String subPath) throws NoSuchFieldException, IllegalAccessException {
        if (!ReflectionUtils.isArrayOrCollection(input)) {
            Object fieldValue = ReflectionUtils.getFieldValue(input, subPath);
            return SinglePathResult.builder().path(path).subPath(subPath).data(fieldValue).build();
        }
        if (ReflectionUtils.isArray(input)) {
            Object[] objectsArr = (Object[]) input;
            Collection<Object> collectionDatas =
                    Arrays.stream(objectsArr).map(o -> {
                        try {
                            return ReflectionUtils.getFieldValue(o, subPath);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            return null;
                        }
                    }).collect(Collectors.toList());
            return CollectionPathResult.builder().path(path).subPath(subPath).data(collectionDatas).build();
        }
        Collection<Object> objectsCol = (Collection) input;
        Collection<Object> collectionData =
                objectsCol.stream().map(o -> {
                    try {
                        return ReflectionUtils.getFieldValue(o, subPath);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        return null;
                    }
                }).collect(Collectors.toList());
        return CollectionPathResult.builder().path(path).subPath(subPath).data(collectionData).build();
    }
}
