package org.challenge.calculator.enums;

public enum UserStatus {
    ACTIVE,//the user can use the application
    TRIAL,//the user can use the application but only for some time
    INACTIVE,//the user can't use the application while in this state
    DELETED//The user was "deleted" and no longer can use the application.
}
