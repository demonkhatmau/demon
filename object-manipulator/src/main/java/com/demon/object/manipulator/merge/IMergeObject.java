package com.demon.object.manipulator.merge;

import com.demon.object.manipulator.exception.MergeObjectException;

public interface IMergeObject<M> {
    M performMerge(M dest, M source) throws MergeObjectException;
}
