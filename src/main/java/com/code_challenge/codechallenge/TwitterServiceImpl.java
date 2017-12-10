package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.exceptions.FollowerAlreadyFollowsException;
import com.code_challenge.codechallenge.exceptions.UserAlreadyExistsException;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

@Service
public class TwitterServiceImpl implements TwitterService {

    private IdGenerator idGenerator = new IdGenerator();

    private Map<User, List<User>> usersAndFollowers = new ConcurrentHashMap<>();
    private Map<User, Tweet> tweets = new ConcurrentHashMap<>();

    @Override
    public User createUser(String nickname) throws UserAlreadyExistsException {
        User user = new User(idGenerator.createID(), nickname);

        if (usersAndFollowers.containsKey(user)) {
            throw new UserAlreadyExistsException("Given nickname is used by other user!");
        } else {
            usersAndFollowers.put(user, new ArrayList<>());
            return user;
        }
    }

    @Override
    public List<String> getFollowersForUser(String nickname) {
        return null;
    }

    @Override
    public String addFollower(long userId, long followerId) throws UserNotFoundException, FollowerAlreadyFollowsException {
        Optional<User> userOptional = getUserById(userId);
        Optional<User> followerOptional = getUserById(followerId);
        checkAvabilityOfUserAndFollower(userOptional, followerOptional);
        List<User> followers = usersAndFollowers.getOrDefault(userOptional.get(), new LinkedList<>());
        checkFollowerAlreadyFollows(followerOptional.get(), followers);
        followers.add(followerOptional.get());
        usersAndFollowers.put(userOptional.get(), followers);
        return "Given follower has been added to followers of user";
    }

    private void checkFollowerAlreadyFollows(User follower, List<User> followers) throws FollowerAlreadyFollowsException {
        if (followers.contains(follower)) {
            throw new FollowerAlreadyFollowsException("Given follower already follows by user!");
        }
    }

    private void checkAvabilityOfUserAndFollower(Optional<User> userOptional, Optional<User> followerOptional) throws UserNotFoundException {
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("Given user does not exist!");
        }

        if (!followerOptional.isPresent()) {
            throw new UserNotFoundException("Given follower does not exist!");
        }
    }

    private Optional<User> getUserById(long userId) {
        Predicate<User> userMatched = user -> user.getId() == userId;
        return usersAndFollowers.keySet().stream().filter(userMatched).findFirst();
    }

    @Override
    public String removeFollower(long userId, long followerId) throws UserNotFoundException {
        Optional<User> userOptional = getUserById(userId);
        Optional<User> followerOptional = getUserById(followerId);
        checkAvabilityOfUserAndFollower(userOptional, followerOptional);
        return null;
    }


    class IdGenerator {
        private AtomicLong idCounter = new AtomicLong(1);

        Long createID() {
            return idCounter.getAndIncrement();
        }
    }
}
