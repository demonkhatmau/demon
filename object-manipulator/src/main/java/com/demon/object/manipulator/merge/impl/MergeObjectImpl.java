package com.demon.object.manipulator.merge.impl;

import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.exception.MergeObjectException;
import com.demon.object.manipulator.merge.IMergeObject;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MergeObjectImpl<M> implements IMergeObject<M> {
    public static final MergeObjectImpl mergeObject = new MergeObjectImpl();

    @Override
    public M performMerge(M dest, M source) throws MergeObjectException {
        List<Field> listFields = ReflectionUtils.getAllFields(dest.getClass());
        for (Field f : listFields) {
            Class<?> type = f.getType();
            try {
                Object destValue = f.get(dest);
                Object sourceValue = f.get(source);
                if (null != sourceValue) {
                    if (null == destValue) {
                        f.set(dest, sourceValue);
                    } else {
                        if (ReflectionUtils.isPrimitiveWrapperOrString(type)) {
                            f.set(dest, sourceValue);
                        } else if (ReflectionUtils.isCollection(type)) {
                            Collection collectionResult = MergeCollectionImpl.mergeCollection.performMerge((Collection) destValue, (Collection) sourceValue);
                            f.set(dest, collectionResult);
                        } else if (ReflectionUtils.isArray(type)) {
                            Object[] arrayResult = MergeArrayImpl.mergeArray.performMerge((Object[]) destValue, (Object[]) sourceValue);
                            f.set(dest, arrayResult);
                        } else if (ReflectionUtils.isMap(type)) {
                            Map<?, ?> mapResult = MergeMapImpl.mergeMap.performMerge((Map) destValue, (Map) sourceValue);
                            f.set(dest, mapResult);
                        } else {
                            Object objectResult = mergeObject.performMerge(destValue, sourceValue);
                            f.set(dest, objectResult);
                        }
                    }
                }

            } catch (IllegalAccessException e) {
                throw new MergeObjectException(e.getMessage());
            }
        }
        return dest;
    }
}
