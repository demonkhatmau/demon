package com.demon.object.manipulator.path.impl;


import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.exception.InvalidPathFromObject;
import com.demon.object.manipulator.path.IPathResult;
import com.demon.object.manipulator.path.ISubPathProcessor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ArraySubPathProcessor<I> implements ISubPathProcessor<I> {
    private final static char START_OF_ARRAY = '[';

    private final static char END_OF_ARRAY = ']';

    private final ObjectSubPathProcessor<Object> objectSubPathProcessor = new ObjectSubPathProcessor<>();

    @Override
    public IPathResult processSubPath(I input, String path, String subPath) throws NoSuchFieldException, IllegalAccessException, InvalidPathFromObject {
        int indexOfStartArray = StringUtils.indexOf(subPath, START_OF_ARRAY);
        int indexOfEndArray = StringUtils.indexOf(subPath, END_OF_ARRAY);
        String fieldNameWithOutArrayMark = StringUtils.substring(subPath, 0, indexOfStartArray);
        Object fieldValue;
        if (!ReflectionUtils.isArrayOrCollection(input)) {
            fieldValue = ReflectionUtils.getFieldValue(input, fieldNameWithOutArrayMark);
        } else {
            if (ReflectionUtils.isArray(input)) {
                Object[] objectsArr = (Object[]) input;
                fieldValue = Arrays.stream(objectsArr).map(o -> {
                    try {
                        return objectSubPathProcessor.processSubPath(o, path, fieldNameWithOutArrayMark).getPathResult();
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        return null;
                    }
                }).collect(Collectors.toList()).toArray();
            } else {
                Collection objectCollections = (Collection) input;
                fieldValue = objectCollections.stream().map(o -> {
                    try {
                        return objectSubPathProcessor.processSubPath(o, path, fieldNameWithOutArrayMark).getPathResult();
                    } catch (NoSuchFieldException |  IllegalAccessException e) {
                        return null;
                    }
                }).collect(Collectors.toList());
            }
        }

        if (!ReflectionUtils.isArrayOrCollection(fieldValue)) {
            throw new InvalidPathFromObject("Invalid path: " + path + ". An array sub path but object returned is not array or collection");
        }
        boolean isArray = ReflectionUtils.isArray(fieldValue);
        boolean isCollection = ReflectionUtils.isCollection(fieldValue);

        //Get element at index
        if (indexOfStartArray != indexOfEndArray - 1) {
            String indexAsString = StringUtils.substring(subPath, indexOfStartArray + 1, indexOfEndArray);
            Integer index = null;

            if (NumberUtils.isDigits(indexAsString)) {
                index = NumberUtils.createInteger(indexAsString);
            }

            if (null == index || index < 0) {
                throw new InvalidPathFromObject("Invalid path " + path + ". Index in array path can not be null or less than 0");
            }

            if (isArray) {
                Object data = ArrayUtils.get((Object[]) fieldValue, index);
                return SinglePathResult.builder()
                        .path(path)
                        .subPath(subPath)
                        .data(data)
                        .build();
            }
            if (isCollection) {
                Object data = ((Collection) fieldValue).toArray()[index];
                return SinglePathResult.builder()
                        .path(path)
                        .subPath(subPath)
                        .data(data)
                        .build();
            }
        } else {
            if (isArray) {
                return CollectionPathResult.builder()
                        .path(path)
                        .subPath(subPath)
                        .data(Arrays.stream(((Object[]) fieldValue))
                                .collect(Collectors.toList()))
                        .build();
            }
            if (isCollection) {
                return CollectionPathResult.builder()
                        .path(path)
                        .subPath(subPath)
                        .data((Collection) fieldValue)
                        .build();
            }
        }
        return SinglePathResult.builder().path(path).subPath(subPath).data(fieldValue).build();
    }
}
