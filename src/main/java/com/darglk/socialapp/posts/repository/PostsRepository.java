package com.darglk.socialapp.posts.repository;

import com.darglk.socialapp.posts.repository.model.PostEntity;

import java.util.List;

public interface PostsRepository {
    void save(PostEntity entity);

    List<PostEntity> findAllByUsername(String username);

    List<PostEntity> findAllInUsernames(List<String> usernames);

    void deleteAll();
}
