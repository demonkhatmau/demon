package com.demon.object.manipulator.config;

public interface IFileBasedMappingConfig extends IMappingConfig {
    void load(String configFileName);
}
