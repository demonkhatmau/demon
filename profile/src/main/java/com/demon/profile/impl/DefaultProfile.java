package com.demon.profile.impl;

import com.demon.config.container.DefaultConfigContainer;
import com.demon.config.exception.ConfigFileNotFoundException;
import com.demon.profile.IProfile;
import com.demon.template.holder.impl.StringValueHolderFromCollectionCollector;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class DefaultProfile implements IProfile {
    private final static String DEFAULT_CONFIG_FILE_NAME = "app-config.properties";
    private final static String NAMED_CONFIG_FILE_PATTERN = "app-config-%s.properties";
    private String profileName;
    private String configFileNameWithProfile;
    private DefaultConfigContainer defaultConfigContainer = new DefaultConfigContainer();
    private StringValueHolderFromCollectionCollector stringValueHolderFromCollectionCollector = new StringValueHolderFromCollectionCollector();

    /**
     *Create Profile with profileName and configFileNameWithProfilePattern load  file from classpath
     * ex: profileName = dev, configFileNameWithProfilePattern = config-%s.pros => config-dev.pros
     * @param profileName
     * @param configFileNameWithProfilePattern
     * **/
    public DefaultProfile(String profileName, String configFileNameWithProfilePattern) {
        this.profileName = profileName;
        this.configFileNameWithProfile = String.format(configFileNameWithProfilePattern, profileName);
    }

    /**
     *Create Profile with profileName load app-config-{profileName}.properties file from classpath
     * @param profileName
     * **/
    public DefaultProfile(String profileName) {
        this.profileName = profileName;
        this.configFileNameWithProfile = String.format(NAMED_CONFIG_FILE_PATTERN, profileName);
    }

    /**
     *Create Profile with default profileName load app-config.properties file from classpath
     * **/
    public DefaultProfile() {
        this.profileName = StringUtils.EMPTY;
        configFileNameWithProfile = DEFAULT_CONFIG_FILE_NAME;
    }

    @Override
    public void load() {
        //default profile
        if(StringUtils.isEmpty(profileName)) {
            try {
                defaultConfigContainer.load(configFileNameWithProfile);
            } catch (ConfigFileNotFoundException e) {
                log.error("Can not load config with profileName: {}, ex: {}", profileName, e.getMessage());
                try {
                    defaultConfigContainer.load("resource/" + configFileNameWithProfile);
                } catch (ConfigFileNotFoundException configFileNotFoundException) {
                    log.error("Can not load config with profileName: {}, ex: {}", profileName, e.getMessage());
                }
            }

        }
    }

    @Override
    public void resolveProfilePlaceHolder() {
        List<String> configValues = defaultConfigContainer.getConfig().entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .map(String.class::cast)
                .collect(Collectors.toList());

        List<String> listValueHolder = stringValueHolderFromCollectionCollector
                .collect(configValues);
    }


}
