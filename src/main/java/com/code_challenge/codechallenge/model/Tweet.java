package com.code_challenge.codechallenge.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Tweet {
    private User author;
    private Tweet parentTweet;
    private List<Tweet> childrenTweets = new LinkedList<>();
    private LocalDateTime dateTime;

    @NotNull
    @Size(min = 1, max = 140)
    private String message;
}
