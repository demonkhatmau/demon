package com.demon.config.transform.impl;

import com.demon.config.exception.InvalidConfigException;
import com.demon.config.transform.IConfigElement;
import com.demon.config.transform.IConfigElementTransformer;
import org.apache.commons.lang3.StringUtils;

public class DefaultConfigElementTransformer implements IConfigElementTransformer {
    private final static String CONFIG_ELEMENT_SEPARATOR =  "=";

    @Override
    public IConfigElement transform(IConfigElement source) throws InvalidConfigException {
        if(null == source || StringUtils.isBlank(source.getRawConfig())) {
            throw new InvalidConfigException("config value is null or empty, need format {key}={value}");
        }
        String[] configElementArr = source.getRawConfig().split(CONFIG_ELEMENT_SEPARATOR);
        if (configElementArr.length != 2) {
            throw new InvalidConfigException("Invalid config element: " + source.getRawConfig() + ", need format {key}={value}");
        }
        return new DefaultConfigElement(configElementArr[0], configElementArr[1], source.getRawConfig());
    }

    @Override
    public IConfigElement transform(String source) throws InvalidConfigException {
        if(StringUtils.isBlank(source)) {
            throw new InvalidConfigException("config value is null or empty, need format {key}={value}");
        }
        String[] configElementArr = source.split(CONFIG_ELEMENT_SEPARATOR);
        if (configElementArr.length != 2) {
            throw new InvalidConfigException("Invalid config element: " + source + ", need format {key}={value}");
        }

        return new DefaultConfigElement(configElementArr[0], configElementArr[1], source);
    }
}
