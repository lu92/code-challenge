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
    public void addFollowerWhenUserDoesNotExistExpectedNotFoundTest() throws Exception {
        Mockito.when(twitterService.addFollower(Mockito.anyString(), Mockito.anyString())).thenThrow(new UserNotFoundException("Given user does not exist!"));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/addFollower/user/follower").accept(
                MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        System.out.println(response.getContentAsString());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        ApiError apiError = objectMapper.readValue(response.getContentAsString(), ApiError.class);
        assertThat(apiError.getMessage()).isEqualTo("");
    }


}
