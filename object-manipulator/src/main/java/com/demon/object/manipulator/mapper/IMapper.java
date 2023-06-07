package com.demon.object.manipulator.mapper;

public interface IMapper<I, O> {
    O mapTo(I input, Class<O> outputClass) throws IllegalAccessException, InstantiationException;
}
