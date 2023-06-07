package com.demon.object.manipulator.config;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class FromToFieldConfig implements Serializable {
    private String fromPath;
    private String toPath;
    private String condition;
    private String transformClass;
}
