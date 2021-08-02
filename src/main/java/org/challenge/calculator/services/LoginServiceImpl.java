package org.challenge.calculator.services;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.UserAlreadyExistsException;
import org.challenge.calculator.security.jwt.JwtProvider;
import org.challenge.calculator.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private PasswordEncoder passwordEncoder;
    private UserServiceImpl userService;

    @Value("${secretPsw}")
    private String secretPsw;

    @Autowired
    public LoginServiceImpl(AuthenticationManager authenticationManager,
                            JwtProvider jwtProvider, PasswordEncoder passwordEncoder, UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public Token loginUser(String username, String password) {
        Token token = null;
        if (StringUtils.isNoneBlank(username, password)) {
            Optional<User> existingUser = userService.searchUser(username);
            if (existingUser.isPresent()) {
                if (passwordEncoder.matches(password, existingUser.get().getPassword())) {
                    token = generateSecurityToken(existingUser.get(), password);
                } else {
                    LOGGER.error("Username and/or password incorrect");
                }
            }
        }else{
            LOGGER.error("Username and/or password incorrect");
        }
        return token;
    }

    public User registerUser(String username, String password) throws UserAlreadyExistsException {
        User newUser = null;
        if(StringUtils.isNoneBlank(username, password)) {
            Optional<User> existingUser = userService.searchUser(username);
            if (existingUser.isPresent()) {
                throw new UserAlreadyExistsException("The username is already in use.");
            }else if(EmailValidator.getInstance().isValid(username)){
                newUser = userService.createUser(username, password);
            }
        }

        return newUser;
    }

    private Token generateSecurityToken(User user, String password){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtProvider.generateToken(authentication);
        Token token = new Token();
        token.setValue(jwtToken);
        return token;
    }
}
