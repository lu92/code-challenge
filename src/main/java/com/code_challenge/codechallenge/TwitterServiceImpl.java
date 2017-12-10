package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.exceptions.FollowerAlreadyFollowsException;
import com.code_challenge.codechallenge.exceptions.UserAlreadyExistsException;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TwitterServiceImpl implements TwitterService {

    private Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User createUser(String nickname) throws UserAlreadyExistsException {
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
    public String addFollower(String userNickname, String followerNickname) throws UserNotFoundException, FollowerAlreadyFollowsException {
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

}
