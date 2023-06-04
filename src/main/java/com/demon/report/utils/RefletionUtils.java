package com.demon.report.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RefletionUtils {
    private RefletionUtils() {

    }

    /**
     *
     * @param clazz Class to get fields
     * @param recursive get from parents (super class)
     * @param depth depth to recursive (-1 to fully recursive and 0 to skip recursive)
     * @return
     */
    public static List<Field> getFields(Class<?> clazz, boolean recursive, int depth) {
        if (null == clazz) {
            throw  new IllegalArgumentException("RefletionUtils -> getFields is null");
        }
        List<Field> listFields = new ArrayList<>();
        if(recursive && (depth > 0 || depth == -1)) {
            Class<?> parentsClazz = clazz.getSuperclass();
            int currentDepth = depth;
            while (null != parentsClazz) {
                List<Field> listFieldsFromParents = getFields(parentsClazz, true, currentDepth--);
                listFields.addAll(listFieldsFromParents);
            }
        }
        Arrays.stream(clazz.getDeclaredFields()).forEach(f -> {
            if(!isStatic(f)) {
                f.setAccessible(true);
                listFields.add(f);
            }
        });
        return listFields;
    }

    public static List<Field> getFields(Class<?> clazz, boolean recursive) {
       return getFields(clazz, recursive, -1);
    }

    public static List<Field> getFields(Class<?> clazz) {
        return getFields(clazz, true, -1);
    }

    public static Object getFieldValue(Field field, Object target) throws IllegalAccessException {
        if(null == field) {
            throw  new IllegalArgumentException("RefletionUtils -> getFieldValue: field is null");
        }

        if(null == target) {
            throw  new IllegalArgumentException("RefletionUtils -> getFieldValue: target is null");
        }

        if(!field.isAccessible()) {
            field.setAccessible(true);
        }

        return field.get(target);
    }

    public static void setFieldValue(Field field, Object target, Object fieldValue) throws IllegalAccessException {
        if(null == field) {
            throw  new IllegalArgumentException("RefletionUtils -> getFieldValue: field is null");
        }

        if(null == target) {
            throw  new IllegalArgumentException("RefletionUtils -> getFieldValue: target is null");
        }

        if(null == fieldValue) {
            throw  new IllegalArgumentException("RefletionUtils -> getFieldValue: fieldValue is null");
        }

        if(!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(target, fieldValue);
    }

    private static boolean isStatic(Field f) {
        return Modifier.isStatic(f.getModifiers());
    }
}
