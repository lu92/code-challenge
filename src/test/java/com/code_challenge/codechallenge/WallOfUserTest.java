package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import com.code_challenge.codechallenge.model.Wall;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WallOfUserTest {

    private TwitterService twitterService;

    @Before
    public void init() {
        twitterService = new TwitterServiceImpl();
    }

    @Test
    public void emptyWallTest() {
        //given
        User user = twitterService.createUser("user");

        // when
        Wall wall = twitterService.getWall(user.getNickname());

        // then
        assertThat(wall.getTweets()).isEmpty();
    }

    @Test
    public void userHasOneTweet() {
        //given
        User user = twitterService.createUser("user");
        Tweet tweet = twitterService.tweet(user.getNickname(), "some tweet");
        LocalDateTime expectedDateTime = LocalDateTime.now();
        ReflectionTestUtils.setField(tweet, "dateTime", expectedDateTime);
        Tweet expectedTweet = new Tweet(1l, user, "some tweet", expectedDateTime);

        // when
        Wall wall = twitterService.getWall(user.getNickname());

        // then
        assertThat(wall.getTweets()).containsOnly(expectedTweet);
    }

    @Test
    public void userHasThreeTweet() {
        //given
        User user = twitterService.createUser("user");
        Tweet tweet1 = twitterService.tweet(user.getNickname(), "some tweet1");
        Tweet tweet2 = twitterService.tweet(user.getNickname(), "some tweet2");
        Tweet tweet3 = twitterService.tweet(user.getNickname(), "some tweet3");
        LocalDateTime expectedDateTime1 = LocalDateTime.of(2017, 10, 10, 10, 0, 0);
        LocalDateTime expectedDateTime2 = LocalDateTime.of(2017, 11, 10, 10, 0, 0);
        LocalDateTime expectedDateTime3 = LocalDateTime.of(2017, 12, 10, 10, 0, 0);
        ReflectionTestUtils.setField(tweet1, "dateTime", expectedDateTime1);
        ReflectionTestUtils.setField(tweet2, "dateTime", expectedDateTime2);
        ReflectionTestUtils.setField(tweet3, "dateTime", expectedDateTime3);
        Tweet expectedTweet1 = new Tweet(1l, user, "some tweet1", expectedDateTime1);
        Tweet expectedTweet2 = new Tweet(2l, user, "some tweet2", expectedDateTime2);
        Tweet expectedTweet3 = new Tweet(3l, user, "some tweet3", expectedDateTime3);

        // when
        Wall wall = twitterService.getWall(user.getNickname());

        // then
        assertThat(wall.getTweets().get(0)).isEqualTo(expectedTweet1);
        assertThat(wall.getTweets().get(1)).isEqualTo(expectedTweet2);
        assertThat(wall.getTweets().get(2)).isEqualTo(expectedTweet3);
    }
}
