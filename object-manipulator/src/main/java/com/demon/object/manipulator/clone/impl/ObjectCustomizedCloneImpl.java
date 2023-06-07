package com.demon.object.manipulator.clone.impl;

import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.clone.ICustomizedClone;
import com.demon.object.manipulator.exception.CustomizedCloneException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ObjectCustomizedCloneImpl<M> implements ICustomizedClone<M> {
    private final static ObjectCustomizedCloneImpl objectCustomizedClone = new ObjectCustomizedCloneImpl();
    private final static ArrayCustomizedCloneImpl arrayCustomizedClone = new ArrayCustomizedCloneImpl();
    private final static CollectionCustomizedCloneImpl collectionCustomizedClone = new CollectionCustomizedCloneImpl();
    private final static MapCustomizedCloneImpl mapCustomizedClone = new MapCustomizedCloneImpl();

    @Override
    public M performCustomizedClone(M source, boolean deepClone) throws CustomizedCloneException {
        M clonedResult;
        try {
            System.out.println(source.getClass().getName());
            clonedResult = (M) ReflectionUtils.createNewInstance(source);
            List<Field> listFields = ReflectionUtils.getAllFields(source.getClass());
            for (Field f : listFields) {
                Class<?> type = f.getType();
                Object fieldValue = f.get(source);
                if (null == fieldValue) {
                    continue;
                }
                if (!deepClone) {
                    f.set(clonedResult, fieldValue);
                } else {
                    if (ReflectionUtils.isPrimitiveWrapperOrString(type)) {
                        f.set(clonedResult, fieldValue);
                    } else if (ReflectionUtils.isArray(type)) {
                        Object[] arrayResult = arrayCustomizedClone.performCustomizedClone((Object[]) source, true);
                        f.set(clonedResult, arrayResult);
                    } else if (ReflectionUtils.isCollection(type)) {
                        Collection collectionResult = collectionCustomizedClone.performCustomizedClone((Collection) source, true);
                        f.set(clonedResult, collectionResult);
                    } else if (ReflectionUtils.isCMap(type)) {
                        Map mapResult = mapCustomizedClone.performCustomizedClone((Map) source, true);
                        f.set(clonedResult, mapResult);
                    } else {
                        Object objectResult = objectCustomizedClone.performCustomizedClone(source, true);
                        f.set(clonedResult, objectResult);
                    }
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(e.getMessage());
            throw new CustomizedCloneException(e.getMessage());
        }
        return clonedResult;
    }
}
