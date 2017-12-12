package com.code_challenge.codechallenge.exceptions;

public class InvalidTweetException extends RuntimeException {
    public InvalidTweetException() {
        super("Message of tweet is invalid!");
    }
}
