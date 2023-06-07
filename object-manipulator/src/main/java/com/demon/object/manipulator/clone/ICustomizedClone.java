package com.demon.object.manipulator.clone;

import com.demon.object.manipulator.exception.CustomizedCloneException;

public interface ICustomizedClone<M> {
    M performCustomizedClone(M source, boolean deepClone) throws CustomizedCloneException;
}
