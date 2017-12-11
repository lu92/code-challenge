package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import com.code_challenge.codechallenge.model.Wall;

import java.util.Set;

public interface TwitterService {
    User createUser(String nickname);
    Set<String> getFollowersForUser(String nickname);
    String addFollower(String userNickname, String followerNickname);
    String removeFollower(String userNickname, String followerNickname);
    Tweet tweet(String userNickname, String message);
    Tweet retweet(String userNickname, Long parentTweetId, String message);
    Wall getWall(String userNickname);
}
