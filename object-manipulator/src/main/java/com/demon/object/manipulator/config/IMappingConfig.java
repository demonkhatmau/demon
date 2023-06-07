package com.demon.object.manipulator.config;


import com.demon.object.manipulator.constant.MappingMode;

public interface IMappingConfig {
    MappingMode getMappingMode();
    Class<?> getInputClass();
    Class<?> getOutputClass();
}
