package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.challenge.calculator.entity.Role;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.enums.RoleName;
import org.challenge.calculator.enums.UserStatus;
import org.challenge.calculator.repository.RoleRepository;
import org.challenge.calculator.repository.UserRepository;
import org.challenge.calculator.utils.PagingInformationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service in charge of a lifecycle of Users.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /*Configuration name that says which is the initial balance for new users.*/
    private static final String DEFAULT_BALANCE_CONFIGURATION = "DEFAULT_BALANCE";
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private SystemConfigurationService systemConfigurationService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           SystemConfigurationService systemConfigurationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.systemConfigurationService = systemConfigurationService;
    }

    /**
     * Gets a Use by username
     *
     * @param username value used to find a User
     * @return an Optional that might be empty if the user is not found.
     */
    public Optional<User> searchUser(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Retrieves the list of all the users in the application.
     * The list includes ACTIVE, TRIAL, INACTIVE and DELETED users.
     * Method intended only for admins.
     *
     * @param pageIndex    Page number we want back
     * @param pageSize     Number of users in the response list
     * @param sortingField Name of the attribute to use for sorting
     * @return
     */
    public Page<User> listUsers(int pageIndex, int pageSize, String sortingField) {
        Page<User> result;
        Pageable pagingInformation = PagingInformationUtil.buildPagingInformation(pageIndex, pageSize, sortingField);

        try {
            result = userRepository.findAll(pagingInformation);
        } catch (PropertyReferenceException exception) {
            LOGGER.info("The paging information is wrong. Please verify. Returning empty results");
            result = Page.empty();
        }

        return result;
    }

    /**
     * Saves a user in the database
     *
     * @param user the object holding the user's data we want to create.
     * @return the user just created.
     */
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * We create a new user in the database.
     * The new user by default:
     * - has the role ROLE_USER
     * - has status  ACTIVE
     * - and has an initial balance given by the configuration "DEFAULT_BALANCE"
     *
     * @param username username/email for the new user.
     * @param password password for the new user
     * @return the User object just created.
     */
    public User createUser(String username, String password) {
        String encryptedPassword = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);

        //setup user roles
        Set<Role> roles = new HashSet<>();
        Optional<Role> roleUser = roleRepository.findByRoleName(RoleName.ROLE_USER);
        roleUser.ifPresent(role -> {
            roles.add(role);
        });
        user.setRoles(roles);

        //setup status
        user.setStatus(UserStatus.ACTIVE);

        //setup initial balance
        long initialBalance = systemConfigurationService.getLongConfiguration(DEFAULT_BALANCE_CONFIGURATION, 0L);
        user.setBalance(initialBalance);

        return this.createUser(user);
    }

    /**
     * Create a new user entry in the database.
     * We check first if a user with the given username exists so we can update it's values.
     *
     * @param user The user object holding the new values.
     * @return The user updated.
     * @throws UsernameNotFoundException in case there is no user with the specified username.
     */
    public User updateUser(User user) {
        if (user != null) {
            Optional<User> existingUserOptional = userRepository.findByUsername(user.getUsername());
            if (!existingUserOptional.isPresent()) {
                LOGGER.error("we couldn't find the user[" + user.getUsername() + "] for update.");
                throw new UsernameNotFoundException("The user [" + user.getUsername() + "] was not found.");
            }

            User existingUser = existingUserOptional.get();
            //set values for update
            existingUser.setStatus(user.getStatus());

            //get roles
            Set<Role> roles = new HashSet<>();
            for (Role role : user.getRoles()) {

                Optional<Role> roleFromDb = roleRepository.findByRoleName(role.getRoleName());
                roleFromDb.ifPresent(r -> roles.add(r));
            }
            existingUser.setRoles(roles);
            existingUser.setBalance(user.getBalance());

            //update the existing user
            user = userRepository.save(existingUser);
        }
        return user;
    }

    /**
     * This delete method does a soft delete, meaning that we are going to assign
     * a DELETE status to the user. This way we don't lose service records.
     *
     * @param username Username/email of the user we want to delete.
     * @throws IllegalArgumentException if we pass an empty/null/invalid uuid
     */
    @Override
    public void deleteUser(String username) {
        if (StringUtils.isNotBlank(username)) {
            Optional<User> existingUserOptional = userRepository.findByUsername(username);
            if (!existingUserOptional.isPresent()) {
                LOGGER.error("We couldn't find the user with username[" + username + "]");
                throw new IllegalArgumentException("We couldn't find the user with username[" + username + "]");
            }

            //update the status to DELETE
            existingUserOptional.get().setStatus(UserStatus.DELETED);
            userRepository.save(existingUserOptional.get());
            LOGGER.info("User [" + existingUserOptional.get().getUsername() + "] has been deleted");
        } else {
            throw new IllegalArgumentException("We need a username in order to delete the user.");
        }
    }

    /**
     * Returns the enumeration names of RoleName
     */
    @Override
    public List<String> getUserRoles() {
        return Stream.of(RoleName.values()).map(type -> {
            return type.name();
        }).collect(Collectors.toList());
    }

    /**
     * Returns the enumeration names of RoleName
     */
    @Override
    public List<String> getUserStatuses() {
        return Stream.of(UserStatus.values()).map(type -> {
            return type.name();
        }).collect(Collectors.toList());
    }
}
