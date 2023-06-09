package com.demon.object.manipulator.clone.impl;

import com.demon.object.manipulator.clone.ICustomizedClone;
import com.demon.object.manipulator.clone.ICustomizedCloneFactory;
import com.demon.object.manipulator.constant.ObjectType;

public class SimpleCustomizedCloneFactory implements ICustomizedCloneFactory {
    private static ICustomizedClone objectCloner = new ObjectCustomizedCloneImpl();
    private static ICustomizedClone arrayCloner = new ArrayCustomizedCloneImpl();
    private static ICustomizedClone mapCloner = new MapCustomizedCloneImpl();
    private static ICustomizedClone collectionCloner = new CollectionCustomizedCloneImpl();

    @Override
    public ICustomizedClone build(ObjectType type) {
        switch (type) {
            case OBJECT: return objectCloner;
            case ARRAY: return arrayCloner;
            case MAP: return mapCloner;
            case COLLECTION: return collectionCloner;
        }
        throw new IllegalArgumentException("ObjectType is invalid");
    }
}
