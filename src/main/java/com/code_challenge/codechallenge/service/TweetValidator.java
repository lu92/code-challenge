package com.code_challenge.codechallenge.service;

import com.code_challenge.codechallenge.exceptions.InvalidTweetException;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class TweetValidator {
    private Predicate<String> notNull = message -> message != null;
    private Predicate<String> validLength = message -> 1 <= message.length() && message.length() <=140;
    private Predicate<String> validTweetMessage = notNull.and(validLength);

    public void validate(String message) {
        if (!validTweetMessage.test(message)) {
            throw new InvalidTweetException();
        }
    }
}
