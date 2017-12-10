package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.exceptions.FollowerAlreadyFollowsException;
import com.code_challenge.codechallenge.exceptions.UserAlreadyExistsException;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class CodeChallengeApplication implements CommandLineRunner {

    @Autowired
    private TwitterService twitterService;

    public static void main(String[] args) {
        SpringApplication.run(CodeChallengeApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        User user1 = twitterService.createUser("user1");
        User user2 = twitterService.createUser("user2");
        twitterService.createUser("user3");
        twitterService.addFollower(user1.getId(), user2.getId());
    }
}

@RestController
class AppController {

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value = "/createUser/{nickname}")
    @ResponseBody User create(@PathVariable("nickname") String nickname) throws UserAlreadyExistsException {
        return twitterService.createUser(nickname);
    }

    @RequestMapping(value = "/addFollower/{userId}/{followerId}")
    @ResponseBody String addFollower(@PathVariable("userId") Long userId, @PathVariable("followerId") Long followerId) throws UserNotFoundException, FollowerAlreadyFollowsException {
        return twitterService.addFollower(userId, followerId);
    }
}