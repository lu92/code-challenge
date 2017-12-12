package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.exceptions.InvalidTweetException;
import com.code_challenge.codechallenge.service.TweetValidator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TweetValidatorTest {

    private TweetValidator tweetValidator = new TweetValidator();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void messageOfTweetIsNullExpectedFailureTest() {
        expectedException.expect(InvalidTweetException.class);
        expectedException.expectMessage("Message of tweet is invalid!");

        // when
        tweetValidator.validate(null);
    }

    @Test
    public void messageOfTweetIsEmptyExpectedFailureTest() {
        expectedException.expect(InvalidTweetException.class);
        expectedException.expectMessage("Message of tweet is invalid!");

        // when
        tweetValidator.validate("");
    }

    @Test
    public void messageOfTweetExceed140CharactersExpectedFailureTest() {
        expectedException.expect(InvalidTweetException.class);
        expectedException.expectMessage("Message of tweet is invalid!");

        // given
        String longMessage = new String(new char[141]).replace('\0', 'a');

        // when
        tweetValidator.validate(longMessage);
    }
}
