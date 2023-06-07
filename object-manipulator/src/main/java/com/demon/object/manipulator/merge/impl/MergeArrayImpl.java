package com.demon.object.manipulator.merge.impl;


import com.demon.object.manipulator.exception.MergeObjectException;
import com.demon.object.manipulator.merge.IMergeObject;

public class MergeArrayImpl<M> implements IMergeObject<M[]> {
    private IMergeObject<M> mergeObject = new MergeObjectImpl();

    @Override
    public M[] performMerge(M[] dest, M[] source) throws MergeObjectException {
        if (null == dest || dest.length == 0) {
            if (null != source) {
                return source.clone();
            }
            return (M[]) new Object[0];
        }
        int destLen = dest.length;
        int sourceLen = source.length;
        int tempLen = destLen > sourceLen ? destLen : sourceLen;
        Object[] tempArr = new Object[tempLen];
        for (int i = 0; i < tempLen; i++) {
            M currentDest = i < destLen ? dest[i] : null;
            M currentSource = i < sourceLen ? source[i] : null;
            if (null != currentDest && null != currentSource) {
                currentDest = mergeObject.performMerge(currentDest, currentSource);
                tempArr[i] = currentDest;
            } else if (null != currentDest) {
                tempArr[i] = currentDest;
            } else {
                tempArr[i] = currentSource;
            }
        }
        return (M[]) tempArr;
    }
}
