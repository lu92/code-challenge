package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.exceptions.FollowerAlreadyFollowsException;
import com.code_challenge.codechallenge.exceptions.UserAlreadyExistsException;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.User;

import java.util.List;

public interface TwitterService {
    User createUser(String nickname) throws UserAlreadyExistsException;

    List<String> getFollowersForUser(String nickname) throws UserNotFoundException;

    String addFollower(long userId, long followerId) throws UserNotFoundException, FollowerAlreadyFollowsException;

    String removeFollower(long userId, long followerId) throws UserNotFoundException;

}
