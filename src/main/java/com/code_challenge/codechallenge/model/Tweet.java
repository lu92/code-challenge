package com.code_challenge.codechallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Tweet implements Comparable<Tweet> {

    @Getter
    private Long tweetId;

    @Getter @JsonIgnore
    private User author;

    @Getter @Setter @JsonIgnore
    private Tweet parentTweet;

    @Getter
    private List<Tweet> childrenTweets = new LinkedList<>();

    @Getter
    private LocalDateTime dateTime;

    @Getter
    private String message;

    public Tweet(Long tweetId, User author, String message, LocalDateTime dateTime) {
        this.tweetId = tweetId;
        this.author = author;
        this.message = message;
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return Objects.equals(tweetId, tweet.tweetId) &&
                Objects.equals(author, tweet.author) &&
                Objects.equals(dateTime, tweet.dateTime) &&
                Objects.equals(message, tweet.message);
    }

    @Override
    public int hashCode() {

        return Objects.hash(tweetId, author, dateTime, message);
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + tweetId +
                ", author=" + author.getNickname() +
                ", dateTime=" + dateTime +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public int compareTo(Tweet anotherTweet) {
        return anotherTweet.getDateTime().compareTo(getDateTime());
    }
}
