package com.code_challenge.codechallenge;

import com.code_challenge.codechallenge.controller.TwitterController;
import com.code_challenge.codechallenge.exceptions.UserNotFoundException;
import com.code_challenge.codechallenge.model.ApiError;
import com.code_challenge.codechallenge.model.Tweet;
import com.code_challenge.codechallenge.model.User;
import com.code_challenge.codechallenge.service.TwitterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.fest.assertions.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = TwitterController.class, secure = false)
public class TwitterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TwitterService twitterService;

    @Test
    public void getWallTest() throws Exception {
        Tweet tweet = new Tweet(1L, new User("user1"), "message", LocalDateTime.now());
        Mockito.when(
                twitterService.getWall(Mockito.anyString())).thenReturn(Arrays.asList(tweet));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/getWall/user1").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        List<Tweet> tweets = objectMapper.readValue(result.getResponse().getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Tweet.class));
    }

    @Test
    public void createUserWhenExistsExpectedFailureTest() throws Exception {
        // given
        Mockito.when(twitterService.createUser(Mockito.anyString())).thenThrow(new UserNotFoundException("Given nickname is used by other user!"));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/createUser/user").accept(
                MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ApiError apiError = objectMapper.readValue(response.getContentAsString(), ApiError.class);
        assertThat(apiError.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(apiError.getMessage()).isEqualTo("Given nickname is used by other user!");
    }

    @Test
    public void addFollowerTest() throws Exception {
        // given
        Mockito.when(twitterService.addFollower(Mockito.anyString(), Mockito.anyString())).thenReturn("Given follower has been added to followers of user");

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/addFollower/user/follower").accept(
                MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("Given follower has been added to followers of user");
    }

    @Test
    public void addFollowerWhenUserDoesNotExistExpectedNotFoundTest() throws Exception {
        // given
        Mockito.when(twitterService.addFollower(Mockito.anyString(), Mockito.anyString())).thenThrow(new UserNotFoundException("Given user does not exist!"));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/addFollower/user/follower").accept(
                MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ApiError apiError = objectMapper.readValue(response.getContentAsString(), ApiError.class);
        assertThat(apiError.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(apiError.getMessage()).isEqualTo("Given user does not exist!");
    }

    @Test
    public void addFollowerWhenFollowerDoesNotExistExpectedFailureTest() throws Exception {
        // given
        Mockito.when(twitterService.addFollower(Mockito.anyString(), Mockito.anyString())).thenThrow(new UserNotFoundException("Given follower does not exist!"));

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/addFollower/user/follower").accept(
                MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ApiError apiError = objectMapper.readValue(response.getContentAsString(), ApiError.class);
        assertThat(apiError.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(apiError.getMessage()).isEqualTo("Given follower does not exist!");
    }

    @Test
    public void getFollowersWhenUserHasThreeFollowerTest() throws Exception {
        // given
        Set<String> followers = Stream.of("follower1", "follower2", "follower3").collect(Collectors.toSet());
        Mockito.when(twitterService.getFollowersForUser(Mockito.anyString())).thenReturn(followers);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/getFollowers/user").accept(
                MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<String> retrievedFollowers = objectMapper.readValue(response.getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
        assertThat(retrievedFollowers).containsOnly("follower1", "follower2", "follower3");
    }

    @Test
    public void userPostsSingleTweetTest() throws Exception {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        Tweet tweet = new Tweet(1L, new User("user"), "message", dateTime);
        Mockito.when(twitterService.tweet(Mockito.anyString(), Mockito.anyString())).thenReturn(tweet);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/tweet/user").content("message").accept(
                MediaType.APPLICATION_JSON_UTF8);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Tweet retrievedTweet = objectMapper.readValue(response.getContentAsString(), Tweet.class);
        assertThat(retrievedTweet.getTweetId()).isEqualTo(1L);
        assertThat(retrievedTweet.getDateTime()).isEqualTo(dateTime);
        assertThat(retrievedTweet.getChildrenTweets()).isEmpty();
        assertThat(retrievedTweet.getMessage()).isEqualTo("message");
    }


    @Test
    public void getWallForUserWithThreeTweetsTest() throws Exception {
        // given
        LocalDateTime dateTime1 = LocalDateTime.now();
        LocalDateTime dateTime2 = LocalDateTime.now().minusHours(2);

        List<Tweet> expectedWall = Arrays.asList(
                new Tweet(1L, new User("user"), "message1", dateTime1),
                new Tweet(2L, new User("user"), "message2", dateTime2)
        );
        Mockito.when(twitterService.getWall(Mockito.anyString())).thenReturn(expectedWall);

        // when
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/getWall/user").accept(
                MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        List<Tweet> retrievedWall = objectMapper.readValue(response.getContentAsString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Tweet.class));
        assertThat(retrievedWall.get(0).getTweetId()).isEqualTo(1L);
        assertThat(retrievedWall.get(0).getDateTime()).isEqualTo(dateTime1);
        assertThat(retrievedWall.get(0).getChildrenTweets()).isEmpty();
        assertThat(retrievedWall.get(0).getMessage()).isEqualTo("message1");
        assertThat(retrievedWall.get(1).getTweetId()).isEqualTo(2L);
        assertThat(retrievedWall.get(1).getDateTime()).isEqualTo(dateTime2);
        assertThat(retrievedWall.get(1).getChildrenTweets()).isEmpty();
        assertThat(retrievedWall.get(1).getMessage()).isEqualTo("message2");
    }

}
