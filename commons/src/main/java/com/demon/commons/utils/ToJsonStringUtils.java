package com.demon.commons.utils;


import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

@UtilityClass
public class ToJsonStringUtils {
    private final static String QUOTATION_MARK = "\"";
    private final static String COLON = ":";
    private final static String COMA = ",";
    private final static String START_OF_ARRAY = "[";
    private final static String END_OF_ARRAY = "]";
    private final static String START_OF_EMBRACE = "{";
    private final static String END_OF_EMBRACE = "}";

    public static String buildJsonFromClass(Class<?> clazzToBuild) {
        if (ReflectionUtils.isPrimitiveWrapperOrString(clazzToBuild)) {
            String defaultValueAsString = ReflectionUtils.getDefaultPrimitiveOrWrapperOrString(clazzToBuild.getName()).toString();
            return StringUtils.isNotBlank(defaultValueAsString) ? defaultValueAsString : "\"defaultStringValue\"";
        }
        StringBuilder jsonString = new StringBuilder();

        List<Field> listFields = ReflectionUtils.getAllFields(clazzToBuild);

        boolean isArray = clazzToBuild.isArray();

        appendStartOfJsonString(jsonString, isArray);

        int index = 0;
        for (Field field : listFields) {
            if (ReflectionUtils.isPrimitiveWrapperOrString(field.getType())) {
                String defaultValueAsString = ReflectionUtils.getDefaultPrimitiveOrWrapperOrString(field.getType().getName()).toString();
                defaultValueAsString = StringUtils.isNotBlank(defaultValueAsString) ? defaultValueAsString : "\"defaultStringValue\"";
                jsonString
                        .append(QUOTATION_MARK)
                        .append(field.getName())
                        .append(QUOTATION_MARK)
                        .append(COLON).
                        append(defaultValueAsString);
            } else if (ReflectionUtils.isCollection(field.getType())) {
                Class<?> elementClazz = ReflectionUtils.getElementCollectionClass(field);
                jsonString.append(QUOTATION_MARK)
                        .append(field.getName())
                        .append(QUOTATION_MARK)
                        .append(COLON).
                        append(START_OF_ARRAY).
                        append(buildJsonFromClass(elementClazz))
                        .append(END_OF_ARRAY);

            } else if (field.getType().isArray()) {
                Class<?> elementClazz = field.getType().getComponentType();
                jsonString.append(QUOTATION_MARK)
                        .append(field.getName())
                        .append(QUOTATION_MARK)
                        .append(COLON).
                        append(START_OF_ARRAY).
                        append(buildJsonFromClass(elementClazz))
                        .append(END_OF_ARRAY);
            } else {
                jsonString
                        .append(QUOTATION_MARK)
                        .append(field.getName())
                        .append(QUOTATION_MARK)
                        .append(COLON).
                        append(buildJsonFromClass(field.getType()));
            }
            index++;
            if (index < listFields.size()) {
                jsonString.append(COMA);
            }
        }

        appendEndOfJsonString(jsonString, isArray);

        return jsonString.toString();
    }

    private static void appendStartOfJsonString(StringBuilder jsonString, boolean isArray) {
        if (isArray) {
            jsonString.append(START_OF_ARRAY);
        } else {
            jsonString.append(START_OF_EMBRACE);
        }
    }

    private static void appendEndOfJsonString(StringBuilder jsonString, boolean isArray) {
        if (isArray) {
            jsonString.append(END_OF_ARRAY);
        } else {
            jsonString.append(END_OF_EMBRACE);
        }
    }
}
