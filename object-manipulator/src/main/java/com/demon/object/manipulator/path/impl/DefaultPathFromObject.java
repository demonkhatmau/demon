package com.demon.object.manipulator.path.impl;


import com.demon.commons.utils.PathUtils;
import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.exception.InvalidPathFromObject;
import com.demon.object.manipulator.path.IPathFromObject;
import com.demon.object.manipulator.path.IPathResult;
import lombok.Builder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Builder
public class DefaultPathFromObject<S> implements IPathFromObject<S> {
    private final static char PART_SEPARATOR = '.';

    private String rootName = "$";

    private String path;

    private final static char START_OF_ARRAY = '[';

    private final static char END_OF_ARRAY = ']';

    private final static char START_OF_MAP = '<';

    private final static char END_OF_MAP = '>';

    private final ObjectSubPathProcessor objectSubPathProcessor = new ObjectSubPathProcessor();

    private final ArraySubPathProcessor arraySubPathProcessor = new ArraySubPathProcessor();

    @Override
    public String getPath() throws InvalidPathFromObject {
        if (StringUtils.isBlank(path)) {
            throw new InvalidPathFromObject("Path from object can be null or empty");
        }
        return this.path;
    }

    @Override
    public IPathResult getValueFromPath(S input) throws InvalidPathFromObject, NoSuchFieldException, IllegalAccessException {
        String[] partsOfPath = StringUtils.split(path, PART_SEPARATOR);
        if (!StringUtils.startsWith(path, rootName)) {
            throw new InvalidPathFromObject("Path from object must start with: " + rootName);
        }

        if (partsOfPath.length < 2) {
            throw new InvalidPathFromObject("Invalid path " + path + ". Path from object must have at least 2 parts");
        }

        String currentPath = partsOfPath[0];
        Object tempSource = input;
        IPathResult finalResult = null;

        for (int i = 1; i < partsOfPath.length; i++) {
            currentPath = currentPath + PART_SEPARATOR + partsOfPath[i];
            if (PathUtils.isObjectPath(partsOfPath[i])) {
                finalResult = objectSubPathProcessor.processSubPath(tempSource, currentPath, partsOfPath[i]);
                tempSource = finalResult.getPathResult();
            }
            if (PathUtils.isArrayPath(partsOfPath[i])) {
                finalResult = arraySubPathProcessor.processSubPath(tempSource, currentPath, partsOfPath[i]);
                tempSource = finalResult.getPathResult();
            }
            if(PathUtils.isMapPath(partsOfPath[i])){
                throw new InvalidPathFromObject("Invalid path " + path + ". Map path is not supported yet");
            }

        }
        return finalResult;
    }

    private IPathResult processPath(String currentPath, Object tempSource, String totalPath) throws InvalidPathFromObject {
        IPathResult result = null;
        if (!isArrayPath(currentPath)) {
            tempSource = getValueOfField(currentPath, tempSource);
            result = SinglePathResult.builder().path(totalPath).data(tempSource).build();
        } else {
            int indexOfStartArray = StringUtils.indexOf(currentPath, START_OF_ARRAY);
            int indexOfEndArray = StringUtils.indexOf(currentPath, END_OF_ARRAY);
            String fieldNameWithOutArrayMark = StringUtils.substring(currentPath, 0, indexOfStartArray);
            tempSource = getValueOfField(fieldNameWithOutArrayMark, tempSource);
            boolean isArray = tempSource.getClass().isArray();
            boolean isCollection = tempSource instanceof Collection || Collection.class.isAssignableFrom(tempSource.getClass());
            if (indexOfStartArray != indexOfEndArray - 1) {
                String indexAsString = StringUtils.substring(currentPath, indexOfStartArray + 1, indexOfEndArray);
                Integer index = null;

                if (NumberUtils.isDigits(indexAsString)) {
                    index = NumberUtils.createInteger(indexAsString);
                }

                if (null == index || index < 0) {
                    throw new InvalidPathFromObject("Invalid path " + path + ". Index in array path can not be null or less than 0");
                }

                if (!isArray && !isCollection) {
                    throw new InvalidPathFromObject("Invalid path " + path + ". Current path: " + currentPath + " are array path but real class in object is not Array or Collection");
                }

                if (isArray) {
                    tempSource = ArrayUtils.get((Object[]) tempSource, index);
                    result = SinglePathResult.builder()
                            .path(totalPath)
                            .data(tempSource)
                            .build();
                }
                if (isCollection) {
                    tempSource = ((Collection) tempSource).toArray()[index];
                    result = SinglePathResult.builder()
                            .path(totalPath)
                            .data(tempSource)
                            .build();
                }
            } else {
                if (isArray) {
                    result = CollectionPathResult.builder()
                            .path(totalPath)
                            .data(Arrays.stream(((Object[]) tempSource))
                                    .collect(Collectors.toList()))
                            .build();
                }
                if (isCollection) {
                    result = CollectionPathResult.builder()
                            .path(totalPath)
                            .data((Collection) tempSource)
                            .build();
                }
            }
        }
        return result;
    }

    private Object getValueOfField(String pathToGet, Object tempSource) throws InvalidPathFromObject {
        try {
            tempSource = ReflectionUtils.getFieldValue(tempSource, pathToGet);
        } catch (NoSuchFieldException e) {
            throw new InvalidPathFromObject(e.getClass().getSimpleName() + ", " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new InvalidPathFromObject(e.getClass().getSimpleName() + ", " + e.getMessage());
        }
        return tempSource;
    }

    private boolean isArrayPath(String path) {
        int indexOfStartArray = StringUtils.indexOf(path, START_OF_ARRAY);
        int indexOfEndArray = StringUtils.indexOf(path, END_OF_ARRAY);

        return indexOfStartArray > -1 && indexOfEndArray > -1 && indexOfStartArray < indexOfEndArray;
    }


}
