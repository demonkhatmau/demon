package com.demon.object.manipulator.clone;

import com.demon.object.manipulator.constant.ObjectType;

public interface ICustomizedCloneFactory {
    ICustomizedClone build(ObjectType type);
}
