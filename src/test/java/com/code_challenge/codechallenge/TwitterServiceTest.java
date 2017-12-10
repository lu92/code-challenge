package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.exceptions.FollowerAlreadyFollowsException;
import com.code_challenge.codechallenge.exceptions.UserAlreadyExistsException;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceTest {

    private TwitterService twitterService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void init() {
        twitterService = new TwitterServiceImpl();
    }

    @Test
    public void createUserTest() throws UserAlreadyExistsException {
        // when
        User user = twitterService.createUser("nick");

        // then
        assertThat(user.getNickname()).isEqualTo("nick");
    }

    @Test
    public void createUserWhenExistsExpectedFailureTest() throws Exception {
        expectedException.expect(UserAlreadyExistsException.class);
        expectedException.expectMessage("Given nickname is used by other user!");

        // given
        twitterService.createUser("nick");

        // when
        twitterService.createUser("nick");
    }

    @Test
    public void addFollowerTest() throws Exception {
        // given
        User user = twitterService.createUser("user");
        User follower = twitterService.createUser("follower");

        // when
        String message = twitterService.addFollower(user.getNickname(), follower.getNickname());

        // then
        assertThat(message).isEqualTo("Given follower has been added to followers of user");
    }

    @Test
    public void addFollowerWhenUserDoesNotExistTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given user does not exist!");

        // given
        User follower = twitterService.createUser("follower");
        String fakeUser = "fakeUser";

        // when
        twitterService.addFollower(fakeUser, follower.getNickname());
    }

    @Test
    public void addFollowerWhenFollowerDoesNotExistExpectedFailureTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given follower does not exist!");

        // given
        User user = twitterService.createUser("user");
        String fakeFollower = "fakeFollower";

        // when
        twitterService.addFollower(user.getNickname(), fakeFollower);
    }

    @Test
    public void addFollowerWhenFollowerFollowsByUserExpectedFailureTest() throws Exception {
        expectedException.expect(FollowerAlreadyFollowsException.class);
        expectedException.expectMessage("Given follower already follows by user!");

        // given
        User user = twitterService.createUser("user");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user.getNickname(), follower.getNickname());

        // when
        twitterService.addFollower(user.getNickname(), follower.getNickname());
    }

    @Test
    public void removeFollowerWhenExistsTest() throws Exception {
        // given
        User user = twitterService.createUser("user");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user.getNickname(), follower.getNickname());

        // when
        String message = twitterService.removeFollower(user.getNickname(), follower.getNickname());

        // then
        assertThat(message).isEqualTo("Now follower does not follow by user!");
    }

    @Test
    public void removeFollowerWhenUserDoesNotExistExpectedFailureTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given user does not exist!");

        // given
        String fakeUser = "fakeUser";
        User follower = twitterService.createUser("follower");

        // when
        twitterService.removeFollower(fakeUser, follower.getNickname());
    }

    @Test
    public void removeFollowerWhenFollowerDoesNotExistExpectedFailureTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given follower does not exist!");

        // given
        String fakeFollower = "fakeFollower";
        User user = twitterService.createUser("user");

        // when
        twitterService.removeFollower(user.getNickname(), fakeFollower);
    }


    @Test
    public void getFollowersWhenUserHasNoneTest() {
        // given
        User user = twitterService.createUser("user");

        // when
        Set<String> followers = twitterService.getFollowersForUser(user.getNickname());

        // then
        assertThat(followers).isEmpty();
    }

    @Test
    public void getFollowersWhenUserHasOneFollowerTest() {
        // given
        User user = twitterService.createUser("user");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user.getNickname(), follower.getNickname());

        // when
        Set<String> followers = twitterService.getFollowersForUser(user.getNickname());

        // then
        assertThat(followers).contains("follower");
    }

    @Test
    public void getFollowersWhenUserHasThreeFollowerTest() {
        // given
        User user = twitterService.createUser("user");
        User follower1 = twitterService.createUser("follower1");
        User follower2 = twitterService.createUser("follower2");
        User follower3 = twitterService.createUser("follower3");
        twitterService.addFollower(user.getNickname(), follower1.getNickname());
        twitterService.addFollower(user.getNickname(), follower2.getNickname());
        twitterService.addFollower(user.getNickname(), follower3.getNickname());

        // when
        Set<String> followers = twitterService.getFollowersForUser(user.getNickname());

        // then
        assertThat(followers).contains("follower1", "follower2", "follower3");
    }
}
