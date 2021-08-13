package org.challenge.calculator.exception;

/*
Enumeration of the different exceptions/errors that might
occur in the application
 */
public enum ErrorCause {
    INSUFFICIENT_BALANCE,      // the user has not enough balance to execute a service
    INVALID_PARAMETERS,        // the received parameters are not numbers, empty values, etc
    RECORD_NOT_FOUND,          // the record information wasn't found in the DB
    SERVICE_NOT_ACTIVE,        // the service status is either INACTIVE or DELETED
    SERVICE_NOT_FOUND,         // the service wasn't found in the DB
    SERVICE_TYPE_UNSUPPORTED,  // the type of service requested is not supported by the application
    USER_NOT_ACTIVE,           // the status of the user is either INACTIVE or DELETED
    USER_NOT_FOUND,            // the user wasn't found in the DB
    USER_ALREADY_EXISTS,       // the username/email we want to signup is already taken
    WRONG_NUMBER_OF_PARAMETERS,// the number of parameters needed by a service is incomplete
    WRONG_USER_CREDENTIALS     // either the username or the password or both are wrong
}
