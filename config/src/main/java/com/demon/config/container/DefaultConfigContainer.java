package com.demon.config.container;

import com.demon.config.exception.ConfigFileNotFoundException;
import com.demon.config.exception.ConfigNotFoundException;
import com.demon.config.exception.InvalidConfigException;
import com.demon.config.loader.IConfigLoader;
import com.demon.config.transform.IConfigElement;
import com.demon.config.transform.IConfigElementTransformer;
import com.demon.config.transform.impl.DefaultConfigElement;
import com.demon.config.transform.impl.DefaultConfigElementTransformer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

@Slf4j
public class DefaultConfigContainer implements IConfigLoader, IConfigContainer {
    private final Properties properties = new Properties();
    private IConfigElementTransformer configElementTransformer = new DefaultConfigElementTransformer();

    @Override
    public Properties getConfig() {
        return properties;
    }

    @Override
    public void merge(IConfigContainer other) {
        if(null != other && null != other.getConfig()) {
            other.getConfig().entrySet().stream().forEach(e -> {
                properties.put(e.getKey(), e.getValue());
            });
        }
    }

    @Override
    public IConfigElement getConfigElement(String configKey) throws ConfigNotFoundException {
        if(properties.contains(configKey)) {
            String configValue = properties.getProperty(configKey);
            return new DefaultConfigElement(configKey, configValue, configKey +"=" + configKey);
        }
        throw new ConfigNotFoundException("Config with key" + configKey +" not found");
    }

    @Override
    public void load(String configFilePath) throws ConfigFileNotFoundException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        if(null == classLoader) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        classLoader.getResourceAsStream(configFilePath);
        try {
            try (InputStream is = classLoader.getResourceAsStream(configFilePath)){
                if(null == is) {
                    return;
                }
                List<String> configContent = IOUtils.readLines(is, Charset.forName("UTF-8"));
                configContent.forEach(l -> {
                    try {
                        IConfigElement configElement = configElementTransformer.transform(l);
                        properties.put(configElement.getConfigKey(), configElement.getConfigValue());
                    } catch (InvalidConfigException e) {
                        log.error("Error when load config: {}", e.getMessage());
                    }
                });
            }
        } catch (IOException e) {
            throw new ConfigFileNotFoundException(e.getMessage());
        }
    }
}
