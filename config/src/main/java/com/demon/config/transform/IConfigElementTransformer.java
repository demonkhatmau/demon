package com.demon.config.transform;

import com.demon.config.exception.InvalidConfigException;

public interface IConfigElementTransformer {
    IConfigElement transform(IConfigElement source) throws InvalidConfigException;

    IConfigElement transform(String source) throws InvalidConfigException;
}
