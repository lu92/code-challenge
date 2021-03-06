package com.twitter.controller;

import com.twitter.model.Tweet;
import com.twitter.model.User;
import com.twitter.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class TwitterController {

    @Autowired
    private TwitterService twitterService;

    @RequestMapping(value = "/createUser/{nickname}", method = RequestMethod.POST)
    @ResponseBody
    User createUser(@PathVariable("nickname") String nickname) {
        return twitterService.createUser(nickname);
    }

    @RequestMapping(value = "/addFollower/{userNickname}/{followerNickname}", method = RequestMethod.POST)
    @ResponseBody String addFollower(@PathVariable("userNickname") String userNickname,
                                     @PathVariable("followerNickname") String followerNickname) {
        return twitterService.addFollower(userNickname, followerNickname);
    }

    @RequestMapping("/getFollowers/{userNickname}")
    @ResponseBody Set<String> getFollowersForUser(@PathVariable("userNickname") String userNickname) {
        return twitterService.getFollowersForUser(userNickname);
    }

    @RequestMapping(value = "/tweet/{userNickname}", method = RequestMethod.POST)
    @ResponseBody Tweet tweet(@PathVariable("userNickname") String userNickname, @RequestBody String message) {
        return twitterService.tweet(userNickname, message);
    }

    @RequestMapping(value = "/tweet/{userNickname}/{parentTweeterId}", method = RequestMethod.POST)
    @ResponseBody Tweet retweet(@PathVariable("userNickname") String userNickname,
                                @PathVariable("parentTweeterId") Long parentTweeterId,
                                @RequestBody String message) {
        return twitterService.retweet(userNickname, parentTweeterId, message);
    }

    @RequestMapping("/getWall/{userNickname}")
    @ResponseBody  List<Tweet> getWall(@PathVariable("userNickname") String userNickname) {
        return twitterService.getWall(userNickname);
    }

    @RequestMapping("/getTimeline/{userNickname}")
    @ResponseBody List<Tweet> getTimeline(@PathVariable("userNickname") String userNickname) {
        return twitterService.getTimeLine(userNickname);
    }
}
