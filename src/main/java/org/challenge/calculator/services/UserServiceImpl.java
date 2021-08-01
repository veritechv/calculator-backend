package org.challenge.calculator.services;

import org.challenge.calculator.entity.Role;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.entity.UserStatus;
import org.challenge.calculator.enums.RoleName;
import org.challenge.calculator.enums.UserStatusName;
import org.challenge.calculator.repository.RoleRepository;
import org.challenge.calculator.repository.UserRepository;
import org.challenge.calculator.repository.UserStatusRepository;
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
    private UserStatusRepository userStatusRepository;
    private SystemConfigurationService systemConfigurationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           UserStatusRepository userStatusRepository, PasswordEncoder passwordEncoder,
                           SystemConfigurationService  systemConfigurationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userStatusRepository = userStatusRepository;
        this.passwordEncoder = passwordEncoder;
        this.systemConfigurationService = systemConfigurationService;
    }

    public Optional<User> searchUser(String username){
        return userRepository.findByUsername(username);
    }

    public Optional<User> searchUser(long userId){
        return userRepository.findById(userId);
    }

    public Page listUsers(Pageable pageableInformation){
        return userRepository.findAll(pageableInformation);
    }

    //public List<User> getUsers(int page, int size, boolean isSortAscending, String sortField){
    public List<User> listUsers(){
        /*Sort.Direction sortDirection = isSortAscending? Sort.Direction.ASC: Sort.Direction.DESC;

        List<String> sortingFields = new ArrayList<>();
        if(sortField!=null){
            sortingFields.add(sortField);
        }

        Sort sort = new Sort(sortDirection, sortingFields);

        Pageable pageable = new PageRequest(page, size, sort);*/
        //return userRepository.findAll();
        return new ArrayList<>();
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
        Optional<UserStatus> userStatus = userStatusRepository.findByStatusName(UserStatusName.ACTIVE);
        userStatus.ifPresent(status -> {user.setStatus(status);});

        //setup initial balance
        long initialBalance = systemConfigurationService.getLongConfiguration(DEFAULT_BALANCE_CONFIGURATION, 0L);
        user.setBalance(initialBalance);

        return this.saveUser(user);
    }

    public User updateUser(User user){
        return saveUser(user);
    }

}
