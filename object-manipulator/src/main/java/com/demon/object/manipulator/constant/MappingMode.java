package com.demon.object.manipulator.constant;


public enum MappingMode {
    /**
     *Default mode to map fields to fields with the same name (used for POJO)
     **/
    DEFAULT_MODE,

    /**
     * Path mode to map fields base on Path Config
     **/
    PATH_MODE,

    /***
     * Mix code to map fields with both Default mode and Path mode
     **/
    MIX_MODE;
}
