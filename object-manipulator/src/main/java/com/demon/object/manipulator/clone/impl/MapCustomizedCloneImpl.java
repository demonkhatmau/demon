package com.demon.object.manipulator.clone.impl;


import com.demon.object.manipulator.clone.ICustomizedClone;
import com.demon.object.manipulator.exception.CustomizedCloneException;

import java.util.Map;

public class MapCustomizedCloneImpl<K, V> implements ICustomizedClone<Map<K, V>> {
    private ICustomizedClone objectCustomizedClone = new ObjectCustomizedCloneImpl();

    @Override
    public Map<K, V> performCustomizedClone(Map<K, V> source, boolean deepClone) throws CustomizedCloneException {
        try {
            Map<K, V> finalResult = source.getClass().newInstance();
            for (Map.Entry<K, V> e : source.entrySet()) {
                if (!deepClone) {
                    finalResult.put(e.getKey(), e.getValue());
                } else {
                    K clonedKey = (K) objectCustomizedClone.performCustomizedClone(e.getKey(), true);
                    V clonedValue = (V) objectCustomizedClone.performCustomizedClone(e.getValue(), true);
                    finalResult.put(clonedKey, clonedValue);
                }
            }
            return finalResult;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CustomizedCloneException(e.getMessage());
        }
    }
}
