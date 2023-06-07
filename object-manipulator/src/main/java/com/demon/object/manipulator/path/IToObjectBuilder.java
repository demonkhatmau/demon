package com.demon.object.manipulator.path;

import java.util.Optional;

public interface IToObjectBuilder<O> {
    Optional<O> buildToObject(Class<O> clazzToBuild) throws IllegalAccessException, InstantiationException;
}
