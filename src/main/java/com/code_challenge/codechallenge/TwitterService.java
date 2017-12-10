package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.model.User;

import java.util.Set;

public interface TwitterService {
    User createUser(String nickname);
    Set<String> getFollowersForUser(String nickname);
    String addFollower(String userNickname, String followerNickname);
    String removeFollower(String userNickname, String followerNickname);
}
