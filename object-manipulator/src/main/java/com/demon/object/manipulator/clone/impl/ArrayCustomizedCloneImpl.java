package com.demon.object.manipulator.clone.impl;


import com.demon.object.manipulator.clone.ICustomizedClone;
import com.demon.object.manipulator.exception.CustomizedCloneException;

public class ArrayCustomizedCloneImpl<M> implements ICustomizedClone<M[]> {
    public static final ArrayCustomizedCloneImpl arrayCustomizedClone = new ArrayCustomizedCloneImpl();

    @Override
    public M[] performCustomizedClone(final M[] source, final boolean deepClone) throws CustomizedCloneException {
        Object[] finalResult = new Object[source.length];
        if (!deepClone) {
            System.arraycopy(source, 0, finalResult, 0, source.length);
        } else {
            for (int i = 0; i < source.length; i++) {
                M clonedElement = (M) ObjectCustomizedCloneImpl.objectCustomizedClone.performCustomizedClone(source[i], true);
                finalResult[i] = clonedElement;
            }
        }
        return (M[]) finalResult;
    }
}
