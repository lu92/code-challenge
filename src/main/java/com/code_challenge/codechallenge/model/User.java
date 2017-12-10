package com.code_challenge.codechallenge.model;

import com.code_challenge.codechallenge.exceptions.FollowerAlreadyFollowsException;
import lombok.Data;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class User {
    private String nickname;
    private Set<User> followers = new HashSet<>();


    public User(String nickname) {
        this.nickname = nickname;
    }

    public void addFollower(User follower) {
        if (followers.contains(follower)) {
            throw new FollowerAlreadyFollowsException("Given follower already follows by user!");
        }
        followers.add(follower);
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
