package com.demon.config.loader;

import com.demon.config.exception.ConfigFileNotFoundException;

public interface IConfigLoader {
    void load(String configFilePath) throws ConfigFileNotFoundException;
}
