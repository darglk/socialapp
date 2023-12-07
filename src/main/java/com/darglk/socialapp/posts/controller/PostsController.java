package com.darglk.socialapp.posts.controller;

import com.darglk.socialapp.exception.ValidationException;
import com.darglk.socialapp.posts.controller.model.PostRequest;
import com.darglk.socialapp.posts.controller.model.PostResponse;
import com.darglk.socialapp.posts.service.PostsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @Operation(summary = "Creates new post and new user")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "User and post is created"),
                @ApiResponse(responseCode = "422", description = "Invalid input data"),
            }
    )
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody @Valid PostRequest request, Errors errors) {
        if (errors.hasErrors()) throw new ValidationException(errors);
        var newPost = postsService.createPost(request);
        return ResponseEntity.ok(newPost);
    }

    @Operation(summary = "Returns users posts in reversed chronological order")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200")
            }
    )
    @GetMapping("/me/{username}")
    public List<PostResponse> myPosts(@PathVariable("username") String username) {
        return postsService.myPosts(username);
    }

    @Operation(summary = "Returns posts of followed users in reversed chronological order")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200"),
                    @ApiResponse(responseCode = "404", description = "Username was not found")
            }
    )
    @GetMapping("/timeline/{username}")
    public List<PostResponse> timeline(@PathVariable("username") String username) {
        return postsService.timeline(username);
    }
}
