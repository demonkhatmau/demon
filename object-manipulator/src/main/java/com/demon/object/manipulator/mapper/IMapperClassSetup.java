package com.demon.object.manipulator.mapper;


import com.demon.object.manipulator.constant.MappingMode;

public interface IMapperClassSetup <I, O> {
    void setUpMapper(Class<I> inputClass, Class<O> outputClass);
    MappingMode getMappingMode();
}
