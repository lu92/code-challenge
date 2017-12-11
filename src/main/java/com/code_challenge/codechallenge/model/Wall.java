package com.code_challenge.codechallenge.model;

import lombok.Data;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Wall {
    List<Tweet> tweets;

    public Wall(List<Tweet> tweets) {
        this.tweets = tweets.stream().sorted(Comparator.comparing(Tweet::getDateTime)).collect(Collectors.toList());
    }
}
