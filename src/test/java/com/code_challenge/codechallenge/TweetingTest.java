package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.exceptions.TweetNotFoundException;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import com.code_challenge.codechallenge.service.TwitterService;
import com.code_challenge.codechallenge.service.TwitterServiceImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TweetingTest {

    private TwitterService twitterService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        twitterService = new TwitterServiceImpl();
    }

    @Test
    public void userPostsSingleTweetTest() {
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
    public void UserPostsTwoTweetTest() {
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
    public void userPostsOneTweetAndRetweetTest() {
        // given
        User user = twitterService.createUser("user");
        LocalDateTime expectedDateTime = LocalDateTime.of(2017, 10, 10, 10, 10, 10);
        LocalDateTime expectedDateTime2 = LocalDateTime.of(2017, 10, 10, 10, 20, 10);
        Tweet expectedTweet = new Tweet(1l, user, "tweet1", expectedDateTime);
        Tweet expectedTweet2 = new Tweet(2l, user, "tweet2", expectedDateTime2);

        Tweet tweet = twitterService.tweet(user.getNickname(), "tweet1");
        ReflectionTestUtils.setField(tweet, "dateTime", expectedDateTime);

        // when
        Tweet tweet2 = twitterService.retweet(user.getNickname(), tweet.getTweetId(), "tweet2");
        ReflectionTestUtils.setField(tweet2, "dateTime", expectedDateTime2);


        // then
        assertThat(user.getTweets()).containsExactly(expectedTweet);
        Tweet outerTweet = user.getTweets().get(0);
        assertThat(outerTweet.getChildrenTweets()).containsExactly(expectedTweet2);
        Tweet innerTweet = outerTweet.getChildrenTweets().get(0);
        assertThat(innerTweet.getChildrenTweets()).isEmpty();
        assertThat(innerTweet.getParentTweet()).isEqualTo(outerTweet);
    }

    @Test
    public void userRetweetsOnNotExistedTweetExpectedFailureTest() {
        expectedException.expect(TweetNotFoundException.class);
        expectedException.expectMessage("Given tweet does not exist!");

        // given
        User user = twitterService.createUser("user");
        long fakeTweetId = -1;

        // when
        twitterService.retweet(user.getNickname(), fakeTweetId, "tweet");
    }

    @Test
    public void retweetWhenUserDoesNotExistExpectedFailure() {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given user does not exist!");

        // given
        User user = twitterService.createUser("user");
        Tweet tweet = twitterService.tweet(user.getNickname(), "tweet1");
        String fakeUserNickname = "fakeUser";

        // when
        twitterService.retweet(fakeUserNickname, tweet.getTweetId(), "some tweet");
    }

    @Test
    public void tweetWhenUserAndTweetDoesNotExistExpectedFailureTest() {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given user does not exist!");

        // given
        String fakeUserNickname = "fakeUser";

        // when
        twitterService.tweet(fakeUserNickname, "some tweet");
    }

    @Test
    public void retweetWhenUserAndTweetDoesNotExistExpectedFailureTest() {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given user does not exist!");

        // given
        String fakeUserNickname = "fakeUser";
        long fakeTweetId = -1;

        // when
        twitterService.retweet(fakeUserNickname, fakeTweetId, "some tweet");
    }

    @Test
    public void followerRetweetsOnUsersTweetTest() {
        // given
        User user = twitterService.createUser("user");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user.getNickname(), follower.getNickname());
        Tweet usersTweet = twitterService.tweet(user.getNickname(), "user's tweet");

        // when
        Tweet followersTweet = twitterService.retweet(follower.getNickname(), usersTweet.getTweetId(), "follower's tweet");

        // then
        assertThat(usersTweet.getChildrenTweets()).containsExactly(followersTweet);
        assertThat(followersTweet.getParentTweet()).isEqualTo(usersTweet);
        assertThat(followersTweet.getChildrenTweets()).isEmpty();
    }

    @Test
    public void followerRetweetsThreeTimesOnUsersTweetTest() {
        // given
        User user = twitterService.createUser("user");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user.getNickname(), follower.getNickname());
        Tweet usersTweet = twitterService.tweet(user.getNickname(), "user's tweet");

        // when
        Tweet followersTweet1 = twitterService.retweet(follower.getNickname(), usersTweet.getTweetId(), "follower's tweet1");
        Tweet followersTweet2 = twitterService.retweet(follower.getNickname(), usersTweet.getTweetId(), "follower's tweet2");
        Tweet followersTweet3 = twitterService.retweet(follower.getNickname(), usersTweet.getTweetId(), "follower's tweet3");

        // then
        assertThat(usersTweet.getChildrenTweets()).containsExactly(followersTweet1, followersTweet2, followersTweet3);
        Stream.of(followersTweet1, followersTweet2, followersTweet3).forEach(followersTweet -> {
            assertThat(followersTweet.getParentTweet()).isEqualTo(usersTweet);
            assertThat(followersTweet.getChildrenTweets()).isEmpty();
        });
    }

    @Test
    public void OtherUserRetweetsOnUsersTweetTest() {
        // given
        User firstUser = twitterService.createUser("user");
        User secondUser = twitterService.createUser("follower");
        Tweet firstUsersTweet = twitterService.tweet(firstUser.getNickname(), "first user's tweet");

        // when
        Tweet secondUsersTweet = twitterService.retweet(secondUser.getNickname(), firstUsersTweet.getTweetId(), "second user's tweet");

        // then
        assertThat(firstUsersTweet.getChildrenTweets()).containsExactly(secondUsersTweet);
        assertThat(secondUsersTweet.getParentTweet()).isEqualTo(firstUsersTweet);
        assertThat(secondUsersTweet.getChildrenTweets()).isEmpty();
    }

}
