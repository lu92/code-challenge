package com.twitter.exceptions;

public class TweetNotFoundException extends RuntimeException {
    public TweetNotFoundException() {
        super("Given tweet does not exist!");
    }
}
