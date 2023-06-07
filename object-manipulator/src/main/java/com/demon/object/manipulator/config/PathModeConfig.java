package com.demon.object.manipulator.config;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PathModeConfig implements Serializable {
    private String fromClass;
    private String toClass;
    private String rootName;
    private List<FromToFieldConfig> fromToFieldConfig;
}
