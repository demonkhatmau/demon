package com.demon.object.manipulator.merge.impl;


import com.demon.commons.utils.ReflectionUtils;
import com.demon.object.manipulator.exception.MergeObjectException;
import com.demon.object.manipulator.merge.IMergeObject;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class MergeCollectionImpl<M> implements IMergeObject<Collection<M>> {
    private Class<? extends Collection> defaultCollectionClazz = ArrayList.class;
    private MergeArrayImpl mergeArr = new MergeArrayImpl();

    @Override
    public Collection<M> performMerge(Collection<M> dest, Collection<M> source) throws MergeObjectException {

        try {
            if (null == dest && null == source) {
                return defaultCollectionClazz.newInstance();
            }
            if (null == dest) {
                dest = defaultCollectionClazz.newInstance();
            }
            if (null == source) {
                source = defaultCollectionClazz.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new MergeObjectException(e.getMessage());
        }

        final Object[] destArr = dest.toArray();
        final Object[] sourceArr = source.toArray();
        final Object[] result = mergeArr.performMerge(destArr, sourceArr);
        final Constructor destDefaultConstructor = ReflectionUtils.getDefaultConstructor(dest);
        if (null == destDefaultConstructor) {
            return (Collection<M>) Arrays.stream(result).collect(Collectors.toList());
        } else {
            try {
                Collection<M> finalResult = dest.getClass().newInstance();
                Arrays.stream(result).forEach(e -> {
                    finalResult.add((M) e);
                });
                return finalResult;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new MergeObjectException(e.getMessage());
            }
        }
    }
}
