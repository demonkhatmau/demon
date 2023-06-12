package com.demon.config.container;

import com.demon.config.exception.ConfigNotFoundException;
import com.demon.config.transform.IConfigElement;

import java.util.Properties;

public interface IConfigContainer {
    Properties getConfig();
    void merge(IConfigContainer other);
    IConfigElement getConfigElement(String configKey) throws ConfigNotFoundException;
}
