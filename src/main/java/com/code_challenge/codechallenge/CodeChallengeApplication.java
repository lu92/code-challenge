package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.model.User;
import com.code_challenge.codechallenge.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
        twitterService.addFollower(user1.getNickname(), user2.getNickname());
    }
}