package com.code_challenge.codechallenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class Tweet implements Comparable<Tweet> {

    private Long tweetId;

    @JsonIgnore
    private User author;

    @JsonIgnore
    private Tweet parentTweet;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateTime;

    private List<Tweet> childrenTweets = new LinkedList<>();
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
