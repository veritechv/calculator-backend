package org.challenge.calculator.services;

import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.UserAlreadyExistsException;
import org.challenge.calculator.webmodel.Token;

public interface LoginService {
    Token loginUser(String username, String password);
    User registerUser(String username, String password) throws UserAlreadyExistsException;
}
