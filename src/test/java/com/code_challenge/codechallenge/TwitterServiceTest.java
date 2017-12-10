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
        assertThat(user.getId()).isEqualTo(1);
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
        String message = twitterService.addFollower(user.getId(), follower.getId());

        // then
        assertThat(message).isEqualTo("Given follower has been added to followers of user");
    }

    @Test
    public void addFollowerWhenUserDoesNotExistTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given user does not exist!");

        // given
        User follower = twitterService.createUser("follower");
        long fakeUserId = -1;

        // when
        twitterService.addFollower(fakeUserId, follower.getId());
    }

    @Test
    public void addFollowerWhenFollowerDoesNotExistExpectedFailureTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given follower does not exist!");

        // given
        User user = twitterService.createUser("user");
        long fakeFollowerId = -1;

        // when
        twitterService.addFollower(user.getId(), fakeFollowerId);
    }

    @Test
    public void addFollowerWhenFollowerFollowsByUserExpectedFailureTest() throws Exception {
        expectedException.expect(FollowerAlreadyFollowsException.class);
        expectedException.expectMessage("Given follower already follows by user!");

        // given
        User user = twitterService.createUser("user");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user.getId(), follower.getId());

        // when
        twitterService.addFollower(user.getId(), follower.getId());
    }

    @Test
    public void removeFollowerWhenExistsTest() throws Exception {
        // given
        User user = twitterService.createUser("user");
        User follower = twitterService.createUser("follower");
        twitterService.addFollower(user.getId(), follower.getId());

        // when
        String message = twitterService.removeFollower(user.getId(), follower.getId());

        // then
        assertThat(message).isEqualTo("Now follower does not follow by user!");
    }

    @Test
    public void removeFollowerWhenUserDoesNotExistExpectedFailureTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given user does not exist!");

        // given
        long fakeUserId = -1;
        User follower = twitterService.createUser("follower");

        // when
        twitterService.removeFollower(fakeUserId, follower.getId());
    }

    @Test
    public void removeFollowerWhenFollowerDoesNotExistExpectedFailureTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given follower does not exist!");

        // given
        long fakeFollowerId = -1;
        User user = twitterService.createUser("user");

        // when
        twitterService.removeFollower(user.getId(), fakeFollowerId);
    }

    @Test
    public void removeFollowerWhenUserAndFollowerDontExistExpectedFailureTest() throws Exception {
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Given user and follower don't exist!");

        // given
        long fakeUserId = -1;
        long fakeFollowerId = -1;

        // when
        twitterService.removeFollower(fakeUserId, fakeFollowerId);
    }
}
