package com.demon.object.manipulator.clone.impl;


import com.demon.object.manipulator.clone.ICustomizedClone;
import com.demon.object.manipulator.exception.CustomizedCloneException;

import java.util.Collection;

public class CollectionCustomizedCloneImpl<M> implements ICustomizedClone<Collection<M>> {
    public static final CollectionCustomizedCloneImpl collectionCustomizedClone = new CollectionCustomizedCloneImpl();

    @Override
    public Collection<M> performCustomizedClone(Collection<M> source, boolean deepClone) throws CustomizedCloneException {
        try {
            final Collection<M> finalResult = source.getClass().newInstance();
            for (M e : source) {
                if (!deepClone) {
                    finalResult.add(e);
                } else {
                    M clonedElement = (M) ObjectCustomizedCloneImpl.objectCustomizedClone.performCustomizedClone(e, true);
                    finalResult.add(clonedElement);
                }
            }
            return finalResult;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CustomizedCloneException(e.getMessage());
        }
    }
}
