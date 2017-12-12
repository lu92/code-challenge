package com.code_challenge.codechallenge.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("Given nickname is used by other user!");
    }
}
