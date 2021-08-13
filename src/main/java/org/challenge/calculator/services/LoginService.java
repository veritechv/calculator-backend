package org.challenge.calculator.services;

import org.challenge.calculator.entity.User;
import org.challenge.calculator.exception.CalculatorException;
import org.challenge.calculator.model.Token;

/*
    Interface defining the main methods related with the application access
 */
public interface LoginService {
    /**
     * This method should the proper validation against the user and password provided to
     * let the user access the application.
     *
     * @param username username/email used to identify the user willing to access the application.
     * @param password password registered in the application for that username
     * @return a security token if the login is successful
     * @throws CalculatorException if log in couldn't be done
     */
    Token loginUser(String username, String password) throws CalculatorException;

    /**
     * Method used to sign up/register/create a new user of the application
     *
     * @param username name used that will be used to identify a user in the application
     * @param password the password that user should use everytime she wants to log in.
     * @return the application user just created
     * @throws CalculatorException if the registration fails
     */
    User registerUser(String username, String password) throws CalculatorException;
}
