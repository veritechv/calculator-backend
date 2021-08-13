package org.challenge.calculator.services;

/*
 * This service is the one responsible to access the application-wide configuration values,
 * like connection information with third parties, default values for services and users, etc.
 *  */
public interface SystemConfigurationService {
    /**
     * Retrieves a configuration by name that has a value of type long.
     *
     * @param configurationName Name of the configuration.
     * @param defaultValue      Value used in case we can't find the configuration.
     * @return either the configuration's value or the default one
     */
    long getLongConfiguration(String configurationName, long defaultValue);

    /**
     * Retrieves a configuration by name.
     *
     * @param configurationName Name of the configuration.
     * @param defaultValue      Value used in case we can't find the configuration.
     * @return either the configuration's value or the default one
     */
    String getStringConfiguration(String configurationName, String defaultValue);
}
