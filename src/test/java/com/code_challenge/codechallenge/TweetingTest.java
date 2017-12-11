package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TweetingTest {

    private TwitterService twitterService;

    @Before
    public void init() {
        twitterService = new TwitterServiceImpl();
    }

    @Test
    public void postSingleTweet() {
        // given
        User user = twitterService.createUser("user");
        LocalDateTime expectedDateTime = LocalDateTime.of(2017, 10, 10, 10, 10, 10);
        Tweet expectedTweet = new Tweet(1l, user, "tweet1", expectedDateTime);

        // when
        Tweet tweet = twitterService.tweet(user.getNickname(), "tweet1");
        ReflectionTestUtils.setField(tweet, "dateTime", expectedDateTime);

        // then
        assertThat(user.getTweets()).containsExactly(expectedTweet);
    }

    @Test
    public void postTwoTweet() {
        // given
        User user = twitterService.createUser("user");
        LocalDateTime expectedDateTime1 = LocalDateTime.of(2017, 10, 10, 10, 10, 10);
        LocalDateTime expectedDateTime2 = LocalDateTime.of(2017, 10, 10, 10, 20, 10);
        Tweet expectedTweet1 = new Tweet(1l, user, "tweet1", expectedDateTime1);
        Tweet expectedTweet2 = new Tweet(2l, user, "tweet2", expectedDateTime2);

        // when
        Tweet tweet1 = twitterService.tweet(user.getNickname(), "tweet1");
        ReflectionTestUtils.setField(tweet1, "dateTime", expectedDateTime1);

        Tweet tweet2 = twitterService.tweet(user.getNickname(), "tweet2");
        ReflectionTestUtils.setField(tweet2, "dateTime", expectedDateTime2);

        // then
        assertThat(user.getTweets()).containsExactly(expectedTweet1, expectedTweet2);

        user.getTweets().forEach(tweet -> {
            assertThat(tweet.getChildrenTweets()).isEmpty();
            assertThat(tweet.getParentTweet()).isNull();
        });
    }

    @Test
    public void postOneTweetAndRetweetTest() {
        // given
        User user = twitterService.createUser("user");
        LocalDateTime expectedDateTime = LocalDateTime.of(2017, 10, 10, 10, 10, 10);
        LocalDateTime expectedDateTime2 = LocalDateTime.of(2017, 10, 10, 10, 20, 10);
        Tweet expectedTweet = new Tweet(1l, user, "tweet1", expectedDateTime);

        Tweet tweet = twitterService.tweet(user.getNickname(), "tweet1");
        ReflectionTestUtils.setField(tweet, "dateTime", expectedDateTime);

        // when
        Tweet tweet2 = twitterService.retweet(user.getNickname(), tweet.getTweetId(), "tweet2");
        ReflectionTestUtils.setField(tweet2, "dateTime", expectedDateTime2);


        // then
        assertThat(user.getTweets()).containsExactly(expectedTweet);
    }
}
