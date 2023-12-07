package com.darglk.socialapp.users.controller;

import com.darglk.socialapp.SocialappApplication;
import com.darglk.socialapp.common.InstantWrapper;
import com.darglk.socialapp.users.controller.model.FollowRequest;
import com.darglk.socialapp.users.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = { SocialappApplication.class })
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstantWrapper instantWrapper;

    @Autowired
    private UsersRepository usersRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        when(instantWrapper.now()).thenReturn(Instant.parse("2005-04-02T21:37:00Z"));
    }

    @AfterEach
    public void tearDown() {
        usersRepository.deleteAll();
    }

    @Test
    public void testFollowUserWithSuccess() throws Exception {
        // given
        usersRepository.save("voi");
        usersRepository.save("tee");

        // when
        mockMvc.perform(request(POST, "/api/v1/users/follow")
                .content(objectMapper.writeValueAsString(new FollowRequest("voi", "tee")))
                .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isOk());
        var userOpt = usersRepository.findByUsername("voi");
        assertTrue(userOpt.isPresent());
        assertTrue(userOpt.get().following().contains("tee"));
    }

    @Test
    public void testFollowUserAlreadyFollowing() throws Exception {
        // given
        usersRepository.save("voi");
        usersRepository.save("tee");
        usersRepository.saveFollowed("voi", "tee");
        // when
        mockMvc.perform(request(POST, "/api/v1/users/follow")
                        .content(objectMapper.writeValueAsString(new FollowRequest("voi", "tee")))
                        .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[*]", hasSize(1)))
                .andExpect(jsonPath("$.errors.[0].message").value("You already follow this user"));
    }

    @Test
    public void testUserFollowsHimself() throws Exception {
        // given
        usersRepository.save("voi");
        // when
        mockMvc.perform(request(POST, "/api/v1/users/follow")
                        .content(objectMapper.writeValueAsString(new FollowRequest("voi", "voi")))
                        .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.[*]", hasSize(1)))
                .andExpect(jsonPath("$.errors.[0].message").value("You cannot follow yourself"));
    }

    @Test
    public void testUserFollowNonExistingUser() throws Exception {
        // given
        usersRepository.save("voi");
        // when
        mockMvc.perform(request(POST, "/api/v1/users/follow")
                        .content(objectMapper.writeValueAsString(new FollowRequest("voi", "wa")))
                        .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.[*]", hasSize(1)))
                .andExpect(jsonPath("$.errors.[0].message").value("Username wa was not found"));
    }

    @Test
    public void testNonExistingUserFollow() throws Exception {
        // given
        usersRepository.save("voit");
        // when
        mockMvc.perform(request(POST, "/api/v1/users/follow")
                        .content(objectMapper.writeValueAsString(new FollowRequest("voi", "voit")))
                        .contentType(APPLICATION_JSON))
                // then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.[*]", hasSize(1)))
                .andExpect(jsonPath("$.errors.[0].message").value("Username voi was not found"));
    }
}
