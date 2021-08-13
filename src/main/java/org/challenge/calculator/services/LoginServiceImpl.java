package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.exception.ErrorCause;
import org.challenge.calculator.model.Token;
import org.challenge.calculator.security.jwt.JwtProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            JwtProvider jwtProvider, PasswordEncoder passwordEncoder, UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    /**
     * In order to generate a token this method does the following:
     * - Check if a User with the username provided exists in the database.
     * - Compares the password provided against the User's password from the database
     *
     * @param username username/email used to identify the user willing to access the application.
     * @param password password registered in the application for that username
     * @return A json web token for further interaction with the application
     * @throws CalculatorException
     */
    public Token loginUser(String username, String password) throws CalculatorException {
        Token token = null;
        if (StringUtils.isNoneBlank(username, password)) {
            Optional<User> existingUser = userService.searchUser(username);
            if (existingUser.isPresent()) {
                if (existingUser.get().isActive() &&
                        passwordEncoder.matches(password, existingUser.get().getPassword())) {
                    token = generateSecurityToken(existingUser.get(), password);
                } else {
                    LOGGER.error("Username and/or password incorrect");
                    throw new CalculatorException("Username and/or password incorrect",
                            ErrorCause.WRONG_USER_CREDENTIALS);
                }
            }
        } else {
            LOGGER.error("Username and/or password incorrect");
            throw new CalculatorException("Username and/or password incorrect", ErrorCause.WRONG_USER_CREDENTIALS);
        }
        return token;
    }

    /**
     * In order to register a new user with need to:
     * - check that the username is not already in use by another registered user
     * - the email provided as username has the right format
     *
     * @param username name used that will be used to identify a user in the application
     * @param password the password that user should use everytime she wants to log in.
     * @return The user just added to the database
     * @throws CalculatorException if:
     *                             - the username is already taken
     *                             - the username doesn't have a valid email format
     */
    public User registerUser(String username, String password) throws CalculatorException {
        User newUser = null;
        if (StringUtils.isNoneBlank(username, password)) {
            Optional<User> existingUser = userService.searchUser(username);
            if (existingUser.isPresent()) {
                throw new CalculatorException("The username is already in use.", ErrorCause.USER_ALREADY_EXISTS);
            } else if (EmailValidator.getInstance().isValid(username)) {
                newUser = userService.createUser(username, password);
            } else {
                throw new CalculatorException("The username doesn't have the correct format",
                        ErrorCause.INVALID_PARAMETERS);
            }
        }
        return newUser;
    }

    /**
     * Generates a JSON Web Token to validate future requests to the application,
     * also creates an authentication object and updates the security context with it.
     */
    private Token generateSecurityToken(User user, String password) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtProvider.generateToken(authentication);
        Token token = new Token();
        token.setValue(jwtToken);
        return token;
    }
}
