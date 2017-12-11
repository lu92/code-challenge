package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import com.code_challenge.codechallenge.service.TwitterService;
import com.code_challenge.codechallenge.service.TwitterServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

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
        List<Tweet> wall = twitterService.getWall(user.getNickname());

        // then
        assertThat(wall).isEmpty();
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
        List<Tweet> wall = twitterService.getWall(user.getNickname());

        // then
        assertThat(wall).containsOnly(expectedTweet);
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
        List<Tweet> wall = twitterService.getWall(user.getNickname());

        // then
        assertThat(wall.get(0)).isEqualTo(expectedTweet3);
        assertThat(wall.get(1)).isEqualTo(expectedTweet2);
        assertThat(wall.get(2)).isEqualTo(expectedTweet1);
    }

    @Test
    public void userHasOneTweetWithTwoRetweetsFromFollowersTest() {
        // given
        User user = twitterService.createUser("user");
        User follower1 = twitterService.createUser("follower1");
        User follower2 = twitterService.createUser("follower2");
        twitterService.addFollower(user.getNickname(), follower1.getNickname());
        twitterService.addFollower(user.getNickname(), follower2.getNickname());

        // when
        Tweet usersTweet = twitterService.tweet(user.getNickname(), "user's tweet");
        Tweet follower1Tweet = twitterService.retweet(follower1.getNickname(), usersTweet.getTweetId(), "follower1's tweet");
        Tweet follower2Tweet = twitterService.retweet(follower2.getNickname(), usersTweet.getTweetId(), "follower2's tweet");

        // then
        List<Tweet> wall = twitterService.getWall(user.getNickname());
        assertThat(wall).containsExactly(usersTweet);
        assertThat(wall.get(0).getChildrenTweets()).containsSequence(follower2Tweet, follower1Tweet);
    }

    @Test
    public void userHasOneTweetWithTwoRetweetsFromOtherUsersTest() {
        // given
        User user1 = twitterService.createUser("user1");
        User user2 = twitterService.createUser("user2");
        User user3 = twitterService.createUser("user3");

        // when
        Tweet user1Tweet = twitterService.tweet(user1.getNickname(), "user1's tweet");
        Tweet user2Tweet = twitterService.retweet(user2.getNickname(), user1Tweet.getTweetId(), "user2's tweet");
        Tweet user3Tweet = twitterService.retweet(user3.getNickname(), user1Tweet.getTweetId(), "user3's tweet");

        // then
        List<Tweet> wall = twitterService.getWall(user1.getNickname());
        assertThat(wall).containsExactly(user1Tweet);
        assertThat(wall.get(0).getChildrenTweets()).containsSequence(user3Tweet, user2Tweet);
    }
}
