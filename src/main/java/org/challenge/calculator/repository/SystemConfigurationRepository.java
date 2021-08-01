package org.challenge.calculator.repository;

import org.challenge.calculator.entity.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Integer> {
    SystemConfiguration findByName(String name);
}
