package com.darglk.socialapp.posts.controller;

import com.darglk.socialapp.SocialappApplication;
import com.darglk.socialapp.common.InstantWrapper;
import com.darglk.socialapp.posts.controller.model.PostRequest;
import com.darglk.socialapp.posts.repository.PostsRepository;
import com.darglk.socialapp.users.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
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
public class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private InstantWrapper instantWrapper;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String username = "voi-tee-wa";

    @BeforeEach
    public void setup() {
        when(instantWrapper.now()).thenReturn(Instant.parse("2005-04-02T21:37:00Z"));
    }

    @AfterEach
    public void tearDown() {
        usersRepository.deleteAll();
        postsRepository.deleteAll();
    }

    @Test
    public void testCreatePostWithCorrectData() throws Exception {
        //given & when
        createPost(username, "testpost2137")
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Matchers.notNullValue()))
                .andExpect(jsonPath("$.content").value("testpost2137"))
                .andExpect(jsonPath("$.createdAt").value("2005-04-02T21:37:00Z"))
                .andExpect(jsonPath("$.username").value("voi-tee-wa"));

        var posts = postsRepository.findAllByUsername(username);
        var userOpt = usersRepository.findByUsername(username);
        assertEquals(1, posts.size());
        assertTrue(userOpt.isPresent());
        var user = userOpt.get();
        assertEquals(username, user.username());
        assertEquals("2005-04-02T21:37:00Z", user.createdAt().toString());
    }

    @Test
    public void testCreatePostWithBlankUsername() throws Exception {
        // given & when
        createPost("", "testpost2137")
                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.[*]", hasSize(1)))
                .andExpect(jsonPath("$.errors.[0].field").value("username"))
                .andExpect(jsonPath("$.errors.[0].message").value("cannot be blank"));
    }

    @Test
    public void testCreatePostWithBlankContent() throws Exception {
        // given & when
        createPost(username, "")
                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.[*]", hasSize(2)))
                .andExpect(jsonPath("$.errors.[0].field").value("content"))
                .andExpect(jsonPath("$.errors.[1].field").value("content"))
                .andExpect(jsonPath("$.errors.[*].message")
                        .value(containsInAnyOrder("cannot be blank", "size must be bigger than 0 and less than 140 characters")));
    }

    @Test
    public void testCreatePostWithTooLongContent() throws Exception {
        // given & when
        createPost(username, "a".repeat(141))
                // then
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.[*]", hasSize(1)))
                .andExpect(jsonPath("$.errors.[0].field").value("content"))
                .andExpect(jsonPath("$.errors.[*].message")
                        .value("size must be bigger than 0 and less than 140 characters"));
    }

    @Test
    public void testGetMyPosts() throws Exception {
        // given
        for (int i = 0; i < 5; i++) {
            when(instantWrapper.now()).thenReturn(Instant.parse(String.format("200%d-04-02T21:37:00Z", i)));
            createPost(username, String.format("post %d", i));
        }

        // when & then
        mockMvc.perform(request(GET, "/api/v1/posts/me/" + username)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(5)))
                .andExpect(jsonPath("$.[*].content").value(containsInRelativeOrder("post 4", "post 3", "post 2", "post 1", "post 0")));
    }

    @Test
    public void testGetTimeline() throws Exception {
        // given
        usersRepository.save(username);
        var user1 = "rzułtyposter";
        usersRepository.save(user1);
        var user2 = "czerwonyposter";
        usersRepository.save(user2);
        var user3 = "niebieski";
        usersRepository.save(user2);
        for (int i = 0; i < 9; i++) {
            when(instantWrapper.now()).thenReturn(Instant.parse(String.format("200%d-04-02T21:37:00Z", i)));
            if (i % 3 == 0) {
                createPost(user1, String.format("post %d", i));
            } else if (i % 3 == 1) {
                createPost(user2, String.format("post %d", i));
            } else {
                createPost(user3, String.format("post %d", i));
            }
        }
        usersRepository.saveFollowed(username, user1);
        usersRepository.saveFollowed(username, user3);

        // when & then
        mockMvc.perform(request(GET, "/api/v1/posts/timeline/" + username)
                        .contentType(APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(6)))
                .andExpect(jsonPath("$.[*].content")
                        .value(containsInRelativeOrder("post 8", "post 6", "post 5", "post 3", "post 2", "post 0")))
                .andExpect(jsonPath("$.[0].username").value(user3))
                .andExpect(jsonPath("$.[1].username").value(user1))
                .andExpect(jsonPath("$.[2].username").value(user3))
                .andExpect(jsonPath("$.[3].username").value(user1))
                .andExpect(jsonPath("$.[4].username").value(user3))
                .andExpect(jsonPath("$.[5].username").value(user1));
    }

    private ResultActions createPost(String username, String content) throws Exception {
        return mockMvc.perform(request(POST, "/api/v1/posts")
                .content(objectMapper.writeValueAsString(new PostRequest(username, content)))
                .contentType(APPLICATION_JSON));
    }
}
