package com.demon.commons.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class PathUtils {
    private final static char START_OF_ARRAY = '[';

    private final static char END_OF_ARRAY = ']';

    private final static char START_OF_MAP = '<';

    private final static char END_OF_MAP = '>';



    public static boolean isArrayPath(String path) {
        int indexOfStartArray = StringUtils.indexOf(path, START_OF_ARRAY);
        int indexOfEndArray = StringUtils.indexOf(path, END_OF_ARRAY);
        return indexOfStartArray > -1 && indexOfEndArray > -1 && indexOfStartArray < indexOfEndArray;
    }

    public static boolean isObjectPath(String path) {
        return !StringUtils.contains(path, START_OF_ARRAY) && !StringUtils.contains(path, END_OF_ARRAY);
    }

    public static boolean isMapPath(String path) {
        int indexOfStartMap = StringUtils.indexOf(path, START_OF_MAP);
        int indexOfEndMap = StringUtils.indexOf(path, END_OF_MAP);
        return indexOfStartMap > -1 && indexOfEndMap > -1 && indexOfStartMap < indexOfEndMap;
    }
}
