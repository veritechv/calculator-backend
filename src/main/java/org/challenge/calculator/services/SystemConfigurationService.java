package org.challenge.calculator.services;

public interface SystemConfigurationService {
    long getLongConfiguration(String configurationName, long defaultValue);
    String getStringConfiguration(String configurationName, String defaultValue);
}
