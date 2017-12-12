package com.twitter.model;

import com.twitter.exceptions.FollowerAlreadyFollowsException;
import lombok.Getter;

import java.util.*;

public class User {
    @Getter
    private String nickname;

    @Getter
    private Set<User> followers = new HashSet<>();

    @Getter
    private Set<User> followedUsers = new HashSet<>();

    @Getter
    private List<Tweet> tweets = new LinkedList<>();


    public User(String nickname) {
        this.nickname = nickname;
    }

    public void addFollower(User follower) {
        if (followers.contains(follower)) {
            throw new FollowerAlreadyFollowsException();
        }
        follower.followedUsers.add(this);
        followers.add(follower);
    }

    public boolean isFollowed(User followedUser) {
        return followedUsers.contains(followedUser);
    }

    public void addTweet(Tweet tweet) {
        tweets.add(0, tweet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
