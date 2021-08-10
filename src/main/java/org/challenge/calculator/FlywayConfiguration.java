package org.challenge.calculator;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * This configuration is to run Flyway after hibernate has created the tables.
 * When running the application the first time Flyway tries to run the migrations
 * but Hibernate hasn't created the tables and then we get an error.
 */
@Configuration
public class FlywayConfiguration {

    @Autowired
    public FlywayConfiguration(DataSource dataSource, Environment environment) {
        String flywaySchema = environment.getProperty("spring.flyway.schemas");
        Flyway.configure().baselineOnMigrate(true).schemas(flywaySchema).dataSource(dataSource).load().migrate();
    }
}