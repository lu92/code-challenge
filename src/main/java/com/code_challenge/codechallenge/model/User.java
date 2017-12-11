package com.code_challenge.codechallenge.model;

import com.code_challenge.codechallenge.exceptions.FollowerAlreadyFollowsException;
import lombok.Data;

import java.util.*;

@Data
public class User {
    private String nickname;
    private Set<User> followers = new HashSet<>();
    private Set<User> followedUsers = new HashSet<>();
    private List<Tweet> tweets = new LinkedList<>();


    public User(String nickname) {
        this.nickname = nickname;
    }

    public void addFollower(User follower) {
        if (followers.contains(follower)) {
            throw new FollowerAlreadyFollowsException("Given follower already follows by user!");
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
