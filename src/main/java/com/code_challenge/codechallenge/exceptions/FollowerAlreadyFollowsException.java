package com.code_challenge.codechallenge.exceptions;

public class FollowerAlreadyFollowsException extends RuntimeException {

    public FollowerAlreadyFollowsException(String message) {
        super(message);
    }
}
