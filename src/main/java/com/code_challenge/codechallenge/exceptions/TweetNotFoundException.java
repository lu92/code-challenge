package com.code_challenge.codechallenge.exceptions;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException() {
        super("Given tweet does not exist!");
    }
}
