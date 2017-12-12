package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import com.code_challenge.codechallenge.service.TwitterService;
import com.code_challenge.codechallenge.service.TwitterServiceImpl;
import org.apache.tomcat.jni.Local;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TimeLineOfUserTest {

    private TwitterService twitterService;

    @Before
    public void init() {
        twitterService = new TwitterServiceImpl();
    }

    private LocalDateTime firstDateTime = LocalDateTime.of(2017, 10, 10, 10, 0, 0);
    private LocalDateTime secondDateTime = LocalDateTime.of(2017, 11, 11, 11, 0, 0);
    private LocalDateTime thirdDateTime = LocalDateTime.of(2017, 12, 12, 12, 0, 0);

    @Test
    public void getTimeLineOfTwoUsersByFollowerTest() throws Exception {
        // given
        User user1 = twitterService.createUser("user1");
        User user2 = twitterService.createUser("user2");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user1.getNickname(), follower.getNickname());
        twitterService.addFollower(user2.getNickname(), follower.getNickname());

        // when
        Tweet first_tweet_of_user1 = twitterService.tweet(user1.getNickname(), "first tweet of user1");
        ReflectionTestUtils.setField(first_tweet_of_user1, "dateTime", firstDateTime);
        Tweet second_tweet_of_user1 = twitterService.tweet(user1.getNickname(), "second tweet of user1");
        ReflectionTestUtils.setField(second_tweet_of_user1, "dateTime", secondDateTime);
        Tweet first_tweet_of_user2 = twitterService.tweet(user1.getNickname(), "first tweet of user2");
        ReflectionTestUtils.setField(first_tweet_of_user2, "dateTime", thirdDateTime);

        // then
        List<Tweet> timeLine = twitterService.getTimeLine(follower.getNickname());
        assertThat(timeLine).containsSequence(first_tweet_of_user2, second_tweet_of_user1, first_tweet_of_user1);
    }

    @Test
    public void getTimeLineOfTwoUsersByFollowerWithRetweetingTest() {
        // given
        User user1 = twitterService.createUser("user1");
        User user2 = twitterService.createUser("user2");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user1.getNickname(), follower.getNickname());
        twitterService.addFollower(user2.getNickname(), follower.getNickname());

        // when
        Tweet first_tweet_of_user1 = twitterService.tweet(user1.getNickname(), "first tweet of user1");
        Tweet second_tweet_of_user1 = twitterService.tweet(user1.getNickname(), "second tweet of user1");
        Tweet retweet_of_user2 = twitterService.retweet(user2.getNickname(), first_tweet_of_user1.getTweetId(),
                "retweet on first tweet of user1 by user2");
        Tweet first_tweet_of_user2 = twitterService.tweet(user1.getNickname(), "first tweet of user2");

        // then
        List<Tweet> timeLine = twitterService.getTimeLine(follower.getNickname());
        assertThat(timeLine).containsSequence(first_tweet_of_user2, second_tweet_of_user1, first_tweet_of_user1);
        assertThat(first_tweet_of_user1.getChildrenTweets()).containsExactly(retweet_of_user2);
    }
}
