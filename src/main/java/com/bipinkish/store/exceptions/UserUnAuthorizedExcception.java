package com.bipinkish.store.exceptions;

public class UserUnAuthorizedExcception extends UserServiceException {
    public UserUnAuthorizedExcception(String message) {
        super(message);
    }
}
