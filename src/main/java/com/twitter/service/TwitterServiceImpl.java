package com.twitter.service;

import com.twitter.exceptions.InvalidTweetException;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.exceptions.UserAlreadyExistsException;
import com.twitter.exceptions.UserNotFoundException;
import com.twitter.model.Tweet;
import com.twitter.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TwitterServiceImpl implements TwitterService {

    private IdGenerator idGenerator = new IdGenerator();
    private MessageValidator messageValidator = new MessageValidator();
    private Map<String, User> users = new ConcurrentHashMap<>();
    private Map<Long, Tweet> tweets = new ConcurrentHashMap<>();
    private Lock lock = new ReentrantLock();

    @Override
    public User createUser(String nickname) {
        lock.lock();
        try {
            if (users.containsKey(nickname)) {
                throw new UserAlreadyExistsException();
            } else {
                User user = new User(nickname);
                users.put(nickname, user);
                return user;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Set<String> getFollowersForUser(String nickname) {
        User user = getUser(nickname);
        return user.getFollowers().stream().map(User::getNickname).collect(Collectors.toSet());
    }

    @Override
    public String addFollower(String userNickname, String followerNickname) {
        lock.lock();
        try {
            User user = getUser(userNickname);
            User follower = getFollower(followerNickname);
            user.addFollower(follower);
            return "Given follower has been added to followers of user";
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String removeFollower(String userNickname, String followerNickname) {
        lock.lock();
        try {
            User user = getUser(userNickname);
            User follower = getFollower(followerNickname);
            user.getFollowers().remove(follower);
            return "Now follower does not follow by user!";
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Tweet tweet(String userNickname, String message) {
        lock.lock();
        try {
            messageValidator.validate(message);
            User user = getUser(userNickname);
            Tweet tweet = new Tweet(idGenerator.generate(), user, message, LocalDateTime.now());
            user.addTweet(tweet);
            tweets.put(tweet.getTweetId(), tweet);
            return tweet;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Tweet retweet(String userNickname, Long parentTweetId, String message) {
        lock.lock();
        try {
            messageValidator.validate(message);
            User user = getUser(userNickname);
            Tweet parentTweet = retrieveTweet(parentTweetId);
            Tweet newTweet = new Tweet(idGenerator.generate(), user, message, LocalDateTime.now());
            newTweet.setParentTweet(parentTweet);
            parentTweet.getChildrenTweets().add(0, newTweet);
            return newTweet;
        } finally {
            lock.unlock();
        }
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
                TweetNotFoundException::new
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

    private class MessageValidator {
        private Predicate<String> notNull = Objects::nonNull;
        private Predicate<String> validLength = message -> 1 <= message.length() && message.length() <= 140;
        private Predicate<String> validTweetMessage = notNull.and(validLength);

        void validate(String message) {
            if (!validTweetMessage.test(message)) {
                throw new InvalidTweetException();
            }
        }
    }
}
