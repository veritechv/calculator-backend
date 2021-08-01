package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.SystemConfiguration;
import org.challenge.calculator.repository.SystemConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigurationServiceImpl implements SystemConfigurationService{
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemConfigurationServiceImpl.class);
    private SystemConfigurationRepository systemConfigurationRepository;

    @Autowired
    public SystemConfigurationServiceImpl(SystemConfigurationRepository systemConfigurationRepository) {
        this.systemConfigurationRepository = systemConfigurationRepository;
    }

    @Override
    public long getLongConfiguration(String configurationName, long defaultValue) {
        long configurationValue = defaultValue;
        if (StringUtils.isNotBlank(configurationName)) {
            SystemConfiguration systemConfiguration = systemConfigurationRepository.findByName(configurationName);
            if (systemConfiguration != null && StringUtils.isNotBlank(systemConfiguration.getValue())) {
                try {
                    configurationValue = Long.valueOf(systemConfiguration.getValue());
                } catch (NumberFormatException exception) {
                    LOGGER.error("Configuration [" + configurationName + "] is not of type long! Returning default value.");
                }
            } else {
                LOGGER.error("Configuration [" + configurationName + "] doesn't have a value.");
            }
        } else {
            LOGGER.error("Configuration name is empty. Returning default value.");
        }
        return configurationValue;
    }

    @Override
    public String getStringConfiguration(String configurationName, String defaultValue) {
        String configurationValue = defaultValue;
        if (StringUtils.isNotBlank(configurationName)) {
            SystemConfiguration systemConfiguration = systemConfigurationRepository.findByName(configurationName);
            if (systemConfiguration != null && StringUtils.isNotBlank(systemConfiguration.getValue())) {
                    configurationValue = systemConfiguration.getValue();
            } else {
                LOGGER.error("Configuration [" + configurationName + "] doesn't have a value.");
            }
        } else {
            LOGGER.error("Configuration name is empty. Returning default value.");
        }
        return configurationValue;
    }
}
