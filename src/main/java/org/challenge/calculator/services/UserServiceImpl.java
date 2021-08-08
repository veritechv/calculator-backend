package org.challenge.calculator.services;

import org.challenge.calculator.entity.Role;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.enums.RoleName;
import org.challenge.calculator.enums.UserStatus;
import org.challenge.calculator.repository.RoleRepository;
import org.challenge.calculator.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService{
    private static final String DEFAULT_BALANCE_CONFIGURATION = "DEFAULT_BALANCE";
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private SystemConfigurationService systemConfigurationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           SystemConfigurationService  systemConfigurationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.systemConfigurationService = systemConfigurationService;
    }

    public Optional<User> searchUser(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> searchUser(long userId){
        return userRepository.findById(userId);
    }

    public Page<User> listUsers(Pageable pageableInformation){
        return userRepository.findAll(pageableInformation);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User createUser(String username, String password){
        String encryptedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);

        //setup user roles
        Set<Role> roles = new HashSet<>();
        Optional<Role> roleUser = roleRepository.findByRoleName(RoleName.ROLE_USER);
        roleUser.ifPresent(role -> { roles.add(role);});
        user.setRoles(roles);

        //setup status
        user.setStatus(UserStatus.ACTIVE);

        //setup initial balance
        long initialBalance = systemConfigurationService.getLongConfiguration(DEFAULT_BALANCE_CONFIGURATION, 0L);
        user.setBalance(initialBalance);

        return this.saveUser(user);
    }

    public User updateUser(User user){
        return saveUser(user);
    }

}
