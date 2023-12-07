package com.darglk.socialapp.users.repository;

import com.darglk.socialapp.common.InstantWrapper;
import com.darglk.socialapp.users.repository.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@RequiredArgsConstructor
public class UsersRepositoryImpl implements UsersRepository {
    private static final Map<String, UserEntity> usersStorage = new ConcurrentHashMap<>();
    private final InstantWrapper instantWrapper;

    @Override
    public void save(String username) {
        if (!usersStorage.containsKey(username)) {
            var newUser = new UserEntity(username, new ArrayList<>(), instantWrapper.now());
            usersStorage.putIfAbsent(username, newUser);
        }
    }

    @Override
    public void saveFollowed(String follower, String followed) {
        var user = usersStorage.get(follower);
        if (user.following().stream().noneMatch(u -> u.equals(followed))) {
            user.following().add(followed);
        }
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(usersStorage.get(username));
    }

    @Override
    public void deleteAll() {
        usersStorage.clear();
    }
}
