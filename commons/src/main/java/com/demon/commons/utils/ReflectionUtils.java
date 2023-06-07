package com.demon.commons.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@UtilityClass
public class ReflectionUtils {
    private static Map<String, Class<?>> MAP_PRIMITIVE_WRAPPER_STRING = new HashMap<>();

    private static Map<String, Object> MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING = new HashMap<>();

    static {
        MAP_PRIMITIVE_WRAPPER_STRING.put(byte.class.getName(), byte.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(short.class.getName(), short.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(int.class.getName(), int.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(long.class.getName(), long.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(float.class.getName(), float.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(double.class.getName(), double.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(char.class.getName(), char.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(boolean.class.getName(), boolean.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(void.class.getName(), void.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Byte.class.getName(), Byte.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Short.class.getName(), Short.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Integer.class.getName(), Integer.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Long.class.getName(), Long.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Float.class.getName(), Float.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Double.class.getName(), Double.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Character.class.getName(), Character.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Boolean.class.getName(), Boolean.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(Void.class.getName(), Void.class);
        MAP_PRIMITIVE_WRAPPER_STRING.put(String.class.getName(), String.class);

        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(byte.class.getName(), Byte.MIN_VALUE);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(short.class.getName(), 0);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(int.class.getName(), 0);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(long.class.getName(), 0L);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(float.class.getName(), 0f);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(double.class.getName(), 0d);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(char.class.getName(), Character.MIN_VALUE);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(boolean.class.getName(), Boolean.FALSE);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(void.class.getName(), Void.TYPE);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Byte.class.getName(), Byte.MIN_VALUE);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Short.class.getName(), 0);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Integer.class.getName(), 0);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Long.class.getName(), 0L);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Float.class.getName(), 0f);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Double.class.getName(), 0d);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Character.class.getName(), Character.MIN_VALUE);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Boolean.class.getName(), Boolean.FALSE);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(Void.class.getName(), Void.class);
        MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.put(String.class.getName(), StringUtils.EMPTY);
    }

    public static boolean isPrimitiveWrapperOrString(Class<?> clazzToCheck) {
        return MAP_PRIMITIVE_WRAPPER_STRING.containsKey(clazzToCheck.getName());
    }

    public static List<Field> getAllFields(Class<?> clazzToGet) {
        List<Field> listFields = new ArrayList<>();
        Class<?> superClass = clazzToGet.getSuperclass();
        while (null != superClass && !superClass.equals(Object.class) && !superClass.isInterface()) {
            listFields.addAll(getAllFields(superClass));
            superClass = superClass.getSuperclass();
        }

        for (Field f : clazzToGet.getDeclaredFields()) {
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }

            boolean isStaticFinal = Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers());
            if (isStaticFinal) {
                continue;
            }
            listFields.add(f);
        }
        return listFields;
    }

    public static Map<String, Field> getMapFields(Class<?> clazzToGet) {
        return getAllFields(clazzToGet).stream().collect(Collectors.toMap(Field::getName, v -> v));
    }

    public static Object getFieldValue(Object source, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = source.getClass().getDeclaredField(fieldName);
        if (f.isAccessible()) {
            return f.get(source);
        }
        f.setAccessible(true);
        return f.get(source);
    }

    public static Class<?> getElementCollectionClass(Field field) {
        if (!isCollection(field.getType())) {
            return null;
        }
        Type type = field.getGenericType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            for (Type t : pt.getActualTypeArguments()) {
                if (t instanceof Class) {
                    return (Class) t;
                }
                if (t instanceof WildcardType) {
                    Type result = ((WildcardType) t).getUpperBounds()[0];
                    return (Class) result;
                }
            }
        }
        return null;
    }

    public static boolean isArrayOrCollection(Object source) {
        return isArray(source) || isCollection(source);
    }

    public static boolean isArray(Object source) {
        return (null != source) && (source.getClass().isArray());
    }

    public static boolean isArray(Class<?> clazzToCheck) {
        return (null != clazzToCheck) && (clazzToCheck.isArray());
    }

    public static boolean isCollection(Object source) {
        return (null != source) && (source instanceof Collection || Collection.class.isAssignableFrom(source.getClass()));
    }

    public static boolean isCollection(Class<?> clazzToCheck) {
        return (null != clazzToCheck) && (Collection.class.equals(clazzToCheck) || Collection.class.isAssignableFrom(clazzToCheck));
    }

    public static boolean isMap(Object source) {
        return (null != source) && (source instanceof Map || Map.class.isAssignableFrom(source.getClass()));
    }

    public static boolean isCMap(Class<?> clazzToCheck) {
        return (null != clazzToCheck) && (Map.class.equals(clazzToCheck) || Map.class.isAssignableFrom(clazzToCheck));
    }

    public static Object getDefaultPrimitiveOrWrapperOrString(String clazzName) {
        return MAP_DEFAULT_PRIMITIVE_WRAPPER_STRING.get(clazzName);
    }

    public static <T> void setIfNotNull(Consumer<T> setter, T valueToSet) {
        if (!Objects.isNull(valueToSet)) {
            setter.accept(valueToSet);
        }
    }

    public static Constructor getDefaultConstructor(Object objectToGet) {
        return Arrays.stream(objectToGet.getClass().getDeclaredConstructors()).filter(p -> p.getParameterCount() == 0 && Modifier.isPublic(p.getModifiers())).findFirst().orElse(null);
    }

    public static Object createNewInstance(Object objectToCreate) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Constructor cons = getDefaultConstructor(objectToCreate);
        if (null == cons) {
            return objectToCreate.getClass().newInstance();
        }else{
            return cons.newInstance((Object[]) null);
        }
    }
}
