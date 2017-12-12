package com.twitter.exceptions;

public class InvalidTweetException extends RuntimeException {
    public InvalidTweetException() {
        super("Message of tweet is invalid!");
    }
}
