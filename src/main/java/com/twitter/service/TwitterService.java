package com.twitter.service;

import com.twitter.model.Tweet;
import com.twitter.model.User;

import java.util.List;
import java.util.Set;

public interface TwitterService {
    User createUser(String nickname);
    Set<String> getFollowersForUser(String nickname);
    String addFollower(String userNickname, String followerNickname);
    String removeFollower(String userNickname, String followerNickname);
    Tweet tweet(String userNickname, String message);
    Tweet retweet(String userNickname, Long parentTweetId, String message);
    List<Tweet> getWall(String userNickname);
    List<Tweet> getTimeLine(String userNickname);
}
