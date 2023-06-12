package com.demon.config.transform.impl;

import com.demon.config.transform.IConfigElement;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class DefaultConfigElement implements IConfigElement {
    private String configKey;
    private String configValue;
    private String rawConfig;

    @Override
    public String getRawConfig() {
        return rawConfig;
    }

    @Override
    public String getConfigKey() {
        return configKey;
    }

    @Override
    public String getConfigValue() {
        return configValue;
    }
}
