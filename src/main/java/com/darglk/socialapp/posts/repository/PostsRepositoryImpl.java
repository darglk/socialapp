package com.darglk.socialapp.posts.repository;

import com.darglk.socialapp.posts.repository.model.PostEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Comparator.comparing;

@Repository
public class PostsRepositoryImpl implements PostsRepository {
    private static final Map<String, PostEntity> postsStorage = new ConcurrentHashMap<>();

    @Override
    public void save(PostEntity entity) {
        postsStorage.putIfAbsent(entity.id(), entity);
    }

    @Override
    public List<PostEntity> findAllByUsername(String username) {
        return postsStorage.values().stream().filter(p -> p.username().equals(username))
                .sorted(comparing(PostEntity::createdAt).reversed())
                .toList();
    }

    @Override
    public List<PostEntity> findAllInUsernames(List<String> usernames) {
        return postsStorage.values().stream().filter(p -> usernames.contains(p.username()))
                .sorted(comparing(PostEntity::createdAt).reversed())
                .toList();
    }

    @Override
    public void deleteAll() {
        postsStorage.clear();
    }
}
