package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
class TwitterController {

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
}
