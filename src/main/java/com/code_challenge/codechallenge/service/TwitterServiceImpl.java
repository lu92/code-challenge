package com.code_challenge.codechallenge.service;

import com.code_challenge.codechallenge.exceptions.TweetNotFoundException;
import com.code_challenge.codechallenge.exceptions.UserAlreadyExistsException;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TwitterServiceImpl implements TwitterService {

    private IdGenerator idGenerator = new IdGenerator();
    private TweetValidator tweetValidator = new TweetValidator();
    private Map<String, User> users = new ConcurrentHashMap<>();
    private Map<Long, Tweet> tweets = new ConcurrentHashMap<>();

    @Override
    public User createUser(String nickname) {
        if (users.containsKey(nickname)) {
            throw new UserAlreadyExistsException();
        } else {
            User user = new User(nickname);
            users.put(nickname, user);
            return user;
        }
    }

    @Override
    public Set<String> getFollowersForUser(String nickname) {
        User user = getUser(nickname);
        return user.getFollowers().stream().map(User::getNickname).collect(Collectors.toSet());
    }

    @Override
    public String addFollower(String userNickname, String followerNickname) {
        User user = getUser(userNickname);
        User follower = getFollower(followerNickname);

        user.addFollower(follower);
        return "Given follower has been added to followers of user";
    }

    @Override
    public String removeFollower(String userNickname, String followerNickname) {
        User user = getUser(userNickname);
        User follower = getFollower(followerNickname);
        user.getFollowers().remove(follower);
        return "Now follower does not follow by user!";
    }

    @Override
    public Tweet tweet(String userNickname, String message) {
        tweetValidator.validate(message);
        User user = getUser(userNickname);
        Tweet tweet = new Tweet(idGenerator.generate(), user, message, LocalDateTime.now());
        user.addTweet(tweet);
        tweets.put(tweet.getTweetId(), tweet);
        return tweet;
    }

    @Override
    public Tweet retweet(String userNickname, Long parentTweetId, String message) {
        tweetValidator.validate(message);
        User user = getUser(userNickname);
        Tweet parentTweet = retrieveTweet(parentTweetId);
        Tweet newTweet = new Tweet(idGenerator.generate(), user, message, LocalDateTime.now());
        newTweet.setParentTweet(parentTweet);
        parentTweet.getChildrenTweets().add(0, newTweet);
        return newTweet;
    }

    @Override
    public List<Tweet> getWall(String userNickname) {
        User user = getUser(userNickname);
        Collections.sort(user.getTweets());
        return user.getTweets();
    }

    @Override
    public List<Tweet> getTimeLine(String userNickname) {
        User user = getUser(userNickname);
        return tweets.values().stream()
                .filter(tweet -> user.isFollowed(tweet.getAuthor()))
                .filter(tweet -> tweet.getParentTweet() == null)
                .sorted().collect(Collectors.toList());
    }

    private Tweet retrieveTweet(Long tweetId) {
        return Optional.ofNullable(tweets.get(tweetId)).orElseThrow(
                () -> new TweetNotFoundException()
        );
    }

    private User getUser(String userNickname) {
        return getUserByNickname(userNickname).orElseThrow(
                () -> new UserNotFoundException("Given user does not exist!"));
    }

    private User getFollower(String followerNickname) {
        return getUserByNickname(followerNickname).orElseThrow(
                () -> new UserNotFoundException("Given follower does not exist!"));
    }

    private Optional<User> getUserByNickname(String nickname) {
        return Optional.ofNullable(users.get(nickname));
    }

    private class IdGenerator {
        private AtomicLong atomicLong = new AtomicLong(1);

        Long generate() {
            return atomicLong.getAndIncrement();
        }
    }
}
