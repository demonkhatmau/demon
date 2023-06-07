package com.demon.object.manipulator.merge.impl;


import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.exception.MergeObjectException;
import com.demon.object.manipulator.merge.IMergeObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MergeMapImpl<K, V> implements IMergeObject<Map<K, V>> {
    private Class<? extends Map> defaultMapClazz = HashMap.class;
    private IMergeObject<V> mergeValue = new MergeObjectImpl<>();

    @Override
    public Map<K, V> performMerge(Map<K, V> dest, Map<K, V> source) throws MergeObjectException {

        try {
            if (null == dest && null == source) {
                return defaultMapClazz.newInstance();
            }
            if (null == dest) {
                dest = defaultMapClazz.newInstance();
            }
            if (null == source) {
                source = defaultMapClazz.newInstance();
            }
            final Constructor destDefaultConstructor = ReflectionUtils.getDefaultConstructor(dest);
            final Map<K, V> tempMap = (null != destDefaultConstructor) ? (Map) destDefaultConstructor.newInstance(new Object[0]) : defaultMapClazz.newInstance();
            dest.entrySet().stream().forEach(e -> {
                tempMap.put(e.getKey(), e.getValue());
            });
            source.entrySet().stream().forEach(e -> {
                if (!tempMap.containsKey(e.getKey())) {
                    tempMap.put(e.getKey(), e.getValue());
                } else {
                    try {
                        V mergedValue = mergeValue.performMerge(tempMap.get(e.getKey()), e.getValue());
                        tempMap.put(e.getKey(), mergedValue);
                    } catch (MergeObjectException mergeObjectException) {
                    }
                }
            });
            return tempMap;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new MergeObjectException(e.getMessage());
        }
    }
}
