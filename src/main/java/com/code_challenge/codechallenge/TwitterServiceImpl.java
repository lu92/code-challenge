package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.exceptions.UserAlreadyExistsException;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import com.code_challenge.codechallenge.model.Wall;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TwitterServiceImpl implements TwitterService {

    private IdGenerator idGenerator = new IdGenerator();
    private Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User createUser(String nickname) {
        if (users.containsKey(nickname)) {
            throw new UserAlreadyExistsException("Given nickname is used by other user!");
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
        User user = getUser(userNickname);
        Tweet tweet = new Tweet(idGenerator.generate(), user, message, LocalDateTime.now());
        user.addTweet(tweet);
        return tweet;
    }

    @Override
    public Tweet retweet(String userNickname, Long parentTweetId, String message) {
        // TODO
        return null;
    }

    @Override
    public Wall getWall(String userNickname) {
        User user = getUser(userNickname);
        Wall wall = new Wall(user.getTweets());
        return wall;
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
        public Long generate() {
            return atomicLong.getAndIncrement();
        }
    }
}
