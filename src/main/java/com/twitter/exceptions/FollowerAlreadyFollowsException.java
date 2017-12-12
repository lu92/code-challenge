package com.twitter.exceptions;

public class FollowerAlreadyFollowsException extends RuntimeException {

    public FollowerAlreadyFollowsException() {
        super("Given follower already follows by user!");
    }
}
