package com.darglk.socialapp.posts.service;

import com.darglk.socialapp.common.InstantWrapper;
import com.darglk.socialapp.posts.controller.model.PostRequest;
import com.darglk.socialapp.posts.controller.model.PostResponse;
import com.darglk.socialapp.posts.repository.PostsRepository;
import com.darglk.socialapp.posts.repository.model.PostEntity;
import com.darglk.socialapp.users.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostsServiceImpl implements PostsService {
    private final PostsRepository postsRepository;
    private final UsersService usersService;
    private final InstantWrapper instantWrapper;

    @Override
    public PostResponse createPost(PostRequest request) {
        var postId = UUID.randomUUID().toString();
        var newPost = new PostEntity(postId, request.content(), instantWrapper.now(), request.username());
        postsRepository.save(newPost);
        usersService.createUser(request.username());
        return new PostResponse(newPost.id(), newPost.content(), newPost.createdAt(), newPost.username());
    }

    @Override
    public List<PostResponse> myPosts(String username) {
        return postsRepository.findAllByUsername(username)
                .stream()
                .map(p -> new PostResponse(p.id(), p.content(), p.createdAt(), p.username()))
                .toList();
    }

    @Override
    public List<PostResponse> timeline(String username) {
        var user = usersService.getUser(username);
        return postsRepository.findAllInUsernames(user.following())
                .stream()
                .map(p -> new PostResponse(p.id(), p.content(), p.createdAt(), p.username()))
                .toList();
    }
}
