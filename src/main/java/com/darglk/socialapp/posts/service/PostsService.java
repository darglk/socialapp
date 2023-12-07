package com.darglk.socialapp.posts.service;

import com.darglk.socialapp.posts.controller.model.PostRequest;
import com.darglk.socialapp.posts.controller.model.PostResponse;

import java.util.List;

public interface PostsService {
    PostResponse createPost(PostRequest request);

    List<PostResponse> myPosts(String username);

    List<PostResponse> timeline(String username);
}
